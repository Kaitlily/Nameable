package me.kaitlily.nameable.mixins;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.kaitlily.nameable.mixed.MixedTileEntity;

@Mixin(TileEntity.class)
public abstract class MixinTileEntity implements MixedTileEntity {

    @Unique
    public String nameable$tileEntityName = "";

    @Inject(method = "readFromNBT", at = @At("HEAD"))
    public void nameable$readNameFromNBT(NBTTagCompound compound, CallbackInfo ci) {
        this.nameable$tileEntityName = compound.getString("tileEntityName");
    }

    @Inject(method = "writeToNBT", at = @At("HEAD"))
    public void nameable$writeNameToNBT(NBTTagCompound compound, CallbackInfo ci) {
        compound.setString("tileEntityName", nameable$tileEntityName);
    }

    @Override
    public String nameable$getName() {
        return nameable$tileEntityName;
    }

    @Override
    public void nameable$setName(String name) {
        this.nameable$tileEntityName = name;
    }

    @Override
    public boolean nameable$hasName() {
        return !nameable$tileEntityName.isEmpty();
    }
}
