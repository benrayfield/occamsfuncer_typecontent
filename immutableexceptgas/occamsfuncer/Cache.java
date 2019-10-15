package immutableexceptgas.occamsfuncer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import immutableexceptgas.occamsfuncer.util.CallAsKey;

/** renaming this from CacheFuncParamReturn to Cache cuz also merging Dedup into it.
<br><br>
TODO
Datastruct will be something like set of CallAsKey
except might need to be a map, even though the CallAsKey
maybe should contain mutable fn ptr for val.
<br><br>
TODO how to dedup leaf, and should it dedup just the ops
or should it be every leaf which is small enough to be an id?
<br><br>
FIXME? These static funcs are not threadSafe, only cuz of the HashMap used
to store the <func,param,return> caches and not cuz of anything
occamsfuncer does outside of caching. In theory it could
be modified to run in a billion threads with no slowdown,
as hopefully it will across the Internet.
Opencl is considered 1 thread as Occamsfuncer waits on it,
though in hardware is very parallel. That uses the
OpenclUtil and ForestOp classes in the other fork of occamsfuncer
(TODO copy that code in and modify for the new design).
*/
public final class Cache{
	private Cache(){}
	
	private static final Map<CallAsKey,fn> cache = new HashMap();
	
	/** null if not putFuncParamReturn yet */
	public static fn getRetOfFuncParamElseNull(fn func, fn param){
		return cache.get(new CallAsKey(func,param));
	}
	
	public static void putFuncParamReturn(fn func, fn param, fn ret){
		cache.put(new CallAsKey(func,param), ret);
	}
	
	public static void putLeaf(fn leaf){
		throw new Error("TODO separate map for leafs up to max id size");
	}
	
	public static fn dedupLeaf(fn leaf){
		throw new Error("TODO separate map for leafs up to max id size");
	}
	
	/** If is a leaf thats supposed to be deduped (TODO is that just
	ops or is it any leaf small enough to be an id?)then dedups leaf,
	else is a call and dedups by f.L() and f.R().
	<br><br>
	If x is a fn, then x has halted, so x.L().f(x.R()).equals(x),
	so we can dedup it using FuncParamReturn on x.R() and x.L().
	<br><br>
	Only works if childs have been deduped first. 0Everything is always deduped this way
	cuz without that, small combos of Op.s would expand exponentially.
	*/
	public static fn dedup(fn f){
		if(f.isLeaf()){
			return dedupLeaf(f);
			//throw new Error("TODO perfect dedup anything small enough to be an id, and == dedup any bigger leaf");
		}else{
			//Leaf.L() is identityFunc, and Leaf.R() is that leaf,
			//so x.L().f(x.R()).equals(x) is true for leaf and nonleaf,
			//but that would be circularLogic to use that here for leaf.
			fn L=f.L(), R=f.R();
			fn deduped = getRetOfFuncParamElseNull(L, R);
			if(deduped == null) putFuncParamReturn(L, R, deduped=f);
			return deduped;
			
			/*FIXME clear() shouldnt remove these,
			just those that return something other than (this.L this.R),
			cuz it would be expensive to recompute that every time
			and would not dedup correctly since childs have to be deduped first.
			Use CallAsKey.retIsThisPair for that.
			*/
		}
	}
	
	/** clear CacheFuncParamReturn and Dedup.
	This will normally be done once per video frame such as 40 times per second,
	but TODO some parts of the cache maybe should stay longer,
	like there will later be RBM and LSTM learning batches that take 1 second
	since they have to do as many opencl calls as unrollBackprop time cycles,
	using (opencl)ForestOp.java, so will have to upgrade the cache for that.
	*/
	public void clearPartial(){
		//TODO optimize? 2 maps for the 2 possible values of retIsThisPair
		//would avoid looping over those that wont be removed
		//but would slow down cache lookups cuz would have to get from 2 maps.
		//For now I just want the system to work and will optimize later.
		
		Iterator<Map.Entry<CallAsKey,fn>> iter = cache.entrySet().iterator();
		while(iter.hasNext()){
			if(!iter.next().getKey().retIsThisPair) iter.remove();
		}
	}
	
	public void clear(){
		throw new Error("Cant clear where retIsThisPair unless dedup(...) calls itself recursively (and would end 1 level deep if already deduped but would still be slower). The other choice is to garbcol all fn objects and create them again, but thats impractical.");
		//clear all cache maps (will be 2 of them, 1 for <func,param,return> and 1 for leafs)
	}

}
