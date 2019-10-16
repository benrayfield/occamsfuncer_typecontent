/** Ben F Rayfield offers this software opensource MIT license */
package immutableexceptgas.occamsfuncer.fns;
import java.io.OutputStream;
import immutableexceptgas.occamsfuncer.*;
import immutableexceptgas.occamsfuncer.util.Compare;

public abstract class AbstractFn<T> implements fn<T>{
	
	protected fn id;
	
	/** Op of L of L of L... until leaf. Example: Op.plus is the op of (fn:plus 3 4). */
	//public final Op op;
	//protected int vm_switchInt;

	public fn id(){
		if(id == null){
			if(rawLen() <= fn.maxIdSize) id = this;
			//else id = H: followed by multihash of rawGet() or rawGet(OutputStream)
			//else h: if weakref
			throw new Error("TODO H: or h: or ?: then multihash/ipfs id by doubleSha256 or if ipfs only works with sha256 by default then use that at least for the first few years");
		}
		return id;
	}
	
	public fn idWeakref(){
		throw new Error("TODO return an object thats same as id()'s type:content bytes but the type is h: and is a weakref to the same type:content except type is H:. H: is followed by ipfs/multihash hash which is normally 34 bytes so total 36 bytes.");
	}
	
	public final boolean equals(Object o){
		return this==o || ((o instanceof fn) && compareTo((fn)o)==0);
	}

}
