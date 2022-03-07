package com.qbrainx

import com.typesafe.config.ConfigFactory

object AppConfig {

  lazy val config=ConfigFactory.load()

  lazy val config1=config.getConfig("config")

  lazy val jdbc=config1.getConfig("jdbc")

}

