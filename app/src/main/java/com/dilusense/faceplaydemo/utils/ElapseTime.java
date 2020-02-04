package com.dilusense.faceplaydemo.utils;

import java.util.concurrent.TimeUnit;

/**
 * Created by Thinkpad on 2017/11/25.
 */

public class ElapseTime {
    
    private long lastTime = 0;

    public ElapseTime(){
        this.lastTime = System.currentTimeMillis();
    }

    public boolean isSomeLengthTimeElapse(int timeLength, TimeUnit timeUnit){

        long elapseTime = System.currentTimeMillis() - lastTime;

        long timeInterval = timeUnit.toMillis(timeLength);
        
        if(elapseTime >= timeInterval) {
            lastTime = System.currentTimeMillis();
            return true;
        }else{
            return false;
        }
    }

    public boolean isSomeSecondsElapse(int timeLength){
        return isSomeLengthTimeElapse(timeLength, TimeUnit.SECONDS);
    }

    public boolean isSomeMillisElapse(int timeLength){
        return isSomeLengthTimeElapse(timeLength, TimeUnit.MILLISECONDS);
    }
}
