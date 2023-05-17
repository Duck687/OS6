package org.example;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MatrixMultiplicator {
  public static void main(String[] args) {
    int[][] A = {{  1, 2}, {3, 4}}; // Приклад матриці A 2x2
    int[][] B = {{5, 6}, {7, 8}}; // Приклад матриці B 2x2

    int n = A.length; // Кількість рядків у матриці A
    int m = A[0].length; // Кількість стовпців у матриці A
    int k = B[0].length; // Кількість стовпців у матриці B

    int[][] C = new int[n][k]; // Результуюча матриця C

    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime()
        .availableProcessors()); // Створення пула потоків

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < k; j++) {
        final int row = i;
        final int col = j;

        executor.submit(() -> {
          // Обчислення елементу C[row, col]
          for (int p = 0; p < m; p++) {
            C[row][col] += A[row][p] * B[p][col];
          }

          System.out.println("[" + row + ", " + col + "] = " + C[row][col]);
        });
      }
    }

    executor.shutdown();
    try {
      executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Виведення результату
    System.out.println("Результуюча матриця C:");
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < k; j++) {
        System.out.print(C[i][j] + " ");
      }
      System.out.println();
    }
  }
}
