## Korge UI Experiments

A playground to work on additional UI components which would be especially useful in desktop and simulation or strategy games.


### Window

A draggable container with a fixed width, a title bar, and can contain any other Container view. Styling is very flat just now. Should really use a Korge skin, or at least a 9-patch texture. Width could be more configurable - specify a minimum and maximum width, and resize to fit the content? Add a callback when the window is closed. What about other window behaviours, such as minimise?

```kotlin
val windowContents = text("This is the text which appears in a window")
val myWindow = window(200.0,200.0,Colors.WHITE,"Window Title",contents =  windowContents).xy(10,10)
```

### Modal Window

An extension of Window, which cannot be dragged should block all other UI components. Currently implemented as a flag to the Window class. Ideally, this could trigger a dimming of the rest of the scene. How to disable input to all other elements in the scene?

```kotlin
val words = text("Do you wish to quit? Yes/No")
val dialog = window(200.0,200.0,Colors.WHITE,"Really Quit?", contents=words, modal = true).xy(10,10)
```

### Tooltips

My number one requirement for Korge, as I don't think I can implement it myself. My challenge is that I can't get the tooltip to appear on top of all other elements, as it's inlined with its parent and later drawn objects can appear on top. Korge may need some sort of layering or Z-indexing support.

```kotlin
val somewords = text("Hover over me for more information") {
    tooltip("By clicking OK below, you are agreeing to all terms and conditions, even the ones we haven't written yet.")
}
```
