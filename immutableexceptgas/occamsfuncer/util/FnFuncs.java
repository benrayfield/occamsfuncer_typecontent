/** Ben F Rayfield offers this software opensource MIT license */
package immutableexceptgas.occamsfuncer.util;
import immutableexceptgas.occamsfuncer.fn;

public class FnFuncs{
	private FnFuncs(){}
	
	/** todo can optimize this to share L().L()... calls later. just get it working asap. */
	public static double mapPairSize(fn map){
		return map.L().L().L().L().R().d();
	}
	
	/** todo can optimize this to share L().L()... calls later. just get it working asap. */
	public static fn mapPairMinKey(fn map){
		return map.L().L().L().R();
	}
	
	/** todo can optimize this to share L().L()... calls later. just get it working asap. */
	public static fn mapPairMaxKey(fn map){
		return map.L().L().R();
	}
	
	/** todo can optimize this to share L().L()... calls later. just get it working asap. */
	public static fn mapPairMinChild(fn map){
		return map.L().R();
	}
	
	/** todo can optimize this to share L().L()... calls later. just get it working asap. */
	public static fn mapPairMaxChild(fn map){
		return map.R();
	}
	
	public static fn mapSingleKey(fn mapSingle){
		return mapSingle.L().R();
	}
	
	public static fn mapSingleVal(fn mapSingle){
		return mapSingle.R();
	}
	
	public static boolean isMapPair(fn f){
		throw new Error("f.op()==TODO");
	}
	
	//FIXME should mappair be used to hold the key and val of mapsingle
	//and maybe put a val curry in it? or should it be separate mappair and mapsingle?
	
	/** map may be a mapPair or mapSingle */
	public static fn mapMinKey(fn map){
		return isMapPair(map) ? mapPairMinKey(map) : mapSingleKey(map);
	}
	
	/** map may be a mapPair or mapSingle */
	public static fn mapMaxKey(fn map){
		return isMapPair(map) ? mapPairMaxKey(map) : mapSingleKey(map);
	}

}
