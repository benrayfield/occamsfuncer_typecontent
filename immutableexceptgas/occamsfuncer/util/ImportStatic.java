/** Ben F Rayfield offers this software opensource MIT license */
package immutableexceptgas.occamsfuncer.util;
import immutableexceptgas.occamsfuncer.Op;
import immutableexceptgas.occamsfuncer.fn;
import immutableexceptgas.occamsfuncer.fns.Leaf;
import immutableexceptgas.occamsfuncer.fns.Num;

public class ImportStatic{
	
	/** wrap double in fn */
	public static fn wr(double d){
		return new Num(d);
	}
	
	public static fn wr(Object o){
		if(o instanceof fn) return (fn)o;
		if(o instanceof Double){
			return wr((double)o);
		}
		if(o instanceof String){
			return new Leaf("g:"+o); //FIXME weakDedup here (like in CacheFuncParamReturn)?
		}
		throw new Error("TODO");
	}
	
	/** f(a b c) means a.f(b).f(c). f(a b c d) means a.f(b).f(c).f(d).
	f(a) is a. fn() is error. Normally this would have at least 2 params.
	*/
	public static fn f(fn... curryList){
		fn x = curryList[0];
		for(int i=1; i<curryList.length; i++){
			x = x.f(curryList[i]);
		}
		return x;
	}
	
	/** 1 s-lambda-call level above f(...) */
	public static fn F(fn... sCallList){
		//FIXME will have to think about how the F/sCall op works in this
		//new fork of occamsfuncer since in the old fork SCall.java is a coretype,
		//but this fork has no coretypes since theres only funcall pairs.
		fn x = Op.F.x;
		for(int i=1; i<sCallList.length; i++){
			
			TODO
		}
		return x;
	}
	
	/** creates linkedlist. wr(Object), which is the fn itself if already a fn. */
	public static fn l(Object... list){
		fn linkedList = Op.nil.f;
		fn cons = Op.cons.f;
		for(int i=list.length-1; i>=0; i++){
			linkedList = cons.f(list[i],linkedList);
		}
		return linkedList;
	}

}
