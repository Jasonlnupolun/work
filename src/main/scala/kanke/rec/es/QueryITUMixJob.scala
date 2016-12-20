package kanke.rec.es

import java.util.Date

import com.java.kanke.utils.bean.UserHistory
import org.apache.log4j.Logger

object TimeInterval{
  lazy val second:Long = 1000
  lazy val minute:Long = second*60
  lazy val hour:Long = minute*60
  lazy val day:Long = hour*24
  lazy val month:Long = day*30
}
/**
  * Created by gavin on 16-4-12.
  * northland89@163.com 
  */
case class QueryITUMixJob(id:Long,size:Int,days:Int,esIndex:String) extends ESJob[UserHistory](classOf[UserHistory]){
  val time = System.currentTimeMillis()-days*TimeInterval.day
  override def getSql: String = s"select * from lsyindex where userid = $id and time > $time order by time desc limit $size"
}
case class QueryITUClassJob(id:Long,size:Int,days:Int,videoType:String,esIndex:String) extends ESJob[UserHistory](classOf[UserHistory]){
  val time = System.currentTimeMillis()-days*TimeInterval.day
  Logger.getLogger(getClass).info(new Date(time).toString)
  override def getSql: String = s"select * from lsyindex where userid = $id and time > $time and type=\'$videoType\' order by time desc limit $size"
}

