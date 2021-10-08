package net.quetzi.morpheus.world;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.quetzi.morpheus.api.INewDayHandler;

public class DefaultOverworldHandler implements INewDayHandler {
    private static final long dayLength = 2400;

    @Override
    public void startNewDay() {
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
        world.setWorldTime(world.getWorldTime() + getTimeToSunrise(world));
    }

    private long getTimeToSunrise(World world) {
        return dayLength - (world.getWorldTime() % dayLength);
    }
}
