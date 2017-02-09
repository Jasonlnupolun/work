package scalademo

/**
  * Created by Administrator on 2017/2/6.
  */
object Demo {
  case class demotese(a:String,b:String,c:String)
  def main(args: Array[String]) {
//    val tagsresult = "{}"
//    val tagsArray = tagsresult.replaceAll("[\\{\\}]","").split(",")
//    print(tagsArray)
      val ss = demotese("1111","2","d")
      println(ss.copy(a="sss",b="5555"))
  }
}
