import java.io.File
import scala.io.Source

object Util {

  val tempKey = Source.fromFile(new File("conf/temp-token.txt"), "UTF-8").getLines().mkString("")

}
