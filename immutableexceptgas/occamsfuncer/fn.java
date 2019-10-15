/** Ben F Rayfield offers this software opensource MIT license */
package immutableexceptgas.occamsfuncer;
import static immutableexceptgas.occamsfuncer.util.ImportStatic.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/** the core object type.
<br><br>
This is a p2p gaming-low-lag system
for millions of people to program, research, and play games together,
that uses ipfs/multihash compatible ids
but does not depend on ipfs or any other networking system
and can work across multiple such systems such as
storing objects locally, in bittorrent, in ipfs, on central servers,
and all those can work together seamlessly, in theory.
<br><br>
Fibonacci computed recursively does not expand exponentially, for example,
cuz of caching triples of <func,param,return> by == and hashCode,
which is not strong dedup but close enough to optimize for many uses.
(TODO that code is in CacheFuncParamReturn.java in other fork of occamsfuncer).
This cache will be cleared many times per second
though may exist in Etherum, Ipfs, etc longterm
depending which implementations of cache are used.
<br><br>
This is a spec for functional programming in merkle forest
and does not depend on any specific networking layer or language.
<br><br>
In lazyEval way for efficiency, each object is represented
as the bytes of type:content such as "image/jpeg:...bytes of jpg file..."
or something like "`:...idOfFunc...idOfParam..."
(cuz ` means call in unlambda and iota),
where ids are a secureHash of such type:content.
<br><br>
FIXME I want ipfs compatibility of the ids
but I also want to use the hash function sha256(xor(content,tile(sha256(content))))
where tile copies blocks of the 256 bits up to the length of content,
but since thats not a standard hash func, might need to use doubleSha256
which is sha256(sha256(content)), so should also use (github project) multihash
prefix to say what kind of hash it is.
IPFS seems to use mostly 34 byte sha256. Maybe I should use byte[] in mappair
instead of making another class for it, and just cache the byte[] in such id class.
<br><br>
Every object is halted. There are no objects representing a live function call,
but calls within a call can themselves be halted before the outer call is halted,
and thats represented as <func,param> or <func,param,return>
or <timeAve,timeStdDev,nonce,func,param,return> etc.
*/
public interface fn<T>{
	
	/** example: H:34bytesofMultihashAkaIpfsName.
	Anything thats at most this many bytes is its own id.
	*/
	public static final int maxIdSize = 36;
	
	public fn f(fn param);
	
	/** same as f(fn) but wraps param the standard way (ImportStatic.wr(double)) */
	public default fn f(double param){
		return f(wr(param));
	}
	
	/** same as f(fn) but wraps param the standard way (ImportStatic.wr(Object)) */
	public default fn f(Object param){
		return f(wr(param));
	}
	
	public boolean isLeaf();
	
	/** x.L().f(x.R()).equals(x), for all fn x.
	Leaf is (identityFunc this).
	equals(Object) is by binary forest shape and type:content leafs
	and will have multiple possible forests that have the same function behavior,
	but if equals then it has the same behavior.
	*/
	public fn L();
	
	/** see comment in L() */
	public fn R();
	
	/** for optimization, generated code will assign an int to common funcs
	such as cons car and cdr and use a big switch statement.
	This is nondeterministic, possibly mutable,
	and should not be called by user level code (same as any other vm_ funcs).
	*
	public int vm_switchInt();
	*/
	public Op op();
	
	/** return x where get L of L of L... of L returns x where x.L is ??/TheImportFunc.
	This is cached in Call.
	*
	public fn op();
	*/
	
	/** May trigger lazyHash.
	either a secureHash of rawGet() or a made up id with a certain prefix
	thats only to be used locally since its not part of the merkle forest.
	<br><br>
	Returns a type:content where type is h: or H: or ?:
	as explained in the conte
	*/
	public fn id();
	
	/** up to maxIdSize. literals can be smaller and are their own id. */
	public int idSize();
	
	/** A weakref to an id is that same id but with the first byte different,
	and as a func returns n:1 if its param is the thing its a weakref to
	else returns n:0.
	First byte is h: instead of H:,
	or if id() is local (not isMerkle) then gets the global form first.
	You cant have a weakref to a weakref.
	*/
	public fn idWeakref();
	
	/** curries remaining before eval */
	public byte cur();
	
	/** Returns true if id()'s type is h (weakref) or H (strongref),
	else that type is ?: (local id, not to cross untrusted borders).
	If id() would return a secureHash of rawGet bytes, returns true,
	else id() is a made up id with a certain prefix only to be used locally
	or is a weakref to the hash form. If false, this object should not be
	shared across untrusted borders since its properties are
	defined outside the merkle structure, normally for efficiency.
	These 3 possibilities each start with their own 1-byte type: "h:" "H:" and "?:".
	h:multihashBytes for weakref to H:multihashBytes for same multihashBytes.
	?:anyChosenBytes for a made up id not to be shared across untrusted borders.
	*/
	public boolean isMerkle();
	
	/** my value as double. if I'm not a number, returns 0.
	TODO override for efficiency.
	*/
	public default double d(){
		Object v = v();
		if(v instanceof Number) return ((Number)v).doubleValue();
		return 0;
	}
	
	/** whatever I wrap, get that, such as float[][][] or Double or int[][]. */
	public T v();
	
	/** length in bytes of type:content.
	For bigger bytestrings, can put them in a treemap or avltreelist
	which will themselves be type:content nodes.
	*/
	public int rawLen();
	
	/** reads rawLen bytes */
	public void rawGet(OutputStream out);
	
	public default byte[] rawGet(){
		ByteArrayOutputStream b = new ByteArrayOutputStream(rawLen()){
			public synchronized byte[] toByteArray(){
				if(buf.length == count) return buf;
				return super.toByteArray(); //copy
			}
		};
		rawGet(b);
		return b.toByteArray();
	}
	
	//TODO avlTreeList funcs

}