package com.scala.kanke.server.hot.server

import com.scala.kanke.server.hot.service.impl.DaoServerImpl

/**
  * Created by Administrator on 2016/11/8.
  */
object HotMain {

  val videosDao = new DaoServerImpl()
  val hotserver = new Server
  def main(args: Array[String]) {
     hotserver.server(videosDao)
  }
}
