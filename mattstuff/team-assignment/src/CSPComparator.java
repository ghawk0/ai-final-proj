import java.util.Comparator;

public class CSPComparator implements Comparator<CSP> {
	
	public int compare(CSP c1, CSP c2) {
		return c1.getCost() - c2.getCost();
	}
	
}
