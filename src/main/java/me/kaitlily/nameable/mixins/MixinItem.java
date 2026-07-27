package me.kaitlily.nameable.mixins;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import cpw.mods.fml.common.network.NetworkRegistry;
import me.kaitlily.nameable.CommonProxy;
import me.kaitlily.nameable.MessageTileEntityNameChange;
import me.kaitlily.nameable.mixed.MixedTileEntity;

@Mixin(Item.class)
public class MixinItem {

    @Inject(method = "onItemUse", at = @At("TAIL"))
    private void nameable$addNameToTileEntity(ItemStack itemStack, EntityPlayer player, World world, int x, int y,
        int z, int side, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> cir) {
        TileEntity tileEntity = player.getEntityWorld()
            .getTileEntity(x, y, z);
        if (itemStack.getItem() instanceof ItemNameTag && itemStack.hasDisplayName()
            && !world.isRemote
            && player.isSneaking()
            && tileEntity != null) {
            String name = itemStack.getDisplayName();
            ((MixedTileEntity) tileEntity).nameable$setName(name);
            tileEntity.markDirty();
            CommonProxy.NETWORK_WRAPPER.sendToAllAround(
                new MessageTileEntityNameChange(x, y, z, name),
                new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 64));
        }
    }
}
