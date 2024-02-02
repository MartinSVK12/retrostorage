package net.sunsetsatellite.retrostorage.util;

import net.minecraft.src.TileEntity;

import java.util.ArrayList;

public abstract class Task {
    public ArrayList<Task> requires;
    public Task parent;
    public boolean completed = false;
    public TileEntity processor;
    public int attempts = 5;

    public boolean requirementsMet(){
        return requires.stream().allMatch((R)->R.completed);
    }
}
