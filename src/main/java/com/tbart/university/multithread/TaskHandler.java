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
 * Created by arhont on 4/25/2014.
 */
public class TaskHandler {
    private int id;
    private static AtomicInteger taskHandlerId = new AtomicInteger(0);

    private CompletionService<int[]> threadPool;
    private ExecutorService fixedThreadPool;
    private Logger logger = LogManager.getFormatterLogger(TaskHandler.class);
    private int taskId;
    private String logPrefix;

    public TaskHandler(int size) {
        fixedThreadPool = Executors.newFixedThreadPool(size);
        threadPool = new ExecutorCompletionService<>(fixedThreadPool);
        id = taskHandlerId.getAndIncrement();
        logPrefix = "[" + id + "]: ";
        logger.info(logPrefix + "Task handler was created [pool size = %d]", size);

        taskId = 0;
    }

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

    public int[] submitSort(List<int[]> parts) {
        int size = parts.size();
        int threadsCount = size;
        for (int[] part : parts) {
            Task task = new SortTask(part);
            task.setTaskId(taskId++);
            threadPool.submit(task);
        }

        logger.info(logPrefix + "%d tasks were submitted", threadsCount);

        int[] tmp = null;
        while (threadsCount != 0) {
            try {
                int[] newTmp = threadPool.take().get();
                if (tmp == null) {
                    tmp = newTmp;
                    threadsCount--;
                } else {
                    Task task = new MergeTask(tmp, newTmp);
                    task.setTaskId(taskId++);
                    threadPool.submit(task);
                    tmp = null;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        logger.info(logPrefix + "%d tasks were completed", size);

        return tmp;
    }


    public void shutdown() {
        fixedThreadPool.shutdown();
        logger.info(logPrefix + "Task handler was killed");
    }
}
