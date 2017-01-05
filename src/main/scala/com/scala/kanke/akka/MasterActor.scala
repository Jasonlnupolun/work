package com.scala.kanke.akka

import akka.actor.{ActorRef, Props, Actor}
import akka.actor.Actor.Receive

/**
  * Created by Administrator on 2017/1/4.
  */
class MasterActor extends Actor {


  var mapActor: ActorRef = context.actorOf(Props[MapActor], name = "map")

  override def receive: Receive = {

    case message: String =>
      println(message)
//      mapActor ! message
    //    case message: Result => aggregateActor ! message
    case _ =>
  }
}
