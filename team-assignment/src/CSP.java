import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * The {@link CSP} class is an abstract class with the necessary methods for representing
 * a CSP problem and obtaining a solution using AC3. This class implements all necessary
 * methods, but backtracking must be implemented by inheriting, non-abstract classes. 
 * 
 * 
 * @author Dominic Bosco
 *
 */
public abstract class CSP {

	/** Minimum key value in the variable HashMap */
	public int minKey;
	
	/** HashMap of Integer to Variable ArrayList. Each key represents a different
	 *  class of variable in the CSP problem. */
	public HashMap<Integer, ArrayList<Variable>> varLists;
	
	/** Boolean indicating whether the assignment and current CSP is invalid (violates constraints) */
	public Boolean failure;
	
	/**
	 * Constructs a new CSP. 
	 */
	public CSP() {
		this.varLists = new HashMap<Integer, ArrayList<Variable>>();
		this.minKey = Integer.MAX_VALUE;
		this.failure = false;
	}
	
	/**
	 * Copy an existing CSP, assigning a value to the indicated variable. 
	 * 
	 * @param oldCSP : The old CSP to modify. 
	 * @param listToInsert : Variable list number of variable whose value we will modify. 
	 * @param newIndex : Index in list of variable whose value will modify. 
	 * @param val : Value to assign to specified variable.
	 */
	public CSP(CSP oldCSP, int listToInsert, int newIndex, int val) {
		// Copy constants from existing CSP. 
		this.varLists = new HashMap<Integer, ArrayList<Variable>>();
		this.minKey = oldCSP.minKey;
		this.failure = false;

		// Copy all variables from the existing CSP. 
		for (Integer listNum : oldCSP.varLists.keySet()) {
			ArrayList<Variable> oldList = oldCSP.varLists.get(listNum);
			
			for (int i = 0; i < oldList.size(); i++) {
				Variable oldVar = oldList.get(i);
				
				// Update value of specified variable while copying. Otherwise, simply copy. 
				if (listToInsert == listNum && i == newIndex) {
					ArrayList<Integer> domain = new ArrayList<Integer>();
					domain.add(val);
					this.addVariable(domain, listNum);
	
				} else {
					this.addVariable(oldVar, listNum);
				}
			}
		}
		
		// Reset neighbor variables for all variables in the CSP. 
		for (Integer listNum : oldCSP.varLists.keySet()) {
			for (Variable var : oldCSP.varLists.get(listNum)) {
				for (Variable pair : var.pairList) {
					this.addNeighbor(var.listNum, var.index, pair.listNum, pair.index);
				}
			}
		}
		
	}
	
	/**
	 * Function to execute the ac3 algorithm for constraint propagation. 
	 * 
	 * @return false if no valid assignment is possible, true otherwise. 
	 */
	public Boolean ac3() {
		// Get queue of constraints. 
		LinkedList<Constraint> constraints = this.getConstraints();
		Constraint nextConstraint;
		
		while (!constraints.isEmpty()) {
			// Get next constraint (xi, xj).
			nextConstraint = constraints.poll();
			
			// Check whether we edit the domain of xi
			if (revise(nextConstraint.var1, nextConstraint.var2)) {
				// If xi's domain is zero, no valid assignment is possible. 
				if (nextConstraint.var1.domainSize() == 0) {
					return false;
				}
				
				// If valid, add all constraints (xk, xi) for neighbors xk of xi where xk != xi. 
				for (Variable pair : nextConstraint.var1.pairList) {
					if (!(pair == nextConstraint.var2)) {
						constraints.offer(new Constraint(pair, nextConstraint.var1));
					}
				}
			}
			
		}
		
		return true;
	}
	
	/**
	 * Remove values in the domain of x1 inconsistent with the domain of x2. 
	 * 
	 * @param x1 : first variable in the constraint. 
	 * @param x2 : second variable in the constraint. 
	 * @return 	 : true if some value in the domain of x1 is inconsistent with 
	 * 			   domain of x2 and is removed, false otherwise. 
	 */
	public abstract Boolean revise(Variable x1, Variable x2);	
	
