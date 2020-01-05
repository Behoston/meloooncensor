package io.github.behoston.meloooncensor;

import com.bugsnag.Bugsnag;
import io.github.behoston.meloooncensor.log.ViolationLogger;
import io.github.behoston.meloooncensor.config.Configuration;
import io.github.behoston.meloooncensor.listener.ChatEventListener;
import io.github.behoston.meloooncensor.command.CensorCommandExecutor;
import io.github.behoston.meloooncensor.listener.PlayerJoinEventListener;
import io.github.behoston.meloooncensor.listener.SignChangeEventListener;
import io.github.behoston.meloooncensor.updater.CheckForUpdatesTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class MelooonCensor extends JavaPlugin {

    private Configuration config;
    private CheckForUpdatesTask updater;
    private Bugsnag bugsnag;
    private ViolationLogger chatLogger;
    private ViolationLogger signLogger;

    protected void startBugsnag () {
        bugsnag = new Bugsnag("fc44da4898af2627b9157e8f512c4254");
        bugsnag.setAppVersion(getDescription().getVersion());
        bugsnag.setProjectPackages("io.github.behoston.meloooncensor");
    }

    protected void createViolationLoggers () {
        try {
            chatLogger = new ViolationLogger(getDataFolder(), "chat");
            signLogger = new ViolationLogger(getDataFolder(), "signs");
        } catch (IOException err) {
            bugsnag.notify(err);
        }
    }

    protected void registerEvents () {
        getServer().getScheduler().runTaskTimerAsynchronously(
            this, updater = new CheckForUpdatesTask(this, bugsnag), 0L, 36000L
        );

        getServer().getPluginManager().registerEvents(
            new ChatEventListener(config, chatLogger), this
        );

        getServer().getPluginManager().registerEvents(
            new SignChangeEventListener(config, signLogger), this
        );

        getServer().getPluginManager().registerEvents(
            new PlayerJoinEventListener(this, config, updater), this
        );

        getCommand("censor").setExecutor(
            new CensorCommandExecutor(config)
        );
    }

    protected void setupConfig () {
        config = new Configuration(this, bugsnag);
    }

    @Override
    public void onEnable () {
        startBugsnag();
        setupConfig();
        createViolationLoggers();
        registerEvents();
    }

}
