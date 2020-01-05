package io.github.behoston.meloooncensor.listener;
import io.github.behoston.meloooncensor.MelooonCensor;
import io.github.behoston.meloooncensor.config.Configuration;
import io.github.behoston.meloooncensor.log.ViolationLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

public class ChatEventListener implements Listener {

    Configuration config;
    ViolationLogger logger;
    JavaPlugin plugin;
    Hashtable<String, Integer> mistakes;
    Hashtable<String, HashSet<Long>> mistakesLongTime;

    public ChatEventListener(Configuration config, ViolationLogger logger, JavaPlugin plugin) {
        this.config = config;
        this.logger = logger;
        this.plugin = plugin;
        this.mistakes = new Hashtable<>();
        this.mistakesLongTime = new Hashtable<>();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (config.isEnabled()) {
            Player player = event.getPlayer();
            String message = event.getMessage();
            if (config.isEnabledPunish()) {
                deleteOldMistakes(player);
            }
            if (config.isEnabledLongTimePunish()){
                deleteOldMistakesLong(player);
            }
            if (!config.allowBypass(player)) {
                if (config.getFilter().violatesPolicy(message)) {
                    String censoredMessage = config.getFilter().censorMessage(message);
                    if (censoredMessage == null) {
                        event.setCancelled(true);
                    } else {
                        if (config.isEnabledPunish()) {
                            shortTimePunishment(player);
                        }
                        if (config.isEnabledLongTimePunish()) {
                            longTimePunishment(player);
                        }
                        if (player.isOp() || player.hasPermission("meloooncensor.write")) {
                            event.setMessage(censoredMessage);
                        }
                    }
                    HashMap<String, String> values = new HashMap<>();
                    values.put("player", player.getName());
                    player.sendMessage(ChatColor.GRAY + config.getFormattedMessage(values));

                    if (logger != null) {
                        // The check above is in case the log file failed to create
                        logger.log(player, message);
                    }
                }
            }
        }
    }

    private void deleteOldMistakes(Player player) {
        if (player.getStatistic(Statistic.PLAY_ONE_MINUTE)%config.getTime()==0) {
            if (this.mistakes.contains(player.getName())) {
                this.mistakes.remove(player.getName());
            }
        }
    }

    private void deleteOldMistakesLong(Player player) {
        if (this.mistakesLongTime.containsKey(player.getName())) {
            HashSet<Long> newList = new HashSet<>();
            for (Long s : this.mistakesLongTime.get(player.getName())) {
                long cyclicTime = config.getTimeLong();
                if ((s + cyclicTime) >= System.currentTimeMillis()) {
                    newList.add(s);
                }
            }
            this.mistakesLongTime.put(player.getName(), newList);
        }
    }

    private void shortTimePunishment(Player player) {
            if (player.getStatistic(Statistic.PLAY_ONE_MINUTE) > config.getTime()) {
                this.mistakes.remove(player.getName());
            } else if (this.mistakes.containsKey(player.getName())) {
                this.mistakes.put(player.getName(), this.mistakes.get(player.getName()) + 1);
            } else {
                this.mistakes.put(player.getName(), 1);
            }
            if (this.mistakes.containsKey(player.getName())) {
                if (this.mistakes.get(player.getName()) >= config.getMistakes()) {
                    if (player.getStatistic(Statistic.PLAY_ONE_MINUTE) <= config.getTime()) {
                        HashMap<String, String> mistake = new HashMap<>();
                        mistake.put("player", player.getName());
                        mistake.put("mistakes", Integer.toString(this.mistakes.get(player.getName())));
                        String message_all = ChatColor.GRAY + config.getFormattedMessagePermission(player.getName(), this.mistakes.get(player.getName()));
                        String command = config.getCommandForPemission(mistake);
                        Bukkit.getScheduler().runTask(MelooonCensor.getPlugin(), new Runnable() {
                            @Override
                            public void run() {
                                Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
                                Bukkit.broadcastMessage(message_all);
                            }
                        });
                    }
                }
            }
        }

    private void longTimePunishment(Player player) {
        if (this.mistakesLongTime.containsKey(player.getName())) {
            this.mistakesLongTime.get(player.getName()).add(System.currentTimeMillis());
        } else {
            this.mistakesLongTime.put(player.getName(), new HashSet<>());
            this.mistakesLongTime.get(player.getName()).add(System.currentTimeMillis());
        }
        if (this.mistakesLongTime.containsKey(player.getName())) {
            if (this.mistakesLongTime.get(player.getName()).size() >= config.getMistakesLong()) {
                HashMap<String, String> mistake = new HashMap<>();
                mistake.put("player", player.getName());
                mistake.put("mistakes", Long.toString(this.mistakesLongTime.get(player.getName()).size()));
                String message_all = ChatColor.GRAY + config.getFormattedMessagePermissionLong(player.getName(),
                        this.mistakesLongTime.get(player.getName()).size());
                String command = config.getCommandForPemissionLong(mistake);
                Bukkit.getScheduler().runTask(MelooonCensor.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
                        if (mistakesLongTime.get(player.getName()).size() < config.getMistakesLong()){
                            Bukkit.broadcastMessage(message_all);
                        }
                    }
                });
                if (config.isResetCountLongTimePunish()){
                    this.mistakesLongTime.remove(player.getName());
                } else if (this.mistakesLongTime.get(player.getName()).size() > config.getMistakesLong()) {
                    long tmpMinTime = 0;
                    for (Long time : this.mistakesLongTime.get(player.getName())) {
                        if (tmpMinTime == 0){
                            tmpMinTime = time;
                        }else {
                            if (tmpMinTime > time){
                                tmpMinTime = time;
                            }
                        }
                    }
                    this.mistakesLongTime.get(player.getName()).remove(tmpMinTime);
                }
            }
        }
    }
    }



