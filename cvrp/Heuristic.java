package cvrp;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Heuristic {
    List<List<Integer>> calculateRoutes(Map<Integer, Node> nodes, List<Integer> customerIds, int depotId, int capacity);
}