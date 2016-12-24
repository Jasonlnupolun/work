package akka

/**
  * Created by Administrator on 2016/12/23.
  */
import akka.actor.{ActorLogging, Actor}
import image.PixelData

class ReducerActor extends Actor with ActorLogging{

  def receive = idle

  def idle: Receive = {
    case initialCentroid: String => {
      context become collecting(initialCentroid, List.empty)
    }
  }

  def collecting(currentCentroid: String, clusterPoints: List[PixelData]): Receive = {
    case point: PixelData => {
      context become collecting(currentCentroid, point :: clusterPoints)
    }
    case EndOfIteration => {
      val total = clusterPoints.foldLeft(0, 0, 0){
        (acc: (Int, Int, Int), p: PixelData) => (1,2,3)
//          (acc._1 + p.rgb.red, acc._2 + p.rgb.green, acc._3 + p.rgb.blue)
      }
      val clusterSize = clusterPoints.size
      val newCentroid = ""
//        String(total._1 / clusterSize, total._2 / clusterSize, total._3 / clusterSize)
      log.info(s"ReducerActor: NewCentroid = $newCentroid")

      context.parent ! newCentroid
      context become collecting(newCentroid, clusterPoints)
    }
    case PrepareForNextIteration => {
      context become collecting(currentCentroid, List.empty)
    }
    case SendClusterData => {
      val clusterCoordinates = clusterPoints.map(p => (p.x, p.y))
      sender ! ClusterData(currentCentroid, clusterCoordinates)
    }
  }
}
