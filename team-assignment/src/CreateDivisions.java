import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class CreateDivisions {

	public static void main(String[] args) {
		
		String inputFile = null;
		
		try {
			inputFile = args[0];
		} catch (Exception ex) {
			System.out.println("Please specify an input file.");
			System.exit(-1);
		}
		
		// Domain for variables representing number of teams on a given division. 
		ArrayList<Integer> domainOne = new ArrayList<Integer>();
		
		// Domain for variables representing team size on a given division. 
		ArrayList<Integer> domainTwo = new ArrayList<Integer>();
		
		int numPlayers = 0; 	// Number of players in tournament. 
		int maxDivisions = 0;	// Maximum allowable number of divisions. 
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			
			numPlayers = Integer.parseInt(reader.readLine());
			maxDivisions = Integer.parseInt(reader.readLine());
			
			while (reader.ready()) {
				domainTwo.add(Integer.parseInt(reader.readLine()));	// Add permissible team sizes to domain two. 
			}
			
			reader.close();
			
		} catch (Exception ex) {
			System.err.println("Problem reading from file!");
			ex.printStackTrace();
			System.exit(-1);
		}
		
		for (int i = 0; i <= numPlayers; i++) {
			domainOne.add(i);
		}
		
		CSPDivs solution = new CSPDivs(0, 0);
		int numDivs = 1;
		Boolean solved = false;
		
		// Iterate through increasing numbers of divisions while no solution is found. s
		while (numDivs <= maxDivisions && !(solved)) {
			CSPDivs problem = new CSPDivs(numDivs, numPlayers);
			
			// Add numDivs variables to both lists of variables in the CSP
			for (int i = 0; i < numDivs; i++) {
				problem.addVariable(domainOne, 1);
				problem.addVariable(domainTwo, 2);
			}
			
			// Add constraints between all variables in the second list of variables.
			for (int indX = 0; indX < numDivs; indX++) {
				for (int indY = 0; indY < numDivs; indY++) {
					if (indX != indY) {
						problem.addNeighbor(2, indX, 2, indY);
					}
				}
			}
			
			solution = problem.backtrack();
			solved = !solution.failure;
			numDivs++;
		}
		
		ArrayList<Variable> teamNumbers = solution.varLists.get(1);
		ArrayList<Variable> divTeamSizes = solution.varLists.get(2);

		// Print out solution in readable format. 
		
		System.out.println("Number of divisions needed: " + solution.maxDivisions);
		System.out.println("Number of teams per division: ");
		
		for (Variable var : teamNumbers) {
			int divNum = var.index + 1;
			System.out.println("\tDivision " + divNum + ": " + var.val);
		}
		
		System.out.println("Team size per division: ");
		
		for (Variable var : divTeamSizes) {
			int divNum = var.index + 1;
			System.out.println("\tDivision " + divNum + ": " + var.val);
		}
		
	}
	

}
