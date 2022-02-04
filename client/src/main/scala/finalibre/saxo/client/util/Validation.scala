package finalibre.saxo.client.util

import finalibre.saxo.client.util.CommonUtil.$i
import org.querki.jquery.$

object Validation {

  def validate(nonEmptyTextInputs : Seq[String] = Nil) : Boolean = {
    var ret = true
    nonEmptyTextInputs.foreach {
      case inp => {
        println(s"Validating: $inp")
        ret = ret & validate(inp, nonEmptyText)
      }
    }
    ret
  }

  def validateAndPerform[A](nonEmptyTextInputs : Seq[String] = Nil, afterwards : () => A) : Option[A] = {
    if(validate(nonEmptyTextInputs))
      Some(afterwards())
    else None
  }

  private def fieldName(id : String) = {
    var nam = id
    $(s"label[for='$id']").foreach(el => nam = $(el).text())
    nam
  }

  private def nonEmptyText(id : String) = {
    var nam = fieldName(id)
    val value = $i(id).value()
    if(value != null && value.toString.trim.size > 0) None
    else Some(s"Field $nam may not be empty")
  }

  private def validate(inputId : String, func : String => Option[String]) : Boolean = {
    var ret = true
    $i(inputId).siblings("[usage='validation']").remove()
    List("is-valid", "is-invalid").foreach(cls => $i(inputId).removeClass(cls))
    func(inputId) match {
      case None => {
        $i(inputId).parent().append(
          $("<div class='valid-feedback' usage='validation'>").text("OK")
        )
        $i(inputId).addClass("is-valid")
      }
      case Some(err) => {
        $i(inputId).parent().append(
          $("<div class='invalid-feedback' usage='validation'>").text(err)
        )
        $i(inputId).addClass("is-invalid")
        ret = false
      }
    }
    ret
  }

}
