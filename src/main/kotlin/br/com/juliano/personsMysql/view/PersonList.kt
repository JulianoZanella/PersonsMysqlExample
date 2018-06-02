package br.com.juliano.personsMysql.view

import br.com.juliano.personsMysql.app.controller.PersonController
import br.com.juliano.personsMysql.app.model.Person
import tornadofx.*

class PersonList : View() {

    private val controller: PersonController by inject()

    override val root = tableview(controller.persons){
        column(messages["id"], Person::idProperty)
        column(messages["name"], Person::nameProperty)
        bindSelected(controller.selectedPerson)
        smartResize()
    }
}
