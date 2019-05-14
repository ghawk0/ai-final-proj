import java.util.Comparator;

/**
 * The {@link CSPComparator} implements the Comparator interface to 
 * compare two CSP's. For use in minimizing CSP solutions according
 * to their cost functions. 
 * 
 * @author Dominic Bosco
 *
 */
public class CSPComparator implements Comparator<CSP> {
	
	public int compare(CSP c1, CSP c2) {
		return c1.getCost() - c2.getCost();
	}
	
}
