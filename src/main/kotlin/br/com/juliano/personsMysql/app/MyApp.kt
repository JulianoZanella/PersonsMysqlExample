package br.com.juliano.personsMysql.app

import br.com.juliano.personsMysql.view.MainView
import br.com.julianozanella.util.Database
import tornadofx.App

class MyApp: App(MainView::class, Styles::class){

    init {
        Database.createConnection(URL, USER, PASS)
    }

    companion object {
        private const val URL = "jdbc:mysql://localhost:3306/personsDB"
        private const val USER = "root"
        private const val PASS = ""
    }
}