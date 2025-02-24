package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.terminals;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.DungeonManager;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class ClickInOrderSolver {

    public int round = 1;
    private static final int SLOT_SIZE = 16;
    private static final int REGION_COLS = 7;
    private static final int REGION_ROWS = 2;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onDrawScreenPre(GuiScreenEvent.DrawScreenEvent.Pre event) {
        if (!(event.gui instanceof GuiChest)) return;
        if (!DungeonManager.checkEssentialsF7()) return;
        Container container = ((GuiChest) event.gui).inventorySlots;
        if (!(container instanceof ContainerChest)) return;
        String title = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
        if (Configuration.dungeonsCustomGui && title.startsWith("Click in")) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onOpen(GuiOpenEvent e) {
        if (!Configuration.dungeonsTerminalClickInOrderSolver) return;
        if (!DungeonManager.checkEssentialsF7()) return;
        if (e.gui instanceof GuiChest) {
            GuiChest chest = (GuiChest) e.gui;
            Container container = chest.inventorySlots;
            if (container instanceof ContainerChest) {
                String title = ((ContainerChest) container).getLowerChestInventory()
                        .getDisplayName().getUnformattedText();
                if (!title.startsWith("Click in")) return;
                round = 1;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onDrawScreenPost(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (!Configuration.dungeonsTerminalClickInOrderSolver) return;
        if (!DungeonManager.checkEssentialsF7()) return;
        if (!(event.gui instanceof GuiChest)) return;
        GuiChest chest = (GuiChest) event.gui;
        Container container = chest.inventorySlots;
        if (!(container instanceof ContainerChest)) return;
        String title = ((ContainerChest) container).getLowerChestInventory()
                .getDisplayName().getUnformattedText();
        if (!title.startsWith("Click in")) return;
        ContainerChest containerChest = (ContainerChest) container;

        if (Configuration.dungeonsCustomGui) {
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            float scale = Configuration.terminalsScale;
            int guiWidth = (int) (REGION_COLS * SLOT_SIZE * scale);
            int guiHeight = (int) (REGION_ROWS * SLOT_SIZE * scale);
            int screenWidth = sr.getScaledWidth();
            int screenHeight = sr.getScaledHeight();
            int guiLeft = (screenWidth - guiWidth) / 2;
            int guiTop = (screenHeight - guiHeight) / 2;

            GlStateManager.pushMatrix();
            GlStateManager.translate(guiLeft, guiTop, 0);
            GlStateManager.scale(scale, scale, 1.0f);

            Gui.drawRect(-2, -12, (REGION_COLS * SLOT_SIZE) + 2, (REGION_ROWS * SLOT_SIZE) + 2, 0x80000000);
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
                    "§8[§bNEF§8] §aClick in Order!",
                    0, -10, 0xFFFFFF);

            for (int row = 1; row <= REGION_ROWS; row++) {
                for (int col = 1; col <= REGION_COLS; col++) {
                    int slotIndex = row * 9 + col;
                    Slot slot = containerChest.getSlot(slotIndex);
                    if (slot == null || slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;
                    if (slot.getStack() == null || !(Block.getBlockFromItem(slot.getStack().getItem()) instanceof BlockStainedGlassPane))
                        continue;

                    int overlayColor = 0;
                    if (slot.getStack().stackSize == round) {
                        overlayColor = Configuration.dungeonsCorrectColor.getRGB();
                    } else if (slot.getStack().stackSize == round + 1) {
                        overlayColor = Configuration.dungeonsAlternativeColor.getRGB();
                    } else if (slot.getStack().stackSize == round + 2) {
                        Color alt = new Color(
                                Configuration.dungeonsAlternativeColor.getRed(),
                                Configuration.dungeonsAlternativeColor.getGreen(),
                                Configuration.dungeonsAlternativeColor.getBlue(), 150);
                        overlayColor = alt.getRGB();
                    }

                    if (Configuration.dungeonsTerminalHideIncorrect &&
                            slot.getStack().stackSize > round + 1 &&
                            slot.getStack().getItemDamage() == 14) {
                        slot.getStack().getItem().setDamage(slot.getStack(), 15);
                        overlayColor = new Color(113, 113, 113).getRGB();
                    }

                    int x = (col - 1) * SLOT_SIZE;
                    int y = (row - 1) * SLOT_SIZE;
                    drawRect(x, y, x + SLOT_SIZE, y + SLOT_SIZE, overlayColor);

                    String stackSizeText = String.valueOf(slot.getStack().stackSize);
                    int textWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(stackSizeText);
                    int textX = x + (SLOT_SIZE / 2) - (textWidth / 2);
                    int textY = y + (SLOT_SIZE / 2) - 4;
                    if (slot.getStack().stackSize >= round) {
                        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
                                stackSizeText, textX, textY, 0xFFFFFF);
                    }
                }
            }
            GlStateManager.popMatrix();
        } else {
            for (int row = 1; row <= REGION_ROWS; row++) {
                for (int col = 1; col <= REGION_COLS; col++) {
                    int slotIndex = row * 9 + col;
                    Slot slot = containerChest.getSlot(slotIndex);
                    if (slot == null || slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;
                    if (slot.getStack() == null || !(Block.getBlockFromItem(slot.getStack().getItem()) instanceof BlockStainedGlassPane))
                        continue;

                    int overlayColor = 0;
                    if (slot.getStack().stackSize == round) {
                        overlayColor = Configuration.dungeonsCorrectColor.getRGB();
                    } else if (slot.getStack().stackSize == round + 1) {
                        overlayColor = Configuration.dungeonsAlternativeColor.getRGB();
                    }

                    RenderUtils.drawOnSlot(container.inventorySlots.size(), slot.xDisplayPosition, slot.yDisplayPosition, overlayColor);

                    String stackSizeText = String.valueOf(slot.getStack().stackSize);
                    int textX = slot.xDisplayPosition + 9 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(stackSizeText) / 2;
                    int textY = slot.yDisplayPosition + 6;
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(stackSizeText, textX, textY, 0xFFFFFF);
                }
            }
        }
    }

    @SubscribeEvent
    public void onGuiRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!Configuration.dungeonsTerminalClickInOrderSolver) return;
        if (!DungeonManager.checkEssentialsF7()) return;
        if (event.gui instanceof GuiChest) {
            GuiChest chest = (GuiChest) event.gui;
            Container container = chest.inventorySlots;
            if (container instanceof ContainerChest) {
                String title = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
                if (!title.startsWith("Click in")) return;
                ContainerChest containerChest = (ContainerChest) container;
                for (int i = 1; i < 3; i++) {
                    for (int j = 1; j < 8; j++) {
                        Slot slot = containerChest.getSlot(i * 9 + j);
                        if (slot == null || slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;
                        if (slot.getStack() == null || !(Block.getBlockFromItem(slot.getStack().getItem()) instanceof BlockStainedGlassPane)) continue;

                        if (slot.getStack().stackSize == round) {
                            if (slot.getStack().getItemDamage() == 14 || slot.getStack().getItemDamage() == 15) {
                                slot.getStack().getItem().setDamage(slot.getStack(), 0);
                            }
                            RenderUtils.drawOnSlot(container.inventorySlots.size(), slot.xDisplayPosition, slot.yDisplayPosition, Configuration.dungeonsCorrectColor.getRGB());
                        } else if (slot.getStack().stackSize == round + 1) {
                            if (slot.getStack().getItemDamage() == 14 || slot.getStack().getItemDamage() == 15) {
                                slot.getStack().getItem().setDamage(slot.getStack(), 0);
                            }
                            RenderUtils.drawOnSlot(container.inventorySlots.size(), slot.xDisplayPosition, slot.yDisplayPosition, Configuration.dungeonsAlternativeColor.getRGB());
                        } else if (slot.getStack().stackSize == round + 2) {
                            if (slot.getStack().getItemDamage() == 14 || slot.getStack().getItemDamage() == 15) {
                                slot.getStack().getItem().setDamage(slot.getStack(), 0);
                            }
                            RenderUtils.drawOnSlot(container.inventorySlots.size(), slot.xDisplayPosition, slot.yDisplayPosition, new Color(Configuration.dungeonsAlternativeColor.getRed(), Configuration.dungeonsAlternativeColor.getGreen(), Configuration.dungeonsAlternativeColor.getBlue(), 150).getRGB());
                        }

                        if (Configuration.dungeonsTerminalHideIncorrect && slot.getStack().stackSize > round + 1 && slot.getStack().getItemDamage() == 14) {
                            slot.getStack().getItem().setDamage(slot.getStack(), 15);
                            RenderUtils.drawOnSlot(container.inventorySlots.size(), slot.xDisplayPosition, slot.yDisplayPosition, new Color(113, 113, 113).getRGB());
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onMouseClick(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (!Configuration.dungeonsTerminalClickInOrderSolver) return;
        if (!DungeonManager.checkEssentialsF7()) return;
        if (!Configuration.dungeonsTerminalHideIncorrect) return;
        if (!Mouse.getEventButtonState() || Mouse.getEventButton() != 0) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (!(mc.currentScreen instanceof GuiChest)) return;
        GuiChest guiChest = (GuiChest) mc.currentScreen;
        Container container = guiChest.inventorySlots;
        if (!(container instanceof ContainerChest)) return;
        String title = ((ContainerChest) container).getLowerChestInventory()
                .getDisplayName().getUnformattedText();
        if (!title.startsWith("Click in")) return;

        if (Configuration.dungeonsCustomGui) {
            ScaledResolution sr = new ScaledResolution(mc);
            float scale = Configuration.terminalsScale;
            int guiWidth = (int) (REGION_COLS * SLOT_SIZE * scale);
            int guiHeight = (int) (REGION_ROWS * SLOT_SIZE * scale);
            int screenWidth = sr.getScaledWidth();
            int screenHeight = sr.getScaledHeight();
            int guiLeft = (screenWidth - guiWidth) / 2;
            int guiTop = (screenHeight - guiHeight) / 2;

            int mouseX = (Mouse.getEventX() * sr.getScaledWidth()) / mc.displayWidth;
            int mouseY = sr.getScaledHeight() - (Mouse.getEventY() * sr.getScaledHeight()) / mc.displayHeight - 1;

            if (mouseX < guiLeft || mouseX >= guiLeft + guiWidth ||
                    mouseY < guiTop || mouseY >= guiTop + guiHeight) {
                event.setCanceled(true);
                return;
            }

            float relX = (mouseX - guiLeft) / scale;
            float relY = (mouseY - guiTop) / scale;
            int col = (int) (relX / SLOT_SIZE);
            int row = (int) (relY / SLOT_SIZE);
            if (col < 0 || col >= REGION_COLS || row < 0 || row >= REGION_ROWS) {
                event.setCanceled(true);
                return;
            }
            int slotIndex = (row + 1) * 9 + (col + 1);
            Slot slot = ((ContainerChest) container).getSlot(slotIndex);
            if (slot == null || slot.getStack() == null) {
                event.setCanceled(true);
                return;
            }
            if (slot.getStack().stackSize == round) {
                slot.getStack().getItem().setDamage(slot.getStack(), 14);
                mc.playerController.windowClick(
                        ((ContainerChest) container).windowId,
                        slot.slotNumber,
                        0,
                        0,
                        mc.thePlayer
                );
                mc.playerController.windowClick(
                        ((ContainerChest) container).windowId,
                        slot.slotNumber,
                        0,
                        0,
                        mc.thePlayer
                );
                round++;
            } else {
                event.setCanceled(true);
            }
        } else {
            GuiChest gui = (GuiChest) mc.currentScreen;
            Slot hoveredSlot = gui.getSlotUnderMouse();
            if (hoveredSlot != null && hoveredSlot.getStack() != null) {
                if (hoveredSlot.getStack().stackSize == round) {
                    hoveredSlot.getStack().getItem().setDamage(hoveredSlot.getStack(), 14);
                    mc.playerController.windowClick(
                            ((ContainerChest) container).windowId,
                            hoveredSlot.slotNumber,
                            0,
                            0,
                            mc.thePlayer);
                    mc.playerController.windowClick(
                            ((ContainerChest) container).windowId,
                            hoveredSlot.slotNumber,
                            0,
                            0,
                            mc.thePlayer);
                    round++;
                }
            }
        }
    }

    private static void drawRect(int left, int top, int right, int bottom, int color) {
        Gui.drawRect(left, top, right, bottom, color);
    }
}