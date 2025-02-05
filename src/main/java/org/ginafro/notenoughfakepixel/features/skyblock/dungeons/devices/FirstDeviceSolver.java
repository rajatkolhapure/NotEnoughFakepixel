package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.devices;

import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockButtonStone;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.DungeonManager;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;

import java.awt.*;
import java.util.Objects;

public class FirstDeviceSolver {

    private BlockPos[] positionsToSolve;
    private int[] positionsIndexSolved = new int[]{-1,-1,-1,-1,-1};
    private boolean startMemorising = false;
    private boolean resolving = false;
    private int round = 1;
    private int positionInRound = 0;

    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        if (!Configuration.dungeonsFirstDeviceSolver) return;
        if (!DungeonManager.checkEssentialsF7()) return;
        // Check for sea lanterns
        if (startMemorising) {
            if (positionsToSolve == null) return;
            for (int i = 0; i < positionsToSolve.length; i++) {
                Block targetBlock = Minecraft.getMinecraft().theWorld.getBlockState(positionsToSolve[i]).getBlock();
                if (targetBlock == Blocks.sea_lantern && positionsIndexSolved[round-1] == -1 && !checkIfAdded(positionsIndexSolved,i)) {
                    positionsIndexSolved[round-1] = i;
                    resolving = true;
                    startMemorising = false;
                }
            }
        }
        // Show highligted button and next button
        if (resolving) {
            for (int i=0; i<round;i++) {
                if (i == positionInRound) {
                    RenderUtils.highlightBlock(positionsToSolve[positionsIndexSolved[i]], new Color(Configuration.dungeonsCorrectColor.getRed(), Configuration.dungeonsCorrectColor.getGreen(), Configuration.dungeonsCorrectColor.getBlue(), 200), false, true, event.partialTicks);
                } else if (i == positionInRound+1) {
                    RenderUtils.highlightBlock(positionsToSolve[positionsIndexSolved[i]], new Color(Configuration.dungeonsAlternativeColor.getRed(), Configuration.dungeonsAlternativeColor.getGreen(), Configuration.dungeonsAlternativeColor.getBlue(), 150), false, true, event.partialTicks);
                }
            }
        }
    }

    /*@SubscribeEvent
    public void onBreak(PlayerDestroyItemEvent event) {
        System.out.println(event.original.getUnlocalizedName());
    }*/

    // Check for initial button interact
    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        if (!Configuration.dungeonsFirstDeviceSolver) return;
        if (!DungeonManager.checkEssentialsF7()) return;
        if (Minecraft.getMinecraft().thePlayer != event.entityPlayer) return;
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            Block buttonBlock = Minecraft.getMinecraft().theWorld.getBlockState(event.pos).getBlock();
            if (buttonBlock instanceof BlockButtonStone) {
                EnumFacing enumfacing = Minecraft.getMinecraft().theWorld.getBlockState(event.pos).getValue(BlockButton.FACING);
                Block blockClicked = Minecraft.getMinecraft().theWorld.getBlockState(getBlockUnderButton(event.pos, enumfacing)).getBlock();
                // Check if button is pressed over an emerald block
                if (Objects.equals(blockClicked.getUnlocalizedName(), Blocks.emerald_block.getUnlocalizedName()) && !startMemorising) {
                    BlockPos pos = getBlockUnderButton(event.pos, enumfacing);
                    positionsToSolve = getSurroundingBlocks(pos, enumfacing);
                    reset();
                    startMemorising = true;
                // Else if over obsidian
                } else if (Objects.equals(blockClicked.getUnlocalizedName(), Blocks.obsidian.getUnlocalizedName())){
                    positionInRound++;
                    if (positionInRound == round) {
                        positionInRound = 0;
                        round++;
                        startMemorising = true;
                        resolving = false;
                    }
                    if (round == 6) {
                        reset();
                    }
                }
            }
        }
    }

    private static BlockPos getBlockUnderButton(BlockPos pos, EnumFacing facing) {
        System.out.println(facing);
        switch (facing) {
            case NORTH:
                return new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1);
            case EAST:
                return new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ());
            case SOUTH:
                return new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1);
            case WEST:
                return new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ());
            default:
                return null;
        }
    }

    public static BlockPos[] getSurroundingBlocks(BlockPos startPos, EnumFacing enumFacing) {
        BlockPos[] surroundingBlocks = new BlockPos[16];

        int index = 0;
        for (int yOffset = -1; yOffset <= 2; yOffset++) { // Y goes from P.y - 1 to P.y + 2
            for (int hOffset = 1; hOffset <= 4; hOffset++) { // Moves horizontally
                int dx = 0, dz = 0, dy = yOffset;

                switch (enumFacing) {
                    case NORTH: // Moves along X, Z is constant
                        dx = -hOffset;
                        break;
                    case SOUTH: // Moves along X, Z is constant
                        dx = hOffset;
                        break;
                    case WEST: // Moves along Z, X is constant
                        dz = hOffset;
                        break;
                    case EAST: // Moves along Z, X is constant
                        dz = -hOffset;
                        break;
                }

                surroundingBlocks[index++] = new BlockPos(startPos.getX() + dx, startPos.getY() + dy, startPos.getZ() + dz);
            }
        }

        return surroundingBlocks;
    }

    private boolean checkIfAdded(int[] array, int index) {
        for (int j : array) {
            if (j == index) return true;
        }
        return false;
    }

    private void reset() {
        positionsIndexSolved = new int[]{-1,-1,-1,-1,-1};
        startMemorising = false;
        resolving = false;
        positionInRound = 0;
        round = 1;
    }

    @SubscribeEvent()
    public void onWorldUnload(WorldEvent.Unload event) {
        if (Configuration.dungeonsFirstDeviceSolver) reset();
    }

}
