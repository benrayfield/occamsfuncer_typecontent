/** Ben F Rayfield offers this software opensource MIT license */
package mutable.occamsfuncer.ui;

import javax.swing.JPanel;

/** todo like benrayfield's iotavm on github,
except i want it in grid squares. Left drag x onto y
calls x on y and returns z into still dragging,
which can continue by dropping onto another object
or end by dropping into an empty square.
rightClick deletes whats under mouse cursor
but does not delete whats dragging.
Dragging can continue even when no mouse buttons
are held.
*/
public class DragAndDropFuncOntoFuncToReturnFunc extends JPanel{
	
	TODO allow drawing on icon and changing #name per icon
	so Humans can remember it, which does not affect icon.data.id(),
	and have a textarea in left for displaying code form
	of whatevers selected (or maybe only if you do a certain thing)
	
	TODO what is icon class? anything that holds a BufferedImage or maybe int[][]?
	How to generate a random icon?
	How to generate the image of a known icon, since most of them
	will be combos of things that have no quickly understandable meaning
	to Humans and can only be understood through calling combos
	of other funcs on them.
	Ids will be 36 bytes, so 48 base64 digits,
	so could put in 7x7 grid of chars.

}
