
package assign6.parser;
import java.util.*; 
import assign6.lexer.*; 

public class Env {

	public Hashtable<Node,Type> table;
	protected Env prev;
	public Env() { table = new Hashtable<Node,Type>();}
	public Env(Env n) { table = new Hashtable<Node,Type>(); prev = n; }

	public void put(Node w, Type i) { table.put(w, i); }

	public Type get(Node w) {
		for( Env e = this; e != null; e = e.prev ) {
			Type found = (Type)(e.table.get(w));
			if( found != null ) return found;
		}
		return null;
	}
}
