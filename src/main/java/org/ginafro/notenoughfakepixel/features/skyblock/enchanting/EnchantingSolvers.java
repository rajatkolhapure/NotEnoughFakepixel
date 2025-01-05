package org.ginafro.notenoughfakepixel.features.skyblock.enchanting;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.TablistParser;

public class EnchantingSolvers {

    public static SolverTypes currentSolverType = SolverTypes.NONE;

    private enum SolverTypes {
        NONE,
        CHRONOMATRON,
        ULTRASEQUENCER,
        SUPERPAIRS
    }

    private SolverState solverState = SolverState.NONE;

    private enum SolverState {
        NONE,
        WAITING,
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

        }
    }

}
