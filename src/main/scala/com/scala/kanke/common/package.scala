package com.scala.kanke

import com.java.kanke.utils.PropertyUtil

/**
  * Created by Administrator on 2016/11/16.
  */
package object common {
     val zookeeper = PropertyUtil.get("zookeeper.connect")
     val topic = PropertyUtil.get("kafka.topic")
}
