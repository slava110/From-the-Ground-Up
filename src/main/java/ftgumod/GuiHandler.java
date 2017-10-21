package ftgumod;

import ftgumod.inventory.ContainerIdeaTable;
import ftgumod.inventory.ContainerResearchTable;
import ftgumod.technology.TechnologyManager.GUI;
import ftgumod.tileentity.TileEntityInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		if (tileEntity != null) {
			if (ID == GUI.IDEATABLE.ordinal()) {
				return new ContainerIdeaTable((TileEntityInventory) tileEntity, player.inventory);
			} else if (ID == GUI.RESEARCHTABLE.ordinal()) {
				return new ContainerResearchTable((TileEntityInventory) tileEntity, player.inventory);
			}
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

}
