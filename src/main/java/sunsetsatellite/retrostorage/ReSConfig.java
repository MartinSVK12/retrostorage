package sunsetsatellite.retrostorage;

import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static sunsetsatellite.retrostorage.RetroStorage.MOD_ID;

public class ReSConfig {
    public static int itemIdStart = 18000;
    public static int blockIdStart = 1400;

    public static final TomlConfigHandler config;

    static {
        List<Field> blockFields = Arrays.stream(ReSBlocks.class.getDeclaredFields()).filter((F) -> Block.class.isAssignableFrom(F.getType())).collect(Collectors.toList());
        List<Field> itemFields = Arrays.stream(ReSItems.class.getDeclaredFields()).filter((F) -> Item.class.isAssignableFrom(F.getType())).collect(Collectors.toList());

        Toml defaultConfig = new Toml("RetroStorage configuration file.");
        defaultConfig.addCategory("BlockIDs");
        defaultConfig.addCategory("ItemIDs");
        defaultConfig.addCategory("Other");
        defaultConfig.addEntry("Other.goldenDiscLoot", false);

        int blockId = blockIdStart;
        int itemId = itemIdStart;
        for (Field blockField : blockFields) {
            defaultConfig.addEntry("BlockIDs." + blockField.getName(), blockId++);
        }
        for (Field itemField : itemFields) {
            defaultConfig.addEntry("ItemIDs." + itemField.getName(), itemId++);
        }

        config = new TomlConfigHandler(MOD_ID, new Toml("RetroStorage configuration file."), false);

        File configFile = config.getConfigFile();

        if (config.getConfigFile().exists()) {
            config.loadConfig();
            config.setDefaults(config.getRawParsed());
            Toml rawConfig = config.getRawParsed();
            int maxBlocks = ((Toml) rawConfig.get(".BlockIDs")).getOrderedKeys().size();
            int maxItems = ((Toml) rawConfig.get(".ItemIDs")).getOrderedKeys().size();
            int newNextBlockId = blockIdStart + maxBlocks;
            int newNextItemId = itemIdStart + maxItems;
            boolean changed = false;

            for (Field F : blockFields) {
                if (!rawConfig.contains("BlockIDs." + F.getName())) {
                    rawConfig.addEntry("BlockIDs." + F.getName(), newNextBlockId++);
                    changed = true;
                }
            }
            for (Field F : itemFields) {
                if (!rawConfig.contains("ItemIDs." + F.getName())) {
                    rawConfig.addEntry("ItemIDs." + F.getName(), newNextItemId++);
                    changed = true;
                }
            }

            if (!rawConfig.contains("Other.goldenDiscLoot")) {
                rawConfig.addEntry("Other.goldenDiscLoot", false);
                changed = true;
            }

            if (changed) {
                config.setDefaults(rawConfig);
                config.writeConfig();
                config.loadConfig();
            }
        } else {
            config.setDefaults(defaultConfig);
            try {
                //noinspection ResultOfMethodCallIgnored
                configFile.getParentFile().mkdirs();
                //noinspection ResultOfMethodCallIgnored
                configFile.createNewFile();
                config.writeConfig();
                config.loadConfig();
            } catch (IOException e) {
                throw new RuntimeException("Failed to generate config!", e);
            }
        }
    }

    public static int item(String cfgId) {
        return config.getInt("ItemIDs." + cfgId);
    }

    public static int block(String cfgId) {
        return config.getInt("BlockIDs." + cfgId);
    }
}
