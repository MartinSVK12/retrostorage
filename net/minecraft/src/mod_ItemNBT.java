package net.minecraft.src;

public class mod_ItemNBT extends BaseMod {

	public mod_ItemNBT() {
	}


	@Override
	public String Version() {
		return "1.2";
	}
	
	public String Name() {
		return "ItemNBT";
	}
	
	public String Description() {
		return "Allows items to carry custom NBT data.";
	}
}
