package com.ocsoftware

import java.util.concurrent.locks.ReentrantLock

class ConcurrentSortedList() {
  private class Node() {
    var value = 0
    lateinit var prev: Node
    lateinit var next: Node

    val lock = ReentrantLock()

    constructor(value: Int, prev: Node, next: Node): this() {
      this.value = value
      this.prev = prev
      this.next = next
    }
  }

  private var head: Node
  private var tail: Node

  init {
    head = Node()
    tail = Node()

    head.next = tail
    tail.prev = head
  }

  fun insert(value: Int) {
    var current = head
    current.lock.lock()
    var next = current.next

    try {
      while(true) {
        next.lock.lock()
        try {
          if(next == tail || next.value < value) {
            val node = Node(value, current, next)
            next.prev = node
            current.next = node
            return
          }
        } finally {
          current.lock.unlock()
        }

        current = next
        next = current.next
      }
    } finally {
      next.lock.unlock()
    }
  }

  fun size(): Int {
    var current = tail
    var count = 0

    while(current.prev != head) {
      val lock = current.lock
      lock.lock()
      try {
        ++count
        current = current.prev
      } finally {
        lock.unlock()
      }
    }

    return count
  }
}