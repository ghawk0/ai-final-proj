
public class CSPDivs extends CSP {
	
	public final int maxDivisions, numPlayers;
	
	public CSPDivs(int maxDivisions, int numPlayers) {
		super();
		this.maxDivisions = maxDivisions;
		this.numPlayers = numPlayers;
	}
	
	public CSPDivs(CSPDivs oldCSP, int listToInsert, int newIndex, int val) {
		super(oldCSP, listToInsert, newIndex, val);
		this.maxDivisions = oldCSP.maxDivisions;
		this.numPlayers = oldCSP.numPlayers;
	}

	public CSPDivs backtrack() {
		Boolean complete = true;
		int sum = 0;
		
		for (int i = 0; i < this.maxDivisions; i++) {
			sum += this.varLists.get(1).get(i).val * this.varLists.get(2).get(i).val;
		}
		
		if (sum > this.numPlayers) {
			this.failure = true;
			return this;
		}
		
		for (Integer numList : varLists.keySet()) {
			for (Variable var : varLists.get(numList)) {
				if (!(var.assigned)) {
					complete = false;
				}
			}
		}
		
		if (complete) {
			this.failure = (sum != this.numPlayers);
			return this;
		}
		
		Variable var = selectVar();
		
		for (Integer val : var.domain) {
			Boolean consistent = true;
			
			if (var.listNum == 2) {
				for (Variable pair : var.pairList) {
					//System.out.println(pair.val);
					if (val == pair.val) {
						consistent = false;
						System.out.println("The values were not consistent: " + val + " " + pair.val);
					}
				}
			}
			
			if (consistent) {
				CSPDivs newCSP = new CSPDivs(this, var.listNum, var.index, val);
				
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
