package immutableexceptgas.occamsfuncer.util;

public class Compare{
	private Compare(){}
	
	/** -1, 0, or 1 if a < = or > b. Compares length first, then unsigned content. */
	public static int compare(byte[] a, byte[] b){
		if(a.length != b.length) return a.length<b.length ? -1 : 1;
		for(int i=0; i<a.length; i++){
			if(a[i] != b[i]){
				return (0xff&a[i]) < (0xff&b[i]) ? -1 : 1;
			}
		}
		return 0;
	}

}
