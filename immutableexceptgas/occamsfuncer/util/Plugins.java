/** Ben F Rayfield offers this software opensource MIT license */
package immutableexceptgas.occamsfuncer.util;
import static immutableexceptgas.occamsfuncer.Gas.*;
import static immutableexceptgas.occamsfuncer.util.ImportStatic.*;
import static mutable.util.Lg.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.UnaryOperator;
import immutableexceptgas.occamsfuncer.Gas;
import immutableexceptgas.occamsfuncer.fn;

/** plugins must obey the same design constraints as Occamsfn,
especially being immutable/stateless and limiting their compute resources by HaltingDictator.
<br><br>
Its easier to make core Opcodes deterministic.
Plugins are meant to have the same behavior in all implementations,
and once a behavior is defined it doesnt change,
except that a behavior may change from costing infinnite resources (in HaltingDictator)
to existing and running in sometimes finite resources,
which is consistent with the design since its still deterministic just faster.
I dont expect this will actually happen perfectly so occamsfn user level code
which calls the plugins will have to change every time a plugin is redesigned,
so eventually I want there to be no plugins and instead derive them
from occamsfn user level code and auto optimize by opencl and javassist.
<br><br>
Any call of a plugin on the same param may be cached and reused
since its meant to be immutable/stateless.
<br><br>
Plugins cant do stateful things such as web calls except if the content is constant such as by hash,
and they cant write to graphics or sound card etc, but external code which calls occamsfn can,
and occamsfn can read external code's message inside the occamsfn (by forkEdit)
then returna a fn containing a response (like request/response).
<br><br>
TODO explain that better and give an example.
<br><br>
Example occamsfn code:
f(?? ;plugin ;immutable.occamsfn.Plugins.ocfnplugExamplePlusOne 100) evals to 101.
*/
public class Plugins{
	
	private static final Map<String,UnaryOperator<fn>> plugins =
		Collections.synchronizedMap(new HashMap());
	
	/** any static java func whose name starts with this,
	and takes 1 param a fn and returns a fn, is automatically in the whitelist,
	and nothing else in the whitelist (TODO verify security).
	*/
	public static final String whitelistPrefix = "ocfnplug";
	
	/** throws HaltingDictator.throwMe if plugin not found */
	public static UnaryOperator<fn> plugin(String javaFuncName){
		UnaryOperator<fn> func = plugins.get(javaFuncName);
		if(func != null) return func;
		
		$(10000);
		javaFuncName = javaFuncName.trim();
		int i = javaFuncName.lastIndexOf('.');
		if(i == -1) throw Gas.instance; //caught at innermost Opcode.spend
		String className = javaFuncName.substring(0,i);
		try{
			Class c = Class.forName(className);
			String funcName = javaFuncName.substring(i+1);
			if(!funcName.startsWith(whitelistPrefix)) throw Gas.instance; //caught at innermost Opcode.spend
			final Method m = c.getMethod(funcName, fn.class);
			int modifiers = m.getModifiers();
			if(!Modifier.isStatic(m.getModifiers())) throw Gas.instance; //caught at innermost Opcode.spend
			Class retType = m.getReturnType();
			if(retType != fn.class) throw Gas.instance; //caught at innermost Opcode.spend
			UnaryOperator<fn> plugin = (fn p)->{
				try{
					return (fn) m.invoke(null, p);
				}catch(IllegalAccessException | IllegalArgumentException x){
					//this should never happen since its verified before making this lambda
					throw new Error(x);
				}catch(InvocationTargetException x){
					//caught either HaltingDictator.throwMe or something wrapping it or plugin is badly designed
					throw Gas.instance; //caught at innermost Opcode.spend
				}
			};
			plugins.put(javaFuncName, plugin);
			lg("Loaded plugin: "+javaFuncName);
			return plugin;
		}catch(ClassNotFoundException | NoSuchMethodException | SecurityException e){
			//TODO if its a SecurityException, log it the first time.
			//Else its easy to figure out from params and which java funcs exist and their names. 
			throw Gas.instance; //caught at innermost Opcode.spend
		}
	}
	
	/** an example occamsfn plugin that returns its param plus one */
	public static fn ocfnplugExamplePlusOne(fn p){
		$();
		return wr(p.d()+1);
	}
	
	/** a counterexample thats not an occamsfn plugin cuz of its name, for security */
	public static fn notanocfnplugExamplePlusOne(fn p){
		lgErr("This should not be callable from occamsfn code, but can be called from java code.");
		return ocfnplugExamplePlusOne(p);
	}
	
	/** 
	*
	public static void put(String pluginName, UnaryOperator<fn> func){
		plugins.put(pluginName, func);
	}
	
	public static fn call(String pluginName, fn param){
		TODO how to auto add plugins? Static java code doesnt always run
		but can be forced by Class.forName etc.
		Should plugins be javaClass that implements UnaryOperator<fn>?
		Do I want custom classloading?
		Make an example plugin called ;example .
		
		UnaryOperator<fn> func = plugins.get(pluginName);
		if(func == null) HaltingDictator.evalInfiniteLoop(); //caught at innermost Opcode.spend
		return func.apply(param);
	}*/

}
