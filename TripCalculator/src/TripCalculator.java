import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.lang.Math;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TripCalculator {
	static Map<String,ArrayList<Pair<String, Double>>> road_graph;
    static Map<String, String> road_number;
    
    public TripCalculator() throws FileNotFoundException, IOException, ParseException{
        JSONObject json_object = (JSONObject) new JSONParser().parse(new FileReader("file/interchanges.json"));
        road_graph = new HashMap<String,ArrayList<Pair<String, Double>>>();
        road_number = new HashMap<String, String>();

        JSONObject locations = (JSONObject) json_object.get("locations");
        for(Iterator itr = locations.keySet().iterator(); itr.hasNext();){
            String location = (String) itr.next();
            JSONObject road = (JSONObject) locations.get(location);
            String road_name = (String) road.get("name");
            JSONArray routes = (JSONArray) road.get("routes");
            road_number.put(road_name, location);
            road_graph.put(location, new ArrayList<>());
            for (int i=0;i<routes.size();i++){
                JSONObject route = (JSONObject) routes.get(i);
                String dest = String.valueOf(route.get("toId"));
                Object dist_obj = route.get("distance");
                double dist;
                if (dist_obj instanceof Double) {
                	dist = (double) route.get("distance");
                } else if (dist_obj instanceof Long) {
                	Long l = new Long((long) dist_obj);
                	dist = l.doubleValue();
                } else {
                	dist = 0;
                }
                road_graph.get(location).add(new Pair<String, Double>(dest, dist));
            }
        }
    }

    public double bfs(String src, String dest, double total_dist, HashSet<String> visited){
        if (src.equals(dest)){
            return total_dist;
        }
        visited.add(src);
        ArrayList<Pair<String, Double>> neighbors = (ArrayList<Pair<String, Double>>) road_graph.get(src);
        for(int i=0;neighbors != null && i<neighbors.size();i++){
            Pair<String, Double> dest_dist = (Pair<String, Double>) neighbors.get(i);
            String next_dest = dest_dist.getKey();
            Double next_dist = dest_dist.getValue();
            if (!visited.contains(next_dest)){
                double final_dist = bfs(next_dest, dest, total_dist+next_dist, visited);
                if (final_dist != -1){
                    return final_dist;
                }
            }
        }
        return (double) -1;
    }

    public Double costOfTrip(String source, String dest){
    	if (source.length() == 0 || dest.length() == 0) return (double) -1;
    	if (!road_number.containsKey(source) || !road_number.containsKey(dest)) {
    		return (double) -1;
    	}
        String src_location = road_number.get(source);
        String dest_location = road_number.get(dest);
        double distance = bfs(src_location, dest_location, 0, new HashSet<String>());
        return (double) Math.round(distance*1000)/1000;
    }
    
    public static void main(String args[]) throws FileNotFoundException, IOException, ParseException {
    	TripCalculator tripCalculator = new TripCalculator();
    	double distance = tripCalculator.costOfTrip("QEW", "Highway 400");
    	if (distance != -1) {
    		System.out.println("Distance: " + distance);
    		System.out.println("Cost: " + distance*0.25);
    	} else {
    		System.out.println("Cannot calculate distance");
    	}
    }
}
