package br.com.juliano.personsMysql.app.dao

import br.com.juliano.personsMysql.app.model.Person
import br.com.juliano.personsMysql.app.util.*
import br.com.juliano.personsMysql.app.util.exceptiom.InvalidTypeException
import java.lang.reflect.InvocationTargetException
import java.sql.Connection
import java.sql.SQLException
import java.util.ArrayList


class PersonDao {

    private val connection: Connection = Connector().connection()

    fun insert(person: Person) {
        try {
            connection.insert(TABLE, FIELDS,
                    listOf(person.name, person.birthDate, person.sex))
        } catch (ex: SQLException) {
            showMessage(ex.message!!)
        } catch (ex: InvalidTypeException) {
            showMessage(ex.message!!)
        }
    }

    fun update(person: Person) {
        try {
            connection.update(TABLE, FIELDS,
                    listOf(person.name, person.birthDate, person.sex), person.id)
        } catch (ex: SQLException) {
            showMessage(ex.message!!)
        } catch (ex: InvalidTypeException) {
            showMessage(ex.message!!)
        }
    }

    fun delete(person: Person) {
        try {
            connection.delete(TABLE, person.id)
        } catch (ex: SQLException) {
            showMessage(ex.message!!)
        } catch (ex: InvalidTypeException) {
            showMessage(ex.message!!)
        }
    }

    /**
     * @param id use if you want select person by id.
     */
    fun select(id: Int? = null): List<Person> {
        val list = mutableListOf<Person>()
        try {
            val resultSet = connection.select(TABLE, id)
            if (resultSet != null) {
                while (resultSet.next()) {
                    val person = Person()
                    person.id = resultSet.getInt(PK_FIELD)
                    person.name = resultSet.getString(FIELDS[0])
                    person.birthDate = resultSet.getDate(FIELDS[1]).toLocalDate()
                    person.sex = resultSet.getString(FIELDS[2]).toCharArray()[0]
                    list.add(person)
                }
            }
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
        } catch (ex: IllegalArgumentException) {
            showMessage(ex.message!!)
        }
        return list
    }


    companion object {
        private const val PK_FIELD = "id"
        private val FIELDS = listOf("name", "birthDate", "sex")
        private val TABLE = Person::class.simpleName!!.toLowerCase()
    }

}