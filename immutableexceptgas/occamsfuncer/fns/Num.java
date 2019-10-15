/** Ben F Rayfield offers this software opensource MIT license */
package immutableexceptgas.occamsfuncer.fns;
import java.io.IOException;
import java.io.OutputStream;

import immutableexceptgas.occamsfuncer.Op;
import immutableexceptgas.occamsfuncer.fn;
import immutableexceptgas.occamsfuncer.util.Const;

public class Num implements fn{
	
	//TODO Leaf and Num share a common subclass, and rename Leaf to something else?
	
	public final double val;
	
	public Num(double val){
		this.val = val;
	}
	
	public double d(){
		return val;
	}
	
	public Object v(){
		return val;
	}
	
	public fn f(fn param){
		return this;
	}

	public Op op(){
		return Op.infLoop;
	}

	public fn L(){
		return Op.i.f;
	}

	public fn R(){
		return this;
	}

	public int vm_switchInt(){
		throw new RuntimeException("TODO");
	}

	public fn id(){
		throw new RuntimeException("TODO");
	}

	public int idSize(){
		throw new RuntimeException("TODO");
	}

	public fn idWeakref(){
		throw new RuntimeException("TODO");
	}

	public byte cur(){
		return 1;
	}

	public boolean isMerkle(){
		return true;
	}

	public int rawLen(){
		//FIXME depends if use the Leaf datastruct or not.
		return 10;
	}

	public void rawGet(OutputStream out){
		/*FIXME should this use the more general Leaf datastruct
		which is defined in the other fork of occamsfuncer?
		Probably so, but look it up and implement it in Leaf.java
		in this fork first before I decide.
		*/
		try{
			out.write((byte)'d');
			out.write((byte)':');
			throw new Error("TODO write 8 bytes of the double");
		}catch(IOException e){ throw new RuntimeException(e); }
	}
	
	public boolean isLeaf(){
		return true;
	}

}
