package nachiten.machines.mod.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import nachiten.machines.mod.MachinesMod;
import nachiten.machines.mod.common.container.VerticalDrillContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class VerticalDrillBlockScreen extends ContainerScreen<VerticalDrillContainer> {

    private static final ResourceLocation VERTICAL_DRILL_GUI = new ResourceLocation(MachinesMod.MOD_ID, "textures/gui/display_case.png");

    public VerticalDrillBlockScreen(VerticalDrillContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);

        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 175;
        this.ySize = 201;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int x, int y) {
        this.font.func_243248_b(matrixStack, this.playerInventory.getDisplayName(), (float) this.playerInventoryTitleX, (float) this.playerInventoryTitleY, 4210752);
        // Nombre del bloque
        this.font.drawString(matrixStack, this.title.getString(), 8.0f, 7.0f, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        assert this.minecraft != null;
        this.minecraft.textureManager.bindTexture(VERTICAL_DRILL_GUI);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.blit(matrixStack, x, y, 0, 0, this.xSize, this.ySize);

        this.blit(matrixStack, x + 115, y + 52, 176, 0, 18, this.container.getProccess());
    }
}
