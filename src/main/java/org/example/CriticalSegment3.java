package org.example;

import java.awt.SystemTray;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CriticalSegment3 {
  private static final Object lock = new Object();
  private static volatile int sharedVariable = 0;
  private static boolean thread1Turn = true;

  public static void main(String[] args) throws InterruptedException {
    Thread thread1 = new Thread(() -> {
      for (int i = 0; i < 1000; i++) {
        synchronized (lock) {
          while (!thread1Turn) {
            try {
              lock.wait();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
          sharedVariable++;
          System.out.println("First");
          thread1Turn = false;
          lock.notify();
        }
      }
    });

    Thread thread2 = new Thread(() -> {
      for (int i = 0; i < 1000; i++) {
        synchronized (lock) {
          while (thread1Turn) {
            try {
              lock.wait();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
          sharedVariable++;
          System.out.println("Second");
          thread1Turn = true;
          lock.notify();
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
