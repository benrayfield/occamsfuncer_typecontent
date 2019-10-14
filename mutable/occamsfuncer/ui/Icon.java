/** Ben F Rayfield offers this software opensource MIT license */
package mutable.occamsfuncer.ui;
import java.awt.image.BufferedImage;

import immutableexceptgas.occamsfuncer.fn;

/** icon image, name, and position does not affect icon.data.id() */
public class Icon{
	
	public Icon(fn data){
		this.data = data;
		//TODO
	}
	
	public final fn data;
	
	/** drawn stretched. TODO get code from other project to do that.
	TODO allow drawing on this with mouse so can remember it,
	or maybe #naming them, which does not affect id().
	*/
	public BufferedImage image;
	
	/** #name */
	public String name;
	
	/** in a grid, width and height are always 1 and these are always integers */
	public double y, x;

}
