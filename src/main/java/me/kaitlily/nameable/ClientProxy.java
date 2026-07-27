package me.kaitlily.nameable;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {

    // Override CommonProxy methods here, if you want a different behaviour on the client (e.g. registering renders).
    // Don't forget to call the super methods as well.

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        NETWORK_WRAPPER.registerMessage(
            MessageTileEntityNameChangeHandler.class,
            MessageTileEntityNameChange.class,
            0,
            Side.CLIENT);
        super.preInit(event);
    }
}
