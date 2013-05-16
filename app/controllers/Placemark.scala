package controllers

import play.api.mvc._
import play.api.libs.json.{JsArray, JsObject, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.api.indexes.{IndexType, Index}
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.bson.BSONString

object Placemark extends Controller with ReactiveRest {

  def collectionName = "placemarks"

  override def collection : JSONCollection = super.collection

  collection.drop()
  collection.indexesManager.ensure(Index(Seq("point"->IndexType.Geo2D)))
  collection.insert(Json.obj("title" -> "Placemark g1", "point" -> Json.arr(30.305, 59.953)))
  collection.insert(Json.obj("title" -> "Placemark g2", "point" -> Json.arr(30.405, 59.943)))
  collection.insert(Json.obj("title" -> "Placemark g3", "point" -> Json.arr(30.305, 59.933)))
  collection.insert(Json.obj("title" -> "Placemark g4", "point" -> Json.arr(30.205, 59.943)))

  def query(south: Option[Double], east: Option[Double], north: Option[Double], west: Option[Double]) = Action {
    Async {
      val q = Json.obj(
        "point" -> Json.obj(
          "$within" -> Json.obj( "$box" -> Json.arr(
            Json.arr(west.get, south.get), Json.arr(east.get, north.get)
          ))
        )
      )
      println(q)
      super.queryItems(q)
    }
  }
  
}