package com.ocsoftware

import java.util.*
import java.util.concurrent.locks.ReentrantLock

/**
 * Dining philosopher example using condition variables
 */
class ConditionalPhilosopher(private val table: ReentrantLock) : Thread() {
  var eating = false
  lateinit var left: ConditionalPhilosopher
  lateinit var right: ConditionalPhilosopher
  val random = Random()
  val condition = table.newCondition()

  fun think() {
    table.lock()

    try {
      eating = false
      left.condition.signal()
      right.condition.signal()
    } finally {
      table.unlock()
    }

    Thread.sleep(1000)
  }

  fun eat() {
    table.lock()

    try {
      // the condition that will keep the philosopher waiting to eat
      while(left.eating || right.eating) {
        condition.await()
      }

      eating = true
    } finally {
      table.unlock()
    }

    Thread.sleep(1000)
  }

  override fun run() {
    while(true) {
      think()
      eat()
    }
  }
}