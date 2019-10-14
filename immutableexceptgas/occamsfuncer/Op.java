/** Ben F Rayfield offers this software opensource MIT license */
package immutableexceptgas.occamsfuncer;
import static immutableexceptgas.occamsfuncer.Gas.*;
import static immutableexceptgas.occamsfuncer.util.ImportStatic.*;

import java.util.function.BinaryOperator;

import immutableexceptgas.occamsfuncer.fns.Leaf;

public enum Op{
	
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
	
	s(3, (BinaryOperator<fn>)(fn L, fn R)->{
		//(fn:k x y R) returns ((x R)(y R))
		fn x = L.L().R();
		fn y = L.R();
		return x.f(R).f(y.f(R));
	}),
	
	k(2, (BinaryOperator<fn>)(fn L, fn R)->{
		//(fn:k q i) returns q
		return L.R();
	}),
	
	/** TODO see the other fork of occamsfuncer where its a coretype,
	but there are no coretypes here. This is the F(...) syntax.
	The first 2 curries define a binary tree, similar to see comment in sLinkedList.
	*/
	sCall(3, (BinaryOperator<fn>)(fn L, fn R)->{
		throw new Error("TODO");
	}),
	
	/** TODO see the other fork of occamsfuncer where its a coretype,
	but there are no coretypes here. This is the L(...) syntax.
	The first 2 curries define a binary tree. One is the next thing
	to prefix to the list and the other is the rest of the list,
	and the last curry is like the param of s (except this is 1 abstraction higher).
	*/
	sLinkedList(3, (BinaryOperator<fn>)(fn L, fn R)->{
		throw new Error("TODO");
	}),
	
	/** often used to signal errors or for opcodes that are not available
	in this occamsfuncerVM but may be available in other opensource forks
	of occamsfuncerVM so as long as its just being infinitely slow
	(which actually skips to the end of that infinity instantly
	by throwing Gas.instance), its the same lambda math just slower.
	*/
	infLoop(3, (BinaryOperator<fn>)(fn L, fn R)->{
		return fn.infLoop();
	}),
	
	/** (fn:igfp IGnoreData Func Param) returns (Func Param) */
	igfp(3, (BinaryOperator<fn>)(fn L, fn R)->{
		$();
		return L.R().f(R);
	}),
	
	/** (fn:plus x y) returns x+y */
	plus(2, ((BinaryOperator<fn>)(fn L, fn R)->{
		$();
		return wr(L.R().d()+R.d());
	})),
	
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
	plug(1, (BinaryOperator<fn>)(L,R)->{
		$();
		throw new Error("TODO code works in other fork of occamsfuncer. mostly copy that here. also set up a search for all ocfnplug funcs avail.");
	}),
	
	/** The gas and spend ops are the only 2 mutable ops, other than what may call them.
	Read how much gas is available from here and deeper calls.
	parent caller and farther back may have more.
	*/
	gas(1, (BinaryOperator<fn>)(L,R)->{
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
	*/
	spend(2, (BinaryOperator<fn>)(L,R)->{
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
	});
	
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
	
	/** Its BinaryOperator instead of UnaryOperator
	cuz Call.java is not created for the last curry unless
	the call caused by that last curry returns itself therefore doesnt run again.
	*/
	public final BinaryOperator<fn> func;
	
	public final fn f;
	
	/** cur is number of curries before execute. Less than that returns self. */
	private Op(int cur, BinaryOperator<fn> func){
		this.cur = (byte)cur;
		this.func = func;
		f = new Leaf("fn:"+this.name());
	}
	
	/*You can call anything as a func, but if its not one of these then
	it must fn.infLoop() to avoid changing behaviors to something else
	and instead infLoop() is like the correct math but infinitely slow.
	*/
}