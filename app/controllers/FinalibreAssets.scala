package controllers

import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject._

@Singleton
class FinalibreAssets @Inject() (val assets : controllers.Assets, cc : ControllerComponents) extends AbstractController(cc) {

  def jsLibrary() = assets.at("/", "client-fastopt-bundle.js")
  def jsLibraryMap() = assets.at("/", "client-fastopt-bundle.js.map")
  def at(file : String) = assets.at(file)


}
