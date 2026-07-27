package me.kaitlily.nameable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import me.kaitlily.nameable.mixed.MixedTileEntity;

public class EventHandler {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        EntityPlayer player = event.entityPlayer;
        World world = player.worldObj;
        ItemStack itemStack = player.getHeldItem();

        if (itemStack == null || world.isRemote || !player.isSneaking()) {
            return;
        }

        TileEntity tileEntity = world.getTileEntity(event.x, event.y, event.z);

        if (!(itemStack.getItem() instanceof ItemNameTag) || !itemStack.hasDisplayName()
            || !(tileEntity instanceof MixedTileEntity)) {
            return;
        }

        String name = itemStack.getDisplayName();
        ((MixedTileEntity) tileEntity).nameable$setName(name);
        tileEntity.markDirty();

        CommonProxy.NETWORK_WRAPPER.sendToAllAround(
            new MessageTileEntityNameChange(event.x, event.y, event.z, name),
            new NetworkRegistry.TargetPoint(world.provider.dimensionId, event.x, event.y, event.z, 64));
    }

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
