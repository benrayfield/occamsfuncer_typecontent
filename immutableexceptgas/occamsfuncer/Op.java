/** Ben F Rayfield offers this software opensource MIT license */
package immutableexceptgas.occamsfuncer;
import static immutableexceptgas.occamsfuncer.util.FnFuncs.*;
import static immutableexceptgas.occamsfuncer.Gas.*;
import static immutableexceptgas.occamsfuncer.util.FnFuncs.mapMaxKey;
import static immutableexceptgas.occamsfuncer.util.FnFuncs.mapMinKey;
import static immutableexceptgas.occamsfuncer.util.FnFuncs.mapPairMaxChild;
import static immutableexceptgas.occamsfuncer.util.FnFuncs.mapPairMaxKey;
import static immutableexceptgas.occamsfuncer.util.FnFuncs.mapPairMinChild;
import static immutableexceptgas.occamsfuncer.util.FnFuncs.mapPairMinKey;
import static immutableexceptgas.occamsfuncer.util.FnFuncs.mapPairSize;
import static immutableexceptgas.occamsfuncer.util.ImportStatic.*;

import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;

import immutableexceptgas.occamsfuncer.fns.Call;
import immutableexceptgas.occamsfuncer.fns.Leaf;
import immutableexceptgas.occamsfuncer.util.Compare;

public enum Op{
	
	/** The gas and spend ops are the only 2 mutable ops, other than what may call them.
	Read how much gas is available from here and deeper calls.
	parent caller and farther back may have more.
	*/
	gas(1, DetLev.nondet, null, (BinaryOperator<fn>)(L,R)->{
		$();
		return wr(Gas.top);
	}),
	
	/** Limit gas use in deeper calls, which can themselves further limit it,
	and if that limit is not enough (run out of compute resources),
	while doing (w x), and if thats not enough compute resources
	then after that fails does (y z) charged to parent call's gas.
	<br><br>
	(fn:spend limit w x y z)
	<br><br>
	The gas and spend ops are the only 2 mutable ops, other than what may call them.
	spend gas on (w x) up to a limit else at parent caller's (y z).
	You can set spend limit, for deeper calls, between 0 and Gas.top (aka gas op),
	else it will be truncated into that range.
	Its best to leave at least a little gas in case of failure
	so your failure code can return instead of not having enough gas left to start.
	Any gas that was available not used is still available after the call.
	<br><br>
	FIXME nondeterminism complicates caching since whatever it returns the first time
	will be what it returns in all future calls until Cache.clear(),
	but it has to be that way cuz theres nowhere else to store things
	between when a call starts and ends such as a calculation that
	calls the same fn in multiple other fns as the s kind of controlFlow,
	which is the simplest kind of controlFlow, usually does.
	Fibonacci defined recursively costs linear instead of exponential.
	*/
	spend(5, DetLev.unknownTodoFixThis, null, (BinaryOperator<fn>)(L,R)->{
		//TODO can gasAtLeast and spend be merged and make spend be DetLev.detUnlessFail?
		$();
		//TODO optimize by not L.L.L... multiple times.
		double limit = L.L().L().L().R().d();
		fn w = L.L().L().R();
		fn x = L.L().R();
		if(limit < 0) limit = 0;
		limit = Math.min(Gas.top, limit);
		double removedFromTopGas = Gas.top-limit;
		double newTopGas = limit;
		boolean failed = true;
		try{
			fn ret = w.f(x); //cost inside spend call
			Gas.top += removedFromTopGas;
			return ret;
		}catch(Gas g){
			Gas.top += removedFromTopGas;
			fn y = L.R();
			fn z = R;
			return y.f(z); //cost outside spend call
		}
	}),
	
	plugNondet(1,  DetLev.nondet, null,(BinaryOperator<fn>)(L,R)->{
		$();
		throw new Error("TODO.");
	}),
	
	/** same as Op.spend but chooses (w x) vs (y z) instantly
	based on if theres minGas or more amount of gas left or not.
	Either path could still run out of compute resources
	after starting the call.
	The advantage of this over Op.spend is if it takes the (w x) path,
	its still deterministic as its future actions dont depend
	on any specific value of Gas.top (Op.gas)
	so does not change Gas.deterministicSoFar so can sync across Internet.
	*/
	gasAtLeast(5,  DetLev.detUnlessFail, null,(BinaryOperator<fn>)(L,R)->{
		//TODO can gasAtLeast and spend be merged and make spend be DetLev.detUnlessFail?
		$();
		throw new Error("TODO");
	}),
	
	
	
