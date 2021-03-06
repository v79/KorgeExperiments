import com.soywiz.korge.Korge
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korma.geom.degrees
import org.liamjd.korge.experiments.*

suspend fun main() = Korge(width = 1024, height = 768, bgcolor = Colors["#2b2b2b"]) {

	// text area for wrapping text
	val wrappingTextLabel = text("Wrapping text").xy(10.0,10.0)
	val textArea = textArea(text = "", wrapWidth = 220.0) {
		text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
		alignTopToBottomOf(wrappingTextLabel,5f)
	}

	val anotherWrappingTextLabel = text("Align right, different font").xy(textArea.scaledWidth + 50.0, 10.0)
	val anotherTextArea = textArea(text = "",wrapWidth = 300.0,textSize = 14.0,color = Colors.YELLOW,wrapAlignment = WrapAlignment.RIGHT) {
		text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
		alignLeftToRightOf(textArea,5f)
		alignTopToBottomOf(anotherWrappingTextLabel, 5f)
	}

	// windows
	val windowLabel = text("Window").xy(10.0,300.0)
	val windowContents = TextArea(text = "Drag the title bar to move this window around. Press the small white square to close the window.", color = Colors.BLACK, textSize = 14.0, wrapWidth = 180.0)
	val windowTest = window(200.0,200.0,Colors.PALEGREEN,"Window Title",contents =  windowContents).xy(10,360)

	val modalLabel = text("Modal window").xy(windowTest.scaledWidth + 50.0,300.0)
	val modalWindow = window(width = 150.0, height = 150.0, color = Colors.CORNFLOWERBLUE, title="Modal window", modal  = true).xy(modalLabel.x, 360.0)

	// tooltips - not yet working
	val tooltipContainer = Container()
	val tooltipLabel = text("Testing tooltips").xy(600.0,10.0)
	val textList = listOf("Every","Good","Boy","Deserves","Sherbert")
	val listX = 620.0
	var listY = 40.0
	textList.forEach {
		text(it, textSize = 18.0, color = Colors.DARKBLUE) {
			xy(listX,listY)
			addTooltip(text = it.toUpperCase(),textSize = 16.0, color = Colors.BLACK, backgroundColor = Colors.YELLOW, container = tooltipContainer)
		}
		listY += 20f
	}

	// slider - WIP
	val sliderLabel = text("Slider is VERY sensitive and broken").xy(600.0,200.0)
	val slider = slider(100.0, 20.0) {
		alignTopToBottomOf(sliderLabel,20.0)
		alignRightToRightOf(tooltipLabel)
	}

	val editableTextLabel = text("Editable text control - not yet written").xy(600.0, 300.0)

	val radioButtonControls = text("Radio button control - not yet written").xy(600.0,400.0)

	val tableLabel = text("A table layout is a stretch goal").xy(600.0,500.0)


	// adding the tooltipContainer as the very last child in an attempt to make it the top-most element on the page
	addChild(tooltipContainer)
}
