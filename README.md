# occamsfuncer
minimalist programming language for massively multiplayer collaboration at gaming-low-lag - every object is a funcall pair or type:content leaf, and can be secureHashed as ipfs-compatible id. Planning opencl and music tools optimizations.

fn is the core object type.

x.L().f(x.R()).equals(x), for all fn x. Each leaf type has a certain number of curries, before which (fn:plus 3) returns (fn:plus 3) but (fn:plus 3 4) returns 7. There will be more convenient syntaxes, especially for shared branches being #localNamed and for abstraction of s-lambda levels. All fn:xyz (for any xyz) are defined as part of the occamsfuncerVM and are not runtime variable names. There are no variable names because there are no variables, only constants. Everything is a kind of number, though its syntax will be intuitive enough for Humans age 5 and up (you cant break it, infinite loops are impossible for example). This system is similar to urbit but more designed to be intuitive for Humans and optimizable and a workaround for safety against the lack of halting oracles and other sandboxed safety.

Id types start with H: (normal hash id) or h: (weakref is a func that returns 1 if its param is the thing its a weakref to in H: form, else 0) or ?: followed by an arbitrary local id not to be shared across untrusted borders. Only H: and h: are shared across untrusted borders as they are the merkle-forest (blockchain-like) kinds of ids suffixed by ipfs-compatible id.

Any system, such as ipfs, bittorrent, ethereum, local, authoritarian websites, andOr direct peer to peer, can all work together as 1 network, though I'm skeptical of ethereum being 32 byte words and these being 36 byte which would further slow it down.

type:content leaf examples:

fn:plus

fn:cons

n:5

image/jpeg:...bytes of jpg file... (any content-type)

