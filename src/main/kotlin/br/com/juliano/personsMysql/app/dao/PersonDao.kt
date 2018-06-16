package br.com.juliano.personsMysql.app.dao

import br.com.juliano.personsMysql.app.model.Person
import br.com.juliano.personsMysql.app.util.*
import br.com.juliano.personsMysql.app.util.exceptiom.InvalidTypeException
import br.com.julianozanella.util.Database
import java.lang.reflect.InvocationTargetException
import java.sql.ResultSet
import java.sql.SQLException


class PersonDao {

    init {
        Database.createConnection(URL, USER, PASS)
    }

    fun insert(person: Person) {
        try {
            Database.insert(TABLE,
                    hashMapOf(Pair(FIELDS[0], person.name), Pair(FIELDS[1], person.birthDate), Pair(FIELDS[2], person.sex)))
        } catch (ex: SQLException) {
            showMessage(ex.message!!)
        } catch (ex: InvalidTypeException) {
            showMessage(ex.message!!)
        }
    }

    fun update(person: Person) {
        try {
            Database.update(TABLE,
                    hashMapOf(Pair(FIELDS[0], person.name),
                            Pair(FIELDS[1], person.birthDate), Pair(FIELDS[2], person.sex)),
                    person.id)
        } catch (ex: SQLException) {
            showMessage(ex.message!!)
        } catch (ex: InvalidTypeException) {
            showMessage(ex.message!!)
        }
    }

    fun delete(person: Person) {
        try {
            Database.delete(TABLE, person.id)
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
            val resultSet: ResultSet = if (id == null) {
                Database.select(TABLE)
            } else {
                Database.select(TABLE, id)
            }
            while (resultSet.next()) {
                val person = Person()
                person.id = resultSet.getInt(PK_FIELD)
                person.name = resultSet.getString(FIELDS[0])
                person.birthDate = resultSet.getDate(FIELDS[1]).toLocalDate()
                person.sex = resultSet.getString(FIELDS[2]).toCharArray()[0]
                list.add(person)
            }
            resultSet.close()
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
        } finally {

        }
        return list
    }


    companion object {
        private const val PK_FIELD = "id"
        private val FIELDS = listOf("name", "birthDate", "sex")
        private val TABLE = Person::class.simpleName!!.toLowerCase()
        private const val URL = "jdbc:mysql://localhost:3306/personsDB"
        private const val USER = "root"
        private const val PASS = ""
    }

}