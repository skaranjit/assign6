package assign6.parser;

import java.util.*; 
import assign6.lexer.*; 
import assign6.ast.*;

/*
public class Env {

	public Hashtable<String,Type> table;
	protected Env prev;

	public Env() {
		table = new Hashtable<String,Type>();
	}

	public Env(Env n) {
		table = new Hashtable<String,Type>();
		prev = n;
	}

	public void put(String w, Type i) { table.put(w, i); }

	public Type get(String w) {
		for( Env e = this; e != null; e = e.prev ) {
			Type found = (Type)(e.table.get(w));
			if( found != null ) return found;
		}
		return null;
	}
}
*/

// new code
public class Env {

	private Hashtable table;
	public Env prev;

	public Env(Env n) {
		table = new Hashtable();
		prev = n;
	}

	public void put(Token w, IdentifierNode i) {
		table.put(w, i);
	}

	public IdentifierNode get(Token w) {
		for (Env e = this; e != null; e = e.prev) {
			IdentifierNode found = (IdentifierNode)(e.table.get(w));
			if (found != null)
				return found;
		}
		return null;
	}
}
