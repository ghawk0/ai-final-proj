import java.util.ArrayList;

public class CSPFields extends CSP {

	public final int numFields, numTeams, numDivs;
	
	/** Array where index i denotes the number of teams on division i + 1 */
	public Integer[] teamsPerDiv;
	
	/**
	 * Construct a new CSPFields instance. 
	 * @param numFields : The number of available fields. 
	 * @param numTeams  : The number of teams. 
	 * @param teamsPerDiv : Array of number of teams per division. 
	 */
	public CSPFields(int numFields, int numTeams, Integer[] teamsPerDiv) {
		super();
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
	public CSPFields(CSPFields oldCSP, int listToInsert, int newIndex, int val) {
		super(oldCSP, listToInsert, newIndex, val);
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
	public void backtrack(ArrayList<CSPFields> solutions) {
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
				CSPFields newCSP = new CSPFields(this, var.listNum, var.index, val);
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
