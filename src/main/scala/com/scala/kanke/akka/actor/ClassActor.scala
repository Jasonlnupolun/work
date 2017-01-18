package com.scala.kanke.akka.actor

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.scala.kanke.akka.classrec

/**
  * Created by Administrator on 2017/1/4.
  */
class ClassActor extends Actor {

  override def receive: Receive = {
    case classrec(userid,vedioid) =>
    case _ =>
  }



}
