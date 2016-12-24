import akka.actor.ActorRef
import image.Coordinate

/**
  * Created by Administrator on 2016/12/23.
  */
package object akka {
  case class NewCentroids(centroids: List[Centroid])
  case class Centroid(data: String, reducerRef: ActorRef)

  case object Start
  case object NextJob

  case class ClusterData(centroidRGB: String, clusterPoints: List[Coordinate])
  case object EndOfIteration
  case object PrepareForNextIteration
  case object SendClusterData
}
