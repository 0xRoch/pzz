package model

/**
 * @author alari
 * @since 5/17/13 2:06 PM
 */
case class Placemark(id: String, title: String, vertical: String, coordinates: Coordinates, filters: Seq[String], props: Seq[String]) {

}


case class Coordinates(lng: Double, lat: Double)