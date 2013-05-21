package controllers

import play.api.mvc._


import model.{Flag, FlagsBlock, Flags, Vertical}
import play.api.libs.json._

object VerticalApi extends Controller {

  implicit val formatF = Json.format[Flag]
  implicit val formatFB = Json.format[FlagsBlock]
  implicit val formatFS = Json.format[Flags]
  implicit val formatV = Json.format[Vertical]


  val verticals = Map(
    "food" ->  Vertical("Еда", "food",
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
    ),
    "sport" -> Vertical("Спорт", "sport",
      Flags(Seq(
        FlagsBlock(Seq(
          Flag("Заняться фитнесом", "fitness"),
          Flag("Катки", "ski"),
          Flag("Бассейны", "swimming"),
          Flag("Детям нужен Спорт!", "kidsport"),
          Flag("Частные тренера", "coach"),
          Flag("Секции", "sections"),
          Flag("Аренда", "rent"),
          Flag("Танцы", "dance")
        )),
        FlagsBlock(Seq(
          Flag("Спортивные трансляции", "sporttranslations"),
          Flag("Спортивные магазины", "sportshops")
        ))
      )),

      Flags(Seq(
        FlagsBlock(Seq(
          Flag("Для новичков", "newbie")
        )),
        FlagsBlock(Seq(
          Flag("Футбол", "football"),
          Flag("Баскетбол", "basketball"),
          Flag("Теннис", "tennis"),
          Flag("Волейбол", "voleyball")
        ), Option("Вид спорта"))
      ))
    ),
    "shopping" -> Vertical("Шопинг", "shopping",
      Flags(Seq(
        FlagsBlock(Seq(
          Flag("Обувь", "shoes"),
          Flag("Одежда", "clothes"),
          Flag("Подарки", "gift"),
          Flag("Аксессуары", "accessories"),
          Flag("Ювелирные", "jewelry"),
          Flag("Для красоты", "beauty"),
          Flag("Для души", "soul"),
          Flag("Бутики", "boutique")
        )),
        FlagsBlock(Seq(
          Flag("Электроника", "electronics")
        )),
        FlagsBlock(Seq(
          Flag("Торговые центры", "shopmalls"),
          Flag("Дисконтные магазины", "discount"),
          Flag("Секонд-хэнды", "secondhand")
        ))
      )),

      Flags(Seq(
        FlagsBlock(Seq(
          Flag("Скидки", "lowerprice"),
          Flag("Акции", "actions"),
          Flag("Распродажи", "sale"),
          Flag("Сетевые магазины", "netshopping")
        ))
      ))
    ),
    "tourism" -> Vertical("Туризм", "tourism",
      Flags(Seq(
        FlagsBlock(Seq(
          Flag("Музеи", "museum"),
          Flag("Мосты", "bridge"),
          Flag("Соборы", "cathedral")
        ), more=Option(Seq(
          Flag("Ансамбли", "ansamble"),
          Flag("Дворцы и парки", "palacesParks"),
          Flag("Памятники", "statue")
        ))),
        FlagsBlock(Seq(
          Flag("Отели", "hotel"),
          Flag("Хостелы", "hostel"),
          Flag("Схема метро", "metro")
        ), more=Option(Seq(
          Flag("Квартиры посуточно", "flatsDaily")
        ))),
        FlagsBlock(Seq(
          Flag("Аэропорты", "airport"),
          Flag("Вокзалы", "railway"),
          Flag("Турфирмы", "tourFirm")
        ), more=Option(Seq(
          Flag("Аренда автомобиля", "carRent"),
          Flag("Такси", "taxi")
        )))
      )),

      Flags(Seq(
        FlagsBlock(Seq(), more=Option(Seq(
          Flag("При Петре I", "peter1"),
          Flag("При Екатерине II", "catherine2"),
          Flag("Петроград-Ленинград", "petrogradLeningrad"),
          Flag("Наследие СССР", "ussrHeritage"),
          Flag("Новое время", "newTime")

        )), moreTitle = Option("По эпохам")),
        FlagsBlock(Seq(
          Flag("Фонд ЮНЭСКО", "UNESCO"),
          Flag("Экскурсии", "excursions")
        ))
      ))
    ),
    "events" -> Vertical("События", "events",
      Flags(Seq(
        FlagsBlock(Seq(
          Flag("Выставки", "exhibition"),
          Flag("Вечеринки", "party"),
          Flag("Кинопремьеры", "cinemaPremiere"),
          Flag("Концерты", "concert"),
          Flag("Спектакли", "performance")
        )),
        FlagsBlock(Seq(
          Flag("Лекции", "lection"),
          Flag("Мастер-классы", "masterClass"),
          Flag("Показы", "show"),
          Flag("Презентации", "presentation")
        )),
        FlagsBlock(Seq(
          Flag("Фестивали", "festival"),
          Flag("Церемонии", "ceremony"),
          Flag("Экскурсии", "eventExcursion"),
          Flag("Форумы", "forum")
        ))
      )),

      Flags(Seq(
        FlagsBlock(Seq(), more=Option(Seq(
          Flag("При Петре I", "peter1"),
          Flag("При Екатерине II", "catherine2"),
          Flag("Петроград-Ленинград", "petrogradLeningrad"),
          Flag("Наследие СССР", "ussrHeritage"),
          Flag("Новое время", "newTime")

        )), moreTitle = Option("По эпохам")),
        FlagsBlock(Seq(
          Flag("Фонд ЮНЭСКО", "UNESCO"),
          Flag("Экскурсии", "excursions")
        ))
      ))
    ),
    "rest" ->  Vertical("Отдых", "rest",
      Flags(Seq(
        FlagsBlock(Seq(
          Flag("Кино", "cinema"),
          Flag("Концерты", "conserts"),
          Flag("Театры", "theatre"),
          Flag("Галереи", "gallery"),
          Flag("Развлекательные центры", "entertainmentCenter")
        ), title=Option("Для души")),
        FlagsBlock(Seq(
          Flag("Клубы", "club"),
          Flag("Бильярд", "billiard"),
          Flag("Боулинг", "bowling"),
          Flag("Бани и сауны", "sauna"),
          Flag("Пейнтбол", "paintball")
        ), title=Option("Для тела"))
      )),

      Flags(Seq(
        FlagsBlock(Seq(
          Flag("Для детей", "forKids")
        ))
      ))
    )
  )

  def getByCode(code: String) = Action {
    Ok(formatV.writes(verticals(code)))
  }

  def listCodes = Action {
    Ok(verticals.values.foldLeft(JsArray()){(arr, v) => arr.append(Json.obj("code" -> v.code, "title" -> v.title))})
  }
}