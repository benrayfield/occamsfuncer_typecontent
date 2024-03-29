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
	public static void testSkk(){
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
	
	/** https://en.wikipedia.org/wiki/Church_encoding
	Theres optimization where car and cdr check if their param
	is (fn:cons x y) for any x y) and if so they dont call the cons
	but just return the x or y directly using L and R.
	That must get the same result as churchEncoding.
	*/
	public static void testConsCarCdrNilAsChurchEncoding(){
		fn cons = Op.cons.f;
		fn car = Op.car.f;
		fn cdr = Op.cdr.f;
		fn nil = Op.nil.f;
		throw new Error("TODO");
	}
	
	/** hypot is a function of 2 params that returns sqrt(x^2 + y^2).
	This test is of the curry2 op fn:cc. Curry3 would be fn:ccc.
	*/
	public static void testCurry2Hypot(){
		/*fn s = Op.s.f;
		//fn F = Op.F.f; //sCall, 1 s-lambda-level above s.
		fn k = Op.k.f;
		fn i = Op.i.f;
		fn cc = Op.cc.f; //curry2
		fn getX = null;//fixme; //get second last param when cc.f(...) gets all its params
		fn getY = null;//fixme; //get last param
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
		*/
		fn hypot = null; //fixme
		assertTrue(f(hypot,3,4).d()==5);
		assertTrue(f(hypot,8,15).d()==17);
	}
	
	/** Since theres by design no fn:recur, this will use (TODO choose) fn:c or fn:C
	which means curry 1, since that includes the func itself in the curry list
	so can recurse.
	*/
	public static void testRecurFibonacci(){
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
	public static void testRecurOfCurry2(){
		/*
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
		//TODO this must simultaneously handle how currying affects the param of func
		//and how recur affects the func. Are they compatible? I havent defined
		//what param currying generates yet (2019-10-15) in this fork of occamsfuncer,
		//and I'm using this testRecurOfCurry2 to help me figure out how to design it.
		*/
		fn sumOfSquaresIn2LinkedLists = null; //fixme
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
		//TestBasics t = new TestBasics();
		testSkk();
		System.err.println("first test passed. system is booted.");
		testCurry2Hypot();
		testRecurFibonacci();
		testRecurOfCurry2();
		testMapPutGet();
		testPixelsOfImageJpegContentType();
		System.err.println("OK.");
	}

}

