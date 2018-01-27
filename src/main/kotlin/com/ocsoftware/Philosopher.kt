package com.ocsoftware

import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

/**
 * Implementation of the dining philosopher that can't deadlock forever via use of ReentrantLocks and the
 * ability to timeout.
 *
 * Keep in mind this doesn't necessarily prevent deadlock, it just provides a way to recover from a
 * deadlock. It also doesn't prevent or handle livelocks.
 *
 * Point is that timeouts are still not an idea solution - the ideal solution is to prevent deadlocks
 * altogether.
 */
class Philosopher(val leftChopstick: ReentrantLock, val rightChopstick: ReentrantLock) : Thread() {
  val random = Random()

  override fun run() {
    while (true) {
      Thread.sleep(random.nextInt(1000).toLong()) // think for a bit
      leftChopstick.lock()
      try {
        if (rightChopstick.tryLock(1000, TimeUnit.MILLISECONDS)) {
          // Got the right chopstick
          try {
            Thread.sleep(random.nextInt(1000).toLong())
          } finally {
            rightChopstick.unlock()
          }
        } else {
          // didn't get the right chopstick, go back to thinking
        }
      } finally {
        leftChopstick.unlock()
      }
    }
  }
}