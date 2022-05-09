# RetroStorage 
Digital storage system mod for Minecraft b1.7.3.

Requires Modloader.

**Features**:

 - ItemNBT (Allows items to carry NBT data)
 - Digital Network (Store your items on a disc, no more chests!)
 - Autocrafting using Recipe Discs and Assemblers
 - I/O that allows you to somewhat automate machines from other mods.
 - and more to come.
 
 **Planned**:
 - [ ] Finish crafting recipes
 - [ ] Config file with ID's
 - [ ] Documentation

**WARNING**
Experimental *Alpha* release, code is mostly untested: I only cared about making it work, many things are either not yet implemented or they could/will be done better in the future.

Please report bugs in Github Issues, and if you have any questions, I'm in the [Modification Station](https://discord.gg/8Qky5XY) discord server as MartinSVK12#0075.

Have fun! :D


## Installation
[MultiMC](https://multimc.org/) recommended, Modloader required.

Install as a jar mod using 'Add to minecraft.jar' in MultiMC.

[HMI](https://github.com/rekadoodle/HowManyItems/releases) also recommended atleast until all items have proper recipes.


## Compatibility
Does edit some base classes for now:
 - `ItemStack.java` and some that call `ItemStack()`to implement ItemNBT including `Entity/InventoryPlayer.java`
 - Recipe related class such as `IRecipe.java, Shaped/ShapelessRecipes.java, CraftingManager.java` to implement autocrafting
 - `Container & GuiContainer.java` moves GUI's higher to make the custom ones fit + adds item lore (used for displaying capacity of discs)
 - `RenderBlocks.java` for the cable model
 
 Shouldn't catastrophically fail even if some classes get overridden, tested on a 44 mod modpack and the only thing broken was the cable model.

## Building
Step 1: Download source.

Step 2: Yeet into a [RetroMCP](https://github.com/MCPHackers/RetroMCP-Java/releases) workspace with Modloader preinstalled.

Step 3: ???

Step 4: Profit!
 
 ## Licence
 This mod is licensed under **Apache License 2.0**,
 please credit if using parts in your own mods.

