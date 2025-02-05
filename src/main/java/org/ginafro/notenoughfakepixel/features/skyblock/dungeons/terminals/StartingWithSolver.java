package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.terminals;


import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.DungeonManager;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.lwjgl.input.Mouse;

public class StartingWithSolver {

    @SubscribeEvent
    public void onGuiRender(GuiScreenEvent.BackgroundDrawnEvent e){
        if(!Configuration.dungeonsTerminalStartsWithSolver) return;
        if (!DungeonManager.checkEssentialsF7()) return;
        if(!(e.gui instanceof GuiChest)) return;

        GuiChest chest = (GuiChest) e.gui;
        Container container = chest.inventorySlots;

        if(!(container instanceof ContainerChest)) return;

        ContainerChest containerChest = (ContainerChest) container;
        String name = containerChest.getLowerChestInventory().getDisplayName().getUnformattedText();
        if(name.contains("What starts with")){
            char letter = name.charAt(name.length() - 2);

            for(Slot slot : containerChest.inventorySlots){
                if(slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;

                ItemStack item = slot.getStack();
                if(item == null) continue;

                if (containerChest.inventorySlots.indexOf(slot) == 49) {
                    if (!Configuration.dungeonsTerminalHideIncorrect) continue;
                    item.setItem(containerChest.inventorySlots.get(0).getStack().getItem());
                    item.getItem().setDamage(item, 15);
                    continue;
                }

                // Hide already clicked
                if (item.isItemEnchanted()) {
                    if (!Configuration.dungeonsTerminalHideIncorrect) continue;
                    slot.getStack().setItem(containerChest.inventorySlots.get(0).getStack().getItem());
                    slot.getStack().getItem().setDamage(slot.getStack(), 15);
                    continue;
                }


                if(StringUtils.stripControlCodes(item.getDisplayName()).charAt(0) == letter) {
                    RenderUtils.drawOnSlot(chest.inventorySlots.inventorySlots.size(), slot.xDisplayPosition , slot.yDisplayPosition , Configuration.dungeonsCorrectColor.getRGB());
                } else {
                    // Hide unwanted slots
                    if (!Configuration.dungeonsTerminalHideIncorrect) continue;
                    if (Block.getBlockFromItem(slot.getStack().getItem()) instanceof BlockStainedGlassPane) {
                        //RenderUtils.drawOnSlot(chest.inventorySlots.inventorySlots.size(), slot.xDisplayPosition , slot.yDisplayPosition , new Color(0,0,0).getRGB());
                        // Change to gray glass pane
                        if (slot.getStack().getMetadata() != 15) {
                            slot.getStack().getItem().setDamage(slot.getStack(), 15);
                        }
                    } else {
                        slot.getStack().setItem(containerChest.inventorySlots.get(0).getStack().getItem());
                        slot.getStack().getItem().setDamage(slot.getStack(), 15);
                    }
                }

            }
        }
    }

    // Prevent missclicks
    @SubscribeEvent
    public void onMouseClick(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (!Configuration.dungeonsPreventMissclicks) return;
        if (!Configuration.dungeonsTerminalStartsWithSolver) return;
        if (!DungeonManager.checkEssentialsF7()) return;
        if (!Mouse.getEventButtonState()) return;
        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiChest)) return; // Check if the current screen is a chest GUI
        GuiChest guiChest = (GuiChest) Minecraft.getMinecraft().currentScreen;
        Container container = guiChest.inventorySlots;
        if (!(container instanceof ContainerChest)) return;
        String title = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
        if (!title.contains("What starts with")) return;
        if (guiChest.getSlotUnderMouse() == null || guiChest.getSlotUnderMouse().getStack() == null) return;
        if (Block.getBlockFromItem(guiChest.getSlotUnderMouse().getStack().getItem()) instanceof BlockStainedGlassPane && guiChest.getSlotUnderMouse().getStack().getMetadata() == 15) {
            event.setCanceled(true);
        }
    }
}
