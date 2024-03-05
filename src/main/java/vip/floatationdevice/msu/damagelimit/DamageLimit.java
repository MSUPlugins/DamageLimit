package vip.floatationdevice.msu.damagelimit;

import org.bukkit.plugin.java.JavaPlugin;
import vip.floatationdevice.msu.ConfigManager;
import vip.floatationdevice.msu.I18nManager;

import java.util.logging.Logger;

public final class DamageLimit extends JavaPlugin
{
    static DamageLimit instance;
    static Logger log;
    static ConfigManager cm;
    static I18nManager i18n;

    @Override
    public void onEnable()
    {
        instance = this;
        log = getLogger();
        cm = new ConfigManager(this, 1).initialize();
        i18n = new I18nManager(this).setLanguage(cm.get(String.class, "language"));

        getServer().getPluginManager().registerEvents(new DLEventListener(), this);

        log.info("DamageLimit has been loaded");
    }

    @Override
    public void onDisable()
    {
        log.info("DamageLimit is being disabled");
    }
}
