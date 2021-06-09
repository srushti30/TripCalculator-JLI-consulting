import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import org.json.simple.parser.ParseException;

public class TestTripCalculator {
	public static void main(String args[]) throws FileNotFoundException, IOException, ParseException {
		TripCalculator tripCalculator = new TripCalculator();
		test(tripCalculator);
	}
	
	public static void test(TripCalculator tripCalculator) {
		Map<Pair<String, String>, Double> test_cases = new HashMap<Pair<String, String>, Double>();
		test_cases.put(new Pair<String, String>("", ""), (double) -1);
		test_cases.put(new Pair<String, String>("asdasd", "qweqweqw"), (double) -1);
		test_cases.put(new Pair<String, String>("QEW", "Highway 400"), 67.748);
		test_cases.put(new Pair<String, String>("Weston Road", "Bathurst Street"), 9.787);
		test_cases.put(new Pair<String, String>("Salem Road", "QEW"), 107.964);
		test_cases.put(new Pair<String, String>("QEW", "Salem Road"), 115.277);
		test_cases.put(new Pair<String, String>("Bayview Avenue", "Leslie Street"), 2.076);
		for(Map.Entry<Pair<String, String>, Double> test_case : test_cases.entrySet()) {
			Pair<String, String> params = test_case.getKey();
			double dist = tripCalculator.costOfTrip(params.getKey(), params.getValue());
			if (dist != test_case.getValue()) {
				System.out.println("Testcase with params " + params.getKey() + " to " + params.getValue() + " failed" );
				return;
			}
		}
		System.out.println("All testcases passed");
	}
}

