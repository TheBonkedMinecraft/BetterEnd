package org.betterx.betterend.integration.emi;

import org.betterx.ui.layout.components.render.RenderHelper;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;

import dev.emi.emi.EmiRenderHelper;
import dev.emi.emi.api.render.EmiRender;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.config.EmiConfig;

public class TransparentSlotWidget extends SlotWidget {

    public TransparentSlotWidget(EmiIngredient stack, int x, int y) {
        super(stack, x, y);
    }


    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        Bounds bounds = this.getBounds();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int width = bounds.width();
        int height = bounds.height();
        if (this.drawBack) {
            if (this.textureId != null) {
                RenderSystem.setShaderTexture(0, this.textureId);
                GuiComponent.blit(
                        matrices,
                        bounds.x(), bounds.y(), width, height,
                        this.u, this.v, width, height, 256, 256
                );
            } else {
                renderSlot(matrices);
            }
        }

//        if (this.getRecipe() == null
//                && EmiClient.availableForCrafting.containsKey(this.getStack())
//                && !this.getStack().isEmpty()
//                && !(Boolean) EmiClient.availableForCrafting.get(this.getStack())) {
//            GuiComponent.fill(
//                    matrices,
//                    bounds.x(), bounds.y(),
//                    bounds.x() + bounds.width(), bounds.y() + bounds.height(),
//                    0x44FF0000
//            );
//        }

        int xOff = (width - 16) / 2;
        int yOff = (height - 16) / 2;
        this.getStack().render(matrices, bounds.x() + xOff, bounds.y() + yOff, delta);
        if (this.catalyst) {
            EmiRender.renderCatalystIcon(this.getStack(), matrices, this.x + xOff, this.y + yOff);
        }

        if (EmiConfig.showHoverOverlay && bounds.contains(mouseX, mouseY)) {
            EmiRenderHelper.drawSlotHightlight(
                    matrices,
                    bounds.x() + 1, bounds.y() + 1,
                    bounds.width() - 2, bounds.height() - 2
            );
        }

    }

    public void renderSlot(PoseStack matrices) {
        Bounds bounds = this.getBounds();
        int width = bounds.width();
        int height = bounds.height();

        GuiComponent.fill(matrices, bounds.x(), bounds.y(), bounds.x() + width, bounds.y() + height, 0xB08b8b8b);
        RenderHelper.outline(
                matrices,
                bounds.x(),
                bounds.y(),
                bounds.x() + width,
                bounds.y() + height,
                0xFA373737,
                0xFAFFFFFF
        );
        RenderHelper.vLine(matrices, bounds.x() + width - 1, bounds.y(), bounds.y(), 0xFA8B8B8B);
        RenderHelper.hLine(matrices, bounds.x(), bounds.x(), bounds.y() + bounds.height() - 1, 0xFA8B8B8B);
    }
}
