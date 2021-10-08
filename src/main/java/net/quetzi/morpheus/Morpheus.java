package net.quetzi.morpheus;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.quetzi.morpheus.helpers.MorpheusEventHandler;
import net.quetzi.morpheus.world.WorldSleepState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Mod(modid = Morpheus.MODID, name = Morpheus.NAME, version = Morpheus.VERSION)
public class Morpheus {
    public static final String MODID = "morpheus";
    public static final String NAME = "Morpheus";
    public static final String VERSION = "1.0";

    public static Morpheus instance;
    public static Logger logger = LogManager.getLogger(MODID);

    public static int groundLevel;
    public static boolean includeMiners;

    public static final HashMap<Integer, WorldSleepState> playerSleepStatus = new HashMap<>();

    public Morpheus() {
        instance = this;

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new MorpheusEventHandler());
    }
}
