package org.liamjd.korge.experiments

import com.soywiz.korge.debug.uiCollapsibleSection
import com.soywiz.korge.debug.uiEditableValue
import com.soywiz.korge.input.onOut
import com.soywiz.korge.input.onOver
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.font.DefaultTtfFont
import com.soywiz.korim.font.Font
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korim.text.TextRenderer
import com.soywiz.korio.resources.Resourceable
import com.soywiz.korui.UiContainer

class Tooltip(
	override var text: String, textSize: Double = Text.DEFAULT_TEXT_SIZE,
	color: RGBA = Colors.WHITE, font: Resourceable<out Font> = DefaultTtfFont,
	backgroundColor: RGBA = Colors.BLACK,
	alignment: TextAlignment = TextAlignment.TOP_LEFT,
	wrapWidth: Double,
	wrapAlignment: WrapAlignment,
	renderer: TextRenderer<String> = TextAreaRenderer(wrapWidth, wrapAlignment),
	autoScaling: Boolean = true,
) : Container(), ViewLeaf, IText {
	private val textBox = TextArea(text, textSize, color, font, alignment, wrapWidth, renderer, autoScaling)
	private val background = SolidRect(width = wrapWidth, height = textBox.scaledHeight, color = backgroundColor)

	init {
		addChild(background)
		addChild(textBox)
	}

	override fun buildDebugComponent(views: Views, container: UiContainer) {
		container.uiCollapsibleSection("Tooltip") {
			uiEditableValue(::text)
		}
		super.buildDebugComponent(views, container)
	}
}

inline fun Container.tooltip(
	text: String,
	textSize: Double = 16.0,
	wrapWidth: Double,
	wrapAlignment: WrapAlignment = WrapAlignment.LEFT,
	color: RGBA = Colors.WHITE,
	backgroundColor: RGBA = Colors.BLACK,
	font: Resourceable<out Font> = DefaultTtfFont,
	container: Container,
	callback: @ViewDslMarker TextArea.() -> Unit = {}
) =
	Tooltip(
		text,
		textSize = textSize,
		color = color,
		backgroundColor = backgroundColor,
		font = font,
		wrapWidth = wrapWidth,
		wrapAlignment = wrapAlignment,
		renderer = TextAreaRenderer(wrapWidth, wrapAlignment)
	).addTo(container)

fun Container.addTooltip(
	text: String,
	textSize: Double = 16.0,
	color: RGBA = Colors.WHITE,
	backgroundColor: RGBA = Colors.BLACK,
	container: Container,
): Tooltip {
	val tt = tooltip(
		text,
		wrapWidth = (text.length * textSize),
		textSize = textSize,
		color = color,
		backgroundColor = backgroundColor,
		container = container
	).apply {
		visible = false
		position(this.x + 15.0, this.y + 15)
	}
	this.addChild(tt)
	onOver {
		tt.visible = true
	}
	onOut {
		tt.visible = false
	}
	return tt
}
