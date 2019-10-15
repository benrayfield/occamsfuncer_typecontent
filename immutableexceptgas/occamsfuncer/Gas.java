/** Ben F Rayfield offers this software opensource MIT license */
package immutableexceptgas.occamsfuncer;

/** alternative to halting oracle (which are impossible) */
public class Gas extends RuntimeException{
	
	/** Think of this as simulating free market trading
	between memory and compute cycles, both paid in units of Gas.top.
	When memory gets low, this changes so memory costs more.
	When memory is freed, it changes back.'
	Its not trading since theres only 1 process,
	and whats being controlled by the gas and mem and com limits
	are how much compute resources deeper calls can use
	before returning to parent call and so on.
	FIXME this hasnt been hooked in as of 2019-10-11,
	but Gas.top (HaltingDictator.topWallet in other forks of occamsfuncer)
	has been in those other forks of occamsfuncer.
	*/
	public static float memToComRatio;
	
	/** used by fn:gas and fn:spend
	TODO update comments and var names cuz renamed wallet to gas,
	like ethereum calls it.
	Starts as 2^53 cuz from 0 to that is a dense integer range
	that double can represent all of.
	*/
	public static double top;
	
	private static Gas instance;
	static{
		memToComRatio = 1;
		top = 1L<<53;
		/*Gas g = null;
		try{
			throw new Gas();
		}catch(Throwable t){
			g = (Gas)t;
		}*/
		//Gas g = new Gas();
		//instance = g;
	}
	public static Gas instance(){
		/** else:
		 * Exception in thread "main" immutableexceptgas.occamsfuncer.Gas
			at immutableexceptgas.occamsfuncer.Gas.<clinit>(Gas.java:39)
			at immutableexceptgas.occamsfuncer.tests.TestBasics.main(TestBasics.java:57)
		 */
		if(instance == null) instance = new Gas();
		return instance;
	}
	
	public Gas(){}
	
	/** If true, since last Cache.clear(), has not failed an Op.spend
	which would have diverted it to a catch cuz didnt have
	enough compute resources for the deterministic way,
	and has not done an Op.gas which nondeterministicly reads
	how much compute resources are left,
	but it can do Op.gasAtLeast since that takes the
	nondeterministic path if theres at least that much gas left,
	unless theres not then the nondeterministic path. 
	This becomes true after every call of Cache.clear().
	Deterministic calculations can be shared across Internet
	without sync problems,
	and the rest we will have to figure out as the system evolves
	but will probably end up being something only doable locally
	that leads to an object at the determinisstic level
	thats synced by ed25519 publicKeys to digsig arbitrary inputs
	paired with a double time and keep the newest object per publicKey.
	*/
	public static boolean deterministicSoFar = true;
	
	/*public static void init(){
		instance.toString(); //make static block run that sets Gas.top to nonzero
	}*/
	
	//TODO <peerId,long expireTime,objectId> and can pay to extend expireTime
	//for an object but cant shrink it, and can only extend it
	//to at most the expireTimes of its childs so nothing deeply reachable
	//can expire without becoming nonreachable. long nanosecondsUTC.
	
	public static void $() throws Gas{
		double newTopGas = top-1;
		if(newTopGas < 0) throw instance();
		top = newTopGas;
	}
	
	public static void $(double spend) throws Gas{
		double newTopGas = top-spend;
		if(newTopGas < 0) throw instance();
		top = newTopGas;
	}
	
	/** throws HaltingDictator.instance cuz doesnt have
	enough compute resources for an infinite loop.
	In this software everything halts but not every turingComplete calculation
	has enough computeCycles and memory (HaltingDictator.topWallet) to finish.
	*/
	public static fn infLoop(){
		throw instance();
	}

}
