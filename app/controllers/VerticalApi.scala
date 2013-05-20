package controllers

import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.api.indexes.{IndexType, Index}
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.json._
import model.{Flag, FlagsBlock, Flags, Vertical}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Reads._

object VerticalApi extends Controller with ReactiveRest with securesocial.core.SecureSocial {

  def collectionName = "verticals"

  override def collection : JSONCollection = super.collection

  implicit val formatF = Json.format[Flag]
  implicit val formatFB = Json.format[FlagsBlock]
  implicit val formatFS = Json.format[Flags]
  implicit val formatV = Json.format[Vertical]

  implicit val readCode = ((__ \ 'code).json.pickBranch and (__ \ 'title).json.pickBranch).reduce

  collection.drop()

  collection.indexesManager.ensure(Index(Seq("code" -> IndexType.Ascending), unique = true, dropDups = true))

  collection.insert(
  Vertical("Еда", "food",
    Flags(Seq(
      FlagsBlock(Seq(
        Flag("Кафе", "cafe"),
        Flag("Бары", "bar"),
        Flag("Рестораны", "restaurant"),
        Flag("Кофейни", "coffeeshop"),
        Flag("Кондитерские", "candy"),
        Flag("Столовые", "massfood")
      )),
      FlagsBlock(Seq(
        Flag("Русская и Европейская", "ruseuro"),
        Flag("Итальянская", "italic"),
        Flag("Восточная", "east"),
        Flag("Рыбные рестораны", "fish")
      ), Option("Кухня")),
      FlagsBlock(Seq(
        Flag("Средний чек", "middlecheque")
      ))
    )),

    Flags(Seq(
      FlagsBlock(Seq(
        Flag("Завтраки", "breakfast"),
        Flag("Бизнес-ланчи", "businesslanch"),
        Flag("Романтический ужин", "romantic")
      )),
      FlagsBlock(Seq(
        Flag("Вегетарианское меню", "vegan"),
        Flag("Сетевые заведения", "net")
      )),
      FlagsBlock(Seq(
        Flag("Wi-fi", "wifi")
      )),
      FlagsBlock(Seq(
        Flag("Живая музыка", "lifemusic"),
        Flag("Караоке", "karaoke"),
        Flag("Кальян", "kalyan"),
        Flag("Детская комната", "kidsroom")
      ))
    ))
  ))

  def getByCode(code: String) = Action {
    Async {
      collection.find(Json.obj("code" -> code)).one[Vertical].map{
        case None => NotFound(Json.obj("res" -> "KO", "error" -> s"$collectionName :: $code not found"))
        case Some(p) => Ok(formatV.writes(p))
      }
    }
  }

  def listCodes = Action {
    Async {
      collection.find(Json.obj()).cursor[JsObject].toList().map{
        list => Ok(Json.toJson(list.map(i => i.transform(readCode).asOpt)))
      }
    }
  }
}