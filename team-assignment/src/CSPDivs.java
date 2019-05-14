import java.util.ArrayList;

/**
 * The {@link CSPDivs} class extends the CSP class to represent a CSP
 * specifically for solving the problem of assigning team sizes to divisions
 * and assigning numbers of teams to divisions. 
 * 
 * @author Dominic Bosco
 *
 */
public class CSPDivs extends CSP {
	
	/** Constants representing the number of allowable divisions and number of players. */
	public final int maxDivisions, numPlayers;
	
	/**
	 * Construct CSPDivs. 
	 * 
	 * @param maxDivisions : number of allowable divisions. 
	 * @param numPlayers   : number of players. s
	 */
	public CSPDivs(int maxDivisions, int numPlayers) {
		super();
		this.maxDivisions = maxDivisions;
		this.numPlayers = numPlayers;
	}
	
	/**
	 * Construct a CSPDivs by copying an existing CSPDivs. 
	 *
	 * Overwrites inherited Constructor. 
	 */
	public CSPDivs(CSPDivs oldCSP, int listToInsert, int newIndex, int val) {
		super(oldCSP, listToInsert, newIndex, val);
		this.maxDivisions = oldCSP.maxDivisions;
		this.numPlayers = oldCSP.numPlayers;
	}


	public Boolean revise(Variable x1, Variable x2) {
		Boolean revised = false;
		// List of integers to remove from the domain of x1. 
		ArrayList<Integer> toRemove = new ArrayList<Integer>();
		
		for (int i = 0; i < x1.domainSize(); i++) {
			Boolean satisfied = false;
			
			for (int j = 0; j < x2.domainSize(); j++) {
				// If some value in x2's domain is not equal to current value in x1's domain,
				// the constraint is satisfied. 
				if (x1.domain.get(i) != x2.domain.get(j)) {
					satisfied = true;
				}
			}
			
			// If the constraint is not satisfied, remove the current value and set revised to true. 
			if (!satisfied) {
				toRemove.add(x1.domain.get(i));
				revised = true;
			}
		}
		
		// Remove values from domain of x1 that do not satisfy the constraint. 
		for (Integer x : toRemove) {
			x1.remove(x);
		}
		
		return revised;
	}	
	
	/**
	 * Implementation of backtracking algorithm specific to constraints of the 
	 * CSPDivs problem. 
	 * 
	 * @return A CSP, which represents a solution to the problem if CSP.failure = false 
	 */
	public CSPDivs backtrack() {
		Boolean complete = true;
		int sum = 0;
		
		// Obtain sum of total players accounted for. 
		for (int i = 0; i < this.maxDivisions; i++) {
			sum += this.varLists.get(1).get(i).val * this.varLists.get(2).get(i).val;
		}
		
		// If the number of players in assignment is greater than desired number of players,
		// return with failure set to true. 
		if (sum > this.numPlayers) {
			this.failure = true;
			return this;
		}
		
		// Check whether the assignment is complete. 
		for (Integer numList : varLists.keySet()) {
			for (Variable var : varLists.get(numList)) {
				if (!(var.assigned)) {
					complete = false;
				}
			}
		}
		
		// If it's complete, return current CSP with failure set 
		// according to whether there are the right number of players. 
		if (complete) {
			this.failure = (sum != this.numPlayers);
			return this;
		}
		
		Variable var = selectVar();
		
		for (Integer val : var.domain) {
			Boolean consistent = true;
			
			// If variable comes from div assignment list, make sure that the 
			// value is not equal to the value of its neighbors. 
			if (var.listNum == 2) {
				for (Variable pair : var.pairList) {
					if (val == pair.val) {
						consistent = false;
					}
				}
			}
			
			if (consistent) {
				// Copy the current CSP, assigning val to selected var. 
				CSPDivs newCSP = new CSPDivs(this, var.listNum, var.index, val);
				
				// If current assignment is valid, backtrack on newCSP. 
				if (newCSP.ac3()) {
					CSPDivs result = newCSP.backtrack();
					if (!(result.failure)) {
						return result;
					}
				}
			}
		}

		this.failure = true;
		return this;
	}
	
	
	public int getCost() {
		return 0;
	}
	
	
	
}