	//Everything below is DetLev.det
	

	
	/** any static java func whose name (after package.a.b.classname) starts with
	"ocfnplug", and which takes 1 param which is a fn, can be called this way.
	Such functions are considered part of the occamsfuncerVM
	so make sure they only come from trusted sources.
	<br><br>
	(fn:plug ;immutableexceptgas.occamsfuncer.util.Plugins.ocfnplugExamplePlusOne 10)
	returns 11.
	(fn:plug ;immutableexceptgas.occamsfuncer.util.Plugins.notanocfnplugExamplePlusOne 10)
	does infLoop() (throws Gas.instance).
	*/
	plugDet(1,  DetLev.det, null,(BinaryOperator<fn>)(L,R)->{
		$();
		throw new Error("TODO code works in other fork of occamsfuncer. mostly copy that here. also set up a search for all ocfnplug funcs avail.");
	}),
	
	/*I want the form of these that uses java stack
	for mapGet and mapPut etc recursion
	so doesnt add to CacheFuncParamReturn those internal calls.
	Use the funcs in FnFuncs such as FnFuncs.mapMinKey,
	which are not the most efficient
	way (dont unroll and share L.L.L...) but
	this will be efficient enough for a prototype
	to run pacman in realtime, and will opencl optimize later.
	
	
	TODO should this be a static func somewhere that uses L() and R() etc
	instead of instance func? Keeps core interface simpler.
	
	public default fn mapGet(fn key){
		throw new RuntimeException("TODO this in Call.f(fn) first, the pure interpreted way.");
	}
	
	public default fn mapPut(fn key, fn val){
		throw new RuntimeException("TODO this in Call.f(fn) first, the pure interpreted way.");
	}*/
	
	/** La.Lb.Lc.ac(bc), the s of https://en.wikipedia.org/wiki/SKI_combinator_calculus */
	s(3,  DetLev.det, null,(BinaryOperator<fn>)(fn L, fn R)->{
		//(fn:s x y R) returns ((x R)(y R))
		$();
		
		//FIXME? L.L() is fn:i when L is fn:s cuz fn:s is a leaf
		//but L should never be fn:s here cuz that not enough curries.
		fn x = L.L().R();
		fn y = L.R();
		return x.f(R).f(y.f(R)); //calls of fn.f(fn) are already Cache.dedup(...)
	}),
	
	/** La.Lb.a, the k of https://en.wikipedia.org/wiki/SKI_combinator_calculus */
	k(2, DetLev.det, null, (BinaryOperator<fn>)(fn L, fn R)->{
		//(fn:k q i) returns q
		$();
		return L.R();
	}),
	
	/** identity function. */
	i(1,  DetLev.det, null,(BinaryOperator<fn>)(fn L, fn R)->{
		//(fn:i R) returns R
		$();
		return R;
	}),
	
	cons(3, DetLev.det, null, (BinaryOperator<fn>)(fn L, fn R)->{
		//(fn:cons itsCar itsCdr x) returns (z itsCar itsCdr), as in churchEncoding.
		//TODO and as an optimization if z is Op.car or Op.cdr it doesnt call cons,
		//just looks in it using L and R.
		$();
		//FIXME TestBasics.testConsCarCdrNilAsChurchEncoding will help me figure this out
		//cuz theres "Two pairs as a list node" kind and "One pair as a list node" kind and "Represent the list using right fold".
		fn itsCar = L.L().R();
		fn itsCdr = L.R();
		return R.f(itsCar).f(itsCdr);
	}),
	
	car(1, DetLev.det, null, (BinaryOperator<fn>)(fn L, fn R)->{
		//(fn:car aCons) returns TODO, as in churchEncoding.
		//TODO see the optimization described in cons.
		$();
		if(R.op() == cons){ //optimization of churchEncoding, generates same ids.
			//(fn:car (fn:cons itsCar itsCdr))
			return R.L().R();
		}else{ //the literal churchEncoding way
			//FIXME TestBasics.testConsCarCdrNilAsChurchEncoding will help me figure this out
			//cuz theres "Two pairs as a list node" kind and "One pair as a list node" kind and "Represent the list using right fold".
			throw new Error("TODO");
		}
	}),
	
