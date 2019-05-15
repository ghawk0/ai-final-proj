import java.util.ArrayList;
import java.util.PriorityQueue;

public class CSPFields extends CSP {

	public final int numFields, numTeams, numDivs;
	
	public Integer[] teamsPerDiv;
	
	public CSPFields(int numFields, int numTeams, Integer[] teamsPerDiv) {
		super();
		this.numFields = numFields;
		this.numTeams = numTeams;
		this.numDivs = teamsPerDiv.length;
		this.teamsPerDiv = teamsPerDiv;
	}
	
	public CSPFields(CSPFields oldCSP, int listToInsert, int newIndex, int val) {
		super(oldCSP, listToInsert, newIndex, val);
		this.numFields = oldCSP.numFields;
		this.numTeams = oldCSP.numTeams;
		this.numDivs = oldCSP.numDivs;
		this.teamsPerDiv = oldCSP.teamsPerDiv;
	}
	
	public void backtrack(PriorityQueue<CSPFields> solutions) {
		Boolean complete = true;
		
		ArrayList<Variable>  teamAssignments = this.varLists.get(1);
		ArrayList<Variable> divAssignments = this.varLists.get(2);
		int[] divTotals = new int[numDivs];
		
		for (int i = 0; i < numFields; i++) {
			if (divAssignments.get(i).assigned) {
				divTotals[divAssignments.get(i).val - 1] += teamAssignments.get(i).val;
			}
		}
		
		for (int i = 0; i < numDivs; i++) {
			if (divTotals[i] > this.teamsPerDiv[i]) {
				this.failure = true;
				return;
			}
		}
		
		for (Integer numList : varLists.keySet()) {
			for (Variable var : varLists.get(numList)) {
				if (!(var.assigned)) {
					complete = false;
				}
			}
		}
		
		if (complete) {
			for (int i = 0; i < numDivs; i++) {
				if (divTotals[i] != this.teamsPerDiv[i]) {
					this.failure = true;
					return;
				}
			}
			
			this.failure = false;
			if (!(this.failure)) {
				solutions.offer(this);
				return;
			}
		}
		
		Variable var = selectVar();
		
		for (Integer val : var.domain) {
			Boolean consistent = true;
			
			if (consistent) {
				CSPFields newCSP = new CSPFields(this, var.listNum, var.index, val);
				
				newCSP.backtrack(solutions);
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
