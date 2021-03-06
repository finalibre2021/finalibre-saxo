package finalibre.saxo.client.util

import java.text.DecimalFormat
import java.time._
import java.time.format._
import java.util.Locale
import scala.scalajs.js
import scala.scalajs.js.Date
import scala.util.Try

object JsExtensions {

  implicit class DoubleExtensions(d : Double) {
    import DoubleExtensions._
    def isZero = {
      val rem = d % 1.0
      rem < 0.01 && rem > -0.01
    }

    def toPrettyString = {
       JsExtensions.DoubleExtensions.formatPretty.format(d)
    }

    def toInputString = {
      if(isZero) {
        inputFormatInt.format(d) + ".00"
      }
      else inputFormat.format(d)
    }
  }

  implicit class DateExtensions(d : Date) {
    import DateExtensions._
    def toDateString ={
      val localDateTime = Instant.ofEpochMilli(d.getTime.toLong)
      dateFormatter.format(LocalDateTime.ofInstant(localDateTime, ZoneOffset.ofHours(1)))
    }
    def toInputDateString = {
      val localDateTime = Instant.ofEpochMilli(d.getTime.toLong)
      inputDateFormatter.format(LocalDateTime.ofInstant(localDateTime, ZoneOffset.ofHours(1)))
    }

  }

  implicit class DateTimeExtensions(d : LocalDateTime) {
    import DateTimeExtensions._

    def toDateTimeString = d.format(dateTimeFormatter)
    def toInputDateString = d.format(inputDateTimeFormatter)
  }


  implicit class LongExtensions(l : Long) {
    def toDate = new Date(l)
    def toDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneOffset.ofHours(1))
  }

  implicit class StringExtensions(str : String) {
    def toDateFromInput = (Try {
      val parsed = LocalDate.parse(str, DateExtensions.inputDateFormatter)
      new Date(parsed.getYear, parsed.getMonth.getValue, parsed.getDayOfMonth)
    }).toOption

    def toDateTimeFromInput = (Try{
      val parsed = LocalDateTime.parse(str, DateTimeExtensions.inputDateTimeFormatter)
      parsed
    }).toOption

    def toDoubleFromInput = (Try{
      str.toDouble
    }).toOption

  }

  implicit class ArrayExtensions[A](arr : scala.Array[A]) {
    def toJsArray : js.Array[A] = {
      val ret = new js.Array[A]()
      arr.foreach(en => ret.push(en))
      ret
    }
  }

  implicit class SeqExtensions[A](seq : scala.Seq[A]) {
    def toJsArray : js.Array[A] = {
      js.Array(seq : _*)
    }
  }

  object DoubleExtensions {
    val locale = Locale.forLanguageTag("da-DK")
    val formatPretty = new java.text.DecimalFormat()
    formatPretty.applyPattern("###,##0.00")
    formatPretty.getDecimalFormatSymbols.setDecimalSeparator(',')
    formatPretty.getDecimalFormatSymbols.setGroupingSeparator('.')

    val formatPrettyInt = new DecimalFormat()
    formatPrettyInt.applyPattern("0")

    val inputFormat = new DecimalFormat("####0.00")
    inputFormat.getDecimalFormatSymbols.setDecimalSeparator('.')
    val inputFormatInt = new DecimalFormat("####0")
    inputFormatInt.getDecimalFormatSymbols.setDecimalSeparator('.')

  }



  object DateExtensions {
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val inputDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  }

  object DateTimeExtensions {
    val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    val inputDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
  }







}
