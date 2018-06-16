package br.com.juliano.personsMysql.view

import br.com.juliano.personsMysql.app.controller.PersonController
import javafx.scene.control.ToggleGroup
import tornadofx.*

class PersonEditor : View() {

    private val controller: PersonController by inject()
    private val toggleGroup = ToggleGroup()

    override val root = form {
        fieldset(messages["person"]) {
            field(messages["name"]) {
                textfield(controller.selectedPerson.name)
            }
            field(messages["birth"]) {
                datepicker(controller.selectedPerson.birthDate) {}
            }
            toggleGroup.bind(controller.selectedPerson.sex)
            field(messages["sex"]) {
                vbox {
                    radiobutton(messages["masc"], toggleGroup, value = M) {
                        isSelected = true
                    }
                    radiobutton(messages["fem"], toggleGroup, value = F)
                    radiobutton(messages["other"], toggleGroup, value = O)
                }
            }
            buttonbar {
                button(messages["btnSave"]) {
                    enableWhen(controller.selectedPerson.dirty)
                    isDefaultButton = true
                    action {
                        controller.save()
                    }
                }
                button(messages["btnUpdate"]) {
                    enableWhen(controller.selectedPerson.dirty)
                    action {
                        controller.update()
                    }
                }
                button(messages["btnDelete"]) {
                    action {if (controller.selectedPerson.id.value <= 0){
                        openInternalWindow(ErrorView::class)
                    }
                        controller.delete()
                    }
                }
                button(messages["btnClear"]) {
                    enableWhen(!controller.selectedPerson.dirty)
                    action {
                        controller.clearPerson()
                    }
                }
            }

        }
    }


    companion object {
        private const val F = 'F'
        private const val M = 'M'
        private const val O = 'O'
    }
}
