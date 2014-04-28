package com.tbart.university.multithread;

/**
 * Created by tbart on 4/26/2014.
 * <p/>
 * This type of task receive two arrays and merger their into one,
 * new arrays will be sorted.
 * Main concept like in the merger part of merge sort.
 */
public class MergeTask extends Task {
    private int[] data1;
    private int[] data2;

    /**
     * @param data1 - first array for merging
     * @param data2 - second array for merging
     */
    public MergeTask(int[] data1, int[] data2) {
        this.data1 = data1;
        this.data2 = data2;
    }

    @Override
    public int[] call() throws Exception {
        int size = data1.length + data2.length;
        int[] mergedData = new int[size];

        int iD1 = 0;
        int iD2 = 0;

        for (int i = 0; i < size; i++) {
            if (iD1 == data1.length) {
                mergedData[i] = data2[iD2];
                iD2++;
            } else if (iD2 == data2.length) {
                mergedData[i] = data1[iD1];
                iD1++;
            } else {
                if (data1[iD1] < data2[iD2]) {
                    mergedData[i] = data1[iD1];
                    iD1++;
                } else {
                    mergedData[i] = data2[iD2];
                    iD2++;
                }
            }
        }

        return mergedData;
    }
}
