/** Ben F Rayfield offers this software opensource MIT license */
package immutableexceptgas.occamsfuncer;

/** alternative to halting oracle (which are impossible) */
public final class Gas extends RuntimeException{
	
	public static final Gas instance = new Gas();
	
	private Gas(){}
	
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
	public static float memToComRatio = 1;
	
	/** used by fn:gas and fn:spend
	TODO update comments and var names cuz renamed wallet to gas,
	like ethereum calls it.
	*/
	public static double top;
	
	//TODO <peerId,long expireTime,objectId> and can pay to extend expireTime
	//for an object but cant shrink it, and can only extend it
	//to at most the expireTimes of its childs so nothing deeply reachable
	//can expire without becoming nonreachable. long nanosecondsUTC.
	
	public static void $() throws Gas{
		double newTopGas = top-1;
		if(newTopGas < 0) throw instance;
		top = newTopGas;
	}
	
	public static void $(double spend) throws Gas{
		double newTopGas = top-spend;
		if(newTopGas < 0) throw instance;
		top = newTopGas;
	}
	
	/** throws HaltingDictator.instance cuz doesnt have
	enough compute resources for an infinite loop.
	In this software everything halts but not every turingComplete calculation
	has enough computeCycles and memory (HaltingDictator.topWallet) to finish.
	*/
	public static fn infLoop(){
		throw instance;
	}

}
