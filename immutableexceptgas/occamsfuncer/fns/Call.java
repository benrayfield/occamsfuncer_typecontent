/** Ben F Rayfield offers this software opensource MIT license */
package immutableexceptgas.occamsfuncer.fns;
import static immutableexceptgas.occamsfuncer.util.ImportStatic.*;
import static immutableexceptgas.occamsfuncer.Gas.$;
import static immutableexceptgas.occamsfuncer.util.FnFuncs.*;
import java.io.IOException;
import java.io.OutputStream;
import immutableexceptgas.occamsfuncer.*;
import immutableexceptgas.occamsfuncer.old.Vm_switchInt;
import immutableexceptgas.occamsfuncer.util.Compare;

/** a halted call such as s or sk or skk but not skks.
Every call L().L().L()...L() until find a leaf
uses that leaf as the function and things along that path
as its currylist of cur() number of params.
For example, f(λ:plus 3 4) returns 7,
or maybe should be fn: instead of λ: since λ should probably
be used only for self contained lambdas, and fn:plus is just
an arbitrary name of the plus function such as on doubles.
Hopefully the world will converge to a small set of
core function types, but any type:content can technically
be a function (if it describes a stateless process somehow)
such as "javascript:(function(x){ return x*x; })". 
*/
public final class Call<T> extends AbstractFn<T>{
	
	/*TODO do I want to keep ??/TheImportFunc
	vs use any object as a func
	such as (g:plus n:3 n:4) instead of (const:TheImportFunc g:plus n:3 n:4)
	or I could write it as fn:plus.
	Its 1 less depth without ??/TheImportFunc.
	*/
	
	//FIXME can cacheOp be removed and just use int cache_vm_switchInt
	//and can cache_vm_switchInt just be vm_switchInt instead of 2 ints?
	
	public final fn L, R;
	
	public final Op cacheOp;
	
	/** curries remaining before would eval */
	public final byte cacheCur;
	
	public Call(fn L, fn R){
		this.L = L;
		this.R = R;
		this.cacheCur = (byte)(L.cur()-1);
		//this.op = func.op() else get it from func unless func is ??/TheImportFunc
			//in which case (the dedup of) I am my own leftmostOp.
		this.cacheOp = L.op(); //L of L of L... return leaf.
	}
	
	/*TODO should I start using enums again (like in the other fork of occamsfuncer)
	or keep the 2 things: int of Vm_switchInt and FnFuncs.eachfuncname(fn)
	or is there a third way? I want to keep the int so generated code
	can optimize it at runtime using javassist.
	A switch case cant be enum.name.ordinal() even though it is constant.
	I choose enum.
	*/
	
