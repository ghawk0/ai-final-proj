import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class AssignFields {

	public static void main(String[] args) {
		
		String inputFile = args[0];
		
		ArrayList<Integer> domainOne = new ArrayList<Integer>();
		ArrayList<Integer> domainTwo = new ArrayList<Integer>();
		//ArrayList<Integer> teamsPerDivision = new ArrayList<Integer>();
		
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
			
		} catch (Exception ex) {
			System.err.println("Problem reading from file!");
			ex.printStackTrace();
			System.exit(-1);
		}
		
		for (int i = 0 ; i <= numTeams;  i++) {
			domainOne.add(i);
		}
		
		for (int i = 1 ; i <= teamsPerDivision.length; i++) {
			domainTwo.add(i);
		}
		
		CSPFields problem = new CSPFields(numFields, numTeams, teamsPerDivision);
		
		
		for (int i = 0; i < numFields; i++) {
			problem.addVariable(domainOne, 1);
			problem.addVariable(domainTwo, 2);
		}
		
		//System.out.println(problem);
		
		PriorityQueue<CSPFields> solutions = new PriorityQueue<CSPFields>(new CSPComparator());
		
		
		problem.backtrack(solutions);
		
		CSPFields solution = solutions.poll();
		
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