	/**
	 * 
	 * @return a cost associated with the current CSP. 
	 */
	public abstract int getCost();
	
	/**
	 * 
	 * @return Unassigned variable with the smallest domain size. 
	 */
	public Variable selectVar() {
		int minSize = Integer.MAX_VALUE;
		Variable minVar = this.varLists.get(this.minKey).get(0);
		
		for (Integer numList: this.varLists.keySet()) {
			for (Variable var : this.varLists.get(numList)) {
				if (var.assigned) {
					continue;
				}
				
				if (var.domainSize() < minSize) {
					minVar = var;
					minSize = var.domainSize();
				}
			}
		}
			
		return minVar;
	}
	
	/**
	 * 
	 * @return Queue containing all the constraints in the problem. 
	 */
	public LinkedList<Constraint> getConstraints() {
		
		LinkedList<Constraint> constraints = new LinkedList<Constraint>();
		
		for (Integer listNum : varLists.keySet()) {
			for (Variable currVar : varLists.get(listNum)) {
				for (Variable pairVar : currVar.pairList) {
					constraints.offer(new Constraint(currVar, pairVar));
				}
			}
		}
		
		return constraints;
		
	}
	
	/**
	 * Add var Y to list of neighbors of var X. 
	 * @param listNumX : The list number var X appears in. 
	 * @param indX	   : The index of var X in its list. 
	 * @param listNumY : The list number var Y appears in. 
	 * @param indY	   : The list number of var Y in its list. 
	 */
	public void addNeighbor(int listNumX, int indX, int listNumY, int indY) {
		Variable var1 = varLists.get(listNumX).get(indX);
		var1.pairList.add(varLists.get(listNumY).get(indY));
	}
	
	/**
	 * Adds a variable to the CSP into the given list and with the specified domain. 
	 * 
	 * @param domain  : Domain of variable to be added. 
	 * @param listNum : List to add the variable to. 
	 */
	public void addVariable(ArrayList<Integer> domain, int listNum) {
		if (listNum < this.minKey) {
			this.minKey = listNum;
		}
		if (varLists.containsKey(listNum)) {
			ArrayList<Variable> varList = this.varLists.get(listNum);
			varList.add(new Variable(domain, listNum, varList.size()));
		} else {
			ArrayList<Variable> varList = new ArrayList<Variable>();
			varList.add(new Variable(domain, listNum, varList.size()));
			this.varLists.put(listNum, varList);
		}
	}
	
	/**
	 * Adds a copy of an existing variable into the given list. 
	 * 
	 * @param oldVariable : Variable to be copied. 
	 * @param listNum     : List to add the copied variable to. 
	 */
	public void addVariable(Variable oldVariable, int listNum) {
		if (listNum < this.minKey) {
			this.minKey = listNum;
		}
		if (varLists.containsKey(listNum)) {
			ArrayList<Variable> varList = this.varLists.get(listNum);
			varList.add(new Variable(oldVariable));
		} else {
			ArrayList<Variable> varList = new ArrayList<Variable>();
			varList.add(new Variable(oldVariable));
			this.varLists.put(listNum, varList);
		}
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Failure: " + this.failure + "\n");
		
		for (Integer listNum : varLists.keySet()) {
			str.append("Variable list " + listNum + ":\n");
			
			for (Variable var : varLists.get(listNum)) {
				str.append("\t");
				str.append(var.toString());
				str.append("\n");
			}
			
			str.append("\n");
		}
		
		LinkedList<Constraint> constraints = getConstraints();
		
		str.append("Constraints: \n");
		
		for (Constraint constraint : constraints) {
			str.append(constraint.toString() + "\n");
		}
		
		return str.toString();
	}
	
}
