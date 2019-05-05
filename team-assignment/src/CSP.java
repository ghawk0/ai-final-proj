import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public abstract class CSP {

	
	public int minKey;
	
	public HashMap<Integer, ArrayList<Variable>> varLists;
	
	public Boolean failure;
	
	public CSP() {
		this.varLists = new HashMap<Integer, ArrayList<Variable>>();
		this.minKey = Integer.MAX_VALUE;
		this.failure = false;
	}
	
	public CSP(CSP oldCSP, int listToInsert, int newIndex, int val) {
		this.varLists = new HashMap<Integer, ArrayList<Variable>>();
		this.minKey = oldCSP.minKey;
		this.failure = false;

		
		for (Integer listNum : oldCSP.varLists.keySet()) {
			ArrayList<Variable> oldList = oldCSP.varLists.get(listNum);
			
			for (int i = 0; i < oldList.size(); i++) {
				Variable oldVar = oldList.get(i);
				
				if (listToInsert == listNum && i == newIndex) {
					ArrayList<Integer> domain = new ArrayList<Integer>();
					domain.add(val);
					this.addVariable(domain, listNum);
	
				} else {
					this.addVariable(oldVar, listNum);
				}
			}
		}
		
		for (Integer listNum : oldCSP.varLists.keySet()) {
			for (Variable var : oldCSP.varLists.get(listNum)) {
				for (Variable pair : var.pairList) {
					this.addNeighbor(var.listNum, var.index, pair.listNum, pair.index);
				}
			}
		}
		
	}
	
	public Boolean ac3() {
		LinkedList<Constraint> constraints = this.getConstraints();
		Constraint nextConstraint;
		
		while (!constraints.isEmpty()) {
			nextConstraint = constraints.poll();
			
			if (revise(nextConstraint.var1, nextConstraint.var2)) {
				if (nextConstraint.var1.domainSize() == 0) {
					return false;
				}
				
				for (Variable pair : nextConstraint.var1.pairList) {
					if (!(pair == nextConstraint.var2)) {
						constraints.offer(new Constraint(pair, nextConstraint.var1));
					}
				}
			}
			
		}
		
		return true;
	}
	
	public Boolean revise(Variable x1, Variable x2) {
		Boolean revised = false;
		
		ArrayList<Integer> toRemove = new ArrayList<Integer>();
		
		for (int i = 0; i < x1.domainSize(); i++) {
			Boolean satisfied = false;
			
			for (int j = 0; j < x2.domainSize(); j++) {
				if (x1.domain.get(i) != x2.domain.get(j)) {
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
	
	public abstract int getCost();
	
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
	
	public void addNeighbor(int listNumX, int indX, int listNumY, int indY) {
		Variable var1 = varLists.get(listNumX).get(indX);
		var1.pairList.add(varLists.get(listNumY).get(indY));
	}
	
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
