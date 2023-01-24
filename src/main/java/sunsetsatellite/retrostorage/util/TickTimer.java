package sunsetsatellite.retrostorage.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TickTimer {
    public Object owner;
    public Method timeout;
    public int max = 0;
    public int value = 0;
    public boolean loop = true;

    public TickTimer(Object owner, Method timeout, int max, boolean loop){
        this.owner = owner;
        this.timeout = timeout;
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
