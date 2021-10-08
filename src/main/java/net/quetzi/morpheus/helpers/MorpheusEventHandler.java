package net.quetzi.morpheus.helpers;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.quetzi.morpheus.Morpheus;

public class MorpheusEventHandler {
    @SubscribeEvent
    public void loggedInEvent(PlayerLoggedInEvent event) {
        if (!event.player.getEntityWorld().getMinecraftServer().getWorld(event.player.dimension).isRemote) {
            if (Morpheus.pla)
        }
    }
}
