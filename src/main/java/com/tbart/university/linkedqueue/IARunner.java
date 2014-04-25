package com.tbart.university.linkedqueue;

import java.io.IOException;

/**
 * Created by tbart on 4/11/2014.
 */
public class IARunner {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("<input file path> <output file path>");
        }
        try {
            new InstructionsHandler(args[0], args[1]).handle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
