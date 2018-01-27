package com.ocsoftware

import java.util.concurrent.locks.ReentrantLock

/**
 * Re-implementation of Uninterruptible that interrupts gracefully using
 * Reentrant Locks
 */
class Interruptible {
  companion object {
    @JvmStatic fun main(args: Array<String>) {
      val l1 = ReentrantLock()
      val l2 = ReentrantLock()

      val t1 = Thread({
        try {
          l1.lockInterruptibly()
          Thread.sleep(1000)
          l2.lockInterruptibly()
        } catch (e: InterruptedException) {
          println("t1 interrupted")
        }
      })

      val t2 = Thread({
        try {
          l2.lockInterruptibly()
          Thread.sleep(1000)
          l1.lockInterruptibly()
        } catch (e: InterruptedException) {
          println("t2 interrupted")
        }
      })

      t1.start()
      t2.start()
      Thread.sleep(2000)
      t1.interrupt()
      t2.interrupt()
      t1.join()
      t2.join()
    }
  }
}