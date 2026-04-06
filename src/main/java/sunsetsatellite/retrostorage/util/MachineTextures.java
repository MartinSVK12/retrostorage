package sunsetsatellite.retrostorage.util;

import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Side;

import java.util.Arrays;
import java.util.HashMap;

public class MachineTextures {
    public HashMap<Side, String> defaultTextures;
    public HashMap<Side, String> activeTextures;
    public HashMap<Side, String> overbrightTextures;

    public MachineTextures() {
        this.defaultTextures = (HashMap<Side, String>) Catalyst.mapOf(Arrays.stream(Side.values()).toArray(Side[]::new), Catalyst.arrayFill(new String[Side.values().length], "retrostorage:block/unknown"));
        this.activeTextures = (HashMap<Side, String>) Catalyst.mapOf(Arrays.stream(Side.values()).toArray(Side[]::new), Catalyst.arrayFill(new String[Side.values().length], "retrostorage:block/unknown"));
        this.overbrightTextures = (HashMap<Side, String>) Catalyst.mapOf(Arrays.stream(Side.values()).toArray(Side[]::new), Catalyst.arrayFill(new String[Side.values().length], null));
    }

    public MachineTextures(boolean advanced){
        this.defaultTextures = (HashMap<Side, String>) Catalyst.mapOf(Arrays.stream(Side.values()).toArray(Side[]::new), Catalyst.arrayFill(new String[Side.values().length], "retrostorage:block/unknown"));
        this.activeTextures = (HashMap<Side, String>) Catalyst.mapOf(Arrays.stream(Side.values()).toArray(Side[]::new), Catalyst.arrayFill(new String[Side.values().length], "retrostorage:block/unknown"));
        this.overbrightTextures = (HashMap<Side, String>) Catalyst.mapOf(Arrays.stream(Side.values()).toArray(Side[]::new), Catalyst.arrayFill(new String[Side.values().length], null));

        if(!advanced){
            withDefaultTexture("machine_side");
        } else {
            withDefaultTexture("adv_machine_side");
        }
    }
    
    public MachineTextures withDefaultTexture(String texture) {
        defaultTextures.replaceAll((S, I) -> "retrostorage:block/" + texture);
        return this;
    }

    public MachineTextures withActiveTexture(String texture) {
        activeTextures.replaceAll((S, I) -> "retrostorage:block/" + texture);
        return this;
    }

    public MachineTextures withOverbrightTextures(String texture) {
        overbrightTextures.replaceAll((S, I) -> "retrostorage:block/" + texture);
        return this;
    }

    public MachineTextures withDefaultSideTextures(String texture) {
        defaultTextures.replaceAll((S, I) -> {
            if (S != Side.UP && S != Side.DOWN) {
                return "retrostorage:block/" + texture;
            }
            return I;
        });
        return this;
    }

    public MachineTextures withActiveSideTextures(String texture) {
        activeTextures.replaceAll((S, I) -> {
            if (S != Side.UP && S != Side.DOWN) {
                return "retrostorage:block/" + texture;
            }
            return I;
        });
        return this;
    }

    public MachineTextures withOverbrightSideTextures(String texture) {
        overbrightTextures.replaceAll((S, I) -> {
            if (S != Side.UP && S != Side.DOWN) {
                return "retrostorage:block/" + texture;
            }
            return I;
        });
        return this;
    }

    public MachineTextures withDefaultTopBottomTextures(String texture) {
        defaultTextures.replaceAll((S, I) -> {
            if (S == Side.UP || S == Side.DOWN) {
                return "retrostorage:block/" + texture;
            }
            return I;
        });
        return this;
    }

    public MachineTextures withActiveTopBottomTextures(String texture) {
        activeTextures.replaceAll((S, I) -> {
            if (S == Side.UP || S == Side.DOWN) {
                return "retrostorage:block/" + texture;
            }
            return I;
        });
        return this;
    }

    public MachineTextures withOverbrightTopBottomTextures(String texture) {
        overbrightTextures.replaceAll((S, I) -> {
            if (S == Side.UP || S == Side.DOWN) {
                return "retrostorage:block/" + texture;
            }
            return I;
        });
        return this;
    }

    public MachineTextures withDefaultTopTexture(String texture) {
        defaultTextures.replace(Side.UP, "retrostorage:block/" + texture);
        return this;
    }

    public MachineTextures withActiveTopTexture(String texture) {
        activeTextures.replace(Side.UP, "retrostorage:block/" + texture);
        return this;
    }

    public MachineTextures withOverbrightTopTexture(String texture) {
        overbrightTextures.replace(Side.UP, "retrostorage:block/" + texture);
        return this;
    }

    public MachineTextures withDefaultBottomTexture(String texture) {
        defaultTextures.replace(Side.DOWN, "retrostorage:block/" + texture);
        return this;
    }

    public MachineTextures withActiveBottomTexture(String texture) {
        activeTextures.replace(Side.DOWN, "retrostorage:block/" + texture);
        return this;
    }

    public MachineTextures withOverbrightBottomTexture(String texture) {
        overbrightTextures.replace(Side.DOWN, "retrostorage:block/" + texture);
        return this;
    }

    public MachineTextures withDefaultNorthTexture(String texture) {
        defaultTextures.replace(Side.NORTH, "retrostorage:block/" + texture);
        return this;
    }

    public MachineTextures withActiveNorthTexture(String texture) {
        activeTextures.replace(Side.NORTH, "retrostorage:block/" + texture);
        return this;
    }

    public MachineTextures withOverbrightNorthTexture(String texture) {
        overbrightTextures.replace(Side.NORTH, "retrostorage:block/" + texture);
        return this;
    }

    public MachineTextures withDefaultSouthTexture(String texture) {
        defaultTextures.replace(Side.SOUTH, "retrostorage:block/" + texture);
        return this;
    }

    public MachineTextures withActiveSouthTexture(String texture) {
        activeTextures.replace(Side.SOUTH, "retrostorage:block/" + texture);
        return this;
    }

    public MachineTextures withOverbrightSouthTexture(String texture) {
        overbrightTextures.replace(Side.SOUTH, "retrostorage:block/" + texture);
        return this;
    }

    public MachineTextures withDefaultWestTexture(String texture) {
        defaultTextures.replace(Side.WEST, "retrostorage:block/" + texture);
        return this;
    }

    public MachineTextures withActiveWestTexture(String texture) {
        activeTextures.replace(Side.WEST, "retrostorage:block/" + texture);
        return this;
    }

    public MachineTextures withOverbrightWestTexture(String texture) {
        overbrightTextures.replace(Side.WEST, "retrostorage:block/" + texture);
        return this;
    }

    public MachineTextures withDefaultEastTexture(String texture) {
        defaultTextures.replace(Side.EAST, "retrostorage:block/" + texture);
        return this;
    }

    public MachineTextures withActiveEastTexture(String texture) {
        activeTextures.replace(Side.EAST, "retrostorage:block/" + texture);
        return this;
    }

    public MachineTextures withOverbrightEastTexture(String texture) {
        overbrightTextures.replace(Side.EAST, "retrostorage:block/" + texture);
        return this;
    }
}