package com.tbart.university.multithread;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Created by tbart on 4/25/2014.
 * <p/>
 * This class handles tasks.
 * <p>Algorithm:</p>
 * <ul>
 * <li>Receives either list of arrays or file (list of arrays is read from file).</li>
 * <li>Each array is submitted as SortTask to thread pool</li>
 * <li>When a task completed it return one array</li>
 * <li>Result of two tasks (2 arrays) are submitted as MergeTask</li>
 * <li>Run previous step until there is an one array</li>
 * <li>The last arrays is result (sorted array of all numbers in submitted arrays)</li>
 * </ul>
 */
public class TaskHandler {
    private int id;
    private static AtomicInteger taskHandlerId = new AtomicInteger(0);
    private static AtomicInteger taskId = new AtomicInteger(0);

    private CompletionService<int[]> threadPool;
    private ExecutorService fixedThreadPool;
    private Logger logger = LogManager.getFormatterLogger(TaskHandler.class);
    private String logPrefix;


    public TaskHandler(int thredPoolSize) {
        fixedThreadPool = Executors.newFixedThreadPool(thredPoolSize);
        threadPool = new ExecutorCompletionService<>(fixedThreadPool);
        id = taskHandlerId.getAndIncrement();
        logPrefix = "[" + id + "]: ";
        logger.info(logPrefix + "Task handler was created [pool size = %d]", thredPoolSize);
    }

    /**
     * @param filePath - path to a file with arrays for sorting
     * @return - sorted array of all numbers from the @param filePath
     * @throws IOException
     */
    public int[] submitSortFromFile(String filePath) throws IOException {
        List<int[]> parts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] splts = line.split(" ");
                int[] numbers = new int[splts.length];
                for (int i = 0; i < splts.length; i++) {
                    numbers[i] = Integer.parseInt(splts[i]);
                }
                parts.add(numbers);
            }
        }
        return submitSort(parts);
    }

    /**
     * @param parts - arrays for sorting
     * @return - sorted array of all numbers from the @param parts
     */
    public int[] submitSort(List<int[]> parts) {
        int size = parts.size();
        int aliveTaskCount = size;
        for (int[] part : parts) {
            int newTaskId = taskId.getAndIncrement();
            Task task = new SortTask(part);
            task.setTaskId(newTaskId);
            threadPool.submit(task);
        }

        logger.info(logPrefix + "%d tasks were submitted", aliveTaskCount);

        int[] tmp = null;
        while (aliveTaskCount != 0) {
            try {
                int[] newTmp = threadPool.take().get();
                aliveTaskCount--;
                if (tmp == null) {
                    tmp = newTmp;
                } else {
                    int newTaskId = taskId.getAndIncrement();
                    Task task = new MergeTask(tmp, newTmp);
                    task.setTaskId(newTaskId);
                    threadPool.submit(task);
                    aliveTaskCount++;
                    tmp = null;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        logger.info(logPrefix + "%d tasks were completed", size);

        return tmp;
    }

    /**
     * clean up
     */
    public void shutdown() {
        fixedThreadPool.shutdown();
        logger.info(logPrefix + "Task handler was killed");
    }
}
