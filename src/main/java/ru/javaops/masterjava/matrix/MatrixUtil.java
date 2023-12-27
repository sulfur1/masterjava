package ru.javaops.masterjava.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        CompletionService<int[][]> completionService = new ExecutorCompletionService<>(executor);
        int taskSize = 4;
        final int divMatrixSize = matrixSize / taskSize;
        List<Future<int[][]>> futures = new ArrayList<>();
        for (int i = 0; i < matrixSize; i += divMatrixSize) {
            final int to = i == (matrixSize / taskSize) * (taskSize - 1) ? matrixSize : i + divMatrixSize;
            futures.add(completionService.submit(getCall(matrixA, matrixB, i, to, matrixC)));
        }
        while (!futures.isEmpty()) {
            Future<int[][]> matrix = completionService.poll();
            if (matrix != null) futures.remove(matrix);
        }

        return matrixC;
    }

    private static Callable<int[][]> getCall(int[][] matrixA, int[][] matrixB, int of, int to, int[][] matrixC) {
        return () -> {
            final int matrixSize = matrixA.length;
            final int[][] matrixBT = new int[matrixSize][matrixSize];
            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    matrixBT[j][i] = matrixB[i][j];
                }
            }
            for (int i = of; i < to; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    int sum = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        sum += matrixA[i][k] * matrixBT[j][k];
                    }
                    matrixC[i][j] = sum;
                }
            }
            return matrixC;
        };
    }


    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
       /* final int[][] matrixBT = new int[matrixB.length][matrixB.length];
        final int matrixBTSize = matrixBT.length;
        for (int i = 0; i < matrixBTSize; i++) {
            for (int j = 0; j < matrixBTSize; j++) {
                matrixBT[j][i] = matrixB[i][j];
            }
        }*/

        final int matrixSize = matrixA.length;
        final int matrixSizeB = matrixB.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        final int[] matrixStr = new int[matrixSizeB];

        for (int i = 0; i < matrixSize; i++) {
            for (int k = 0; k < matrixSize; k++) {
                matrixStr[k] = matrixB[k][i];
            }
            for (int j = 0; j < matrixSize; j++) {
                final int[] thisRow = matrixA[j];
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += thisRow[k] * matrixStr[k];
                }
                matrixC[j][i] = sum;
            }
        }

        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
