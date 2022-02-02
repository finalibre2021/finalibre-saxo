package finalibre.saxo.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object DateExtensions {
  val DanishDateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
  val DanishDateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

  implicit class ExtendedLocalDateTime(val date : LocalDateTime)  {
    def toDanishDateString = date.format(DanishDateFormat)
    def toDanishDateTimeString = date.format(DanishDateTimeFormat)
    def minutesAndSecondsBetween(other : LocalDateTime) : (Long, Long) = {
      val (min, max) = if(other.isBefore(date)) (other, date) else (date, other)
      var tempDate = LocalDateTime.from(min)
      val minutes = tempDate.until(max, ChronoUnit.MINUTES)
      tempDate = tempDate.plusMinutes(minutes)
      val seconds = tempDate.until(max, ChronoUnit.SECONDS)
      (minutes, seconds)
    }
  }


}
