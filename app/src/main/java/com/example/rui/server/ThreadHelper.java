package com.example.rui.server;

/**
 * Created by Rui on 14/12/2014.
 */
public class ThreadHelper {

    private boolean finished;

    public ThreadHelper(boolean finished){
        this.finished = finished;
    }

    public void setFinished(boolean b){
        this.finished = b;
    }

    public boolean getFinished() {
        return  this.finished;
    }

}
