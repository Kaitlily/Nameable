package me.kaitlily.nameable.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.kaitlily.nameable.mixed.MixedTileEntity;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

    @Inject(
        method = "drawSelectionBox",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/RenderGlobal;drawOutlinedBoundingBox(Lnet/minecraft/util/AxisAlignedBB;I)V",
            shift = At.Shift.AFTER))
    private void nameable$NameForTileEntity(EntityPlayer player, MovingObjectPosition target, int subID,
        float partialTicks, CallbackInfo ci) {
        MixedTileEntity tileEntity = (MixedTileEntity) player.getEntityWorld()
            .getTileEntity(target.blockX, target.blockY, target.blockZ);

        if (subID != 0 || target.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK
            || tileEntity == null
            || !tileEntity.nameable$hasName()) return;

        String text = tileEntity.nameable$getName();

        FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        RenderManager rm = RenderManager.instance;

        double dx = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
        double dy = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
        double dz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;

        float tx = target.blockX + 0.5F - (float) dx;
        float ty = target.blockY + 1.25F - (float) dy;
        float tz = target.blockZ + 0.5F - (float) dz;

        float viewScale = 1.6F;
        float scale = 0.016666668F * viewScale;

        GL11.glPushMatrix();
        GL11.glTranslatef(tx, ty, tz);
        GL11.glRotatef(-rm.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(rm.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scale, -scale, scale);

        GL11.glDisable(GL11.GL_DEPTH_TEST);

        Tessellator tess = Tessellator.instance;
        int halfW = fr.getStringWidth(text) / 2;

        tess.startDrawingQuads();
        tess.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
        tess.addVertex(-halfW - 1, -1.0D, 0.0D);
        tess.addVertex(-halfW - 1, 8.0D, 0.0D);
        tess.addVertex(halfW + 1, 8.0D, 0.0D);
        tess.addVertex(halfW + 1, -1.0D, 0.0D);
        tess.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        fr.drawString(text, -halfW, 0, 0x20FFFFFF);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        fr.drawString(text, -halfW, 0, -1);

        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glPopMatrix();
    }
}
