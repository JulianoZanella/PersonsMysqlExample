package br.com.juliano.personsMysql.app.util.exceptiom

class InvalidTypeException : Exception() {

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }


    override val message: String?
        get() = "Invalid Type of Values, Please, use only supported types."


}