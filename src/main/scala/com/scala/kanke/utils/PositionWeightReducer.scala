package com.scala.kanke.utils

import scala.collection.mutable

/**
  * 带参构造
  *
  * @param weight 位置权重,决定结果覆盖率的大小
  * @param size   最多要衰减多少个,获知size 将权值控制在0 到 1之间
  */
class PositionWeightReducer(val weight: Double, size: Double) {
  val decrement = weight / size
  val counter: mutable.Map[Int, Int] = mutable.Map[Int, Int]()

  def reduce(id: Int): Double = {
    //    println("reduceID:"+id)
    if (!counter.contains(id)) {
      counter.update(id, 0)
    }
    val count = counter(id)
    counter.update(id, count + 1)
    count * decrement
  }

  def string: Unit = {
    println(weight)
    for ((id, cnt) <- counter) {
      println("id:" + id + "|" + cnt)
    }
  }
}

object testReduce {
  def main(args: Array[String]) {
    val pw = new PositionWeightReducer(5, 100)
    for (i <- 0 until 10) {
      println(pw.reduce(5))
    }
    println("==")
    for (i <- 0 until 10) {
      println(pw.reduce(3))
    }
    pw.string
  }
}
