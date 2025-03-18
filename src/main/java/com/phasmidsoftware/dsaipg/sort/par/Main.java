/*
 * Copyright (c) 2024. Robin Hillyard
 */
package com.phasmidsoftware.dsaipg.sort.par;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much. CONSIDER tidy
 * it up a bit.
 */
public class Main {

    private static int initialCutoff = 1000;

    public static void main(String[] args) {
        processArgs(args);
        System.out.println("Degree of parallelism: " + ForkJoinPool.getCommonPoolParallelism());
        int[] arraySizes = {500000, 1000000, 1500000, 2000000};
        String outputFile = "./src/result.csv";
        int maxThreads = 128;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            bw.write("Array Size,Threads,Cutoff,Avg Time (ms)\n");

            for (int size : arraySizes) {
                System.out.println("\n Testing Array Size: " + size);

                for (int thread = 2; thread <= maxThreads; thread *= 2) {
                    ForkJoinPool pool = new ForkJoinPool(thread);
                    System.out.println("\nThreads: " + pool.getParallelism());

                    int[] array = new int[size];
                    ParSort.cutoff = size / 4 + 10000;

                    for (int j = 0; j < 25; j++) {
                        long totalTime = 0;

                        for (int t = 0; t < 10; t++) {
                            fillArray(array);
                            long startTime = System.currentTimeMillis();
                            pool.submit(() -> ParSort.sort(array, 0, array.length)).join();
                            long endTime = System.currentTimeMillis();
                            totalTime += (endTime - startTime);
                        }

                        long avgTime = totalTime / 10;
                        System.out.println("Array Size: " + size + " | Threads: " + thread + " | Cutoff: " + ParSort.cutoff + " | Avg Time: " + avgTime + "ms");
                        bw.write(size + "," + thread + "," + ParSort.cutoff + "," + avgTime + "\n");
                        bw.flush();

                        ParSort.cutoff += 10000;
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void fillArray(int[] array) {
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(10000000);
        }
    }

    private static void processArgs(String[] args) {
        if (args.length > 0) {
            try {
                initialCutoff = Integer.parseInt(args[0]);
                System.out.println("🔹 Cutoff set to: " + initialCutoff);
            } catch (NumberFormatException e) {
                System.err.println("⚠️ Invalid cutoff value. Using default: " + initialCutoff);
            }
        }
    }

    private static String[] processArg(String[] xs) {
        String[] result = new String[0];
        System.arraycopy(xs, 2, result, 0, xs.length - 2);
        processCommand(xs[0], xs[1]);
        return result;
    }

    private static void processCommand(String x, String y) {
        if (x.equalsIgnoreCase("N")) {
            setConfig(x, Integer.parseInt(y)); 
        }else if (x.equalsIgnoreCase("P")) // Ignore result
        {
            ForkJoinPool.getCommonPoolParallelism();
        }
    }

    private static void setConfig(String x, int i) {
        configuration.put(x, i);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Integer> configuration = new HashMap<>();

}
