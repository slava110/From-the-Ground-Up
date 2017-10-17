package ftgumod.client.gui;

import ftgumod.FTGU;
import ftgumod.FTGUAPI;
import ftgumod.inventory.ContainerResearchTable;
import ftgumod.packet.PacketDispatcher;
import ftgumod.packet.server.RequestMessage;
import ftgumod.tileentity.TileEntityInventory;
import ftgumod.util.IngredientResearch;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.util.Collections;

public class GuiResearchTable extends GuiContainer {

	private final ResourceLocation texture;
	private final InventoryPlayer player;
	private final IInventory tileentity;

	public GuiResearchTable(InventoryPlayer player, TileEntityInventory tileentity) {
		super(tileentity.createContainer(player, player.player));
		this.player = player;
		this.tileentity = tileentity;

		texture = new ResourceLocation(FTGU.MODID + ":textures/gui/container/" + tileentity.getName() + ".png");
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = FTGUAPI.b_researchTable.getLocalizedName();
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		fontRenderer.drawString(player.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);

		Slot slot = getSlotUnderMouse();
		if (slot != null && !slot.getHasStack()) {
			ContainerResearchTable table = (ContainerResearchTable) inventorySlots;
			int index = slot.getSlotIndex() - table.combine;
			if (slot.inventory == tileentity && table.recipe != null && index >= 0 && index < 9 && !table.recipe.getResearchRecipe().isEmpty(index)) {
				IngredientResearch ingredient = table.recipe.getResearchRecipe().get(index);
				if (ingredient.hasHint()) {
					String hint = ingredient.getHint().getUnformattedText();
					String formatting = "";
					if (ingredient.getHint().getStyle().isEmpty())
						formatting = TextFormatting.GRAY.toString() + TextFormatting.ITALIC.toString();
					if (table.deciphered == null || !table.deciphered.contains(index)) {
						if (table.deciphered == null)
							PacketDispatcher.sendToServer(new RequestMessage(1));
						hint = formatting + TextFormatting.OBFUSCATED.toString() + hint;
					} else
						hint = formatting + hint;
					drawHoveringText(Collections.singletonList(hint), mouseX - guiLeft, mouseY - guiTop, fontRenderer);
				}
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float arg0, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		ContainerResearchTable table = (ContainerResearchTable) inventorySlots;
		if (table.recipe != null)
			for (int i = 0; i < 9; i++)
				if (!table.recipe.getResearchRecipe().isEmpty(i)) {
					Slot slot = inventorySlots.inventorySlots.get(i + table.combine);
					if (!slot.getHasStack())
						this.drawTexturedModalRect(slot.xPos + guiLeft, slot.yPos + guiTop, 176, 0, 16, 16);
				}
	}

}
