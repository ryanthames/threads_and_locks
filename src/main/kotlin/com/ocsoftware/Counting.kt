package com.ocsoftware

import java.util.concurrent.atomic.AtomicInteger

class Counting {
  companion object {
    @JvmStatic fun main(args: Array<String>) {
      val counter = AtomicInteger()

      class CountingThread : Thread() {
        override fun run() {
          for(x in 0..10000) {
            counter.incrementAndGet()
          }
        }
      }

      val t1 = CountingThread()
      val t2 = CountingThread()

      t1.start()
      t2.start()
      t1.join()
      t2.join()

      println(counter.get())
    }
  }
}