package finalibre.saxo.client.util

import org.querki.jquery.$
import org.scalajs.dom

import java.util.Base64

object CommonUtil {

  def updateNode(node : dom.Node, text : String) = {
    node.innerText = text
  }

  def toImageDataString(bytes : Array[Byte], fileType : String) = "data:image/" + fileType + ";base64," + Base64.getEncoder.encodeToString(bytes)
  def base64encode(bytes : Array[Byte]) = Base64.getEncoder.encodeToString(bytes)

  def $i(idStr : String) = $(s"#$idStr")

}
