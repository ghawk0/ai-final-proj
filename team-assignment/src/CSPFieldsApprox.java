import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * 
 * @author Dominic Bosco
 *
 */
public class CSPFieldsApprox extends CSP {

	public final int error, numFields, numTeams, numDivs;
	
	/** Array where index i denotes the number of teams on division i + 1 */
	public Integer[] teamsPerDiv;
	
	/**
	 * Construct a new CSPFields instance. 
	 * @param error : Permissible error (difference of number of teams on a field
	 * to desired target of numTeams / numFields). 
	 * @param numFields : The number of available fields. 
	 * @param numTeams  : The number of teams. 
	 * @param teamsPerDiv : Array of number of teams per division. 
	 */
	public CSPFieldsApprox(int error, int numFields, int numTeams, Integer[] teamsPerDiv) {
		super();
		this.error = error;
		this.numFields = numFields;
		this.numTeams = numTeams;
		this.numDivs = teamsPerDiv.length;
		this.teamsPerDiv = teamsPerDiv;
	}
	
	/**
	 * Construct a new CSPFields instance by copying an existing CSPFields. 
	 * Overwrites inherited constructor. 
	 *
	 */
	public CSPFieldsApprox(CSPFieldsApprox oldCSP, int listToInsert, int newIndex, int val) {
		super(oldCSP, listToInsert, newIndex, val);
		this.error = oldCSP.error;
		this.numFields = oldCSP.numFields;
		this.numTeams = oldCSP.numTeams;
		this.numDivs = oldCSP.numDivs;
		this.teamsPerDiv = oldCSP.teamsPerDiv;
	}
	
	
	public Boolean revise(Variable x1, Variable x2) {
		Boolean revised = false;
		
		ArrayList<Integer> toRemove = new ArrayList<Integer>();
		
		for (int i = 0; i < x1.domainSize(); i++) {
			Boolean satisfied = false;
			
			for (int j = 0; j < x2.domainSize(); j++) {
				if (x1.listNum == 1 && (x1.domain.get(i) <= this.teamsPerDiv[x2.domain.get(j) - 1])) {
					satisfied = true;
				} else if (x1.listNum == 2 && (this.teamsPerDiv[x1.domain.get(i) - 1] >= x2.domain.get(j))) {
					satisfied = true;
				}
			}
			
			if (!satisfied) {
				toRemove.add(x1.domain.get(i));
				revised = true;
			}
		}
		
		for (Integer x : toRemove) {
			x1.remove(x);
		}
		
		return revised;
	}
	
	/**
	 * Implementation of backtracking algorithm specific to constraints of the CSPFields problem
	 * 
	 * @param solutions: ArrayList to store solutions found. 
	 */
	//
	public void backtrack(PriorityQueue<CSPFieldsApprox> solutions) {
		// Check whether best solution found so far is a good enough approximation. 
		if (!solutions.isEmpty()) {
			int target = this.numTeams / this.numFields;
			Boolean approxSatisfied = true;
			
			// Check whether each team number in best solution is within error of target. 
			for (Variable var : solutions.peek().varLists.get(1)) {
				if (Math.abs(var.val - target) > this.error) {
					approxSatisfied = false;
				}
			}
			
			// If it is an approximate solution, return. 
			if (approxSatisfied) {
				return;
			}
		}
		
		Boolean complete = true;
		
		ArrayList<Variable>  teamAssignments = this.varLists.get(1);
		ArrayList<Variable> divAssignments = this.varLists.get(2);
		int[] divTotals = new int[numDivs];
		
		// Count the number of teams assigned to each division. 
		for (int i = 0; i < numFields; i++) {
			if (divAssignments.get(i).assigned) {
				divTotals[divAssignments.get(i).val - 1] += teamAssignments.get(i).val;
			}
		}
		
		// If one division assigned more teams than on the division, this
		// is not a valid assignment. Return failure. 
		for (int i = 0; i < numDivs; i++) {
			if (divTotals[i] > this.teamsPerDiv[i]) {
				this.failure = true;
				return;
			}
		}
		
		// Check if complete. 
		for (Integer numList : varLists.keySet()) {
			for (Variable var : varLists.get(numList)) {
				if (!(var.assigned)) {
					complete = false;
				}
			}
		}
		
		// If complete, check that we have the right number of teams per division. 
		if (complete) {
			for (int i = 0; i < numDivs; i++) {
				if (divTotals[i] != this.teamsPerDiv[i]) {
					this.failure = true;
					return;
				}
			}
			
			this.failure = false;
			// Add to list of solutions if it is a proper solution. 
			if (!(this.failure)) {
				solutions.add(this);
				return;
			}
		}
		
		Variable var = selectVar();
		
		for (Integer val : var.domain) {
			
			Boolean consistent = true;
			
			for (Variable pair : var.pairList) {
				if (pair.assigned) {
					if (var.listNum == 1 && (val > this.teamsPerDiv[pair.val - 1])) {
						consistent = false;
					} else if (var.listNum == 2 && (this.teamsPerDiv[val - 1] < pair.val)) {
						consistent = false;
					}
				}
			}
			
			if (consistent) {
				CSPFieldsApprox newCSP = new CSPFieldsApprox(this, var.listNum, var.index, val);
				if (newCSP.ac3()) {
					newCSP.backtrack(solutions);
				}
			}
		}
	}
	
	
	public int getCost() {
		int cost = 0;
		
		int target = numTeams / numFields;
		
		for (Variable field : this.varLists.get(1)) {
			cost += (Math.abs(target - field.val));
		}
		
		return cost;
	}
 
}
