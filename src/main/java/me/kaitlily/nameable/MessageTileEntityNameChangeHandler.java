package me.kaitlily.nameable;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import me.kaitlily.nameable.mixed.MixedTileEntity;

public class MessageTileEntityNameChangeHandler implements IMessageHandler<MessageTileEntityNameChange, IMessage> {

    @Override
    public IMessage onMessage(MessageTileEntityNameChange message, MessageContext ctx) {
        World world = Minecraft.getMinecraft().theWorld;
        TileEntity te = world.getTileEntity(message.x, message.y, message.z);
        if (te instanceof MixedTileEntity) {
            ((MixedTileEntity) te).nameable$setName(message.name);
        }
        return null;
    }
}