	cdr(1, DetLev.det, null, (BinaryOperator<fn>)(fn L, fn R)->{
		//(fn:car aCons) returns TODO, as in churchEncoding.
		//TODO see the optimization described in cons.
		$();
		if(R.op() == cons){ //optimization of churchEncoding, generates same ids.
			//(fn:cdr (fn:cons itsCar itsCdr))
			return R.R();
		}else{ //the literal churchEncoding way
			//FIXME TestBasics.testConsCarCdrNilAsChurchEncoding will help me figure this out
			//cuz theres "Two pairs as a list node" kind and "One pair as a list node" kind and "Represent the list using right fold".
			throw new Error("TODO");
		}
	}),
	
	nil(1, DetLev.det, null, (BinaryOperator<fn>)(fn L, fn R)->{
		//(fn:car aCons) returns TODO, as in churchEncoding.
		//TODO see the optimization described in cons.
		$();
		//FIXME TestBasics.testConsCarCdrNilAsChurchEncoding will help me figure this out
		//cuz theres "Two pairs as a list node" kind and "One pair as a list node" kind and "Represent the list using right fold".
		throw new Error("TODO");
	}),
	
	/** used with the F(...) syntax, which is 1 s-lambda-level higher than f(...).
	Renamed this from sCall to F cuz of F(...) vs f(...) syntax meaning fn.f().f()...
	f(a b c d e) means (a b c d e) means ((((a b) c) d) e).
	<br><br>.
	but F(...) is some Op.s mixed in that in a way that curries a param recursively
	<br><br>.
	TODO see the other fork of occamsfuncer where its a coretype,s
	but there are no coretypes here. This is the F(...) syntax.
	The first 2 curries define a binary tree, similar to see comment in sLinkedList.
	*/
	F(
		3,
		DetLev.det,
		(BiPredicate<fn,fn>)(fn L, fn R)->{
			throw new Error("TODO");
		},
		(BinaryOperator<fn>)(fn L, fn R)->{
			//(fn:F a b c) aka F(a b) returns (TODO).
			//There can be 2 or more things such as F(a b c d e) is multiple of these ops.
			$();
			return R;
		}
	),
	
	/** parame linkedlist.
	Example: f(fn:ccc mult3Things 3 4 5) returns 60,
	by computing: f(mult3Things f(cons f(fn:ccc mult3Things 3 4) 5)).
	The cons is needed cuz f(fn:ccc mult3Things 3 4 5) would eval to 60
	if it didnt infiniteLoop, so it would lose the info of what
	computed the 60.
	f(fn:pl f(cons f(fn:ccc mult3Things 3 4) 5))
	returns l(fn:ccc mult3Things 3 4 5), so mult3Things might contain fn:pl,
	but mult3Things's param must still be f(cons f(fn:ccc mult3Things 3 4) 5)
	for efficiency since (fn:ccc mult3Things 3 4 5) is (((fn:ccc mult3Things 3) 4) 5)
	which contains (fn:ccc mult3Things 3 4).
	*/
	pl(1,  DetLev.det, null, (BinaryOperator<fn>)(fn L, fn R)->{
		throw new Error("TODO");
	}),
	
	/** inverse of pl. You would use this if calling a func in a curry op
	without calling the curry op,
	but normally you would just call it in the curry op without this.
	*/
	plInv(1,  DetLev.det, null, (BinaryOperator<fn>)(fn L, fn R)->{
		throw new Error("TODO");
	}),
	
	/*TODO should curry funcs (cc CC ccc CCC etc) be a different func,
	1 for each size, or should there be an op that when called on itself,
	like (c c c c c) derives its number of curries
	and would have code written for that in Call.f(fn)?
	That would mean cant have the num of curries in Op
	or at least those c and C ops would have a boolean that means variable
	number of curries.
	*/
	
	//TODO fn:c and fn:C as curry1, which is only used for recursion
	//since a func is already of 1 param.
	
	/** same as cc except slower and does fn:pl to param first so i easier to use.
	(fn:CC func a b) returns (func l(fn:CC func a b))
	*/
	CC(3,  DetLev.det, null,(BinaryOperator<fn>)(fn L, fn R)->{
		//TODO use the same BinaryOperator for CC and CCC etc since they're
		//already exactly the same code, BUT that might change when do the optimization.
		fn cons = Op.cons.f;
		fn pl = Op.pl.f;
		fn func = L.L().R();
		//TODO optimize by not creating the cons. create the linkedlist here.
		return func.f(pl.f(cons.f(L).f(R)));
	}),
	
