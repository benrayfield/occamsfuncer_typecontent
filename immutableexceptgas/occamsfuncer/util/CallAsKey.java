/** Ben F Rayfield offers this software opensource MIT license */
package immutableexceptgas.occamsfuncer.util;
import immutableexceptgas.occamsfuncer.fn;

/** used as a java.util.Map key for caching <func,param,return>.
does not trigger lazyHash cuz is by == and hashCode.
Is for fast dedup that works for forests of s and k lambdas etc
which would otherwise do an exponential number of calls.
*/
public class CallAsKey{
	
	/** in Cache, only frequently clear those which execute to do something,
	and keep those which still ahve curries remaining (retIsThisPair)
	until that pair is garbcoled.
	*/
	public final boolean retIsThisPair;
	
	/*Need an ok hash, such as long (or just int hashCode()),
	but not a secureHash.
	See how I did it in acyc32' mutable hashtable,
	or maybe it was a later fork of occamsfuncer,
	whichever one used that ImmutableLinkedListNode for hash bucket,
	but I could just use HashMap for now.
	Put mutable fn val ptr in CallAsKey?
	Id maybe want to use Call as that but I dont want
	any fn to exist which isnt returned yet, so I'll do it in CallAsKey.
	...
	What about leafs? How are they weakDeduped? By == normally,
	but I need it deduped by content if its an Op (like fn:plus)
	and maybe also if its so small it fits in an id (fn.maxIdSize).
	*/
	
	public final fn L, R;
			
	/** null until (func param) returns *
	public fn val;
	*/
	
	/*FIXME "until that pair is garbcoled". Try to do that without Weakref
	cuz that would be extra slow.
	It wont need WeakReference cuz Cache.clear() happens
	normally once per video frame and will remove everything not reachable
	from 1 fn that is the root state, or something like that,
	though I would like flexibility to do it without requiring
	a rootState if I can efficiently but I would probably still
	want to use a rootState anyways. The difference is
	various java objects outside the system can hold pointers to fns
	if a rootState isnt required, but rootState is the main usecase.
	*/
	
	public CallAsKey(fn haltedLR){
		this.retIsThisPair = true;
		this.L = haltedLR.L();
		this.R = haltedLR.R();
	}
	
	public CallAsKey(fn L, fn R){
		this.retIsThisPair = false;
		this.L = L;
		this.R = R;
	}
	
	public boolean equals(Object obj){
		if(!(obj instanceof CallAsKey)) return false;
		CallAsKey c = (CallAsKey)obj;
		return c.L==L && c.R==R;
	}
	
	public int hashCode(){
		//this would call id() since hashCode is derived from it:  return func.hashCode()+param.hashCode();
		return System.identityHashCode(L)+System.identityHashCode(R);
		//throw new Error("TODO??? salt the hashes of the 2 childs, like probably did in other ocfn code in CacheFuncParamReturn class");
	}

}
