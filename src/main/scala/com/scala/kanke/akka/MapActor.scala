package com.scala.kanke.akka

import akka.actor.{Props, ActorRef, Actor}
import akka.actor.Actor.Receive
import com.scala.kanke.akka.actor.{TagActor, MixActor, HotActor, ClassActor}

/**
  * Created by Administrator on 2017/1/4.
  */
class MapActor extends Actor {

  var classActor: ActorRef = context.actorOf(Props[ClassActor], name = "classActor")
  var hotActor: ActorRef = context.actorOf(Props[HotActor], name = "hotActor")
  var mixActor: ActorRef = context.actorOf(Props[MixActor], name = "mixActor")
  var tagActor: ActorRef = context.actorOf(Props[TagActor], name = "tagActor")


  override def receive: Receive = {
    case message: String =>
      classActor!message
      hotActor!message
      mixActor!message
      tagActor!message
    case _ =>
  }

  def save()={

  }
}
