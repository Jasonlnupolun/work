package com.scala.kanke.akka

import java.util.concurrent.TimeUnit

import akka.actor.{Props, ActorSystem}
import com.scala.kanke.common.Constant
import com.scala.kanke.dao.DaoImpl

import org.apache.log4j.Logger

import scala.concurrent.duration.Duration

/**
  * Created by Administrator on 2016/12/5.
  */
object CoreAkka {

  val log = Logger.getLogger(getClass)
  Constant.mapGraph
  val dao = new DaoImpl

  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem("recSys")
    val master = actorSystem.actorOf(Props[MasterActor], name = "master")
    //      scheduler().scheduleOnce(Duration.create(2000, TimeUnit. MILLISECONDS)
    while(true) {
      val users = dao.queryAllUserId()
      for(u <- users){

        master!u
        }
      }
    Thread.sleep(35000L)
    }

}
