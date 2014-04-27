package com.tbart.university.multithread;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by arhont on 4/26/2014.
 */
public class TaskHandlerTest {
    private String relativePath;

    @Before
    public void setUp() throws Exception {
        relativePath = getClass().getClassLoader().getResource("").getPath();
    }

    @Test
    public void testEmptyList() throws Exception {
        List<int[]> arrs=new ArrayList<>();
        TaskHandler taskHandler=new TaskHandler(1);
        int[] sorted = taskHandler.submitSort(arrs);
        taskHandler.shutdown();
        Assert.assertArrayEquals(null,sorted);
    }


    @Test
    public void testEmptyArray() throws Exception {
        List<int[]> arrs=new ArrayList<>();
        int[] arr = new int[]{};
        arrs.add(arr);

        runTest(arrs,1);
    }

    @Test
    public void testOneArray() throws Exception {
        List<int[]> arrs=new ArrayList<>();
        int[] arr = new int[]{10, 23, 3, 8, 7, 6};
        arrs.add(arr);

        runTest(arrs,1);
    }

    @Test
    public void testTwoArrays() throws Exception {
        List<int[]> arrs=new ArrayList<>();
        int[] arr1 = new int[]{3, 6, 10, 29, 101, 55};
        int[] arr2 = new int[]{11, 24, 35, 23, 88, 66};
        arrs.add(arr1);
        arrs.add(arr2);

        runTest(arrs, 2);
    }

    @Test
    public void randomizedTest() throws Exception {
        int iterNumber = 100;
        int arrNumberLimit = 1000;
        int arrSizeLimit = 100;
        int threadPoolLimit = 50;
        Random random = new Random();
        for (int i = 0; i < iterNumber; i++){
            List<int[]> arrs=new ArrayList<>();
            int arrNumber = random.nextInt(arrNumberLimit - 1) + 1;

            for (int j = 0; j < arrNumber; j++){
                int arrSzie = random.nextInt(arrSizeLimit - 1) + 1;
                arrs.add(generateRandomArray(arrSzie, random));
            }

            int poolSize = random.nextInt(threadPoolLimit - 1) + 1;
            runTest(arrs, poolSize);
        }

    }

    private int[] generateRandomArray(int size, Random random){
        int[] arr = new int[size];
        for (int i = 0; i < size; i++){
            arr[i] = random.nextInt(10000);
        }
        return arr;
    }

    private void runTest(List<int[]> arrs, int threadPoolSize){
        TaskHandler taskHandler=new TaskHandler(threadPoolSize);

        int mSize = 0;
        for (int[] arr : arrs) {
            mSize += arr.length;
        }

        int[] merged = new int[mSize];

        int pos = 0;
        for (int[] arr : arrs) {
            System.arraycopy(arr,0,merged,pos,arr.length);
            pos += arr.length;
        }

        Arrays.sort(merged);

        int[] sorted = taskHandler.submitSort(arrs);
        Assert.assertArrayEquals(merged,sorted);

        taskHandler.shutdown();
    }

    @Test
    public void testFileApi() throws Exception {
        TaskHandler taskHandler=new TaskHandler(3);
        int[] sorted = taskHandler.submitSortFromFile(relativePath + "forSort.txt");
        int[] expected = new int[]{1, 9, 10, 11, 13, 21, 21, 23, 33, 34, 55, 66, 123};
        Assert.assertArrayEquals(expected, sorted);
        taskHandler.shutdown();

    }
}
