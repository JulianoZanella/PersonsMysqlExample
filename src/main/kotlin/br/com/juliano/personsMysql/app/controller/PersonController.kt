package br.com.juliano.personsMysql.app.controller

import br.com.juliano.personsMysql.app.dao.PersonDao
import br.com.juliano.personsMysql.app.model.Person
import br.com.juliano.personsMysql.app.model.PersonModel
import javafx.collections.FXCollections
import tornadofx.*
import java.time.LocalDate

class PersonController : Controller() {

    val persons = FXCollections.observableArrayList<Person>()
    var selectedPerson = PersonModel()
    val dao = PersonDao()



    init {
        fillPersons()
    }

    private fun fillPersons() {
        persons.clear()
        val list = dao.select()
        persons.addAll(list)
        clearPerson()
    }

    fun clearPerson() {
        selectedPerson.id.value = 0
        selectedPerson.name.value = ""
        selectedPerson.birthDate.value = LocalDate.now()
        selectedPerson.sex.value = 'M'
        selectedPerson.commit()
    }

    fun save() {
        dao.insert(makePerson())
        fillPersons()
    }

    fun update() {
        selectedPerson.commit()
        dao.update(makePerson())
        fillPersons()
    }

    fun delete() {
        dao.delete(makePerson())
        fillPersons()
    }

    private fun makePerson(): Person {
        val id: Int = selectedPerson.id.value ?: 0
        val name: String = selectedPerson.name.value ?: ""
        val date: LocalDate = selectedPerson.birthDate.value ?: LocalDate.now()
        val sex: Char = selectedPerson.sex.value ?: 'M'
        return Person(id, name, date, sex)
    }

}