package org.ginafro.notenoughfakepixel.features.skyblock.dungeons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.utils.TablistParser;
import org.ginafro.notenoughfakepixel.variables.Location;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoReadyDungeon {

    private static boolean clicked = false;
    private static boolean found = false;

    private static Pattern nickedNamePattern = Pattern.compile("§r§aYou have successfully changed your nickname to (?<rank>(.+) |§r§7)(?<name>(.+))§r§a!§r");

    @SubscribeEvent()
    public void onGuiOpen(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (clicked) return;
        if (!Configuration.autoReadyDungeon) return;
        if (!ScoreboardUtils.currentLocation.isDungeon()) return;
        if (event.gui == null) return;
        if (!(event.gui instanceof GuiChest)) return;

        GuiChest chest = (GuiChest) event.gui;
        Container container = chest.inventorySlots;

        String chestName = TablistParser.currentOpenChestName;
        if (chestName == null || chestName.isEmpty()) return;

        if (chestName.startsWith("Catacombs -")) {
            ContainerChest containerChest = (ContainerChest) container;

            for(Slot slot : containerChest.inventorySlots) {

                if (slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;
                ItemStack item = slot.getStack();
                if (item == null) continue;
                if (item.getItem() instanceof ItemSkull) {
                    String itemName = item.getDisplayName();
                    if (itemName.contains(Minecraft.getMinecraft().thePlayer.getName()) || itemName.contains(Configuration.autoReadyName)){
                        Minecraft.getMinecraft().playerController.windowClick(chest.inventorySlots.windowId, slot.getSlotIndex() + 9, 0, 0, Minecraft.getMinecraft().thePlayer);
                        clicked = true;
                        found = true;
                    }
                }
            }
//            if (!found){
//                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\u00a79[NEF AutoReady] \u00a7cCould not find your head, Perhaps are you nicked?"));
//            }
        }
    }

    @SubscribeEvent()
    public void onWorldUnload(WorldEvent.Unload event) {
        clicked = false;
    }

    @SubscribeEvent
    public void onChatRecieve(ClientChatReceivedEvent e){
        if(Minecraft.getMinecraft().thePlayer == null) return;
        if (Minecraft.getMinecraft().theWorld == null) return;
        if (ScoreboardUtils.currentLocation != Location.NONE) return;

        Matcher matcher = nickedNamePattern.matcher(e.message.getFormattedText());
        if (matcher.matches()) {
            Configuration.autoReadyName = matcher.group("name");
        }

        if (e.message.getFormattedText().startsWith("§r§aYou have successfully reset your nickname!")){
            Configuration.autoReadyName = "example name";
        }
    }

}
