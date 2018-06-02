package br.com.juliano.personsMysql.app.util

import br.com.juliano.personsMysql.app.util.exceptiom.InvalidTypeException
import java.lang.reflect.InvocationTargetException
import java.sql.Connection
import java.sql.Date
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDate
import java.util.ArrayList

/**
 * @author Juliano Zanella
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
            when (value) {
                is Int -> stmt.setInt(i + 1, value)
                is String -> stmt.setString(i + 1, value)
                is Date -> stmt.setDate(i + 1, value)
                is LocalDate -> stmt.setDate(i + 1, value.toSQLDate())
                is Char -> stmt.setString(i + 1, "$value")
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

/**
 * in construction. see-> PersonDao.select
 */
fun Connection.select(c: Class<*>, id: Int = 0): List<Any> {
    val list = ArrayList<Any>()
    try {
        val table = c.simpleName
        var sql = "SELECT * FROM $table"
        if (id > 0) sql += " WHERE ${getPK(table)} = $id"
        try {
            this.prepareStatement(sql).use({ stmt ->
                stmt.executeQuery().use({ resultSet ->
                    while (resultSet.next()) {
                        val obj = fillObject(c, resultSet)
                        list.add(obj)
                    }
                })
            })
        } catch (ex: SQLException) {
            showMessage(ex.message!!)
        } catch (ex: InstantiationException) {
            showMessage(ex.message!!)
        } catch (ex: IllegalAccessException) {
            showMessage(ex.message!!)
        } catch (ex: NoSuchMethodException) {
            showMessage(ex.message!!)
        } catch (ex: SecurityException) {
            showMessage(ex.message!!)
        } catch (ex: IllegalArgumentException) {
            showMessage(ex.message!!)
        } catch (ex: InvocationTargetException) {
            showMessage(ex.message!!)
        }
    } catch (ex: IllegalArgumentException) {
        showMessage(ex.message!!)
    }
    return list
}

private fun fillObject(c: Class<*>, resultSet: ResultSet): Any {
    val obj = c.newInstance()
    for (m in c.methods) {
        if (m.name.substring(0, 3) == "set") {
            val args1 = arrayOfNulls<Class<*>>(1)
            val arrayOfClass = m.parameterTypes
            val s = m.name.substring(3, m.name.length)
            if (arrayOfClass[0].name == "java.lang.String") {
                args1[0] = String::class.java
                obj.javaClass.getMethod(m.name,
                        args1[0]).invoke(obj, resultSet.getString(s))
            }
            if (arrayOfClass[0].name.contains("int", true)) {
                args1[0] = Int::class.javaPrimitiveType
                obj.javaClass.getMethod(m.name,
                        args1[0]).invoke(obj, resultSet.getInt(s))
            }
            if (arrayOfClass[0].name == "double") {
                args1[0] = Double::class.javaPrimitiveType
                obj.javaClass.getMethod(m.name,
                        args1[0]).invoke(obj, resultSet.getDouble(s))
            }
            if (arrayOfClass[0].name == "boolean") {
                args1[0] = Boolean::class.javaPrimitiveType
                obj.javaClass.getMethod(m.name,
                        args1[0]).invoke(obj, resultSet.getBoolean(s))
            }
            if (arrayOfClass[0].name.equals("char", true)) {
                args1[0] = Char::class.javaPrimitiveType
                obj.javaClass.getMethod(m.name,
                        args1[0]).invoke(obj, resultSet.getString(s)[0])
            }
            if (arrayOfClass[0].name.contains("LocalDate", true)) {
                args1[0] = LocalDate::class.javaObjectType
                val date: LocalDate = resultSet.getDate(s).toLocalDate()
                obj.javaClass.getMethod(m.name,
                        args1[0]).invoke(obj, date)
            }
            if (arrayOfClass[0].name.equals("Date", true)) {
                args1[0] = java.util.Date::class.javaObjectType
                obj.javaClass.getMethod(m.name,
                        args1[0]).invoke(obj, resultSet.getDate(s))
            }
        }
    }
    return obj
}


