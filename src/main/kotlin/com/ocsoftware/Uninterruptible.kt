package com.ocsoftware

/**
 * Example of a deadlock caused by intrinsic locking
 *
 * Because a thread that's blocked on an intrinsic lock is not interruptible,
 * we have on way to recover from a deadlock.
 */
class Uninterruptible {
  companion object {
    @JvmStatic fun main(args: Array<String>) {
      val o1 = Any()
      val o2 = Any()

      val t1 = Thread({
        synchronized(o1) {
          Thread.sleep(1000)
          synchronized(o2) {}
        }
      })

      val t2 = Thread({
        synchronized(o2) {
          Thread.sleep(1000)
          synchronized(o1) {}
        }
      })

      t1.start()
      t2.start()
      Thread.sleep(1000)
      t1.interrupt()
      t2.interrupt()
      t1.join()
      t2.join()
    }
  }
}