## Korge UI Experiments

A playground to work on additional UI components which would be especially useful in desktop and simulation or strategy games.


### Window

A draggable container with a fixed width, a title bar, and can contain any other Container view.

```kotlin
   val windowContents = text("This is the text which appears in a window")
   val myWindow = window(200.0,200.0,Colors.WHITE,"Window Title",contents =  windowContents).xy(10,10)
```

### Modal Window

An extension of Window, which cannot be dragged and blocks all other UI components. Currently implemented as a flag to the Window class.

```kotlin
    val words = text("Do you wish to quit? Yes/No")
    val dialog = window(200.0,200.0,Colors.WHITE,"Really Quit?", contents=words, modal = true).xy(10,10)
```

### Tooltips

My number one required for Korge, as I don't think I can implement it myself.

```kotlin
    val somewords = text("Hover over me for more information") {
    	tooltip("By clicking OK below, you are agreeing to all terms and conditions, even the ones we haven't written yet.")
}
```
