package br.com.juliano.personsMysql.view

import tornadofx.*
import tornadofx.FX.Companion.messages

class ErrorView : View(messages["error"]) {
    override val root = vbox {
        setPrefSize(300.0, 100.0)
        label(messages["selectPerson"])
        button(messages["ok"]){
            action { close() }
        }
    }
}
