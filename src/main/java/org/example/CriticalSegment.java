package org.example;

public class CriticalSegment {
  private static int sharedVariable = 0;

  public static void main(String[] args) throws InterruptedException {
    Thread thread1 = new Thread(() -> {
      for (int i = 0; i < 1_000_000_000; i++) {
        synchronized (CriticalSegment.class) {
          sharedVariable++;
        }
      }
    });

    Thread thread2 = new Thread(() -> {
      for (int i = 0; i < 1_000_000_000; i++) {
        synchronized (CriticalSegment.class) {
          sharedVariable++;
        }
      }
    });

    thread1.start();
    thread2.start();

    thread1.join();
    thread2.join();

    System.out.println("Final value of sharedVariable: " + sharedVariable);
  }

}
