package org.example;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
public class MatrixMultiplicationBenchmark {
  public static void main(String[] args) {
    int[][] A = generateMatrix(100, 100); // Генеруємо матрицю A розміром 100x100
    int[][] B = generateMatrix(100, 100); // Генеруємо матрицю B розміром 100x100

    int n = A.length; // Кількість рядків у матриці A
    int m = A[0].length; // Кількість стовпців у матриці A
    int k = B[0].length; // Кількість стовпців у матриці B

    int[][] C = new int[n][k]; // Результуюча матриця C
    int g = Runtime.getRuntime().availableProcessors();

    int[] threadCounts = new int[g]; // Кількість потоків для тестування

    for(int i = 0; i < g; i++)
    {
      threadCounts[i] = i+1;
    }

    for (int threadCount : threadCounts) {
      System.out.println("Кількість потоків: " + threadCount);

      ExecutorService executor = Executors.newFixedThreadPool(threadCount); // Створення пула потоків

      long startTime = System.nanoTime();

      for (int i = 0; i < n; i++) {
        for (int j = 0; j < k; j++) {
          final int row = i;
          final int col = j;

          executor.submit(() -> {
            // Обчислення елементу C[row, col]
            for (int p = 0; p < m; p++) {
              C[row][col] += A[row][p] * B[p][col];
            }
          });
        }
      }

      executor.shutdown();
      try {
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      long endTime = System.nanoTime();
      long duration = endTime - startTime;
      double seconds = (double) duration / 1_000_000_000.0;
      System.out.println("Час виконання: " + seconds + " секунд");

      // Обнулення результату для наступного тесту
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < k; j++) {
          C[i][j] = 0;
        }
      }
    }
  }

  // Генерування випадкової матриці розміром rows x cols
  private static int[][] generateMatrix(int rows, int cols) {
    int[][] matrix = new int[rows][cols];
    for (int i = 0; i
        < rows; i++) {
      for (int j = 0; j < cols; j++) {
        matrix[i][j] = (int) (Math.random() * 10);
      }
    }
    return matrix;
  }
}
