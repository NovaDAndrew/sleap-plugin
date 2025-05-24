package com.sleapplugin;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class LanguageManager {
    private final JavaPlugin plugin;
    private final String language;
    private YamlConfiguration langConfig;
    
    public LanguageManager(JavaPlugin plugin, String language) {
        this.plugin = plugin;
        this.language = language;
        loadLanguage();
    }
    
    private void loadLanguage() {
        String fileName = language + ".yml";
        
        try {
            File langFile = new File(plugin.getDataFolder(), "lang" + File.separator + fileName);
            
            if (!langFile.exists()) {
                langFile.getParentFile().mkdirs();
                
                InputStream defaultLangStream = plugin.getResource("lang/" + fileName);
                if (defaultLangStream != null) {
                    plugin.saveResource("lang/" + fileName, false);
                } else {
                    plugin.getLogger().warning("Could not find default language file: " + fileName);
                }
            }
            
            if (langFile.exists()) {
                langConfig = YamlConfiguration.loadConfiguration(langFile);
                
                InputStream defaultLangStream = plugin.getResource("lang/" + fileName);
                if (defaultLangStream != null) {
                    YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                            new InputStreamReader(defaultLangStream, StandardCharsets.UTF_8));
                    langConfig.setDefaults(defaultConfig);
                }
            } else {
                InputStream defaultLangStream = plugin.getResource("lang/" + fileName);
                if (defaultLangStream != null) {
                    langConfig = YamlConfiguration.loadConfiguration(
                            new InputStreamReader(defaultLangStream, StandardCharsets.UTF_8));
                } else {
                    langConfig = new YamlConfiguration();
                    plugin.getLogger().severe("Failed to load language file: " + fileName);
                }
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error loading language file", e);
            langConfig = new YamlConfiguration();
        }
    }
    
    public String getMessage(String key) {
        if (langConfig.contains(key)) {
            return langConfig.getString(key);
        } else {
            plugin.getLogger().warning("Missing language key: " + key);
            return "Missing text for: " + key;
        }
    }
    
    public String getMessage(String key, Object... args) {
        String message = getMessage(key);
        if (message != null && args != null && args.length > 0) {
            return String.format(message, args);
        }
        return message;
    }
}
