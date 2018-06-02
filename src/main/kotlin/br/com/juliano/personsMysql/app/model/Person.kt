package br.com.juliano.personsMysql.app.model

import tornadofx.ItemViewModel
import tornadofx.getProperty
import tornadofx.property
import java.time.LocalDate

class Person(id: Int = 0, name: String = "", birthDate: LocalDate? = null, sex: Char = 'M') {

    var id by property(id)
    fun idProperty() = getProperty(Person::id)

    var name by property(name)
    fun nameProperty() = getProperty(Person::name)

    var birthDate by property(birthDate)
    fun birthDateProperty() = getProperty(Person::birthDate)


    var sex by property(sex)
    fun sexProperty() = getProperty(Person::sex)

}

class PersonModel : ItemViewModel<Person>() {

    val id = bind { item?.idProperty() }
    val name = bind { item?.nameProperty() }
    val birthDate = bind { item?.birthDateProperty() }
    val sex = bind { item?.sexProperty() }
}