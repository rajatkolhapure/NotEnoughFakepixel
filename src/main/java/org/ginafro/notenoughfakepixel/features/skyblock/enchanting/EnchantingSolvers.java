package org.ginafro.notenoughfakepixel.features.skyblock.enchanting;

import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.utils.TablistParser;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EnchantingSolvers {

    public static SolverTypes currentSolverType = SolverTypes.NONE;
    private static List<UltrasequencerSlot> ultrasequencerSlots = new ArrayList<>();

    private class UltrasequencerSlot{
        public Slot slot;
        public int quantity;

        public UltrasequencerSlot(Slot slot, int quantity){
            this.slot = slot;
            this.quantity = quantity;
        }
    }

    private enum SolverTypes {
        NONE,
        CHRONOMATRON,
        ULTRASEQUENCER,
        SUPERPAIRS
    }

    private SolverState solverState = SolverState.NONE;

    private enum SolverState {
        NONE,
        REMEMBERING,
        SOLVING
    }

    @SubscribeEvent()
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui == null) return;
        if (!(event.gui instanceof GuiChest)) return;

        String chestName = TablistParser.currentOpenChestName;
        if (chestName == null || chestName.isEmpty()) return;


        if (chestName.startsWith("Chronomatron")) {
            currentSolverType = SolverTypes.CHRONOMATRON;
        } else if (chestName.startsWith("Ultrasequencer")) {
            currentSolverType = SolverTypes.ULTRASEQUENCER;
        } else if (chestName.startsWith("Super Pairs")) {
            currentSolverType = SolverTypes.SUPERPAIRS;
        } else {
            currentSolverType = SolverTypes.NONE;
        }
    }

    @SubscribeEvent
    public void onGuiDrawn(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (currentSolverType == SolverTypes.NONE) return;
        if (Configuration.ultraSequencerSolver && currentSolverType == SolverTypes.ULTRASEQUENCER) {

            if(!(event.gui instanceof GuiChest)) return;
            GuiChest chest = (GuiChest) event.gui;
            Container container = chest.inventorySlots;

            if(!(container instanceof ContainerChest)) return;
            String title = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
            if (!title.startsWith("Ultrasequencer (")) return;
            ContainerChest containerChest = (ContainerChest) container;
            // Check if its in remember state
            IInventory lower = ((ContainerChest) container).getLowerChestInventory();
            ItemStack timerStack = lower.getStackInSlot(lower.getSizeInventory() - 5);
            if (timerStack == null) return;
            boolean isClock = timerStack.getItem() == Items.clock;

            // if is clock, then remember the items
            if (!isClock){
                ultrasequencerSlots = new ArrayList<>();
                for(Slot slot : containerChest.inventorySlots) {
                    // select only the items in the chest
                    if (slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;
                    ItemStack item = slot.getStack();
                    if (item == null) continue;


                    if (item.getItem() == Items.dye) {

                        int stackSize = item.stackSize;
                        ultrasequencerSlots.add(new UltrasequencerSlot(slot, stackSize));
                    }
                }
            } else {
                // if not clock, show the items in the list
                for(UltrasequencerSlot slot : ultrasequencerSlots){
                    ItemStack itemInSlot = containerChest.inventorySlots.get(slot.slot.slotNumber).getStack();
                    if (itemInSlot == null) continue;
                    if (itemInSlot.getItem() == Items.dye) continue;

                    RenderUtils.drawOnSlot(containerChest.inventorySlots.size(), slot.slot.xDisplayPosition, slot.slot.yDisplayPosition, new Color(255, 0, 0).getRGB(), slot.quantity);
                }
            }
        }
    }

}
