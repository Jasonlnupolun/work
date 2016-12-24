package image

/**
  * Created by Administrator on 2016/12/23.
  */

class ImageWriter(dimensions: (Int, Int), pathToWrite: String) {

  def write(clusterMap: Map[String, List[Coordinate]]) = {
    val image = null
//      Image.apply(dimensions._1, dimensions._2)

    clusterMap.foreach(p => {
      p._2.foreach(coord => null)
//        image.setPixel(coord._1, coord._2, p._1.toPixel))
    })
  }
}