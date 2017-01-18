package com.scala.kanke.akka


import akka.actor.{Props, ActorSystem}
import com.java.kanke.utils.kafka.KafkaConsumer
import kafka.consumer.ConsumerIterator

import org.apache.log4j.Logger
import scala.concurrent.duration.Duration

/**
  * Created by Administrator on 2016/12/5.
  */
case class classrec(userid:String,vedioid:String)
case class mixrec(userid:String,vedioid:String)
case class tagsrec(userid:String,vedioid:String)
object CoreAkka {
  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem("recSys")
    val master = actorSystem.actorOf(Props[MasterActor], name = "master")
    val it: ConsumerIterator[String, String] = new KafkaConsumer().consume
    while (it.hasNext()){
      master ! classrec(it.next().message(),"film_844128")
      master ! mixrec(it.next().message(),"film_844128")
      master ! tagsrec(it.next().message(),"film_844128")
    }
  }

}
