package net.quetzi.morpheus;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.quetzi.morpheus.helpers.MorpheusEventHandler;
import net.quetzi.morpheus.helpers.References;
import net.quetzi.morpheus.helpers.SleepChecker;
import net.quetzi.morpheus.world.WorldSleepState;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.12,1.13)")
public class Morpheus {
    public static Logger logger;

    public static Configuration config;
    public static int sleepPercentaje;
    public static boolean alertEnabled;
    public static String onSleepText;
    public static String onWakeText;
    public static String onMorningText;
    public static boolean includeMiners;
    public static boolean ignoreOperators;
    public static int groundLevel;
    public static boolean setSpawnDaytime;

    public static MorpheusRegistry register = new MorpheusRegistry();
    public static final HashMap<Integer, WorldSleepState> playerSleepStatus = new HashMap<>();
    public static final SleepChecker checker = new SleepChecker();

    @EventHandler
    public void onPreInitEvent(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("Loading configuration");

        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        sleepPercentaje = config.get("settings", "SleeperPerc", 50).getInt();
        alertEnabled = config.get("settings", "AlertEnabled", true).getBoolean();
        onSleepText = config.get("settings", "OnSleepText", "is now sleeping.").getString();
        onWakeText = config.get("settings", "OnWakeText", "has left their bed.").getString();
        onMorningText = config.get("settings", "OnMorningText", "Wakey, wakey, rise and shine... Good Morning everyone!").getString();
        includeMiners = config.get("settings", "IncludeMiners", true).getBoolean();
        ignoreOperators = config.get("settings", "IgnoreOperators", true).getBoolean();
        groundLevel = config.getInt("settings", "GroundLevel", 64, 1, 255, "Ground Level (1-255)");
        setSpawnDaytime = config.get("settings", "AllowSetSpawnDaytime", true).getBoolean();
        config.save();
    }

    @EventHandler
    public void onPostInitEvent(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new MorpheusEventHandler());
    }
}
