package net.sunsetsatellite.retrostorage.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TickTimer {
    public Object owner;
    public Method timeout;
    public int max = 0;
    public int value = 0;
    public boolean loop = true;

    public TickTimer(Object owner, String timeout, int max, boolean loop){
        this.owner = owner;
        try {
            this.timeout = owner.getClass().getMethod(timeout);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        this.max = max;
        this.loop = loop;
    }

    public void tick(){
        if(value >= 0){
            value++;
        }
        if(value >= max){
            if(loop){
                value = 0;
            } else {
                value = -1;
            }
            try {
                timeout.invoke(owner);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                if(e.getCause() instanceof Error){
                    throw new Error("Fatal error occurred when invoking method.");
                }
            }
        }
    }

    public void unpause(){
        value = 0;
    }

    public void pause(){
        value = -1;
    }

    public boolean isPaused(){
        return value == -1;
    }
}