	/** curry 2. (fn:cc func a b) returns (func (cons (fn:cc func a) b)) */
	cc(3,  DetLev.det, null,(BinaryOperator<fn>)(fn L, fn R)->{
		$();
		fn cons = Op.cons.f;
		fn func = L.L().R();
		return func.f(cons.f(L).f(R));
		
		
		/*fn param = FIXME cant L.f(R) here cuz infinite loop;
		return L.f(param);
		
		It may have been consistent design in the other fork of occamsfuncer
		but here it seems to need param to be either linkedlist (inefficient)
		or for func to take 2 params (complicates and makes inefficient
		cuz requires s-currying since the curry ops
		are how you make a func of multiple params).
		
		How do I want to get the params at user level?
		
		param = f(cons,L,R)?
		
		param = linkedlist of fn:cc func a b?
		
		param = linkedlist of func a b?
		
		param = linkedlist of a b?
		
		param contains some opcode specialized in efficient params?
		No cuz couldnt do better than cons based linkedlist.
		
		Consider how recursion would work. In the other fork of occamsfuncer,
		f(fn:recurse func param) returns f(func f(fn:cons f(fn:recurse func) param)).
		The f(fn:recur func) might need to be reused so maybe should be in param
		that fn:recur creates.
		
		Curry could be defined as including the func, so you wouldnt need
		a separate fn:recur. Maybe they should be the same op.
		
		How about instead of cons L R, lazyEval L R? No, it doesnt take any more params
		so Call.f(...) would always correctly infLoop.
		
		I choose for there to be no fn:recur,
		and the param will be f(fn:cons myL myR),
		and there will be fn:pl
		*/
		
	}),
	
	/** Use curry ops instead, which recursion can be derived in since
	the func is included in its own param list after the curry op.
	f(fn:recur func param) returns f(func f(fn:cons f(fn:recur func) param)).
	func can contain car to get f(fn:recur func) and cdr to get param.
	TODO fibonacci example by recursion. 
	*
	recur(2, DetLev.det, (BinaryOperator<fn>)(fn L, fn R)->{
		$();
		fn recurFunc = L;
		fn func = recurFunc.R();
		fn param = R;
		fn cons = cons.f;
		return func.f(cons.f(recurFunc).f(param));
	}),*/
	
	/** curry 3. (fn:cc func a b) returns (func (fn:cc func a b)) */
	ccc(4,  DetLev.det, null, (BinaryOperator<fn>)(fn L, fn R)->{
		throw new Error("TODO");
	}),
	
	/** same as ccc except slower and does fn:pl to param first so i easier to use.
	(fn:CCC func a b) returns (func l(fn:cc func a b))
	*/
	CCC(4,  DetLev.det, null, (BinaryOperator<fn>)(fn L, fn R)->{
		//TODO use the same BinaryOperator for CC and CCC etc since they're
		//already exactly the same code, BUT that might change when do the optimization.
		fn cons = Op.cons.f;
		fn pl = Op.pl.f;
		fn func = L.L().R();
		//TODO optimize by not creating the cons. create the linkedlist here.
		return func.f(pl.f(cons.f(L).f(R)));
	}),
		
	//TODO curry up to 15 so the num of curries fits in 4 bits.

	
	/** This was renamed from sLinkedList to L cuz of L(...) syntax
	which is 1 s-lambda-level above l(...) syntax for a literal linkedList.
	There is no Op.l cuz its made of Op.cons and Op.nil.
	An L(...) is made of Op.L.
	<br><br>
	TODO see the other fork of occamsfuncer where its a coretype,
	but there are no coretypes here. This is the L(...) syntax.
	The first 2 curries define a binary tree. One is the next thing
	to prefix to the list and the other is the rest of the list,
	and the last curry is like the param of s (except this is 1 abstraction higher).
	*/
	L(3,  DetLev.det, null, (BinaryOperator<fn>)(fn L, fn R)->{
		throw new Error("TODO");
	}),
	
	/** often used to signal errors or for opcodes that are not available
	in this occamsfuncerVM but may be available in other opensource forks
	of occamsfuncerVM so as long as its just being infinitely slow
	(which actually skips to the end of that infinity instantly
	by throwing Gas.instance), its the same lambda math just slower.
	*/
	infLoop(3,  DetLev.det, null,(BinaryOperator<fn>)(fn L, fn R)->{
		return Gas.infLoop();
	}),
	
	/** (fn:igfp IGnoreData Func Param) returns (Func Param) */
	igfp(3,  DetLev.det, null, (BinaryOperator<fn>)(fn L, fn R)->{
		$();
		return L.R().f(R); //calls of fn.f(fn) are already Cache.dedup(...)
	}),
	
