/** Ben F Rayfield offers this software opensource MIT license */
package immutableexceptgas.occamsfuncer.util;
import immutableexceptgas.occamsfuncer.fn;
import immutableexceptgas.occamsfuncer.fns.Leaf;
import immutableexceptgas.occamsfuncer.fns.Num;

public class ImportStatic{
	
	/** wrap double in fn */
	public static fn wr(double d){
		return new Num(d);
	}
	
	public static fn wr(Object o){
		if(o instanceof String){
			return new Leaf("g:"+o); //FIXME weakDedup here (like in CacheFuncParamReturn)?
		}
		throw new Error("TODO");
	}

}
