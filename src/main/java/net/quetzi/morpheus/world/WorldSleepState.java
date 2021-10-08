package net.quetzi.morpheus.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.quetzi.morpheus.Morpheus;

import java.util.HashMap;
import java.util.Map.Entry;

public class WorldSleepState {
    private int dimension;
    private HashMap<String, Boolean> playerStatus;

    public WorldSleepState(int dimension) {
        this.dimension = dimension;
        this.playerStatus = new HashMap<>();
    }

    public int getPercentSleeping() {
        return (this.playerStatus.size() - this.getMiningPlayers() > 0) ?
                (this.getSleepingPlayers() > 0)
                        ? (this.getSleepingPlayers() * 100) / (this.playerStatus.size() - this.getMiningPlayers())
                        : 0
                : 100;
    }

    private int getMiningPlayers() {
        int miningPlayers = 0;

        for (EntityPlayer player : FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(this.dimension).playerEntities) {
            if (player.posY < Morpheus.groundLevel) {
                miningPlayers++;
            }
        }

        return !Morpheus.includeMiners ? miningPlayers : 0;
    }

    public int getSleepingPlayers() {
        int asleepCount = 0;

        for (Entry<String, Boolean> entry : this.playerStatus.entrySet()) {
            if (entry.getValue()) {
                asleepCount++;
            }
        }

        return asleepCount;
    }
}
