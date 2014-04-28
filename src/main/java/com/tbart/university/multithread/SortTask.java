package com.tbart.university.multithread;

import java.util.Arrays;

/**
 * Created by tbart on 4/25/2014.
 * <p/>
 * This type of task receive one array and sort it using standard sort.
 */
public class SortTask extends Task {
    private int[] data;

    /**
     * @param data - array for sorting
     */
    public SortTask(int[] data) {
        this.data = data;
    }

    @Override
    public int[] call() throws Exception {
        Arrays.sort(data);
        return data;
    }
}
