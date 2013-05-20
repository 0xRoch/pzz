package model

/**
 * @author alari
 * @since 5/20/13 1:18 PM
 */
case class Vertical(
  title: String,
  code: String,
  filters: Flags,
  props: Flags
                     )

case class Flags(
  blocks: Seq[FlagsBlock]
                    )

case class FlagsBlock(
  flags: Seq[Flag],
  title: Option[String] = None,
  more: Option[Seq[Flag]] = None,
  moreTitle: Option[String] = None
                        )

case class Flag(title: String, code: String)
