import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class AssignFieldsApprox {

	public static void main(String[] args) {
		
		String inputFile = null;
		int error = 0;
		
		try {
			inputFile = args[0];
			error = Integer.parseInt(args[1]);
		} catch (Exception ex) {
			System.out.println("Please specify an input file and error number. ");
			System.exit(-1);
		}
		
		// Domain for variables representing number of teams on a given field.
		ArrayList<Integer> domainOne = new ArrayList<Integer>();
		
		// Domain for variables representing division assigned to a given field. 
		ArrayList<Integer> domainTwo = new ArrayList<Integer>();
		
		// teamsPerDivision[i] = number of teams on division i. 
		Integer[] teamsPerDivision = null;
		
		int numFields = 0;
		int numDivs = 0;
		int numTeams = 0;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			
			numFields = Integer.parseInt(reader.readLine());
			numDivs = Integer.parseInt(reader.readLine());
			numTeams = Integer.parseInt(reader.readLine());
			
			teamsPerDivision = new Integer[numDivs];
			int i = 0;
			
			while (reader.ready()) {
				teamsPerDivision[i] = Integer.parseInt(reader.readLine());
				i++;
			}
			
			reader.close();
			
		} catch (Exception ex) {
			System.err.println("Problem reading from file!");
			ex.printStackTrace();
			System.exit(-1);
		}
		
		// The maximum number of teams on a given division. 
		int maxTeamNum = 0;
		
		for (int i = 0; i < teamsPerDivision.length; i++) {
			if (teamsPerDivision[i] >= maxTeamNum) {
				maxTeamNum = teamsPerDivision[i];
			}
			
		}
		
		for (int i = 0 ; i <= maxTeamNum;  i++) {
			domainOne.add(i);
		}
		
		for (int i = 1 ; i <= teamsPerDivision.length; i++) {
			domainTwo.add(i);
		}
		
		// Create new CSPFieldsApprox instance. 
		CSPFieldsApprox problem = new CSPFieldsApprox(error, numFields, numTeams, teamsPerDivision);
		
		// Add numFields variables to both lists of CSP. 
		for (int i = 0; i < numFields; i++) {
			problem.addVariable(domainOne, 1);
			problem.addVariable(domainTwo, 2);
		}

		for (int i = 0; i < numFields; i++) {
			problem.addNeighbor(1, i, 2, i);
			problem.addNeighbor(2, i, 1, i);
		}
		
		// Create ArrayList of solutions to CSP. 
		PriorityQueue<CSPFieldsApprox> solutions = new PriorityQueue<CSPFieldsApprox>(new CSPComparator());
		problem.backtrack(solutions);
		
		// If there is no solution, exit. 
		if (solutions.isEmpty()) {
			System.out.println("There is no solution to the given instance. ");
			System.exit(-1);
		}
		
		CSPFieldsApprox solution = solutions.poll();
		
		ArrayList<Variable> numTeamsPerField = solution.varLists.get(1);
		ArrayList<Variable> divPerField = solution.varLists.get(2);
		
		System.out.println("Division to Field assignments: ");
		
		int fieldNum = 0;
		
		for (Variable var : divPerField) {
			fieldNum = var.index + 1;
			System.out.println("\tField " + fieldNum + ": " + var.val);
		}
		
		System.out.println("Number of teams per field: ");
		
		for (Variable var : numTeamsPerField) {
			fieldNum = var.index + 1;
			System.out.println("\tField " + fieldNum + ": " + var.val);
		}
		
	}

}
