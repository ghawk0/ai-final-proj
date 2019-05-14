import java.util.ArrayList;

/**
 * The {@link Variable} class represents a variable in a given 
 * CSP problem. 
 * 
 * @author Dominic Bosco
 *
 */
public class Variable {
	
	/** The value of the variable **/
	public int listNum, index, val;
	
	public Boolean assigned;
	
	/** The current values still in the square's domain */
	public ArrayList<Integer> domain;
	
	/** A list of other variables this square shares a constraint with */
	public ArrayList<Variable> pairList;
	
	/**
	 * Construct a new Variable. 
	 * 
	 * @param domain  : Domain of variable. 
	 * @param listNum : The number of the variable list it appears in. 
	 * @param index   : Its index in the variable list it appears in. 
	 */
	public Variable(ArrayList<Integer> domain, int listNum, int index) {
		this.assigned = false;
		this.val = 0;
		
		// If size of domain is 1, set it to that value. 
		if (domain.size() == 1) {
			this.val = domain.get(0);
			this.assigned = true;
		}
		
		this.listNum = listNum;
		this.index = index;
		this.domain = domain;
		this.pairList = new ArrayList<Variable>();
	}
	
	/**
	 * Construct a new Variable from an existing Variable. 
	 * 
	 * @param oldVar : Variable to be copied. 
	 */
	public Variable(Variable oldVar) {
		this.val = oldVar.val;
		this.listNum = oldVar.listNum;
		this.index = oldVar.index;
		this.assigned = oldVar.assigned;
		this.domain = new ArrayList<Integer>(oldVar.domain); 
		this.pairList = new ArrayList<Variable>();
	}
	
	/**
	 * 
	 * @return : Size of the variable's domain. 
	 */
	public int domainSize() {
		return this.domain.size();
	}
	
	/**
	 * Remove a given value from the variable's domain. 
	 * 
	 * @param val : Value to be removed. 
	 */
	public void remove(int val) {
		this.domain.remove(new Integer(val));
		
		// If domain size is 1, set value of variable. 
		if (domain.size() == 1) {
			this.val = domain.get(0);
			this.assigned = true;
		}
	}
	
	/**
	 * Add variable to list of variables a constraint is shared with. 
	 * 
	 * @param constraint : Variable a constraint is shared with. 
	 */
	public void addConstraint(Variable constraint) {
		this.pairList.add(constraint);
	}
	
	
	public String toString() {
		return "Variable " + Integer.toString(this.index) + " from list " + Integer.toString(this.listNum) + ": " + Integer.toString(this.val);
	}
}
