package br.com.juliano.personsMysql.app.util

import br.com.juliano.personsMysql.app.util.exceptiom.InvalidTypeException
import java.lang.reflect.InvocationTargetException
import java.sql.Connection
import java.sql.Date
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDate
import java.util.ArrayList
import kotlin.reflect.KClass


/**
 * Add functions to Connection Class.
 *
 * @author Juliano Zanella
 */

/**
 *
 *
 * @param tableName The name of Table
 * @param
 */
@Throws(SQLException::class, InvalidTypeException::class)
fun Connection.insert(tableName: String, fields: List<String>, values: List<Any>) {
    var sql = "INSERT INTO $tableName ("
    var valuesString = " VALUES ("
    for (field in fields) {
        sql += "$field, "
        valuesString += "?, "
    }
    sql = sql.removeSuffix(", ")
    valuesString = valuesString.removeSuffix(", ")
    sql += ") $valuesString)"
    this.prepareStatement(sql).use({ stmt ->
        for ((i, value) in values.withIndex()) {
            val index = i + 1
            when (value) {
                is Int -> stmt.setInt(index, value)
                is String -> stmt.setString(index, value)
                is Date -> stmt.setDate(index, value)
                is LocalDate -> stmt.setDate(index, value.toSQLDate())
                is Char -> stmt.setString(index, "$value")
                else -> throw InvalidTypeException()
            }
        }
        stmt.execute()
    })
}

@Throws(SQLException::class, InvalidTypeException::class)
fun Connection.update(tableName: String, fields: List<String>, values: List<Any>, codeId: Int, whereClause: String? = null) {
    val pk = getPK(tableName)
    var sql = "UPDATE $tableName SET "
    var where = " WHERE "
    for (field in fields) {
        if (field.equals(pk, true)) {
            continue
        }//Retirar
        sql += "$field = ? ,"
    }
    sql = sql.removeSuffix(",")
    where += if (whereClause.isNullOrBlank()) {
        "$pk = ?"
    } else {
        whereClause
    }
    sql += where
    this.prepareStatement(sql).use({ stmt ->
        for ((i, value) in values.withIndex()) {
            when (value) {
                is Int -> stmt.setInt(i + 1, value)
                is String -> stmt.setString(i + 1, value)
                is Date -> stmt.setDate(i + 1, value)
                is LocalDate -> stmt.setDate(i + 1, value.toSQLDate())
                is Char -> stmt.setString(i + 1, "$value")
                else -> throw InvalidTypeException()
            }
        }
        if (whereClause.isNullOrBlank()) {
            stmt.setInt(values.size + 1, codeId)
        }
        stmt.execute()
    })
}

@Throws(SQLException::class, InvalidTypeException::class)
fun Connection.delete(tableName: String, codeId: Int?, whereClause: String? = "") {
    if (codeId == null && whereClause.isNullOrBlank()) {
        throw InvalidTypeException()
    }
    val pk = getPK(tableName)
    var sql = "DELETE FROM $tableName"
    var where = " WHERE "
    where += if (whereClause.isNullOrBlank()) {
        "$pk = ?"
    } else {
        whereClause
    }
    sql += where
    this.prepareStatement(sql).use({ stmt ->
        if (whereClause.isNullOrBlank() && codeId != null) stmt.setInt(1, codeId)
        stmt.execute()
    })
}

@Throws(SQLException::class)
fun Connection.getPK(table: String): String {
    var pK = ""
    val databaseName = this.catalog
    val sql = ("SELECT information_schema.KEY_COLUMN_USAGE.COLUMN_NAME as \"keyName\" \n"
            + "FROM information_schema.KEY_COLUMN_USAGE \n"
            + "WHERE information_schema.KEY_COLUMN_USAGE.CONSTRAINT_NAME LIKE \"PRIMARY\" \n"
            + "AND information_schema.KEY_COLUMN_USAGE.TABLE_SCHEMA LIKE \"" + databaseName + "\""
            + " AND information_schema.KEY_COLUMN_USAGE.TABLE_NAME LIKE \"" + table + "\"")

    val stmt = this.prepareCall(sql)
    val resultSet = stmt.executeQuery()
    while (resultSet.next()) {
        pK = resultSet.getString("keyName")
    }
    return pK
}

@Throws(SQLException::class, InstantiationException::class, IllegalAccessException::class, NoSuchMethodException::class,
        SecurityException::class, IllegalArgumentException::class, InvocationTargetException::class, IllegalArgumentException::class)
fun Connection.select(tableName: String, id: Int? = 0): ResultSet? {
    var resultSetRet: ResultSet? = null
    var sql = "SELECT * FROM $tableName"
    if (id != null && id > 0) sql += " WHERE ${getPK(tableName)} = $id"
    this.prepareStatement(sql).use({ stmt ->
        stmt.executeQuery().use({ resultSet ->
            resultSetRet = resultSet
        })
    })
    return resultSetRet
}



