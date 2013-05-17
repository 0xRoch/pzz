package controllers

import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.mvc._
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Reads._

trait ReactiveRest extends Controller {
  val db = ReactiveMongoPlugin.db

  // Object ID formatters
  val objectIdFormat = OFormat[String](
    (__ \ "$oid").read[String],
    OWrites[String] {
      s => Json.obj("$oid" -> s)
    }
  )

  val toObjectId = OWrites[String] {
    s => Json.obj("_id" -> Json.obj("$oid" -> s))
  }
  val fromObjectId = (__ \ '_id).json.copyFrom((__ \ '_id \ '$oid).json.pick[JsString])

  // Collection to query
  def collectionName: String

  // Item reader/validator
  def itemReads: Reads[JsObject] = (
    (__ \ 'id).json.copyFrom((__ \ '_id \ '$oid).json.pick[JsString]) and
      (__ \ '_id).json.prune
    ).reduce


  def collection: JSONCollection = db.collection[JSONCollection](collectionName)

  def toMongoUpdate: Reads[JsObject] = (__ \ '$set).json.copyFrom(__.json.pickBranch)

  // queries and renders a list
  def queryItems(q: JsObject) = collection.find(q).cursor[JsObject].toList().map(list => Ok(Json.toJson(list.map(i => i.transform(itemReads).asOpt))))

  // default finder
  def defaultQueryFinder: JsObject = Json.obj()

  // vadidator/reader for query
  def queryFinderReads: Reads[JsObject] = __.json.pickBranch

  def get(id: String) = Action {
    Async {
      collection.find(toObjectId.writes(id)).one[JsValue].map {
        case None => NotFound(Json.obj("res" -> "KO", "error" -> s"$collectionName :: $id not found"))
        case Some(p) =>
          p.transform(itemReads).map {
            jsonp =>
              Ok(jsonp)
          }.recoverTotal {
            e =>
              BadRequest(JsError.toFlatJson(e))
          }
      }
    }
  }

  def save(id: String) = Action(parse.json) {
    request =>
      Async {
        collection.update(toObjectId.writes(id), request.body.transform(toMongoUpdate).get).map(err => Ok(err.stringify))
      }
  }

  def create() = Action(parse.json) {
    request =>
      Async {
        collection.insert(request.body).map(err => Ok(err.stringify))
      }
  }

  def query(q: Option[String]) = Action {
    implicit request =>
      if (q.isEmpty) {
        Async {
          queryItems(defaultQueryFinder)
        }
      } else {
        Json.parse(q.get).transform(queryFinderReads).map({
          jsobj =>
            Async {
              queryItems(jsobj)
            }
        }).recoverTotal({
          case e =>
            BadRequest(Json.obj("Cannot parse request" -> e.toString))
        })
      }
  }

  def delete(id: String) = Action {
    Async {
      collection.remove[JsValue](toObjectId.writes(id)).map {
        lastError =>
          if (lastError.ok)
            Ok(Json.obj("msg" -> s"$collectionName :: $id deleted"))
          else
            InternalServerError(Json.obj("error" -> "error %s".format(lastError.stringify)))
      }
    }
  }
}
