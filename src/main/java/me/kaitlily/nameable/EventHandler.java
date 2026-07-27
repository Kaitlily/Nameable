package me.kaitlily.nameable;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkWatchEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import me.kaitlily.nameable.mixed.MixedTileEntity;

public class EventHandler {

    @SubscribeEvent
    public void onChunkWatch(ChunkWatchEvent.Watch event) {
        Chunk chunk = event.player.worldObj.getChunkFromChunkCoords(event.chunk.chunkXPos, event.chunk.chunkZPos);

        for (Object obj : chunk.chunkTileEntityMap.values()) {
            if (obj instanceof MixedTileEntity mixedTileEntity) {
                TileEntity tileEntity = (TileEntity) obj;

                CommonProxy.NETWORK_WRAPPER.sendTo(
                    new MessageTileEntityNameChange(
                        tileEntity.xCoord,
                        tileEntity.yCoord,
                        tileEntity.zCoord,
                        mixedTileEntity.nameable$getName()),
                    event.player);
            }
        }
    }
}
