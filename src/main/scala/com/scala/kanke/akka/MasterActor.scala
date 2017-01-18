package com.scala.kanke.akka

import akka.actor.{ActorRef, Props, Actor}
import com.scala.kanke.akka.actor.{MixActor, TagActor, ClassActor}

/**
  * Created by Administrator on 2017/1/4.
  */
class MasterActor extends Actor {

//  var mapActor: ActorRef = context.actorOf(Props(new MapActor(reduceActor)), name = "map")
  val class_rec = context.actorOf(Props[ClassActor], name = "class")
  val tags_rec =   context.actorOf(Props[TagActor], name = "tags")
  val mix_rec = context.actorOf(Props[MixActor], name = "mix")

  override def receive: Receive = {
    case classrec(userid,vedioid) =>class_rec ! classrec(userid,vedioid)
    case tagsrec(userid,vedioid) =>tags_rec ! tagsrec(userid,vedioid)
    case mixrec(userid,vedioid) =>mix_rec ! mixrec(userid,vedioid)
    case _ =>
  }
}
