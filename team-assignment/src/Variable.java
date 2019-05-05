import java.util.ArrayList;

public class Variable {
	
	/** The value of the variable **/
	public int listNum, index, val;
	
	public Boolean assigned;
	
	/** The current values still in the square's domain */
	public ArrayList<Integer> domain;
	
	/** A list of other variables this square shares a constraint with */
	public ArrayList<Variable> pairList;
	
	public Variable(ArrayList<Integer> domain, int listNum, int index) {
		// If size of domain is 1, set it to that value
		this.assigned = false;
		this.val = 0;
		
		if (domain.size() == 1) {
			this.val = domain.get(0);
			this.assigned = true;
		}
		
		this.listNum = listNum;
		this.index = index;
		this.domain = domain;
		this.pairList = new ArrayList<Variable>();
	}
	
	public Variable(Variable oldVar) {
		this.val = oldVar.val;
		this.listNum = oldVar.listNum;
		this.index = oldVar.index;
		this.assigned = oldVar.assigned;
		this.domain = new ArrayList<Integer>(oldVar.domain); 
		this.pairList = new ArrayList<Variable>();
	}
	
	public int domainSize() {
		return this.domain.size();
	}
	
	public void remove(int val) {
		this.domain.remove(new Integer(val));
		
		if (domain.size() == 1) {
			this.val = domain.get(0);
			this.assigned = true;
		}
	}
	
	public void addConstraint(Variable constraint) {
		this.pairList.add(constraint);
	}
	
	
	public void setPairList(ArrayList<Variable> pairList) {
		this.pairList = pairList;
	}
	
	public String toString() {
		return "Variable " + Integer.toString(this.index) + " from list " + Integer.toString(this.listNum) + ": " + Integer.toString(this.val);
	}
}
