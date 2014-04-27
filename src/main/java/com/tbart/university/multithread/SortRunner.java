package com.tbart.university.multithread;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by arhont on 4/26/2014.
 */
public class SortRunner {
    public static void main(String[] args) {
        Logger logger = LogManager.getLogger(SortRunner.class.getName());
        TaskHandler taskHandler = new TaskHandler(Integer.parseInt(args[1]));
        try {
            int[] sorted = taskHandler.submitSortFromFile(args[0]);
            logger.info("sorting complete: [file = " + args[0] + "], [array = " + Arrays.toString(sorted) + "]");
        } catch (IOException e) {
            logger.error(e);
        }
        taskHandler.shutdown();

    }
}