	/** (fn:add x y) returns x+y */
	add(2,  DetLev.det, null, (BinaryOperator<fn>)(fn L, fn R)->{
		$();
		return Cache.dedup(wr(L.R().d()+R.d()));
	}),
	
	/** (fn:add x y) returns x*y */
	mul(2,  DetLev.det, null, (BinaryOperator<fn>)(fn L, fn R)->{
		$();
		return Cache.dedup(wr(L.R().d()*R.d()));
	}),
	
	emptyMap(1, DetLev.det, null, (BinaryOperator<fn>)(fn L, fn R)->{
		//(fn:emptyMap key) returns value at that key: TODO_what_does_map_return_if_notContains.
		$();
		return nil.f;
	}),
	
	//TODO mapSingle?
	
	/** A map returns value at param key. Before that many params,
	it builds the map fields
	Constraints are checked at cur()-1, and key is at cur(),
	as in the "cur and cur-1" comment of fn.cur().
	<br><br>
	FIXME if allow arbitrary ids as map key then can DoSAttack
	maps up to their worst case of depth which is num of bits in an id
	(2019-10-14 this is 288 aka 36 bytes).
	Is much more DoSAttack resistant if it has to be hashed.
	<br><br>
	case Vm_switchInt.mapPair:
		//(fn:mapPair size minKey maxKey minChild maxChild)
		//aka (((((fn:mapPair size) minKey) maxKey) minChild) maxChild)
		//Verify constraints. Do nothing aka Call(this,R) if pass, else infLoop().
		
		//TODO optimize by unrolling these funcs and reusing shared L.L.L... etc.
		//size = mapPairSize(R);
		fn minKey = mapPairMinKey(R);
		fn maxKey = mapPairMaxKey(R);
		fn minChild = mapPairMinChild(R);
		fn maxChild = mapPairMaxChild(R);
		if(
			mapPairSize(R) == mapPairSize(minChild)+mapPairSize(maxChild)
			&& minKey == mapMinKey(minChild)
			&& maxKey == mapMaxKey(maxChild)
			&& Compare.compare(mapMaxKey(minChild).rawGet(),mapMinKey(maxChild).rawGet()) < 0
		){
			ret = new Call(this,R);
		}else{
			ret = Gas.infLoop();
		}
		
		
		
		//TODO use the funcs above instead of L L L R every time.
		//
		//fn RL=R.L(), RLL=RL.L(), RLLL=RLL.L(), RLLLL=RLL.L();
		//double size = RLLLL.R().d();
		//fn minKey = RLLL.R();
		//fn maxKey = RLL.R();
		//fn minChild = RL.R();
		//fn maxChild = R.R();
		//fn minChildLL = minChild.L().L();
		//fn maxChildLL = maxChild.L().L();
		//double minChild_size = minChildLL.L().L().R().d();
		//double maxChild_size = maxChildLL.L().L().R().d();
		////TODO optimize: some of these share .L().L()... calls.
		//fn minChild_minKey = minChildLL.L().R();
		//fn minChild_maxKey = minChildLL.R();
		//fn maxChild_minKey = maxChildLL.L().R();
		//fn maxChild_maxKey = maxChildLL.R();
		////TODO these constraints will need to be checked for in p2p network also
		//if(
		//	size == minChild_size+maxChild_size
		//	|| minKey == minChild_minKey
		//	|| maxKey == maxChild_maxKey
		//	|| minChild's maxKey < maxChild's minKey.
		//	TODO how to compare byte[] to byte[]? and is it signed?
		//){
		//	ret = new Call(this,R);
		//}else{
		//	ret = fn.infLoop();
		//}
	break;
	...
	//dedup was in CacheFuncParamReturn.java in other fork of occamsfuncer
	//and does not trigger lazyHash cuz compares by == and hashCode
	//so isnt strong dedup but works enough for forest of s and k calls
	//to not expand exponentially,
	//imilar to it would for fibonnacci computed by recursion.
	return dedup of ret;
	*/
	mapPair(
		5,
		DetLev.det,
		(BiPredicate<fn,fn>)(fn L, fn R)->{
			//(fn:mapPair size minKey maxKey minChild maxChild)
			$();
			//(fn:mapPair size minKey maxKey minChild maxChild)
			//aka (((((fn:mapPair size) minKey) maxKey) minChild) maxChild)
			//Verify constraints. Do nothing aka Call(this,R) if pass, else infLoop().
			
			//TODO optimize by unrolling these funcs and reusing shared L.L.L... etc.
			//size = mapPairSize(R);
			fn minKey = mapPairMinKey(R);
			fn maxKey = mapPairMaxKey(R);
			fn minChild = mapPairMinChild(R);
			fn maxChild = mapPairMaxChild(R);
			return mapPairSize(R) == mapPairSize(minChild)+mapPairSize(maxChild)
				&& minKey == mapMinKey(minChild)
				&& maxKey == mapMaxKey(maxChild)
				&& Compare.compare(mapMaxKey(minChild).rawGet(),mapMinKey(maxChild).rawGet()) < 0;
		},
		(BinaryOperator<fn>)(fn L, fn R)->{
			$();
			return mapPairGet(L, R); //map key. Should already be Cache.dedup(...)
		}
	),
	
