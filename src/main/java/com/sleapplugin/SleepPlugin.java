package com.sleapplugin;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SleepPlugin extends JavaPlugin implements Listener {
    
    private final Map<World, Set<Player>> sleepingPlayers = new HashMap<>();
    private final Map<World, BukkitRunnable> sleepTasks = new HashMap<>();
    
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        
        getLogger().info("SleepPlugin включен! Теперь для пропуска ночи нужна только половина игроков.");
    }
    
    @Override
    public void onDisable() {
        for (BukkitRunnable task : sleepTasks.values()) {
            if (task != null && !task.isCancelled()) {
                task.cancel();
            }
        }
        sleepTasks.clear();
        sleepingPlayers.clear();
        
        getLogger().info("SleepPlugin отключен!");
    }
    
    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (event.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK) {
            return;
        }
        
        Player player = event.getPlayer();
        World world = player.getWorld();
        
        if (!isNightOrStorm(world)) {
            return;
        }
        
        sleepingPlayers.computeIfAbsent(world, k -> new HashSet<>()).add(player);
        
        checkSleepRequirement(world);
    }
    
    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        
        Set<Player> sleeping = sleepingPlayers.get(world);
        if (sleeping != null) {
            sleeping.remove(player);
            if (sleeping.isEmpty()) {
                sleepingPlayers.remove(world);
            }
        }
    
        BukkitRunnable task = sleepTasks.get(world);
        if (task != null && !task.isCancelled()) {
            task.cancel();
            sleepTasks.remove(world);
            
            broadcastToWorld(world, Component.text("Пропуск ночи отменен - игрок проснулся!", NamedTextColor.YELLOW));
        }
    }
    
    private void checkSleepRequirement(World world) {
        Set<Player> sleeping = sleepingPlayers.get(world);
        if (sleeping == null || sleeping.isEmpty()) {
            return;
        }
        
        int onlinePlayersInWorld = (int) world.getPlayers().stream()
                .filter(p -> !p.isSleepingIgnored())
                .count();
        
        if (onlinePlayersInWorld == 0) {
            return;
        }
        
        int requiredSleeping = calculateRequiredSleeping(onlinePlayersInWorld);
        int currentSleeping = sleeping.size();
        
        if (currentSleeping >= requiredSleeping) {
            startNightSkip(world, currentSleeping, onlinePlayersInWorld);
        } else {
            String message = String.format("Спят %d из %d необходимых игроков для пропуска ночи", 
                    currentSleeping, requiredSleeping);
            broadcastToWorld(world, Component.text(message, NamedTextColor.AQUA));
        }
    }
    
    private int calculateRequiredSleeping(int onlinePlayers) {
        if (onlinePlayers == 2) {
            return 1;
        }
        else if (onlinePlayers % 2 == 1) {
            return (onlinePlayers - 1) / 2;
        } else {
            return onlinePlayers / 2;
        }
    }
    
    private void startNightSkip(World world, int sleepingCount, int totalCount) {
        BukkitRunnable existingTask = sleepTasks.get(world);
        if (existingTask != null && !existingTask.isCancelled()) {
            existingTask.cancel();
        }
        

        String message = String.format("Пропуск ночи через 3 секунды... (%d/%d игроков спят)", 
                sleepingCount, totalCount);
        broadcastToWorld(world, Component.text(message, NamedTextColor.GREEN));
        
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                Set<Player> currentSleeping = sleepingPlayers.get(world);
                if (currentSleeping == null || currentSleeping.isEmpty()) {
                    sleepTasks.remove(world);
                    return;
                }
                
                int currentOnline = (int) world.getPlayers().stream()
                        .filter(p -> !p.isSleepingIgnored())
                        .count();
                
                if (currentSleeping.size() >= calculateRequiredSleeping(currentOnline)) {
                    if (world.isThundering()) {
                        world.setThundering(false);
                        world.setStorm(false);
                    }
                    
                    if (isNight(world)) {
                        world.setTime(1000);
                    }
                    
                    broadcastToWorld(world, Component.text("Ночь пропущена! Доброе утро!", NamedTextColor.GOLD));
                    
                    sleepingPlayers.remove(world);
                }
                
                sleepTasks.remove(world);
            }
        };
        
        task.runTaskLater(this, 60L); 
        sleepTasks.put(world, task);
    }
    
    private boolean isNightOrStorm(World world) {
        return isNight(world) || world.hasStorm();
    }
    
    private boolean isNight(World world) {
        long time = world.getTime();
        return time >= 12541 && time <= 23458; 
    }
    
    private void broadcastToWorld(World world, Component message) {
        for (Player player : world.getPlayers()) {
            player.sendMessage(message);
        }
    }
}