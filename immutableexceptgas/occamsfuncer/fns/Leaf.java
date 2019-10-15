/** Ben F Rayfield offers this software opensource MIT license */
package immutableexceptgas.occamsfuncer.fns;
import static immutableexceptgas.occamsfuncer.Gas.*;
import java.io.OutputStream;
import immutable.util.Text;
import immutableexceptgas.occamsfuncer.Cache;
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
	
	protected Op cacheOp;
	
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
	
	public boolean isOp(){
		rawGet();
		return cacheBytes.length > 2 && cacheBytes[0]=='f' && cacheBytes[1]=='n';
	}

	public fn f(fn param){
		//TODO merge duplicate code between Leaf.f(fn) and Call.f(fn)
		$();
		/*TODO optimize. Can be much faster by caching this result,
		isOp, !isOp, or hasnt checked yet.
		If this is an Op (starts with fn:) then wrap in Call(this,param)
		but find a way to do it efficiently, dont want to scan the bytes every time.
		For now, just check the bytes.
		*/
		if(isOp()){
			Op o = op();
			if(o.cur == 1){ //eval. Would be error if cur<1 but that should never happen
				return o.func.apply(this, param);
			}else{ //not enough curries to eval
				return new Call(this,param);
			}
		}else{
			//optimization of Op being fn:i (identityFunc),
			//or would that circularLogic and this is more than an optimization?
			return this;
		}
	}
	
	/** cuz is not known to be an opcode in this occamsfuncerVM
	but may be an opcode in other opsource forks of occamsfuncerVM
	so if called it will infLoop so its behaviors will only be slower
	but not return a different answer so the lambda math is still correct.
	*/
	public Op op(){
		if(cacheOp == null){
			if(isOp()){ //calls rawGet which fills cacheBytes. FIXME this wont always be true for large leafs.
				try{
					String s = Text.bytesToString(cacheBytes); //FIXME might not be valid UTF8
					int i = s.indexOf(':');
					if(i != -1) s = s.substring(i+1);
					cacheOp = Op.valueOf(s);
				}catch(Throwable t){
					//TODO optimize. this could probably be done much faster than catch.
					//not UTF8 (bytesToString) so is not an op name, or not one of the op names
					cacheOp = Op.infLoop;
				}
			}
		}
		return cacheOp;
	}
	
	/** ((L x) (R x)) evals to x for any x thats halted,
	so L of leaf is identityFunc and R is that leaf.
	*/
	public fn L(){
		return Op.i.f; //TODO derive as (iota iota)
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
		if(cacheBytes != null) return cacheBytes.length;
		throw new Error("TODO"); //might be cacheObject and need transforming to bytes
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
		return op().cur;
	}

	public int idSize(){
		return Math.min(rawLen(), fn.maxIdSize);
	}

	public boolean isLeaf(){
		return true;
	}
	
	public String toString(){
		if(rawLen() > 100) return "leaf_size_"+rawLen();
		try{
			return Text.bytesToString(rawGet());
		}catch(Throwable t){
			return "TODO_display_nonUtf8";
		}
	}

}
