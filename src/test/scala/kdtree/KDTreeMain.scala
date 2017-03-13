package kdtree

import com.thesamet.spatial.KDTreeMap

/**
  * Created by Administrator on 2017/3/10.
  */
object KDTreeMain {

  def main(args: Array[String]) {
    val map = KDTreeMap(
        (3, 5) -> "a",
        (7, 6) -> "b",
        (12, 7) -> "c")

    print(map.findNearest((12, 7), 3))
//      "KDTreeMap" should "work" in {
//        map.size should equal (3)
//        map.findNearest((3, 5), 1).toSet should equal (Set((3,5) -> "a"))
//        map.findNearest((3, 5), 2).toSet should equal (Set((3,5) -> "a", (7,6) -> "b"))
//        map.findNearest((7, 6), 2).toSet should equal (Set((3,5) -> "a", (7,6) -> "b"))
//        map.findNearest((8, 6), 2).toSet should equal (Set((7,6) -> "b", (12,7) -> "c"))
//      }
  }
}
