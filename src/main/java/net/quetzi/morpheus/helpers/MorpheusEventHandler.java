package net.quetzi.morpheus.helpers;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.quetzi.morpheus.Morpheus;
import net.quetzi.morpheus.world.WorldSleepState;

public class MorpheusEventHandler {
    @SubscribeEvent
    public void loggedInEvent(PlayerLoggedInEvent event) {
        if (!event.player.getEntityWorld().getMinecraftServer().getWorld(event.player.dimension).isRemote) {
            WorldSleepState worldSleepState = Morpheus.playerSleepStatus.get(event.player.dimension);

            if (worldSleepState == null) {
                worldSleepState = new WorldSleepState(event.player.dimension);
                Morpheus.playerSleepStatus.put(event.player.dimension, worldSleepState);
            }

            worldSleepState.setPlayerAwake(event.player.getGameProfile().getName());
        }
    }

    @SubscribeEvent
    public void loggedOutEvent(PlayerLoggedOutEvent event) {
        if (!event.player.getEntityWorld().getMinecraftServer().getWorld(event.player.dimension).isRemote) {
            WorldSleepState worldSleepState = Morpheus.playerSleepStatus.get(event.player.dimension);

            if (worldSleepState == null) {
                return;
            }

            worldSleepState.removePlayer(event.player.getGameProfile().getName());
        }
    }

    @SubscribeEvent
    public void changedDimensionEvent(PlayerChangedDimensionEvent event) {
        if (!event.player.getEntityWorld().getMinecraftServer().getWorld(event.player.dimension).isRemote) {
            // Ensure sleep state exists for this world
            if (!Morpheus.playerSleepStatus.containsKey(event.toDim)) {
                Morpheus.playerSleepStatus.put(event.toDim, new WorldSleepState(event.toDim));
            }

            // Remove player from old world state
            WorldSleepState oldWorldSleepState = Morpheus.playerSleepStatus.get(event.fromDim);

            if (oldWorldSleepState != null) {
                oldWorldSleepState.removePlayer(event.player.getGameProfile().getName());
            }

            // Add player to new world state
            Morpheus.playerSleepStatus.get(event.toDim).setPlayerAwake(event.player.getGameProfile().getName());
        }
    }

    @SubscribeEvent
    public void worldTickEvent(WorldTickEvent event) {
        if (!event.world.isRemote) {
            // This is called every tick, do something every 20 ticks
            if (event.world.getWorldTime() % 20L == 10 && event.phase == TickEvent.Phase.END) {
                if (event.world.playerEntities.size() > 0) {
                    final int worldDimension = event.world.provider.getDimension();

                    if (!Morpheus.playerSleepStatus.containsKey(worldDimension)) {
                        Morpheus.playerSleepStatus.put(worldDimension, new WorldSleepState(worldDimension));
                    }

                    // Todo: Implement checker logic here!
                }
            }
        }
    }
}
