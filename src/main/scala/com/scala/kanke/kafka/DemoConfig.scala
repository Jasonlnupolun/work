package com.scala.kanke.kafka

/**
  * Created by Administrator on 2016/11/30.
  */

import com.typesafe.config.{Config, ConfigFactory}

object DemoConfig {

  val config: Config = ConfigFactory.load

  object Kafka {
    lazy val bootstrapServers = config.getString("demo.kafka.producer.bootstrap.servers")
    lazy val keySerializer = config.getString("demo.kafka.producer.key.serializer")
    lazy val valueSerializer = config.getString("demo.kafka.producer.value.serializer")
  }
}