(the ` char isnt displaying well in git website)

`:H:abcH:def where abc and def are 34 byte ipfs ids (hash prefix such as sha256 then 32 bytes of hash). This is a funcall pair. ` is the name of this cuz that char is used in unlambda and iota languages.

All such objects are either a leaf or ` funcall pair.

The hash ids are lazyEvaled so dont slow things down except when observed the first time.

Treemaps are trie-like and have a max depth of the number of bits in an id and an average depth of log of their size.

AvlTreeList.

It will come with a fn:ed25519 function for fast digital signatures, to be used for anything you like or not used. Other algorithms can be either added at the VM level or derived at user level (slower) later.

S and K lambda based controlflow in the simplest cases but will also have higher level looping forms, which are also lambdas which forkEdit treemaps (and in optimized forms wont compute the treemaps but will get the same result). There is no mutable system state except Gas.top which is a mutable double for how much computing resources are allowed in calls deeper than each point, as Op.gas and Op.spend. Every object is a self contained lazyEval-hashed merkle forest.

Caches <func,param,return> so can fibonacci recursively in linear instead of exponential time.

(these things are incomplete, but the core design is nearly finished).

Older fork of occamsfuncer is at https://github.com/benrayfield/occamsfuncer_old, some of which will be used, but not the coretypes. There are no coretypes anymore in funcall pairs.


package immutableexceptgas.occamsfuncer.tests;
import static mutable.util.Lg.*;
//https://stackoverflow.com/questions/46717693/eclipse-no-tests-found-using-junit-5-caused-by-noclassdeffounderror-for-launcher
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=525948
//import org.junit.Test;
//import static org.junit.Assert.*;

import static immutableexceptgas.occamsfuncer.util.ImportStatic.*;
import immutableexceptgas.occamsfuncer.*;

public class TestBasics{
	
	public static void assertTrue(boolean b){
		if(!b) throw new RuntimeException("failed assertTrue");
	}
	
	/*TODO JUnit
	
	test skk is identityfunc
	
	test sCall
	
	test sLinkedList
	
	test fn:`` to curry 2 params and compute x^2+y^2
	
	test fibonacci by recursion, using CacheFuncParamReturn.
	
	test sCall and sLinkedlist and linkedList syntax.
	*/
	
	//@Test
	public void testSkk(){
		fn s = Op.s.f; //TODO same as dedup(new Leaf("fn:s"))
		lg("s: "+s+" cur="+s.cur());
		fn k = Op.k.f;
		lg("k: "+k);
		fn abc = wr("abc");
		lg("abc: "+abc);
		fn sk = s.f(k);
		lg("sk: "+sk);
		fn skk = sk.f(k);
		//fn skk = s.f(k).f(k);
		lg("skk: "+skk);
		assertTrue(skk.f(abc).equals(abc));
		assertTrue(skk.f(s).equals(s));
		assertTrue(skk.f(k).equals(k));
	}
	
	/** hypot is a function of 2 params that returns sqrt(x^2 + y^2).
	This test is of the curry2 op fn:cc. Curry3 would be fn:ccc.
	*/
	public void testCurry2Hypot(){
		fn s = Op.s.f;
		//fn F = Op.F.f; //sCall, 1 s-lambda-level above s.
		fn k = Op.k.f;
		fn i = Op.i.f;
		fn cc = Op.cc.f; //curry2
		fn getX = TODO; //get second last param when cc.f(...) gets all its params
		fn getY = TODO; //get last param
		fn mul = Op.mul.f;
		fn add = Op.add.f;
		fn sqrt = Op.sqrt.f;
		fn square = s.f( s.f(k.f(mul)).f(i), i );
		fn kSquare = k.f(square);
		fn squareX = F(k.f(square), getX);
		fn squareY = F(k.f(square), getY);
		fn hypot = f(
			cc,
			F(
				f(k,sqrt),
				F(
					f(k,add),
					F(kSquare, getX),
					F(kSquare, getY)
				)
			)
		);
		assertTrue(f(hypot,3,4).d()==5);
		assertTrue(f(hypot,8,15).d()==17);
	}
	
	/** Since theres by design no fn:recur, this will use (TODO choose) fn:c or fn:C
	which means curry 1, since that includes the func itself in the curry list
	so can recurse.
	*/
	public void testRecurFibonacci(){
		//fn recur = Op.recur.f;
		
		//TODO baseCase 0 returns 0, and baseCase 1 returns 1.
		//fn add = Op.add.f;
		fn fibonacci = null;
		//0 1 1 2 3 5 8 13
		assertTrue(fibonacci.f(5).d()==5);
		assertTrue(fibonacci.f(7).d()==0);
		assertTrue(fibonacci.f(1).d()==1);
		assertTrue(fibonacci.f(7).d()==13);
	}
	
	/** a recursive func of 2 params, using fn:cc and fn:recur.
	TODO This func will take 2 linkedlists of numbers and add the multiply of each pair,
	where a pair is 1 number from each list,
	and it will call itself recursively on the 2 cdrs of linkedlists.
	<br><br>
	Changed the design so theres no fn:recur and instead curry ops
	*/
	public void testRecurOfCurry2(){
		//fn recur = Op.recur.f;
		fn k = Op.k.f;
		fn add = Op.add.f;
		fn cc = Op.cc.f;
		fn car = Op.car.f;
		fn cdr = Op.cdr.f;
		fn pl = Op.pl.f;
		//get second thing in l(fn:cc self listA listB)
		fn getSelf = F(f(k,car), F(f(k,cdr),pl));
		//could have got it from the l(...) which would be more general but this is faster.
		fn getCurrySelf = F(f(k,cc),getSelf);
		//Will call F(getCurrySelf (k restOfListA) (k restOfListB))
		//TODO ,restOfListA syntax, means (k restOfListA), like ;abc means "abc".
		fn sumOfSquaresIn2LinkedLists = TODO;
		/*TODO this must simultaneously handle how currying affects the param of func
		and how recur affects the func. Are they compatible? I havent defined
		what param currying generates yet (2019-10-15) in this fork of occamsfuncer,
		and I'm using this testRecurOfCurry2 to help me figure out how to design it.
		*/
		assertTrue(sumOfSquaresIn2LinkedLists.f(l(3,5,4)).f(l(2,10,8)).d()==3*2+5*10+4*8);
		assertTrue(sumOfSquaresIn2LinkedLists.f(l(13,5,4)).f(l(2,110,8)).d()==13*2+5*110+4*8);
	}
	
	/** do this for up to size 100 map since the recursion needs testing.
	It can intheory handle maps up to size 2^53 or maybe a little smaller,
	cuz double can represent all integers in range plus/minus 2^53
	and cuz its a sparse lazyEval-hash merkle forest datastruct.
	*/
	public static void testMapPutGet(){
		fn emptyMap = Op.emptyMap.f;
		//use fn:mapGet and fn:mapPut to forkEdit emptyMap in loop
		throw new Error("TODO");
	}
	
	/** is Leaf whose bytes start with "image/jpeg:".
	Does not display since it must be automated test,
	but the image should be generated by known pixels, considering compression
	occurs by default in jpg. ImageIO class will handle the bytes after image/jpeg:
	*/
	public static void testPixelsOfImageJpegContentType(){
		throw new Error("TODO");
	}
	
	
	public void testSCall(){
		fn s = Op.s.f;
		fn k = Op.k.f;
		throw new Error("TODO");
	}
	
	public static void main(String[] args){
		TestBasics t = new TestBasics();
		t.testSkk();
		System.err.println("first test passed. system is booted.");
		testCurry2Hypot();
		testRecurFibonacci();
		testRecurOfCurry2();
		testMapPutGet();
		testPixelsOfImageJpegContentType();
		System.err.println("OK.");
	}

}

