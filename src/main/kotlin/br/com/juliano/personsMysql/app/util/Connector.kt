package br.com.juliano.personsMysql.app.util

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


class Connector {


    fun connection(): Connection {
        try {
            Class.forName(DRIVER)
            return DriverManager.getConnection(URL, USER, PASS)
        } catch (ex: ClassNotFoundException) {
            throw RuntimeException("Connection Error!", ex)
        } catch (ex: SQLException) {
            throw RuntimeException("Connection Error!", ex)
        }
    }



    companion object {
        private const val DRIVER = "com.mysql.jdbc.Driver"
        private const val URL = "jdbc:mysql://localhost:3306/personsDB"
        private const val USER = "root"
        private const val PASS = ""
    }
}