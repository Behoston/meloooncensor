package io.github.behoston.meloooncensor.listener;

import io.github.behoston.meloooncensor.log.ViolationLogger;
import io.github.behoston.meloooncensor.config.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeEventListener implements Listener {

    Configuration config;
    ViolationLogger logger;

    public SignChangeEventListener (Configuration config, ViolationLogger logger) {
        this.config = config;
        this.logger = logger;
    }

    @EventHandler
    public void onSignChange (SignChangeEvent event) {
        if (config.isEnabled()) {
            Player player = event.getPlayer();

            if ( ! config.allowBypass(player)) {
                String[] lines = event.getLines();

                for (int index = 0; index < lines.length; index++) {
                    String line = lines[index];

                    if (config.getFilter().violatesPolicy(line)) {
                        String censoredLine = config.getFilter().censorMessage(line);
                        event.setLine(index, censoredLine);

                        if (logger != null) {
                            // The check above is in case the log file failed to create
                            logger.log(player, line, event.getBlock().getLocation().toString());
                        }
                    }
                }
            }
        }
    }

}
