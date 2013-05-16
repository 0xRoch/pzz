package controllers

import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.{JSONQueryBuilder, JSONCollection}
import play.api.mvc._
import play.api.libs.json._
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json
trait ReactiveRest extends Controller{
  val db = ReactiveMongoPlugin.db

  def collectionName : String

  def itemReads : Reads[JsObject] =
    (__ \ 'id).json.put(JsString("ok"))

  def collection: JSONCollection = db.collection[JSONCollection](collectionName)

  val objectIdFormat = OFormat[String](
    (__ \ "$oid").read[String],
    OWrites[String]{ s => Json.obj( "$oid" -> s ) }
  )

  val toObjectId = OWrites[String]{ s => Json.obj("_id" -> Json.obj("$oid" -> s)) }
  val fromObjectId = (__ \ '_id).json.copyFrom( (__ \ '_id \ '$oid).json.pick[JsString] )

  def queryItems(q: JsObject) = collection.find(q).cursor[JsObject].toList().map(list => Ok(Json.toJson(list.map(i=>i.transform(itemReads).asOpt))))

  def get(id: String) = Action {
    Async {
      collection.find(toObjectId.writes(id)).one[JsValue].map{
        case None => NotFound(Json.obj("res" -> "KO", "error" -> s"$collectionName :: $id not found"))
        case Some(p) =>
          p.transform(itemReads).map{ jsonp =>
            Ok( jsonp )
          }.recoverTotal{ e =>
            BadRequest( JsError.toFlatJson(e) )
          }
      }
    }
  }

  def save(id: String) = Action(parse.json) {request =>
    Ok(id)
  }

  def create() = Action(parse.json) {request =>
    Ok
  }

  def query_ = Action {
    Async {
      queryItems(Json.obj())
    }
  }

  def delete(id: String) = Action {
    Async {
      collection.remove[JsValue](toObjectId.writes(id)).map{ lastError =>
        if(lastError.ok)
          Ok( Json.obj("msg" -> s"$collectionName :: $id deleted") )
        else
          InternalServerError( Json.obj("error" -> "error %s".format(lastError.stringify)))
      }
    }
  }
}
