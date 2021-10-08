package net.quetzi.morpheus.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.quetzi.morpheus.Morpheus;

import java.util.HashMap;

public class SleepChecker {
    private HashMap<Integer, Boolean> alertSent = new HashMap<Integer, Boolean>();

    public void updatePlayerStates(World world) {
        // Iterate players and update their status
        for (EntityPlayer player : world.playerEntities) {
            String username = player.getGameProfile().getName();

            if (player.isPlayerFullyAsleep() && !Morpheus.playerSleepStatus.get(player.dimension))
        }
    }
}
