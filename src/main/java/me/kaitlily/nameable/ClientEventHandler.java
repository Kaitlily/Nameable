package me.kaitlily.nameable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import me.kaitlily.nameable.mixed.MixedTileEntity;

public class ClientEventHandler {

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {

        var minecraft = Minecraft.getMinecraft();
        MovingObjectPosition target = minecraft.objectMouseOver;
        EntityPlayer player = minecraft.thePlayer;
        FontRenderer fontRenderer = minecraft.fontRenderer;
        RenderManager renderManager = RenderManager.instance;

        MixedTileEntity tileEntity = (MixedTileEntity) player.getEntityWorld()
            .getTileEntity(target.blockX, target.blockY, target.blockZ);

        if (target.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || tileEntity == null
            || !tileEntity.nameable$hasName()) {
            return;
        }

        String text = tileEntity.nameable$getName();

        double dx = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) event.partialTicks;
        double dy = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) event.partialTicks;
        double dz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) event.partialTicks;

        float tx = target.blockX + 0.5F - (float) dx;
        float ty = target.blockY + 1.25F - (float) dy;
        float tz = target.blockZ + 0.5F - (float) dz;

        float viewScale = 1.6F;
        float scale = 0.016666668F * viewScale;

        GL11.glPushMatrix();

        GL11.glTranslatef(tx, ty, tz);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scale, -scale, scale);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);

        OpenGlHelper.glBlendFunc(770, 771, 1, 0);

        Tessellator tessellator = Tessellator.instance;

        GL11.glDisable(GL11.GL_TEXTURE_2D);

        tessellator.startDrawingQuads();
        int halfW = fontRenderer.getStringWidth(text) / 2;
        tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
        tessellator.addVertex(-halfW - 1, -1.0D, 0.0D);
        tessellator.addVertex(-halfW - 1, 8.0D, 0.0D);
        tessellator.addVertex(halfW + 1, 8.0D, 0.0D);
        tessellator.addVertex(halfW + 1, -1.0D, 0.0D);
        tessellator.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, 0, 553648127);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);

        fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, 0, -1);

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glPopMatrix();

    }

}
