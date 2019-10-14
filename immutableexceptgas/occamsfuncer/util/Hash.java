package immutableexceptgas.occamsfuncer.util;
import java.io.InputStream;

/** Default hash will be whatever ipfs uses by default, probably sha256,
though I would have preferred doubleSha256 or
sha256 of content xored by tiled sha256(content).
*/
public class Hash{
	private Hash(){}
	
	/** returns concat("H:",ipfsCompatibleId). Size is fn.maxIdSize.
	The 3 kinds of id are h: H: ?:
	where h: is weakref to H: (a func that returns 1 if its param is
	the thing its a weakref to, else returns 0),
	and ?: is followed by any arbitrary bytes and should not
	cross untrusted borders cuz its not a merkle id
	but can be converted to merkle id recursively
	as its an optimization that allows a thing to be
	used as a map key without triggering lazyHash.
	*/
	public static byte[] id(byte[] typeColonContent){
		throw new Error("TODO");
	}
	
	/** see id(byte[]) */
	public static byte[] id(InputStream typeColonContent){
		throw new Error("TODO");
	}

}
