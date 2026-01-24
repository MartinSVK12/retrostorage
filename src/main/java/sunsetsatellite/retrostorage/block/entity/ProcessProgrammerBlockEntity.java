package sunsetsatellite.retrostorage.block.entity;

import net.danygames2014.nyalib.block.BlockEntityInit;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalib.item.block.ManagedItemHandlerWithInventory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.block.BlockState;
import sunsetsatellite.catalyst.core.util.ScreenActionListener;
import sunsetsatellite.retrostorage.item.AdvRecipeDiscItem;
import sunsetsatellite.retrostorage.util.Filter;
import sunsetsatellite.retrostorage.util.StackType;

import java.util.HashMap;

public class ProcessProgrammerBlockEntity extends BlockEntity implements ManagedItemHandlerWithInventory, ScreenActionListener, BlockEntityInit {

    public Filter filter = new Filter(1, 1);

    public int task = 0;
    public int slot = 0;
    public boolean isOutput = false;
    public String processName = "";
    public StackType selectedType;
    public HashMap<Integer, HashMap<String, Object>> tasks = new HashMap<>();

    public ProcessProgrammerBlockEntity() {
        addItemSlot();
    }

    @Override
    public void init(BlockState blockState) {

    }

    @Override
    public void buttonClicked(int id, int button, int channel) {
        if (id == 6) {
            isOutput = !isOutput;
        }
        switch (id) {
            case 0:
                saveProcess();
                break;
            case 1:
                clearDisc();
                isOutput = false;
                slot = 0;
                task = 0;
                processName = "New Process";
                break;
            case 2:
                task++;
                break;
            case 3:
                if (task > 0) task--;
                break;
            case 4:
                slot++;
                break;
            case 5:
                if (slot > 0/*-1*/) slot--;
                break;
            case 7:
                setTask(selectedType);
                break;
            case 8:
                selectedType = StackType.ITEM;
                break;
            case 9:
                selectedType = StackType.FLUID;
                break;
        }
    }

    public void setTask(StackType type) {
        switch (type) {
            case ITEM -> {
                if (filter.getStack(0) != null) {
                    HashMap<String, Object> currentTask = new HashMap<>();
                    currentTask.put("slot", slot);
                    currentTask.put("stack", filter.getStack(0).copy());
                    currentTask.put("isOutput", isOutput);
                    currentTask.put("type", type);
                    tasks.put(task, currentTask);
                } else {
                    tasks.remove(task);
                }
            }
            case FLUID -> {
                if (filter.getFluid(0, null) != null) {
                    HashMap<String, Object> currentTask = new HashMap<>();
                    currentTask.put("slot", slot);
                    currentTask.put("stack", filter.getFluid(0, null).copy());
                    currentTask.put("isOutput", isOutput);
                    currentTask.put("type", type);
                    tasks.put(task, currentTask);
                } else {
                    tasks.remove(task);
                }
            }
        }
    }

    public void clearDisc() {
        if (getStack(1) != null && getStack(1).getItem() instanceof AdvRecipeDiscItem) {
            ItemStack disc = getStack(1);
            disc.getStationNbt().put("disc", new NbtCompound());
        }
        tasks.clear();
    }

    public void saveProcess() {
        if (getStack(1) != null && getStack(1).getItem() instanceof AdvRecipeDiscItem) {
            NbtCompound data = new NbtCompound();
            NbtCompound taskData = new NbtCompound();
            tasks.forEach((K, V) -> {
                NbtCompound task = new NbtCompound();
                task.putInt("id", K);
                task.putString("type", ((StackType) V.get("type")).name().toLowerCase());
                task.putInt("slot", (Integer) V.get("slot"));
                task.putBoolean("isOutput", (Boolean) V.get("isOutput"));
                NbtCompound stack = new NbtCompound();
                switch (((StackType) V.get("type"))) {
                    case ITEM -> ((ItemStack) V.get("stack")).writeNbt(stack);
                    case FLUID -> ((FluidStack) V.get("stack")).writeNbt(stack);
                }
                task.put("stack", stack);
                taskData.put("task" + K, task);
            });
            data.put("tasks", taskData);
            data.putString("processName", processName);
            getStack(0).getStationNbt().put("disc", data);
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        filter.writeNbt(nbt);

    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        filter.readNbt(nbt);
    }

    public boolean canUse(PlayerEntity player) {
        return player.getSquaredDistance(x + 0.5d, y + 0.5d, z + 0.5d) <= 64;
    }

    @Override
    public String getName() {
        return "container.retrostorage.processProgrammer";
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return canUse(player);
    }
}
