package immutableexceptgas.occamsfuncer;

/** DETermniism LEVel.
Any nondeterministic action sets Gas.deterministicSoFar to false,
which gets reset to true after each Cache.clear().
<br><br>
This is about the determinism of a specific Op,
not the ops it calls deeper since it may, for example,
look up a fn by id, and that fn is not downloaded until its called
(or predictively half a second before its called),
and that fn may have a different DetLev than those which called it.
Gas.deterministicSoFar measures if this has happened
since the last call of Cache.clear().
<br><br>
TODO? Maybe there should be an Op.forceDet that prevents
nondeterministic ops from having nondeterminstic effect,
like Op.gas would always return some large constant,
and Op.spend would never catch but the throw would
keep going up until the
whole [call since last Cache.clear()] failed.
I dont know how useful that would be,
since anything calculated where Gas.deterministicSoFar
can be synced across Internet. 
*/
public enum DetLev{
	
	det,
	
	detUnlessFail,
	
	nondet,
	
	/** this will not be a permanent part of DetLev, just until I choose a DetLev for each op. */
	unknownTodoFixThis;

}
