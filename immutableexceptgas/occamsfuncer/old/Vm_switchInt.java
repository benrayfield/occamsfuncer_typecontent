package immutableexceptgas.occamsfuncer.old;

@Deprecated //use Op instead
public class Vm_switchInt{
	
	private static int i = 0;
	public static final int iota = i++;
	public static final int S = i++;
	/** T aka K */
	public static final int T = i++;
	public static final int L = i++;
	public static final int R = i++;
	public static final int plus = i++;
	public static final int neg = i++;
	
	public static final int ocfnplug = i++;
	
	/** see ForestOp.java for forest of opencl kernel calls in parallel */
	public static final int parallelForestOp = i++;
	
	/*TODO s-curry lists like F(...), vs normal currylists f(...)
	as in the other occamsfuncer,
	which was done by coretypes like SLinkedList and SCall etc,
	but dont do it yet cuz need the basic currylist working first
	and can define new funcs like fn:SLinkedList and define new syntaxes later,
	and can even use occamsfuncer user level code to define its own parser.
	*/
	
	/** throw HaltingDictator.instance */
	public static final int infiniteLoop = i++;
	
	/** TODO see Opcode.wallet and Opcode.spend in other occamsfuncer fork.
	STATEFULLY returns HaltingDictator.topWallet. This is the ONLY stateful part
	in the whole system other than caching.
	*/
	public static final int wallet = i++;
	
	/** (fn:spend maxSpend func param elseFunc elseParam)
	returns (func param) if that costs less than maxSpend
	(and maxSped <= HaltingDictator.topWallet)
	ELSE returns (elseFunc elseParam)
	with limit of HaltingDictator.topWallet as it is.
	<br><br>  
	TODO see Opcode.wallet and Opcode.spend in other occamsfuncer fork.
	This grabs HaltingDicator.topWallet, replaces topWallet with a new
	smaller double thats allowed to spend in deeper calls,
	and after they finish, adds the saved topWallet back into whats left in topWallet.
	else returns a certain constant (or TODO should it be a func and param to call)
	if there wasnt enough compute resources..
	*/
	public static final int spend = i++;
	
	/*CHANGE COMMENTS AND CODE to have extra params in MapPair
	cuz would complicate the caching without this...
	(fn:mapPair size minKey maxKey leftChild rightChild)
	(fn:mapGet (fn:mapPair size minKey maxKey leftChild rightChild) x)
	*/
	
	
	/** (fn:mapPair leftChild rightChild) is halted if sorted by id.
	(fn:mapPair leftChild rightChild x) gets value at key x.
	<br><br>
	Map is trie-like but skips to next deeper branch instead of always recursing 1 deeper.
	<br><br>
	throws infiniteLoop unless leftChild and rightChild are
	ordered recursively by id() of keys.
	<br><br>
	Used with fn:mapMinKey, fn:mapMaxKey, and fn:mapSize
	which will get cached the normal <func,param,return> way.
	See the other forks of occamsfuncer which have mapPair minKey etc in Opcode.java.
	*/
	public static final int mapPair = i++;

	/** Map is trie-like but skips to next deeper branch instead of always recursing 1 deeper. */
	public static final int mapMinKey = i++;
	
	/** Map is trie-like but skips to next deeper branch instead of always recursing 1 deeper. */
	public static final int mapMaxKey = i++;
	
	/** Map is trie-like but skips to next deeper branch instead of always recursing 1 deeper. */
	public static final int mapSize = i++;
	
	/** cant (map x) gets value at key x CUZ map's execution is used to verify constraints. 
	OLD: OBSOLETE CUZ (map x) gets value at key x.
	Map is trie-like but skips to next deeper branch instead of always recursing 1 deeper. *
	*/
	public static final int mapGet = i++;
	
	
	/** Map is trie-like but skips to next deeper branch instead of always recursing 1 deeper.
	forkEdits to return another map. at most height*someConstant
	number of new objects, where height is limited by the number of bits in an id.
	Height averages about log of map size.
	Worst case of height is log of number of bits in an id.
	*/
	public static final int mapPut = i++;
	
	/** get the global form of a local object.
	A local object's id would be ?:... and a global object's id would be h:... or H:...
	*/
	public static final int global = i++;
	
	/*solveOccamsfuncerParadoxOfLocalObjectAsMapKey
	QUOTE FIXME in other fork of occamsfuncer, every object knows if
	itself is a local vs global object, but theres no type:content
	for a local object since its type:content doesnt exist and its properties
	are stored somewhere else. I'll have to work this out over time,
	trying to make the design consistent. it will come up around the time
	i try to put a local object as a map key.
	A local object only has properties of the form <func,param,return>
	where the param is that local object and func is the kind of property,
	such as <fn:L,(fn:S fn:K),fn:S>, and it may be optimized various
	ways instead of storing in the normal CacheFuncParamReturn system
	such as a database of triples of int64 or CoreTypes
	in other forks of occamsfuncer.
	UNQUOTE.
	*/
	
	//TODO avl treelist ops
	
	//TODO ops for many of the static funcs in java.lang.Math
}
