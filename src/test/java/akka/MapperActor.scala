package akka

/**
  * Created by Administrator on 2016/12/23.
  */

import akka.actor.Actor
import image.PixelData
;
import org.apache.kafka.common.network.Receive;

class MapperActor extends Actor {

  override def receive = idle

  def idle: Receive= {
    case NewCentroids(centroids) => {
      context become working(centroids)
    }
  }

  def working(centroids: List[Centroid]): Receive = {
    case Start => {
      sender ! NextJob
    }
    case p@PixelData(_, _, rgb) => {
      //find the closest centroid and send pixelData to associated ReducerActor
      val closestCentroid = centroids.minBy(c => null)
      closestCentroid.reducerRef ! p
      sender ! NextJob
    }
  }
}
