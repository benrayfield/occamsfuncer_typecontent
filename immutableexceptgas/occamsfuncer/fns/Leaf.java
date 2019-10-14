/** Ben F Rayfield offers this software opensource MIT license */
package immutableexceptgas.occamsfuncer.fns;

import java.io.OutputStream;

import immutable.util.Text;
import immutableexceptgas.occamsfuncer.Op;
import immutableexceptgas.occamsfuncer.fn;
import immutableexceptgas.occamsfuncer.util.Const;

/** any type:content other than Call or the 3 id types (h: H: ?:),
such as "image/jpeg:...bytesOfJpgFile...".
*/
public class Leaf<T> extends AbstractFn<T>{
	
	//TODO Leaf and Num share a common subclass, and rename Leaf to something else?
	
	protected byte[] cacheBytes;
	
	protected T cacheObject;
	
	/** example: "g:hello world" represents the string "hello world",
	and "fn:plus" represents the plus opcode.
	*/
	public Leaf(String s){
		this(Text.stringToBytes(s));
	}
	
	/** example: image/jpeg:...bytesOfJpgFile... */
	public Leaf(byte[] b){
		this.cacheBytes = b;
	}

	public fn f(fn param){
		throw new Error("TODO");
	}
	
	/** cuz is not known to be an opcode in this occamsfuncerVM
	but may be an opcode in other opsource forks of occamsfuncerVM
	so if called it will infLoop so its behaviors will only be slower
	but not return a different answer so the lambda math is still correct.
	*/
	public Op op(){
		return Op.infLoop;
	}
	
	/** ((L x) (R x)) evals to x for any x thats halted,
	so L of leaf is identityFunc and R is that leaf.
	*/
	public fn L(){
		return Const.identity; //TODO derive as (iota iota)
	}
	
	/** ((L x) (R x)) evals to x for any x thats halted,
	so L of leaf is identityFunc and R is that leaf.
	*/
	public fn R(){
		return this;
	}

	public fn id(){
		throw new Error("TODO");
	}

	public boolean isMerkle(){
		return true;
	}

	public T v(){
		throw new Error("TODO");
	}

	public int rawLen(){
		throw new Error("TODO");
	}
	
	public byte[] rawGet(){
		if(cacheBytes == null){
			throw new Error("TODO");
		}
		return cacheBytes;
	}

	public void rawGet(OutputStream out){
		throw new Error("TODO, stream from cacheBytes if exists, and if get any bytes then should this save into cacheBytes or did caller want it just to stream since thats the kind of rawGet they called?");
	}

	public byte cur(){
		return 1;
	}

	public int idSize(){
		return Math.min(rawLen(), fn.maxIdSize);
	}

}
