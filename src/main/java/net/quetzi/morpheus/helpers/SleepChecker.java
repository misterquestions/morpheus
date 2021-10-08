package net.quetzi.morpheus.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.quetzi.morpheus.Morpheus;
import net.quetzi.morpheus.MorpheusRegistry;

import java.util.HashMap;

public class SleepChecker {
    private HashMap<Integer, Boolean> alertSent = new HashMap<Integer, Boolean>();

    public void updatePlayerStates(World world) {
        // Iterate players and update their status
        for (EntityPlayer player : world.playerEntities) {
            String username = player.getGameProfile().getName();

            if (player.isPlayerFullyAsleep() && !Morpheus.playerSleepStatus.get(player.dimension).isPlayerSleeping(username)) {
                Morpheus.playerSleepStatus.get(player.dimension).setPlayerAsleep(username);
                alertPlayers(createAlert(player.dimension, player.getDisplayNameString(), Morpheus.onSleepText), world);
            }
            else if (!player.isPlayerFullyAsleep() && Morpheus.playerSleepStatus.get(player.dimension).isPlayerSleeping(username)) {
                Morpheus.playerSleepStatus.get(player.dimension).setPlayerAwake(username);

                if (!world.isDaytime() && !alertSent.get(world.provider.getDimension())) {
                    alertPlayers(createAlert(player.dimension, player.getDisplayNameString(), Morpheus.onWakeText), world);
                }
            }

            if (areEnoughPlayersAsleep(world.provider.getDimension())) {
                if (!alertSent.containsKey(world.provider.getDimension()))
                    alertSent.put(world.provider.getDimension(), true);

                advanceToMorning(world);
            }
            else {
                alertSent.put(world.provider.getDimension(), false);
            }
        }
    }

    private void alertPlayers(TextComponentString alert, World world) {
        if (Morpheus.alertEnabled && alert != null) {
            for (EntityPlayer player : world.playerEntities) {
                player.sendMessage(alert);
            }
        }
    }

    private TextComponentString createAlert(int dimension, String username, String text) {
        String alertText = TextFormatting.WHITE + username + TextFormatting.GOLD + " " + text + " "
                + Morpheus.playerSleepStatus.get(dimension).toString();
        Morpheus.logger.info(username + " " + text + " " + Morpheus.playerSleepStatus.get(dimension).toString());
        return new TextComponentString(alertText);
    }

    private boolean areEnoughPlayersAsleep(int dimension)
    {
        if (Morpheus.playerSleepStatus.get(dimension).getSleepingPlayers() > 0)
        {
            return ((dimension == 0) || (MorpheusRegistry.registry.get(dimension) != null)) && Morpheus.playerSleepStatus.get(dimension).getPercentSleeping() >= Morpheus.perc;
        }
        return false;
    }

    private void advanceToMorning(World world) {
        try {
            MorpheusRegistry.registry.get(world.provider.getDimension()).startNewDay();
        }
        catch (Exception e) {
            Morpheus.logger.error("Exception caught while starting a new day for dimension " + world.provider.getDimension());
        }

        if (!alertSent.get(world.provider.getDimension()))
        {
            // Send Good morning message
            alertPlayers(new TextComponentString(DateHandler.getMorningText()), world);
            Morpheus.playerSleepStatus.get(world.provider.getDimension()).wakeAllPlayers();
            alertSent.put(world.provider.getDimension(), true);
        }
        world.provider.resetRainAndThunder();
    }
}
