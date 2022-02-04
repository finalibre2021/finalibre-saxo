package controllers

import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class FinalibreAssets @Inject() (val assets : controllers.Assets, cc : ControllerComponents, executionContext : ExecutionContext) extends AbstractController(cc) {
  private implicit val execContext = executionContext
  def jsLibrary() = assets.at("/", "client-fastopt-bundle.js")
  def jsLibraryMap() = assets.at("/", "client-fastopt-bundle.js.map")
  def bootstrapCss() = assets.at("/", "lib/bootstrap/css/bootstrap.css")
  def at(file : String) = assets.at(file)
  /*def at(file : String, contentType : Option[String]) = contentType match {
    case None => assets.at(file)
    case Some(contTyp) =>     Action.async {
      implicit request: Request[AnyContent] =>
        assets.at(file)(request)
          .map(res => res.as(contTyp))
    }

  }*/




}
