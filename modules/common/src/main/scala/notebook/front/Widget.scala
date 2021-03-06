package notebook.front

import java.util.UUID

import notebook.util.ClassUtils

import scala.xml.{Node, NodeSeq}

trait Widget extends Iterable[Node] {
  def toHtml: NodeSeq

  def iterator = toHtml.iterator

  def ++(other: Widget): Widget = toHtml ++ other

  override def toString() = "<" + ClassUtils.getSimpleName(getClass) + " widget>"
}

class SimpleWidget(html: NodeSeq) extends Widget {
  def toHtml = html

  override def toString() = "<widget>"
}

object Widget {
  implicit def toHtml(widget: Widget): NodeSeq = widget.toHtml

  def fromHtml(html: NodeSeq): Widget = new SimpleWidget(html)

  implicit def fromRenderer[A](value: A)
      (implicit renderer: Renderer[A]): Widget = fromHtml(renderer.render(value))

  object Empty extends Widget {
    def toHtml = NodeSeq.Empty

    override def toString() = "<empty widget>"
  }

  // We're stripping out dashes because we want these to be valid JS identifiers.
  // Prepending with the "obs_" accomplishes that as well in that it forces it to
  // start with a letter, but it also helps make the namespace a little more
  // manageable.
  @deprecated("Avoid using IDs in widgets, to support the same widget appearing in multiple places on a page.", "1.0")
  def generateId = "widget_" + UUID.randomUUID().toString.replaceAll("-", "")
}
