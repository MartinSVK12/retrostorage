package sunsetsatellite.retrostorage.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.ScreenActionListener;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.recipe.crafting.RecipeEntryCrafting;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.block.RedstoneEmitterBlock;
import sunsetsatellite.retrostorage.block.base.entity.NetworkDeviceBlockEntity;
import sunsetsatellite.retrostorage.item.AdvRecipeDiscItem;
import sunsetsatellite.retrostorage.item.RecipeDiscItem;
import sunsetsatellite.retrostorage.util.Filter;
import sunsetsatellite.retrostorage.util.VariantStack;
import sunsetsatellite.retrostorage.util.crafting.*;

import static net.modificationstation.stationapi.api.state.property.Properties.HORIZONTAL_FACING;

public class RedstoneEmitterBlockEntity extends NetworkDeviceBlockEntity implements ScreenActionListener {

    public Filter filter = new Filter(1, 0);

    public boolean isActive = false;
    public int mode = 0;
    public int amount = 0;
    public boolean useMeta = true;
    public boolean useData = false;
    public TickTimer workTimer = new TickTimer(this, this::work, 60, true);
    public BlockEntity connectedTile;
    public int asmSlot = 0;

    @Override
    public void tick() {
        super.tick();
        if (world != null && world.isRemote) return;
        int side = world.getBlockState(x, y, z).get(HORIZONTAL_FACING).getOpposite().getId();
        connectedTile = Direction.getDirectionFromSide(side).getTileEntity(world, this);
        workTimer.tick();
        world.setBlockDirty(x, y, z);
        world.notifyNeighbors(x, y, z, isActive ? 15 : 0);
        world.setBlockState(x, y, z, world.getBlockState(x, y, z).with(RedstoneEmitterBlock.ACTIVE, isActive));
        if (getController() != null) {
            if (filter.getStack(0) != null) {
                int id = filter.getStack(0).itemId;
                int dmg = filter.getStack(0).getDamage();
                NbtCompound tag = filter.getStack(0).getStationNbt();
                long count = 0;
                if (useMeta) {
                    count = getController().countItems(id, dmg, tag);
                } else {
                    count = getController().countItems(id, -1, tag);
                }
                switch (mode) {
                    case 0:
                        isActive = count == amount;
                        break;
                    case 1:
                        isActive = count != amount;
                        break;
                    case 2:
                        isActive = count > amount;
                        break;
                    case 3:
                        isActive = count < amount;
                        break;
                    case 4:
                        isActive = count >= amount;
                        break;
                    case 5:
                        isActive = count <= amount;
                        break;
                }
            } else {
                isActive = false;
            }
        } else {
            isActive = false;
        }
    }

    public void work() {
        if (connectedTile != null && getController() != null && isActive) {
            if (connectedTile instanceof AssemblerBlockEntity assembler) {
                ItemStack stack = assembler.getStack(asmSlot);
                if (stack != null) {
                    if (stack.getItem() instanceof RecipeDiscItem) {
                        RecipeEntryCrafting<?, ItemStack> recipe = RetroStorage.findRecipeFromNBT(stack.getStationNbt().getCompound("recipe"));
                        if (recipe != null) {
                            CraftingCalculator calc = new CraftingCalculator(getController(), 1, new VariantStack(recipe.getOutput()), new NetworkCraftable(recipe), getController().getCraftables());
                            CalculationResult result = calc.calculate();
                            if (result.getType() == CalculationResultType.OK) {
                                getController().requestCrafting(result.getTask());
                            }
                        }
                    }
                }
            } else if (connectedTile instanceof AdvInterfaceBlockEntity intfc) {
                if (!intfc.isInUse()) {
                    ItemStack stack = intfc.getStack(asmSlot);
                    if (stack != null) {
                        if (stack.getItem() instanceof AdvRecipeDiscItem) {
                            if (stack.getStationNbt().contains("disc") && stack.getStationNbt().getCompound("disc").contains("processName")) {
                                CraftingProcess process = new CraftingProcess(stack.getStationNbt().getCompound("disc"));
                                NetworkCraftable craftable = new NetworkCraftable(process);
                                CraftingCalculator calc = new CraftingCalculator(getController(), 1, craftable.getOutput().get(0), craftable, getController().getCraftables());
                                CalculationResult result = calc.calculate();
                                if (result.getType() == CalculationResultType.OK) {
                                    getController().requestCrafting(result.getTask());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        filter.readNbt(nbt);
        isActive = nbt.getBoolean("isActive");
        mode = nbt.getInt("mode");
        amount = nbt.getInt("checkAmount");
        useMeta = nbt.getBoolean("useMeta");
        asmSlot = nbt.getInt("asmSlot");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        filter.writeNbt(nbt);
        nbt.putBoolean("isActive", isActive);
        nbt.putInt("checkAmount", amount);
        nbt.putInt("mode", mode);
        nbt.putBoolean("useMeta", useMeta);
        nbt.putInt("asmSlot", asmSlot);
    }

    @Override
    public String getName() {
        return "container.retrostorage.redstoneEmitter";
    }

    @Override
    public void buttonClicked(int id, int button, int channel) {
        if (id == 2) {
            if (amount > 0)
                amount--;
        }
        if (id == 1) {
            amount++;
        }
        if (id == 3) {
            useMeta = !useMeta;
        }
        if (id == 4) {
            useData = !useData;
        }
        if (id == 5) {
            if (connectedTile instanceof AssemblerBlockEntity asm) {
                if (asm.advanced) {
                    if (asmSlot < 26) {
                        asmSlot++;
                    }
                } else {
                    if (asmSlot < 8) {
                        asmSlot++;
                    }
                }
            }
        }
        if (id == 6) {
            if (asmSlot > 0) {
                asmSlot--;
            }
        }
        if (id == 0) {
            mode++;
            if (mode == 6) {
                mode = 0;
            }
        }
    }
}
