package akka

/**
  * Created by Administrator on 2016/12/23.
  */

import java.io.File

import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory


object Application extends App {

  val config = ConfigFactory.load("concurrency")
  val system = ActorSystem("actor-system", config)

  val projectDirectory = System.getProperty("user.dir")
  val imagePath = s"$projectDirectory/image/wall-e.png"
  val imagePathToWrite = s"$projectDirectory/image/wall-e-clustered.png"
  val image =  null
//    Image.fromFile(new File(imagePath))
  val imageDataList = null
//  ImageUtil.pixelDataList(image)

  val imageWriter =null
//    new ImageWriter(image.dimensions, imagePathToWrite)

  val k = 16
  val totalIteration = 50

  val kMeansProcessor = system.actorOf(Props(classOf[Processor], imageDataList, k, totalIteration, imageWriter), "processor")
  kMeansProcessor ! Start
}
