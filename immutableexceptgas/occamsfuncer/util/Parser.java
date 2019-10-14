/** Ben F Rayfield offers this software opensource MIT license */
package immutableexceptgas.occamsfuncer.util;

import immutableexceptgas.occamsfuncer.fn;

public class Parser{
	
	//TODO DragAndDropFuncOntoFuncToReturnFunc before parsing
	
	/** Example: (const:import g:plus n:3 (const:import g:square n:2))
	could be written shorter by creating a #name for const:import
	but this is syntax for binary forest:
	(((const:import g:plus) n:3) ((const:import g:square) n:2)),
	and it returns n:7.
	The string "hello world" is written g:hello%20world
	since space is the byte 0x20. whitespace and parens and # etc
	must be escaped.
	OR... maybe it should use base64 syntax for nonprintables
	such as "image/jpeg:...bytes of jpg file..." would be $...
	or something like that. 
	*/
	public static fn parseCurrylistForest(String c){
		throw new Error("TODO");
	}
	
	/** the simplest form of the language is like unlambda and iota syntax.
	Everything is either a leaf or a call of a func on a param
	so is a binary forest. If the same fn is the child of multiple fn,
	its #named (which doesnt affect id()) and the name occurs in the code
	everywhere except the first place where the code is expanded.
	Remember, everyobject is type:content, but its not written
	exactly that way in code.
	<br><br>
	TODO use * and & pointer syntax like in C language
	except a pointer is id() (such as an ipfs multihash),
	so ``skk is identityfunc of it could also be written as
	``sk*<whateveristhesyntaxforwritingthehashofk>
	and you might also write a literal image/jpeg:...bytesofjpgfile...
	in place of an id but probably its best to write its hash instead.
	Something like &<image/jpeg:...bytesofjpgfile...> would be
	the H: id of image/jpeg:...bytesofjpgfile...
	
	*/
	public static fn stringToBinForest(String s){
		throw new Error("TODO");
	}
	
	public static String binForestToString(fn f){
		throw new Error("TODO");
	}

}
