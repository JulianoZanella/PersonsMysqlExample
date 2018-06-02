package br.com.juliano.personsMysql.view

import br.com.juliano.personsMysql.app.Styles
import tornadofx.*
import tornadofx.FX.Companion.messages

class MainView : View(messages["title"]) {
    override val root = hbox {
        addClass(Styles.heading)
        add<PersonEditor>()
        add<PersonList>()
    }
}