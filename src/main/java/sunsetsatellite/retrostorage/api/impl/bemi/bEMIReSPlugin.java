package sunsetsatellite.retrostorage.api.impl.bemi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import sunsetsatellite.retrostorage.menus.MenuRecipeEncoder;

public class bEMIReSPlugin implements EmiPlugin {
	@Override
	public void register(EmiRegistry registry) {
		registry.addRecipeHandler(MenuRecipeEncoder.class, new EncoderRecipeFiller());
	}
}
