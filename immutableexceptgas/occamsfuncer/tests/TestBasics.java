package immutableexceptgas.occamsfuncer.tests;
import static org.junit.Assert.*;
import static immutableexceptgas.occamsfuncer.util.ImportStatic.*;
import org.junit.Test;
import immutableexceptgas.occamsfuncer.Op;
import immutableexceptgas.occamsfuncer.fn;

public class TestBasics{
	
	/*TODO JUnit
	
	test skk is identityfunc
	
	test sCall
	
	test sLinkedList
	
	test fn:`` to curry 2 params and compute x^2+y^2
	
	test fibonacci by recursion, using CacheFuncParamReturn.
	
	test sCall and sLinkedlist and linkedList syntax.
	*/
	
	@Test
	public void testSkk(){
		fn s = Op.s.f; //TODO same as dedup(new Leaf("fn:s"))
		fn k = Op.k.f;
		fn abc = wr("abc");
		fn skk = s.f(k).f(k);
		assertTrue(skk.f(abc).equals(abc));
		assertTrue(skk.f(s).equals(s));
		assertTrue(skk.f(k).equals(k));
	}
	
	public void testSCall(){
		fn s = Op.s.f;
		fn k = Op.k.f;
	}

}
