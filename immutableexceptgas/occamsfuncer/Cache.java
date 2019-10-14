package immutableexceptgas.occamsfuncer;

/** renaming this from CacheFuncParamReturn to Cache cuz also merging Dedup into it.
<br><br>
TODO
Datastruct will be something like set of CallAsKey
except might need to be a map, even though the CallAsKey
maybe should contain mutable fn ptr for val.
<br><br>
TODO how to dedup leaf, and should it dedup just the ops
or should it be every leaf which is small enough to be an id?
*/
public class Cache{
	private Cache(){}
	
	/** null if not putFuncParamReturn yet */
	public fn getRetOfFuncParamElseNull(fn func, fn param){
		throw new Error("TODO");
	}
	
	public void putFuncParamReturn(fn func, fn param, fn ret){
		throw new Error("TODO");
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
	public fn dedup(fn f){
		if(f.isLeaf()){
			throw new Error("TODO perfect dedup anything small enough to be an id, and == dedup any bigger leaf");
		}else{
			//Leaf.L() is identityFunc, and Leaf.R() is that leaf,
			//so x.L().f(x.R()).equals(x) is true for leaf and nonleaf,
			//but that would be circularLogic to use that here for leaf.
			fn L=f.L(), R=f.R();
			fn deduped = getRetOfFuncParamElseNull(L, R);
			if(deduped == null) putFuncParamReturn(L, R, deduped=f);
			return deduped;
			
			FIXME clear() shouldnt remove these,
			just those that return something other than (this.L this.R),
			cuz it would be expensive to recompute that every time
			and would not dedup correctly since childs have to be deduped first.
			Use CallAsKey.retIsThisPair for that.
		}
	}
	
	/** clear CacheFuncParamReturn and Dedup.
	This will normally be done once per video frame such as 40 times per second,
	but TODO some parts of the cache maybe should stay longer,
	like there will later be RBM and LSTM learning batches that take 1 second
	since they have to do as many opencl calls as unrollBackprop time cycles,
	using (opencl)ForestOp.java, so will have to upgrade the cache for that.
	*/
	public void clear(){
		throw new Error("TODO");
	}
	
	TODO copy from other occamsfuncer fork. use existing CallAsKey.
	Make sure to salt the hash algorithm using SecureRandom
	so its different in each jvm run so cant be DoSAttacked on hash collisions.

}
