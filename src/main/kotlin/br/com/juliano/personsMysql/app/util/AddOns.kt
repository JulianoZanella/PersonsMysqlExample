package br.com.juliano.personsMysql.app.util

import br.com.julianozanella.util.DateUtil
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
    return DateUtil.getSQLDate(this)
}