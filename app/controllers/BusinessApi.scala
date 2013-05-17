package controllers

import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.api.indexes.{IndexType, Index}
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.json._

object BusinessApi extends Controller with ReactiveRest with securesocial.core.SecureSocial {

  def collectionName = "businesses"

  override def collection : JSONCollection = super.collection

}