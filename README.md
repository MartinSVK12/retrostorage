# RetroStorage 
Digital storage system mod for Minecraft b1.7.3.

Requires Modloader (+ Forge 1.0.6 for versions a1.5pre1 and above.). Currently, singleplayer only.

*Versions a1.5pre2 and above also include support for IC2 machines*

**Features**:

 - ItemNBT (Allows items to carry NBT data, available as a standalone mod.)
 - Digital Network (Store your items on a disc, no more chests!)
 - Autocrafting using Recipe Discs and Assemblers
 - I/O that allows you to somewhat automate machines from other mods.
 - and more to come.
 
 **Planned**:
 - [X] Finish crafting recipes
 - [X] Config file with ID's
 - [X] Documentation

**WARNING**
Experimental *Alpha* release, many things are either not yet implemented or they could/will be done better in the future.
**There might be a LOT of bugs, you've been warned.**

Please report bugs in Github Issues, and if you have any questions, I'm in the [Modification Station](https://discord.gg/8Qky5XY) discord server as MartinSVK12#0075.

Have fun! :D


## Installation
[MultiMC](https://multimc.org/) recommended, Modloader + Forge required.
Forge 1.0.6 is recommended and is required for versions a1.5pre1 and above.

Install as a jar mod using 'Add to minecraft.jar' in MultiMC.

[HMI](https://github.com/rekadoodle/HowManyItems/releases) also recommended.


## Compatibility
Does edit some base classes for now:
 - `ItemStack.java` and classes that call `ItemStack()`to implement ItemNBT including `Entity/InventoryPlayer.java` and `BlockFurnace/Dispenser/Chest.java`
 - Recipe related class such as `IRecipe.java, Shaped/ShapelessRecipes.java, CraftingManager.java` to implement autocrafting
 - `Container & GuiContainer.java` moves GUI's higher to make the custom ones fit + adds item lore (used for displaying capacity of discs)
 - All `NBTTag*.java` classes to implement proper `equals()` and some other fuctions.
 
Versions a1.5pre2 and above support IC2 energy storage for powering the Digital Controller and IC2 machines in Interface processing.

## Building
Step 1: Download source.

Step 2: Yeet into a [RetroMCP](https://github.com/MCPHackers/RetroMCP-Java/releases) workspace with Modloader + Forge preinstalled.

Step 3: ???

Step 4: Profit!

## Special Thanks
- [@rythin-sr](https://github.com/rythin-sr) - based bug finder
- [Mango Pack](https://discord.gg/FaPeNqkbJw) - community for probably the most polished b1.7.3 pack.  
 
## License
Please credit if using parts in your own mods/modpacks (like ItemNBT for example)
