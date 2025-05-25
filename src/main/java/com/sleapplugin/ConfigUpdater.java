package com.sleapplugin;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.logging.Logger;

public class ConfigUpdater {
    
    private final JavaPlugin plugin;
    private final Logger logger;
    private final String currentVersion;
    
    public ConfigUpdater(JavaPlugin plugin, String currentVersion) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.currentVersion = currentVersion;
    }
    

    public boolean updateConfig(String fileName, boolean saveComments) {
        File configFile = new File(plugin.getDataFolder(), fileName);
        
        if (!configFile.exists()) {

            plugin.saveResource(fileName, false);
            return true;
        }
        

        if (!needsUpdate(configFile, fileName)) {
            return false;
        }
        
        logger.info("Updating " + fileName + " to version " + currentVersion);
        

        FileConfiguration currentConfig = YamlConfiguration.loadConfiguration(configFile);
        String oldVersion = currentConfig.getString("version", "unknown");
        

        InputStream defaultConfigStream = plugin.getResource(fileName);
        if (defaultConfigStream == null) {
            logger.warning("Could not find default " + fileName + " in plugin resources");
            return false;
        }
        
        FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                new InputStreamReader(defaultConfigStream));
        

        boolean updated = updateConfigValues(currentConfig, defaultConfig, "", saveComments);
        
        if (updated) {

            currentConfig.set("version", currentVersion);
            
            try {
                currentConfig.save(configFile);
                logger.info("Successfully updated " + fileName + " from v" + oldVersion + " to v" + currentVersion);
                return true;
            } catch (IOException e) {
                logger.severe("Could not save updated " + fileName + ": " + e.getMessage());
                return false;
            }
        }
        
        return false;
    }
    

    public boolean updateLanguageFile(String langCode) {
        String fileName = "lang/" + langCode + ".yml";
        File langFile = new File(plugin.getDataFolder(), fileName);
        File langDir = langFile.getParentFile();
        
        if (!langDir.exists()) {
            langDir.mkdirs();
        }
        
        if (!langFile.exists()) {

            plugin.saveResource(fileName, false);
            return true;
        }
        

        if (!needsUpdate(langFile, fileName)) {
            return false;
        }
        
        logger.info("Updating " + fileName + " to version " + currentVersion);
        

        FileConfiguration currentLang = YamlConfiguration.loadConfiguration(langFile);
        

        InputStream defaultLangStream = plugin.getResource(fileName);
        if (defaultLangStream == null) {
            logger.warning("Could not find default " + fileName + " in plugin resources");
            return false;
        }
        
        FileConfiguration defaultLang = YamlConfiguration.loadConfiguration(
                new InputStreamReader(defaultLangStream));
        

        boolean updated = updateConfigValues(currentLang, defaultLang, "", false);
        
        if (updated) {

            currentLang.set("version", currentVersion);
            
            try {
                currentLang.save(langFile);
                logger.info("Successfully updated " + fileName + " to v" + currentVersion);
                return true;
            } catch (IOException e) {
                logger.severe("Could not save updated " + fileName + ": " + e.getMessage());
                return false;
            }
        }
        
        return false;
    }
    

    private boolean needsUpdate(File file, String resourcePath) {

        FileConfiguration existingConfig = YamlConfiguration.loadConfiguration(file);
        String existingVersion = existingConfig.getString("version", "unknown");
        

        if (existingVersion.equals("unknown")) {
            return true;
        }
        

        InputStream defaultStream = plugin.getResource(resourcePath);
        if (defaultStream == null) {
            return false;
        }
        
        FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                new InputStreamReader(defaultStream));
        

        return hasMissingKeys(existingConfig, defaultConfig, "");
    }
    

    private boolean hasMissingKeys(FileConfiguration target, FileConfiguration source, String path) {
        if (path.isEmpty()) {

            for (String key : source.getKeys(false)) {
                if (key.equals("version")) {
                    continue;
                }
                
                if (!target.contains(key)) {
                    return true;
                }
                
                if (source.isConfigurationSection(key)) {
                    boolean missingInSection = hasMissingKeys(target, source, key);
                    if (missingInSection) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            ConfigurationSection sourceSection = source.getConfigurationSection(path);
            if (sourceSection == null) {
                return false;
            }
            
            for (String key : sourceSection.getKeys(false)) {
                String fullPath = path + "." + key;
                
                if (!target.contains(fullPath)) {
                    return true;
                }
                
                if (source.isConfigurationSection(fullPath)) {
                    boolean missingInSection = hasMissingKeys(target, source, fullPath);
                    if (missingInSection) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    

    private boolean updateConfigValues(FileConfiguration target, FileConfiguration source, 
                                      String path, boolean saveComments) {
        boolean updated = false;
        
        if (path.isEmpty()) {

            Set<String> keys = source.getKeys(false);
            for (String key : keys) {

                if (key.equals("version")) {
                    continue;
                }
                
                if (source.isConfigurationSection(key)) {
                    if (!target.isConfigurationSection(key)) {
                        target.createSection(key);
                        updated = true;
                    }
                    boolean sectionUpdated = updateConfigValues(target, source, key, saveComments);
                    updated = updated || sectionUpdated;
                } else if (!target.contains(key)) {
                    target.set(key, source.get(key));
                    updated = true;
                    logger.info("Added new config option: " + key);
                }
            }
        } else {
            ConfigurationSection sourceSection = source.getConfigurationSection(path);
            if (sourceSection == null) {
                return false;
            }
            
            for (String key : sourceSection.getKeys(false)) {
                String fullPath = path + "." + key;
                
                if (source.isConfigurationSection(fullPath)) {
                    if (!target.isConfigurationSection(fullPath)) {
                        target.createSection(fullPath);
                        updated = true;
                    }
                    boolean sectionUpdated = updateConfigValues(target, source, fullPath, saveComments);
                    updated = updated || sectionUpdated;
                } else if (!target.contains(fullPath)) {
                    target.set(fullPath, source.get(fullPath));
                    updated = true;
                    logger.info("Added new config option: " + fullPath);
                }
            }
        }
        
        return updated;
    }
}
