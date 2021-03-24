package org.liamjd.korge.experiments

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.Text
import com.soywiz.korge.view.ViewDslMarker
import com.soywiz.korge.view.addTo
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.font.DefaultTtfFont
import com.soywiz.korim.font.Font
import com.soywiz.korim.font.getTextBounds
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korim.text.TextRenderer
import com.soywiz.korim.text.TextRendererActions
import com.soywiz.korio.resources.Resourceable

/** Based on https://forum.korge.org/topic/40/text-wrapping/3 */
enum class WrapAlignment {
	LEFT, CENTER, RIGHT
}

inline fun Container.textArea(
	text: String,
	wrapWidth: Double,
	textSize: Double = 16.0,
	color: RGBA = Colors.WHITE,
	font: Resourceable<out Font> = DefaultTtfFont,
	wrapAlignment: WrapAlignment = WrapAlignment.LEFT,
	callback: @ViewDslMarker TextArea.() -> Unit = {}
) = TextArea(
	text,
	textSize = textSize,
	color = color,
	font = font,
	wrapWidth = wrapWidth,
	renderer = TextAreaRenderer(wrapWidth, wrapAlignment)
).addTo(this, callback)

open class TextArea(
	text: String, textSize: Double = DEFAULT_TEXT_SIZE,
	color: RGBA = Colors.WHITE, font: Resourceable<out Font> = DefaultTtfFont,
	alignment: TextAlignment = TextAlignment.TOP_LEFT,
	wrapWidth: Double,
	renderer: TextRenderer<String> = TextAreaRenderer(wrapWidth,wrapAlignment = WrapAlignment.LEFT),
	autoScaling: Boolean = true
) : Text(text, textSize, color, font, alignment, renderer, autoScaling) {
	init {
		val f = this.font.getOrNull()

		if (f is Font) {
			val metrics = f.getTextBounds(textSize, text, renderer = renderer)
			metrics.bounds.x = 0.0
			metrics.bounds.y = 0.0
			metrics.bounds.width = wrapWidth
			setTextBounds(metrics.bounds)
		}
	}

	/*override fun buildDebugComponent(views: Views, container: UiContainer) {
		container.uiCollapsibleSection("Wrappable Text") {
			uiEditableValue(::text)
			uiEditableValue(::color)
			uiEditableValue(::textSize, min= 1.0, max = 300.0)
			uiEditableValue(::autoScaling)
			uiEditableValue(::alignment, values = { listOf(TextAlignment.TOP_LEFT,TextAlignment.TOP_RIGHT) })
			uiEditableValue(::fontSource, UiTextEditableValue.Kind.FILE(views.currentVfs) {
				it.extensionLC == "ttf" || it.extensionLC == "fnt"
			})
		}
		super.buildDebugComponent(views, container)
	}
*/
}

class TextAreaRenderer(val wrapWidth: Double, val wrapAlignment: WrapAlignment) : TextRenderer<String> {

	override fun TextRendererActions.run(text: String, size: Double, defaultFont: Font) {
//		println("To wrap at $wrapWidth: $text")

		val lines = mutableListOf(Line())
		reset()
		setFont(defaultFont, size)

		val spaceWidth = getGlyphMetrics(' '.toInt()).xadvance + getKerning(' '.toInt(), 'A'.toInt())

		for (wrapped in text.split('\n')) {
			var curX = 0.0
			for (word in wrapped.split(' ')) {
				var wordWidth = 0.0
				var curWord = ""
				for (n in word.indices) {
					val c = word[n].toInt()
					val c1 = word.getOrElse(n + 1) { '\u0000' }.toInt()

					val g = getGlyphMetrics(c)
					val kerning = getKerning(c, c1)
					val charWidth = g.xadvance + kerning

					if (wordWidth + charWidth + spaceWidth > wrapWidth) {
						// Wrap inside the word
						val word1 = Word(curWord, wordWidth)
						if (lines.last().words.isEmpty()) {
							lines.last().words.add(word1)
						} else {
							lines.add(Line(mutableListOf(word1)))
						}
						curX = 0.0
						wordWidth = 0.0
						curWord = ""
						lines.add(Line())
					}

					wordWidth += charWidth
					curWord += c.toChar()
				}

				curX += wordWidth + spaceWidth

				if (curX > wrapWidth) {
					lines.add(Line())
					curX = wordWidth + spaceWidth
				}

				lines.last().words.add(Word(curWord, wordWidth))
			}
			lines.add(Line())
		}

		for (line in lines) {
//			println("Line ${line.calculateWidth(spaceWidth)}: $line")
			var start =
				when (wrapAlignment) {
					WrapAlignment.LEFT -> 0.0
					WrapAlignment.CENTER -> (wrapWidth - line.calculateWidth(spaceWidth)) / 2
					WrapAlignment.RIGHT -> wrapWidth - line.calculateWidth(spaceWidth)
				}

			for (word in line.words) {
				x = start

				for (n in word.text.indices) {
					val c = word.text[n].toInt()
					val c1 = word.text.getOrElse(n + 1) { '\u0000' }.toInt()

					val g = getGlyphMetrics(c)
					transform.identity()

					val advance = g.xadvance + getKerning(c, c1)

					put(c)
					advance(advance)
				}

				start += word.width + spaceWidth
			}

			newLine(lineHeight)
		}

		put(0)
	}

	data class Line(
		val words: MutableList<Word> = mutableListOf()
	) {
		fun calculateWidth(spaceWidth: Double): Double {
			return words.sumByDouble { it.width } + (words.size - 1) * spaceWidth
		}
	}

	data class Word(val text: String, val width: Double)
}
