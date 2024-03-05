package vip.floatationdevice.msu.damagelimit;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import vip.floatationdevice.msu.damagelimit.DLAlgorithm.Type;

import static vip.floatationdevice.msu.damagelimit.DLAlgorithm.lmtLinear;
import static vip.floatationdevice.msu.damagelimit.DLAlgorithm.lmtLogarithmic;
import static vip.floatationdevice.msu.damagelimit.DamageLimit.*;

public class DLEventListener implements Listener
{
    private static final String BYPASS_PERM = "damagelimit.bypass";
    private final Type algo;
    private final double T, F;
    private final boolean notifyConsole;

    public DLEventListener()
    {
        T = cm.get(Number.class, "threshold").doubleValue();
        F = cm.get(Number.class, "factor").doubleValue();

        String a = cm.get(String.class, "algorithm").toLowerCase();
        if(a.equalsIgnoreCase("linear"))
            algo = Type.LINEAR;
        else if(a.equalsIgnoreCase("logarithmic"))
            algo = Type.LOGARITHMIC;
        else
            throw new RuntimeException("Key \"algorithm\" has invalid value \"" + a + "\" - only \"linear\" and \"logarithmic\" allowed");

        notifyConsole = cm.get(Boolean.class, "notifyConsole");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent event)
    {
        if(event.isCancelled())
            return;

        double damage = event.getDamage();

        // skip events whose damage is not greater than the threshold
        if(damage <= T)
            return;

        Entity damager = event.getDamager(); // damage source: entity who caused the damage
        boolean doLimit = false; // should limit the damage or not

        // check the damage source
        // caused by player
        if(damager instanceof Player)
        {
            // skip for players who have the bypass permission
            if(damager.hasPermission(BYPASS_PERM))
                return;
            // limit the damage
            doLimit = true;
        }
        // caused by projectiles created by player
        else if(damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Player)
        {
            // damage source is the player who shoots
            damager = (Player) ((Projectile) damager).getShooter();
            // skip for players who have the bypass permission
            if(damager.hasPermission(BYPASS_PERM))
                return;
            // limit the damage
            doLimit = true;
        }

        if(doLimit)
        {
            double limitedDamage = algo == Type.LINEAR ? lmtLinear(damage, T, F) : lmtLogarithmic(damage, T, F);
            event.setDamage(limitedDamage);
            if(notifyConsole)
                log.info(i18n.translate("damage-limited-console")
                        .replace("{0}", damager.getName())
                        .replace("{1}", String.valueOf(damage))
                        .replace("{2}", String.valueOf(limitedDamage))
                );
        }
    }
}
