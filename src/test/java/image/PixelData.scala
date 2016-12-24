package image

/**
  * Created by Administrator on 2016/12/23.
  */

case class PixelData(x: Int, y: Int, rgb: String)

object PixelData {

  def apply(x: Int, y: Int, argb: Int): PixelData = {
    PixelData(x, y, null)
  }

  implicit class RichPixel(rgb: String) {
    def distanceBetween(other: String): Int = {
      val sqr = (x: Int) => x * x
      sqr(2 -  1) + sqr(2 - 1) + sqr(1 - 1)
    }
  }
}
