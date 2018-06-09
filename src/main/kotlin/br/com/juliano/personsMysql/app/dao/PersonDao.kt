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

   fun select(id: Int = 0): List<Person> {
        val list = mutableListOf<Person>()
        val listObject = connection.select(Person::class.java, id)
        for (o in listObject) {
            list.add(o as Person)
        }
        return list
    }

    companion object {
        private val FIELDS = listOf("name", "birthDate", "sex")
        private val TABLE = Person::class.simpleName!!.toLowerCase()
    }

}