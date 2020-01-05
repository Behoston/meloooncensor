package io.github.behoston.meloooncensor.config;

import com.bugsnag.Client;
import io.github.behoston.meloooncensor.MelooonCensor;
import io.github.behoston.meloooncensor.filter.ClassicFilter;
import io.github.behoston.meloooncensor.filter.Filter;
import io.github.behoston.meloooncensor.filter.StrictFilter;
import io.github.behoston.meloooncensor.filter.WordFilter;
import io.github.behoston.meloooncensor.lang.Translation;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class Configuration {

    public static final String ENABLE = "censor.enable";
    public static final String BYPASS = "censor.bypass";
    public static final String LANGUAGE = "censor.lang";
    public static final String TYPE = "censor.type";
    public static final String CHAR = "censor.char";
    public static final String CENSOR = "censor.list";
    public static final String IGNORE = "censor.ignore";
    public static final String MESSAGE = "censor.message";
    public static final String ENABLE_PUNISH = "censor.punish.enable";
    public static final String MESSAGE_PUNISH_COMMAND = "censor.punish.message";
    public static final String COMMAND = "censor.punish.command";
    public static final String PLAY_TIME = "censor.punish.playtime";
    public static final String MISTAKES = "censor.punish.mistakes";
    public static final String ENABLE_LONG_TIME_PUNISH = "censor.punish-long-time.enable";
    public static final String RESET_COUNT_LONG_TIME_PUNISH = "censor.punish-long-time.reset";
    public static final String MESSAGE_LONG_TIME_PUNISH_COMMAND = "censor.punish-long-time.message";
    public static final String COMMAND_LONG_TIME = "censor.punish-long-time.command";
    public static final String LONG_TIME_PLAY_TIME = "censor.punish-long-time.playtime";
    public static final String LONG_TIME_MISTAKES = "censor.punish-long-time.mistakes";
    public static final boolean DEFAULT_ENABLE = true;
    public static final boolean DEFAULT_BYPASS = false;
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String DEFAULT_TYPE = "classic";
    public static final char DEFAULT_CHAR = '*';
    public static final String[] DEFAULT_CENSOR = new String[]{"fuck", "shit", "piss", "bitch"};
    public static final String[] DEFAULT_IGNORE = new String[]{"shitsu"};
    public static final String DEFAULT_MESSAGE = "Please don't use that kind of language on this server, {player}.";
    public static final boolean DEFAULT_PUNISH_ENABLE = false;
    public static final String DEFAULT_PUNISH_MESSAGE = "Player {player} has been kicked for {mistakes} mistakes.";
    public static final String DEFAULT_PUNISH_COMMAND = "kick {player}";
    public static final Long DEFAULT_PLAY_TIME = 86400L;
    public static final Integer DEFAULT_MISTAKES = 2;
    public static final boolean DEFAULT_ENABLE_LONG_TIME_PUNISH = true;
    public static final boolean DEFAULT_RESET_COUNT_LONG_TIME_PUNISH = true;
    public static final String DEFAULT_MESSAGE_LONG_TIME_PUNISH_COMMAND = "Player {player} has been kicked for {mistakes} mistakes.";
    public static final String DEFAULT_COMMAND_LONG_TIME = "kick {player}";
    public static final Long DEFAULT_LONG_TIME_PLAY_TIME = 86400L;
    public static final Integer DEFAULT_LONG_TIME_MISTAKES = 4;

    MelooonCensor plugin;
    Client bugsnag;
    Filter filter;
    boolean enabled;
    boolean bypass;
    String language;
    Translation translation;
    String type;
    char _char;
    List<String> censor;
    List<String> ignore;
    String message;
    boolean enabled_punish;
    String messagePermission;
    String command;
    Long play_time;
    Integer mistakes;
    boolean enabledLongTimePunish;
    boolean resetCountLongTimePunish;
    String messageLongTimePermission;
    String commandLongTime;
    Long longTimePlayTime;
    Integer longTimeMistakes;

    public Configuration(MelooonCensor plugin, Client bugsnag) {
        this.plugin = plugin;
        this.bugsnag = bugsnag;
        load();
    }

    protected FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    private void addDefaults() {
        getConfig().options().header("MelooonCensor Configuration");
        getConfig().addDefault(ENABLE, DEFAULT_ENABLE);
        getConfig().addDefault(BYPASS, DEFAULT_BYPASS);
        getConfig().addDefault(LANGUAGE, DEFAULT_LANGUAGE);
        getConfig().addDefault(TYPE, DEFAULT_TYPE);
        getConfig().addDefault(CHAR, DEFAULT_CHAR);
        getConfig().addDefault(CENSOR, DEFAULT_CENSOR);
        getConfig().addDefault(IGNORE, DEFAULT_IGNORE);
        getConfig().addDefault(MESSAGE, DEFAULT_MESSAGE);
        getConfig().addDefault(ENABLE_PUNISH, DEFAULT_PUNISH_ENABLE);
        getConfig().addDefault(MESSAGE_PUNISH_COMMAND, DEFAULT_PUNISH_MESSAGE);
        getConfig().addDefault(COMMAND, DEFAULT_PUNISH_COMMAND);
        getConfig().addDefault(PLAY_TIME, DEFAULT_PLAY_TIME);
        getConfig().addDefault(MISTAKES, DEFAULT_MISTAKES);
        getConfig().addDefault(ENABLE_LONG_TIME_PUNISH, DEFAULT_ENABLE_LONG_TIME_PUNISH);
        getConfig().addDefault(RESET_COUNT_LONG_TIME_PUNISH, DEFAULT_RESET_COUNT_LONG_TIME_PUNISH);
        getConfig().addDefault(MESSAGE_LONG_TIME_PUNISH_COMMAND, DEFAULT_MESSAGE_LONG_TIME_PUNISH_COMMAND);
        getConfig().addDefault(COMMAND_LONG_TIME, DEFAULT_COMMAND_LONG_TIME);
        getConfig().addDefault(LONG_TIME_PLAY_TIME, DEFAULT_LONG_TIME_PLAY_TIME);
        getConfig().addDefault(LONG_TIME_MISTAKES, DEFAULT_LONG_TIME_MISTAKES);

        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void loadConfig() {
        plugin.reloadConfig();
        setEnabled(getConfig().getBoolean(ENABLE));
        setBypass(getConfig().getBoolean(BYPASS));
        setLanguage(getConfig().getString(LANGUAGE));
        setType(getConfig().getString(TYPE));
        setCharString(getConfig().getString(CHAR));
        setCensor(getConfig().getStringList(CENSOR));
        setIgnore(getConfig().getStringList(IGNORE));
        setMessage(getConfig().getString(MESSAGE));
        setEnabledPunish(getConfig().getBoolean(ENABLE_PUNISH));
        setMessagePermission(getConfig().getString(MESSAGE_PUNISH_COMMAND));
        setCommand(getConfig().getString(COMMAND));
        setTime(getConfig().getLong(PLAY_TIME));
        setMistakes(getConfig().getInt(MISTAKES));
        setEnabledLongTimePunish(getConfig().getBoolean(ENABLE_LONG_TIME_PUNISH));
        setResetCountLongTimePunish(getConfig().getBoolean(RESET_COUNT_LONG_TIME_PUNISH));
        setMessagePermissionLong(getConfig().getString(MESSAGE_LONG_TIME_PUNISH_COMMAND));
        setCommandLong(getConfig().getString(COMMAND_LONG_TIME));
        setTimeLong(getConfig().getLong(LONG_TIME_PLAY_TIME));
        setMistakesLong(getConfig().getInt(LONG_TIME_MISTAKES));
    }

    private void saveConfig() {
        plugin.saveConfig();
    }

    public void save() {
        getConfig().set(ENABLE, enabled);
        getConfig().set(BYPASS, bypass);
        getConfig().set(LANGUAGE, language);
        getConfig().set(TYPE, type);
        getConfig().set(CHAR, _char);
        getConfig().set(CENSOR, censor);
        getConfig().set(IGNORE, ignore);
        getConfig().set(MESSAGE, message);
        getConfig().set(ENABLE_PUNISH, enabled_punish);
        getConfig().set(MESSAGE_PUNISH_COMMAND, messagePermission);
        getConfig().set(COMMAND, command);
        getConfig().set(PLAY_TIME, play_time);
        getConfig().set(MISTAKES, mistakes);
        getConfig().set(ENABLE_LONG_TIME_PUNISH, enabledLongTimePunish);
        getConfig().set(RESET_COUNT_LONG_TIME_PUNISH, resetCountLongTimePunish);
        getConfig().set(MESSAGE_LONG_TIME_PUNISH_COMMAND, messageLongTimePermission);
        getConfig().set(COMMAND_LONG_TIME, commandLongTime);
        getConfig().set(LONG_TIME_PLAY_TIME, longTimePlayTime);
        getConfig().set(LONG_TIME_MISTAKES, longTimeMistakes);

        saveConfig();
    }

    public void load() {
        addDefaults();
        loadConfig();
    }

    public void reload() {
        loadConfig();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setBypass(boolean bypass) {
        this.bypass = bypass;
    }

    public boolean allowBypass() {
        return bypass;
    }

    public boolean allowBypass(Player player) {
        return allowBypass() && player.hasPermission("meloooncensor.bypass");
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
        updateTranslation();
    }

    public void updateTranslation() {
        translation = new Translation(language);
    }

    public Translation getTranslation() {
        return translation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        updateFilter();
    }

    public Filter getFilter() {
        return filter;
    }

    private void updateFilter() {
        switch (getType()) {
            case "strict":
                // StrictFilter extends from ClassicFilter's detection technique, but doesn't allow any messages
                // that violate the policy from sending out publicly.
                filter = new StrictFilter(this);
                break;
            case "classic":
                // ClassicFilter takes any words that contain/are censored words, which aren't ignored words,
                // and censors the whole word.
                filter = new ClassicFilter(this);
                break;
            case "word":
                // WordFilter censor only exact word. List of ignored words are ignored in this case.
                filter = new WordFilter(this);
                break;
            default:
                try {
                    Class<?> unknownClass = Class.forName(getType());

                    if (unknownClass.isAssignableFrom(Filter.class)) {
                        // The filter implementation should always accept a single argument in it's constructor
                        filter = (Filter) unknownClass.getDeclaredConstructor(Configuration.class).newInstance(this);
                    }
                } catch (Exception e) {
                    // Not that there is anything we can do at this point, as this error is only ever caused by
                    // human error (on the server admin's side), but it may help notifying 3rd parties that their
                    // filters are incorrectly being utilised (in the case of a recognisable class name).
                    bugsnag.notify(e);

                    // The best thing to do here is activate the strict filter, due to the fact it completely
                    // prevents censored messages from being visible to players (a safe fallback)
                    filter = new StrictFilter(this);
                }
                break;
        }
    }

    public String getCharString() {
        return String.valueOf(getChar());
    }

    public void setCharString(String _char) {
        if (_char != null) {
            this._char = _char.charAt(0);
        }
    }

    public char getChar() {
        return _char;
    }

    public void setChar(char _char) {
        this._char = _char;
    }

    public List<String> getCensor() {
        return censor;
    }

    public void setCensor(List<String> censor) {
        this.censor = censor;
    }

    public boolean addCensor(String word) {
        if (!censor.contains(word)) {
            censor.add(word);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeCensor(String word) {
        if (censor.contains(word)) {
            censor.remove(word);
            return true;
        } else {
            return false;
        }
    }

    public void clearCensor() {
        censor.clear();
    }

    public List<String> getIgnore() {
        return ignore;
    }

    public void setIgnore(List<String> ignore) {
        this.ignore = ignore;
    }

    public boolean addIgnore(String word) {
        if (!ignore.contains(word)) {
            ignore.add(word);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeIgnore(String word) {
        if (ignore.contains(word)) {
            ignore.remove(word);
            return true;
        } else {
            return false;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFormattedMessage(HashMap<String, String> values) {
        // Translate the alt color codes first, in case of user input
        String message = ChatColor.translateAlternateColorCodes('&', getMessage());

        if (values != null && values.size() > 0) {
            for (String key : values.keySet()) {
                // {player} => jacoooooooooooob
                message = message.replaceAll("\\{" + key + "\\}", values.get(key));
            }
        }

        return message;
    }

    public String getMessagePermission() {
        return messagePermission;
    }

    public void setMessagePermission(String messagePermission) {
        this.messagePermission = messagePermission;
    }

    public String getFormattedMessagePermission(String playerName, Integer mistakes) {
        // Translate the alt color codes first, in case of user input
        String messagePermission = ChatColor.translateAlternateColorCodes('&', getMessagePermission());
        messagePermission = messagePermission.replaceAll("\\{player}", playerName);
        messagePermission = messagePermission.replaceAll("\\{mistakes}", mistakes.toString());
        return messagePermission;
    }

    public boolean isEnabledPunish() {
        return enabled_punish;
    }

    public void setEnabledPunish(boolean enabled_punish) {
        this.enabled_punish = enabled_punish;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommandForPemission(HashMap<String, String> values) {
        // Translate the alt color codes first, in case of user input
        String command = ChatColor.translateAlternateColorCodes('&', getCommand());

        if (values != null && values.size() > 0) {
            for (String key : values.keySet()) {
                // {player} => jacoooooooooooob
                command = command.replaceAll("\\{" + key + "}", values.get(key));

            }
        }

        return command;
    }

    public long getTime() {
        return play_time;
    }

    public void setTime(long play_time) {
        this.play_time = play_time;
    }

    public Integer getMistakes() {
        return mistakes;
    }

    public void setMistakes(Integer mistakes) {
        this.mistakes = mistakes;
    }

    public boolean isEnabledLongTimePunish() {
        return enabledLongTimePunish;
    }

    public void setEnabledLongTimePunish(boolean enabled_long_time_punish) {
        this.enabledLongTimePunish = enabled_long_time_punish;
    }

    public boolean isResetCountLongTimePunish() {
        return resetCountLongTimePunish;
    }

    public void setResetCountLongTimePunish(boolean resetCountLongTimePunish) {
        this.resetCountLongTimePunish = resetCountLongTimePunish;
    }

    public String getMessagePermissionLong() {
        return messageLongTimePermission;
    }

    public void setMessagePermissionLong(String messageLongTimePermission) {
        this.messageLongTimePermission = messageLongTimePermission;
    }

    public String getFormattedMessagePermissionLong(String playerName, Integer mistakes) {
        // Translate the alt color codes first, in case of user input
        String messagePermission = ChatColor.translateAlternateColorCodes('&', getMessagePermissionLong());
        messagePermission = messagePermission.replaceAll("\\{player}", playerName);
        messagePermission = messagePermission.replaceAll("\\{mistakes}", mistakes.toString());
        return messagePermission;
    }

    public String getCommandLong() {
        return commandLongTime;
    }

    public void setCommandLong(String commandLongTime) {
        this.commandLongTime = commandLongTime;
    }

    public String getCommandForPemissionLong(HashMap<String, String> values) {
        // Translate the alt color codes first, in case of user input
        String command = ChatColor.translateAlternateColorCodes('&', getCommandLong());

        if (values != null && values.size() > 0) {
            for (String key : values.keySet()) {
                // {player} => jacoooooooooooob
                command = command.replaceAll("\\{" + key + "}", values.get(key));

            }
        }

        return command;
    }

    public long getTimeLong() {
        return longTimePlayTime;
    }

    public void setTimeLong(long longTimePlayTime) {
        this.longTimePlayTime = longTimePlayTime;
    }

    public Integer getMistakesLong() {
        return longTimeMistakes;
    }

    public void setMistakesLong(Integer mistakes) {
        this.longTimeMistakes = mistakes;
    }
}
