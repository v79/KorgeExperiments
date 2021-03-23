package org.liamjd.korge.experiments

import com.soywiz.korge.input.MouseDragInfo
import com.soywiz.korge.input.draggable
import com.soywiz.korge.input.onClick
import com.soywiz.korge.ui.UIView
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA

class Window(
	width: Double,
	height: Double,
	color: RGBA = Colors.WHITE,
	title: String = "Window Title",
	modal: Boolean = false,
	contents: View? = null
) : UIView(width, height) {

	private val background = SolidRect(width, height, color)
	private val titlebar = SolidRect(width, 40.0, Colors.BROWN).centerXOn(background).alignBottomToTopOf(background)
	private val titlebarText = Text(title, 18.0, Colors.WHITE)
	private val closeButton = SolidRect(15.0,15.0, Colors.WHITE)

	private val innerPadding = 5.0
	init {
		name = "windowTest-${title}"
		addChild(titlebar)
		addChild(background)
		contents?.let {
//			it.scaledWidth = background.width - innerPadding
//			it.scaledHeight = background.height - innerPadding
			this.addChild(it)
			it.centerOn(background) }
		addChild(titlebarText)
		addChild(closeButton)
		titlebarText.centerOn(titlebar)
		closeButton.centerYOn(titlebar).alignRightToRightOf(titlebar,5.0)

		if(!modal) {
			draggable(titlebar) {
				beingDragged(it)
			}
			draggable(titlebarText) { beingDragged(it) }
		}

		closeButton.onClick {
			this.visible = false
		}
	}

	private fun beingDragged(it: MouseDragInfo) {
		if (it.start) {
			titlebar.alpha(0.5); background.alpha(0.5);
		}
		if (it.end) {
			titlebar.alpha(1.0); background.alpha(1.0)
		}
	}

}

inline fun Container.window(
	width: Double,
	height: Double,
	color: RGBA = Colors.WHITE,
	title: String = "uiWindow",
	modal: Boolean = false,
	contents: View? = null,
	callback: @ViewDslMarker Window.() -> Unit = {}
) = Window(width, height, color, title, modal, contents).addTo(this)
