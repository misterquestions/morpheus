package net.quetzi.morpheus.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.quetzi.morpheus.Morpheus;

import java.util.HashMap;
import java.util.Map.Entry;

public class WorldSleepState {
    private final int dimension;
    private final HashMap<String, Boolean> playerStatus;

    public WorldSleepState(int dimension) {
        this.dimension = dimension;
        this.playerStatus = new HashMap<>();
    }

    public void setPlayerAwake(String username) {
        this.playerStatus.put(username, false);
    }

    public void removePlayer(String username) {
        this.playerStatus.remove(username);
    }

    public boolean isPlayerSleeping(String username) {
        if (this.playerStatus.containsKey(username)) {
            return this.playerStatus.get(username);
        }

        this.playerStatus.put(username, false);
        return false;
    }

    public void setPlayerAsleep(String username) {
        this.playerStatus.put(username, true);
    }

    public String toString() {
        return getSleepingPlayers() + "/" + getRequiredPlayersToSleep() + " (" + getPercentSleeping() + "%)";
    }

    public int getRequiredPlayersToSleep() {
        return Math.max(playerStatus.size() - getMiningPlayers() - getIgnoredPlayers(), 0);
    }

    public int getPercentSleeping() {
        int requiredPlayerCount = getRequiredPlayersToSleep();

        return (requiredPlayerCount> 0) ?
                (this.getSleepingPlayers() > 0)
                        ? (this.getSleepingPlayers() * 100) / requiredPlayerCount
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

    private int getIgnoredPlayers() {
        int ignoredPlayers = 0;

        for (EntityPlayer player : FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(this.dimension).playerEntities) {
            if (player.canUseCommand(2, "") || player.isSpectator() || player.isCreative()) {
                ignoredPlayers++;
            }
        }

        return Morpheus.ignoreOperators ? ignoredPlayers : 0;
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

    public void wakeAllPlayers() {
        for (Entry<String, Boolean> entry : playerStatus.entrySet()) {
            entry.setValue(false);
        }
    }
}
