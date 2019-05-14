import java.util.Objects;

/**
 * The {@link Constraint} class represents a constraint between two 
 * Variables in a CSP problem. 
 *
 * @author Dominic Bosco
 * @date 03/15/2019
 */

public class Constraint {
	
	/** First square in the constraint */
	public Variable var1;
	
	/** Second square in the constraint */
	public Variable var2;
	
	/**
	 * Constructs a new constraint
	 * 
	 * @param var1 : the first variable
	 * @param var2 : the second second variable
	 */
	public Constraint(Variable var1, Variable var2) {
		this.var1 = var1;
		this.var2 = var2;
	}
	
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Constraint cstr = (Constraint) o;
        return (cstr.var1.index == this.var1.index) && (cstr.var2.index == this.var2.index);
    }
	
	public int hashCode() {
		return Objects.hash(this.var1, this.var2);
	}
	
	public String toString() {
		return this.var1.toString() + " -- " + this.var2.toString();
	}
}
