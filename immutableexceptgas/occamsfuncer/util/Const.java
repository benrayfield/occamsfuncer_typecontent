/** Ben F Rayfield offers this software opensource MIT license */
package immutableexceptgas.occamsfuncer.util;
import immutableexceptgas.occamsfuncer.*;
import immutableexceptgas.occamsfuncer.fns.Leaf;

@Deprecated //use Op.opname.f instead, maybe, but there might be some constants that arent ops?
public class Const{
	
	/** a universal lambda function. aka Lf.fSK
	https://en.wikipedia.org/wiki/Iota_and_Jot
	https://en.wikipedia.org/wiki/SKI_combinator_calculus
	TODO use this syntax instead? new Leaf("λ:A``aBCD``bd`cdEFe");
	*
	//public static final fn iota = new Leaf("λ:λa.a(λb.λc.λd.bd(cd))(λe.λf.e)");
	public static final fn iota = new Leaf("fn:iota");
	
	//public static final fn S = new Leaf("λ:λa.λb.λc.ac(bc)");
	
	/** T aka K *
	public static final fn T = new Leaf("λ:λa.λb.a");
	
	/** see fn.L() *
	public static final fn L = new Leaf("fn:L");
	
	/** see fn.R() *
	public static final fn R = new Leaf("fn:R");
	
	/** see fn.id() *
	public static final fn id = new Leaf("fn:id");
	
	/** see fn.idWeakref() *
	public static final fn idWeakref = new Leaf("fn:idWeakref");
	
	/** see fn.rawLen() *
	public static final fn rawLen = new Leaf("fn:rawLen");
	
	/** iota.f(iota) would also work but less efficiently.
	TODO could still represent it as iota.f(iota) if used vm_switchInt()
	to optimize it.
	*
	//public static final fn identity = new Leaf("fn:identity"); //TODO
	public static final fn identity = iota.f(iota);
	
	public static final fn plus = new Leaf("fn:plus");
	
	/** any static java function whose name starts with "ocfnplug"
	(after the package...className)
	can be called by this. It is an error, though probably wont be detected
	at runtime, for such a func not to limit computeCycle and memory resources
	by the rules of HaltingDictator.topWallet
	(a mutable double used by fn:wallet and fn:spend)
	or to do anything nonrepeatable/nondeterministic other than
	limiting resources by topWallet.
	It must be immutable/stateless and pure-functional.
	<br><br>
	SECURITY FIXME: todo automatically scan all .class files etc
	and make a list of
	the plugins and display it somewhere in case someone tries to sneak one into
	an opensource code by a large pull-request thats not peer-reviewed well,
	and backdoor computers through it, but if they can write code in the VM
	then thats already a backdoor so this only makes it easier to use
	a backdoor if it already exists. Proper security includes
	the lack of backdoors, not the difficulty of using them.
	*
	public static final fn ocfnplug = new Leaf("fn:ocfnplug");
	
	/** ForestOp.java, forest of opencl kernel calls, andOr javassist etc *
	public static final fn parallelForestOp = new Leaf("fn:parallelForestOp");
	*/

}
