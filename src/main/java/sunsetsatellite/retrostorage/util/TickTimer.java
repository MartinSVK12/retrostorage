package sunsetsatellite.retrostorage.util;

/**
 * A timer that calls {@link TickTimer#timeoutMethod} when {@link TickTimer#max} ticks have been counted, can be single-shot or looped.
 */
public class TickTimer {
    public Object owner;
    public Procedure timeoutMethod;
    public int max = 0;
    public int value = 0;
    public boolean loop = true;

    public TickTimer(Object owner, Procedure method, int max, boolean loop){
        this.owner = owner;
        this.timeoutMethod = method;
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
			timeoutMethod.run();
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
