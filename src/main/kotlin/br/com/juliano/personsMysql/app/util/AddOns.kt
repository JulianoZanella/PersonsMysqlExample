package br.com.juliano.personsMysql.app.util

import java.awt.Component
import java.sql.Connection
import java.sql.Date
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import javax.swing.JOptionPane


fun showMessage(message: String, title: String = "", icon: Int = JOptionPane.INFORMATION_MESSAGE, component: Component? = null) {
    JOptionPane.showMessageDialog(component, message, title, icon)
}




fun LocalDate.toSQLDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, this.year)
    calendar.set(Calendar.MONTH, this.monthValue - 1)
    calendar.set(Calendar.DAY_OF_MONTH, this.dayOfMonth)
    return Date(calendar.timeInMillis)
}