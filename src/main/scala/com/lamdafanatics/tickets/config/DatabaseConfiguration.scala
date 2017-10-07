package com.lamdafanatics.tickets.config

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

class DatabaseConfiguration(jdbcUrl: String, dbUser: String, dbPassword: String) {
  private val hikariConfig = new HikariConfig()
  hikariConfig.setJdbcUrl(jdbcUrl)
  hikariConfig.setUsername(dbUser)
  hikariConfig.setPassword(dbPassword)

  private val dataSource = new HikariDataSource(hikariConfig)

  val profile = slick.jdbc.PostgresProfile
  import profile.api._
  val db = Database.forDataSource(dataSource, None)
  db.createSession()
}