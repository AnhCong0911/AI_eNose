package com.example.ainose;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DataSampler {
    private Timer timer;

    public DataSampler() {
        this.timer = null; // default timer
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public void startSampling(){
        // Create a new timer
        timer = new Timer();

        // Create a TimerTask that will be executed every 2 seconds
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // Call a method to get the data
                _10s_sampling();
            }
        };

        timer.scheduleAtFixedRate(task, 0, 10000);
    }

    public void stopSampling() {
        // Cancel the timer to stop sampling data
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void _10s_sampling(){
        if(MainActivity.getInstance().dataList.isEmpty()){
            // Don't do anything. Skip for 10s
        }
        else{
            // Process data in the list
            MainActivity.getInstance().processData();
        }
    }
}
