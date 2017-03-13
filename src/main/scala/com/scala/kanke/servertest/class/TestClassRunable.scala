package com.scala.kanke.servertest

import java.util.concurrent.Executors

/**
  * Created by Administrator on 2017/3/13.
  */
class Test extends  Runnable{
  override def run(): Unit = {
        println( Thread.currentThread().getName())
  }
}

object TestClassRunable {
  def main(args: Array[String]) {
    val executorService = Executors.newCachedThreadPool()
    for ( i <- 1 to 100) {
      executorService.execute(new Test)
    }
  }

}
