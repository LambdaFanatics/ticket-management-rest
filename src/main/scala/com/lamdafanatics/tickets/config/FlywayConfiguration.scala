package com.lamdafanatics.tickets.config

import org.flywaydb.core.Flyway

class FlywayConfiguration(jdbcUrl: String, dbUser: String, dbPassword: String) {

  private[this] val flyway = new Flyway()
  flyway.setDataSource(jdbcUrl, dbUser, dbPassword)

  def migrateDatabaseSchema(): Unit = flyway.migrate()

  def dropDatabase(): Unit = flyway.clean()
}