	/** (fn:ed25519 l(;sign pubkey data)) returns signature.
	(fn:ed25519 l(;verify signature)) returns 1 if verified else 0.
	TODO signature contains pubkey?
	TODO is data any fn or is it a fn representing a byte array,
	and if so does it exclude the type: prefix in type:content
	and the content is just the byte array?
	Probably it will be any fn, just signing its id()
	(sha512 of that id, cuz it needs more hash bits and thats the usual implementation of ed25519).
	TODO if signature not contain pubkey then the linkedlist l(...) must be 1 bigger.
	*/
	ed25519_sha512_fn(1,  DetLev.det, null, (BinaryOperator<fn>)(fn L, fn R)->{
		throw new Error("TODO");
	});
	
	/*ed25519_sha512_bytes(1, DetLev.det, (BinaryOperator<fn>)(fn L, fn R)->{
		throw new Error("TODO");
	});*/
	
	
	
	/*(opencl)ForestOp
	
	s k iota
	
	car cons cdr nil
	
	math ops
	
	map ops
	
	avl ops
	
	` to `````````````` (or how many) curry ops (will have to name them
	something else since thats not valid java name), like fn:c to fn:cccccccccccccc.
	or how about c1 to c14? Do I want it written in baseTen? 
	
	FIXME: dedup where? In each op, or in Call.java which calls them?
	
	FIXME is plus...
	((fn:plus 3) 4)
	or is it...
	(fn:plus ((fn:plus 3) 4))
	???
	Should it be UnaryOperator<fn> or BinaryOperator<fn>?
	Appears to be ((fn:plus 3) 4) and UnaryOperator<fn>.
	x.f(y), as in Call.f(fn), appears to fit better with BinaryOperator<fn>.
	Cant be both, so choose. probably binaryoperator.
	
	//mapGet,
	
	//mapPut
	*/
	
	public final byte cur;
	
	public final DetLev detLev;
	
	/** If null, its always true.
	If nonnull, the fn checks the constraint at fn.cur()-1
	and does Gas.infLoop() if its false/fails.
	If cur==1, constraint must be null since constraint would
	have to be called on x.L() and x.R() but x already exists
	therefore any constraints must already have been checked.
	A Call instance is only for a halted constraint-satisfied state,
	though this might change in p2p design as you might find
	peers not obeying the constraints in the data they claim does obey constraints
	and it would take more steps to converge,
	but if they did all follow the rules, it would converge instantly
	even faster than light since it does not require communication to sync
	a merkle forest since it is derived instead of agreed on.
	<br><br>
	See comment of fn.cur() for details, especially the Op.mapPair example.
	*/
	public final BiPredicate<fn,fn> constraint;
	
	/** Its BinaryOperator instead of UnaryOperator
	cuz Call.java is not created for the last curry unless
	the call caused by that last curry returns itself therefore doesnt run again.
	*/
	public final BinaryOperator<fn> func;
	
	public final fn f;
	//public fn f(){ return f; }
	
	/** cur is number of curries before execute. Less than that returns self. */
	private Op(int cur, DetLev detLev, BiPredicate<fn,fn> constraint, BinaryOperator<fn> func){
		this.cur = (byte)cur;
		if(this.cur != cur) throw new Error("Number of curries doesnt fit in signed byte: "+cur);
		this.detLev = detLev;
		this.constraint = constraint;
		this.func = func;
		//f = Cache.dedup(new Leaf("fn:"+this.name()));
		f = Cache.dedup(new Leaf("fn:"+this.name()));
	}
	
	/*You can call anything as a func, but if its not one of these then
	it must fn.infLoop() to avoid changing behaviors to something else
	and instead infLoop() is like the correct math but infinitely slow.
	*/
}