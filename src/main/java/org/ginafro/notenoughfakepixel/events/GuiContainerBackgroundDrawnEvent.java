package org.ginafro.notenoughfakepixel.events;

import lombok.Value;
import net.minecraft.client.gui.inventory.GuiContainer;


@Value
public class GuiContainerBackgroundDrawnEvent extends CustomEvent {

    public GuiContainer container;
    public float partialTicks;

    public GuiContainerBackgroundDrawnEvent(GuiContainer container, float partialTicks) {
        this.container = container;
        this.partialTicks = partialTicks;
    }

    public GuiContainer getContainer() {
        return container;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

}
