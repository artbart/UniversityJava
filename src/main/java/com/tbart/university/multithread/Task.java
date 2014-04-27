package com.tbart.university.multithread;

import java.util.concurrent.Callable;

/**
 * Created by arhont on 4/25/2014.
 */
public abstract class Task implements Callable<int[]> {
    private int taskId;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
