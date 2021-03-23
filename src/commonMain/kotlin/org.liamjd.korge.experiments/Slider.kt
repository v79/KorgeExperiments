package org.liamjd.korge.experiments

import com.soywiz.korge.input.onClick
import com.soywiz.korge.input.onMouseDrag
import com.soywiz.korge.internal.KorgeUntested
import com.soywiz.korge.ui.UIProgressBar
import com.soywiz.korge.ui.UIView
import com.soywiz.korge.ui.uiProgressBar
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors

@KorgeUntested
class Slider(
	width: Double,
	height: Double,
	current: Double) : UIView(width, height) {
	private val progressBar = uiProgressBar(width, height, current)
	private val incButton = text("+", textSize = 18.0)
	private val decButton = text("-", textSize = 18.0)
	private val progressText = text(progressBar.current.toString(), color = Colors.BLACK)

	init {
		progressText.name = "progressText"
		incButton.alignLeftToRightOf(progressBar, 5f)
		decButton.alignRightToLeftOf(progressBar, 5f)
		progressBar.addChild(incButton)
		progressBar.addChild(decButton)
		progressBar.addChild(progressText)
		progressText.centerOn(progressBar)
		decButton.onClick {
			println("DECREMENT")
			if(it.button.isRight) {
				progressBar.current -= 10.0
			} else {
				progressBar.current--
			}
		}
		incButton.onClick {
			println("INCREMENT")
			if(it.button.isRight) {
				progressBar.current += 10.0
			} else {
				progressBar.current.inc() //  THIS IS DECREMENTING!
			}
		}

		decButton.addUpdater {
			if(progressBar.current <= 0.00) {
				this.blendMode = BlendMode.DARKEN
			} else {
				this.blendMode = BlendMode.INHERIT
			}
		}
		incButton.addUpdater {
			if(progressBar.current >= progressBar.maximum) {
				this.blendMode = BlendMode.DARKEN
			} else {
				this.blendMode = BlendMode.INHERIT
			}
		}

		progressBar.onMouseDrag {
//		println("dx: ${it.dx} - dy: ${it.dy} - localDX: ${it.localDX} - localDY: ${it.localDY}")
			if(it.dx > 0) {
				progressBar.current++
			} else {
				progressBar.current--
			}
		}

		progressBar.addUpdater {
			if(progressBar.current < 0.0) { progressBar.current = 0.0 }
			if(progressBar.current > progressBar.maximum ) { progressBar.current = progressBar.maximum }
			progressBar.getChildByName("progressText").setText(progressBar.current.toString())
		}
	}
}

@KorgeUntested
inline fun Container.slider(
	width: Double,
	height: Double,
	current: Double = 0.0,
	callback: @ViewDslMarker Slider.() -> Unit = {}
) = Slider(
	width, height, current
).addTo(this, callback)
