package org.pixeltime.enchantmentsenhance.events.enchantments;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.pixeltime.enchantmentsenhance.manager.SettingsManager;

import java.util.ArrayList;

public class Snare implements Listener {
    private ArrayList<Projectile> special;
    private ArrayList<Projectile> special2;
    private ArrayList<Projectile> special3;
    private ArrayList<Projectile> special4;

    public Snare() {
        this.special = new ArrayList<Projectile>();
        this.special2 = new ArrayList<Projectile>();
        this.special3 = new ArrayList<Projectile>();
        this.special4 = new ArrayList<Projectile>();
    }

    @EventHandler
    public void onProjectileLaunch(final ProjectileLaunchEvent projectileLaunchEvent) {
        final String translateAlternateColorCodes = ChatColor.translateAlternateColorCodes('&', SettingsManager.lang.getString("enchantments." + "snare"));
        if (projectileLaunchEvent.getEntity() instanceof Arrow) {
            final Arrow arrow = (Arrow) projectileLaunchEvent.getEntity();
            if (arrow.getShooter() instanceof Player) {
                final Player player = (Player) arrow.getShooter();
                try {
                    if (player.getItemInHand().getItemMeta().getLore().contains(String.valueOf(translateAlternateColorCodes) + " I")) {
                        this.special.add(arrow);
                    }
                    if (player.getItemInHand().getItemMeta().getLore().contains(String.valueOf(translateAlternateColorCodes) + " II")) {
                        this.special2.add(arrow);
                    }
                    if (player.getItemInHand().getItemMeta().getLore().contains(String.valueOf(translateAlternateColorCodes) + " III")) {
                        this.special3.add(arrow);
                    }
                    if (player.getItemInHand().getItemMeta().getLore().contains(String.valueOf(translateAlternateColorCodes) + " IV")) {
                        this.special4.add(arrow);
                    }
                } catch (Exception ex) {
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(final EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (entityDamageByEntityEvent.getEntity() instanceof LivingEntity) {
            final LivingEntity livingEntity = (LivingEntity) entityDamageByEntityEvent.getEntity();
            if (entityDamageByEntityEvent.getDamager() instanceof Projectile) {
                final Projectile projectile = (Projectile) entityDamageByEntityEvent.getDamager();
                if (projectile.getShooter() instanceof Player) {
                    final Player player = (Player) projectile.getShooter();
                    if (entityDamageByEntityEvent.isCancelled()) {
                        return;
                    }
                    if (SettingsManager.enchant.getBoolean("allow-worldguard") && WGBukkit.getRegionManager(livingEntity.getWorld()).getApplicableRegions(livingEntity.getLocation()).queryState(null, new StateFlag[]{DefaultFlag.PVP}) == StateFlag.State.DENY) {
                        return;
                    }
                    if (this.special.contains(projectile)) {
                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, SettingsManager.enchant.getInt("snare.level_I.duration") * 20, SettingsManager.enchant.getInt("snare.level_I.potion_lvl") - 1));
                    }
                    if (this.special2.contains(projectile)) {
                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, SettingsManager.enchant.getInt("snare.level_II.duration") * 20, SettingsManager.enchant.getInt("snare.level_II.potion_lvl") - 1));
                    }
                    if (this.special3.contains(projectile)) {
                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, SettingsManager.enchant.getInt("snare.level_III.duration") * 20, SettingsManager.enchant.getInt("snare.level_III.potion_lvl") - 1));
                    }
                    if (this.special4.contains(projectile)) {
                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, SettingsManager.enchant.getInt("snare.level_IV.duration") * 20, SettingsManager.enchant.getInt("snare.level_IV.potion_lvl") - 1));
                    }
                }
            }
        }
    }
}