	/** if the new call halts, then is simply Call(this,param),
	else runs until returns something or
	Gas.top runs out and throws Gas.instance.
	*/
	public fn f(fn R){
		//spend at least 1 nomatter what happens so always halts
		//but doesnt always finish what was requested to do.
		$();
		if(cacheCur>1){
			return Cache.dedup(new Call(this,R)); //TODO optimize by using other Cache func first?
			//throw new Error("return dedup new Call(this,param), best if not create the Call if exists.");
		}
		if(cacheCur <= 0){
			//can == 0 but should never be < 0 but just in case p2p net lies,
			//check for it, and constraints should converge toward satisfied blockchainlike
			//even if some peers lie, but in just 1 computer its straightforward
			//and doesnt need to converge.
			//
			//Example: To protect controlflow of mapPair has const num of args
			//(fn:mapPair size minKey maxKey minChild maxChild)
			//but not (fn:mapPair size minKey maxKey minChild maxChild key),
			//if that param (key) gets in here, infLoop()
			//since you're supposed to use
			//(fn:mapGet (fn:mapPair size minKey maxKey minChild maxChild) key).
			//Same for anything else that returns itself if constraints satisfied.
			return Gas.infLoop();
		}
		
		//FIXME dedup here or inside each op?
		return op().func.apply(this, R);
	}
	
	
	/** if the new call halts, then is simply Call(this,param),
	else runs until returns something or
	Gas.top runs out and throws Gas.instance.
	*
	public fn f(fn R){
	
		//FIXME I was doing it as param was the whole currylist,
		//but todo the currylist is (this_aka_L param_aka_R).
	
	
		//spend at least 1 nomatter what happens so always halts
		//but doesnt always finish what was requested to do.
		$();
		if(cacheCur>1){
			throw new Error("return dedup new Call(this,param), best if not create the Call if exists.");
		}
		if(cacheCur <= 0){
			//can == 0 but should never be < 0 but just in case p2p net lies,
			//check for it, and constraints should converge toward satisfied blockchainlike
			//even if some peers lie, but in just 1 computer its straightforward
			//and doesnt need to converge.
			//
			//Example: To protect controlflow of mapPair has const num of args
			//(fn:mapPair size minKey maxKey minChild maxChild)
			//but not (fn:mapPair size minKey maxKey minChild maxChild key),
			//if that param (key) gets in here, infLoop()
			//since you're supposed to use
			//(fn:mapGet (fn:mapPair size minKey maxKey minChild maxChild) key).
			//Same for anything else that returns itself if constraints satisfied.
			return Gas.infLoop();
		}
		
		//FIXME move the parts of the switch statement into Op.java.
		//Each case is in an enum instance as a BinaryOperator<fn>.
		
		//TODO optimize. this is where javassist generated code
		//might go that uses fn.vm_switchInt() for generated ints.
		
		//TODO other optimizations will prevent this f(param) func
		//from even being called and instead run opencl code etc
		//which does the same thing as combos of these objects (compiled),
		//but this interpreted form still needs to work if called.
		
		//TODO Vm_switchInt... ops for the S form of a few things,
		//like in the other form of occamsfuncer,
		//especially SCall written F(...) as compared to f(...)
		//and SLinkedList written L(...) instead of l(...).
		//That will require a more complex syntax (in Parser.java etc)
		//which I'll build after the basic syntax is working
		//so do the S-ops later. s and k are not S-ops as they are
		//a level of abstraction simpler.
		
		fn ret;
		switch(cacheOp.vm_switchInt()){
		case Vm_switchInt.L:
			//(fn:L getMyL)
			ret = R.L();
		break;
		case Vm_switchInt.R:
			//param is (fn:R getMyR)
			ret = R.R();
		break;
		case Vm_switchInt.ocfnplug:
			throw new Error("Any java func whose name after package.a.b.class starts with ocfnplug. TODO scan for such funcs in case one slips by in an opensource pull request and is accepted without checking if it maches the occamsfuncer spec for being sandboxex, HaltingDictator.topWallet, etc..");
		break;
		case Vm_switchInt.plus:
			//param is (fn:plus x y) aka ((fn:plus x) y)
			ret = wr(L.R().d()+R.d());
		case Vm_switchInt.mapGet:
			//(fn:mapGet map key)
			fn map = R.L().R();
			fn key = R.R();
			
			
			//TODO use the funcs above instead of L L L R every time.
			//
			//fn mapRL=R.L(), mapRLL=RL.L(), mapRLLL=RLL.L(), mapRLLLL=RLL.L();
			//double size = RLLLL.R().d();
			//fn minKey = RLLL.R();
			//fn maxKey = RLL.R();
			//fn minChild = RL.R();
			//fn maxChild = R.R();
			//
			////If param is anything but a MapPair or MapSingle,
			////returns some constant (TODO which?).
			////fn op = param.op(); //which of MapPair or MapSingle etc?
			////TODO
			////fn leftMap = TODO;
			////fn rightMap = TODO;
			////double size = TODO.d();
			////fn minKey = TODO;
			////fn maxKey = TODO;
			
			throw new Error("TODO");
			
			//TODO At first do this by calling mapGet recursively,
			//which will CacheFuncParamReturn which is inefficient,
			//but later optimize this by doing it all here in a loop.
		break;
		
		default:
			//If a func exists in some implementations but not in this one,
			//pretend it does exist but infinitely inefficient,
			//so its still correct lambda math.
			ret = Gas.infLoop();
		}
		//dedup was in CacheFuncParamReturn.java in other fork of occamsfuncer
		//and does not trigger lazyHash cuz compares by == and hashCode
		//so isnt strong dedup but works enough for forest of s and k calls
		//to not expand exponentially,
		//imilar to it would for fibonnacci computed by recursion.
		return Cache.dedup(ret);
	}*/
	
	/** ((L x) (R x)) evals to x for any x thats halted. */
	public fn L(){
		return L;
	}
	
	/** ((L x) (R x)) evals to x for any x thats halted. */
	public fn R(){
		return R;
	}

	public fn id(){
		throw new Error("TODO");
	}

	public boolean isMerkle(){
		return true;
	}

	public T v(){
		return null;
	}

	public int rawLen(){
		//`:idAidB where each id is size fn.idSize
		return 2+L.idSize()+R.idSize();
	}

	public void rawGet(OutputStream out){
		try{
			out.write((byte)'`');
			out.write((byte)':');
			L.id().rawGet(out);
			//FIXME since not every type:content tells its size (such as by varint prefix)
			//then how to know where first id ends?
			R.id().rawGet(out);
		}catch(IOException e){ throw new RuntimeException(e); }
	}

	public byte cur(){ return cacheCur; }

	public int idSize(){
		return fn.maxIdSize;
	}

	public boolean isLeaf(){
		return false;
	}

	public Op op(){
		return cacheOp;
	}
	
	/** FIXME this will expand exponentially if has linear depth of shared branches */
	public String toString(){
		return "("+L+" "+R+")";
	}

}
