package sunsetsatellite.retrostorage.util;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import sunsetsatellite.retrostorage.RetroStorage;

import java.io.*;
import java.lang.reflect.Field;

public class Config {

    public static void init() {
        if (!configFile.exists()) {
            writeConfig();
        }
    }

    public static void writeConfig() {
        try {
            BufferedWriter configWriter = new BufferedWriter(new FileWriter(configFile));
            configWriter.write("// RetroStorage configuration file. Configure options here.");
            configWriter.write(System.getProperty("line.separator") +"//No options for now.");
            configWriter.write(System.getProperty("line.separator") +"//");
            configWriter.write(System.getProperty("line.separator") +"//Configure ID's here. Note: 'null' means a default value will be used.");

            for (Field field : RetroStorage.class.getFields()) {
                if (field.getType() == Block.class) {
                    try {
                        configWriter.write(System.getProperty("line.separator") + field.getName()
                                + "=" + ((Block) field.get(null)).blockID);
                        //idMap.put(field.getName(),((Block) field.get(null)).blockID);
                    } catch (Exception exception) {
                        configWriter.write(System.getProperty("line.separator") + field.getName()
                                + "=null");
                    }
                } else if (field.getType().getSuperclass() == Item.class || field.getType() == Item.class) {
                    try {
                        configWriter.write(System.getProperty("line.separator") + field.getName()
                                + "=" + ((Item) field.get(null)).itemID);
                        //idMap.put(field.getName(),((Item) field.get(null)).shiftedIndex);
                    } catch (Exception exception) {
                        configWriter.write(System.getProperty("line.separator") + field.getName()
                                + "=null");
                    }
                }
            }
            configWriter.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    public static Integer getFromConfig(String s2, Integer base){
        try {
            BufferedReader configReader = new BufferedReader(new FileReader(configFile));
            String s;
            while ((s = configReader.readLine()) != null) {
                if (s.charAt(0) == '/' && s.charAt(1) == '/') {
                    continue; // Ignore comments
                }
                else if (s.contains("=")) {
                    String as[] = s.split("=");
                    String name = as[0];
                    int id = Integer.parseInt(as[1]);
                    //System.out.println(name +" ("+s2+") "+": "+id);
                    if (name.equals(s2)){
                        return id;
                    } else {
                        continue;
                    }
                }
            }

            configReader.close();
        } catch (Exception exception) {
           // exception.printStackTrace();
        }
        return base;
    }

    private static File configFile = new File((Minecraft.getMinecraftDir()) + "/config/RetroStorage.cfg");
}
