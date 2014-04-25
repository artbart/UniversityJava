package com.tbart.university.multithread;

import java.util.Arrays;

/**
 * Created by arhont on 4/25/2014.
 */
public class SortTask extends Task {
    private int[] data;

    public SortTask(int[] data) {
        this.data = data;
    }

    @Override
    public int[] call() throws Exception {
        Arrays.sort(data);
        return data;
    }
}
