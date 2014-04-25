package com.tbart.university.multithread;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Created by arhont on 4/26/2014.
 */
public class SortRunner {
    public static void main(String[] args) {
        Logger logger=LogManager.getLogger(SortRunner.class.getName());
        logger.info("info hello world!");
        logger.error("error hello world!");
    }
}
