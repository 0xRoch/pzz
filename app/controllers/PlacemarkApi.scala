package controllers

import play.api.mvc._
import play.api.libs.json.{JsBoolean, JsArray, JsObject, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.api.indexes.{IndexType, Index}
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Reads._

object PlacemarkApi extends Controller with ReactiveRest with securesocial.core.SecureSocial {

  def collectionName = "placemarks"

  /*

  placemark: {
     title: "string",
     coordinates: {lng: lng, lat: lat},
     filters: ["flag1", "flag2", "flagN"],
     props: ["flag1", "flag2", "flagN"]
  }

   */

  override def collection : JSONCollection = super.collection

  //collection.drop()
  collection.indexesManager.ensure(Index(Seq("coordinates"->IndexType.Geo2D)))
  collection.indexesManager.ensure(Index(Seq("filters" -> IndexType.Descending)))
  collection.indexesManager.ensure(Index(Seq("props" -> IndexType.Descending)))
  collection.indexesManager.ensure(Index(Seq("vertical" -> IndexType.Descending)))

  def canModify(id: String) = UserAwareAction { implicit request =>
    request.user match {
      case Some(u) => Ok(Json.obj("canModify" -> JsBoolean(true)))
      case _ => Ok(Json.obj("canModify" -> JsBoolean(false)))
    }
  }
  
}