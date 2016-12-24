package image

/**
  * Created by Administrator on 2016/12/23.
  */

object ImageUtil {

  def pixelDataList(image: String) = {
    val (width, height) = (1,2)
    for {
      x <- (0 until width).toList
      y <- (0 until height).toList
    } yield PixelData(x, y, null)
  }
}