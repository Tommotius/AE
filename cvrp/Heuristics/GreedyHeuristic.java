package cvrp.Heuristics;

import java.util.*;

import cvrp.Heuristic;
import cvrp.Node;

public class GreedyHeuristic implements Heuristic {

    @Override
    public List<List<Integer>> calculateRoutes(Map<Integer, Node> nodes, List<Integer> customerIds, int depotId,
            int capacity) {
        Set<Integer> unvisited = new HashSet<>(customerIds);
        List<List<Integer>> routes = new ArrayList<>();

        while (!unvisited.isEmpty()) {
            List<Integer> route = new ArrayList<>();
            route.add(depotId);
            int currentId = depotId;
            int remainingCap = capacity;

            while (true) {
                Integer nextId = null;
                double minDist = Double.MAX_VALUE;

                for (int cid : unvisited) {
                    Node current = nodes.get(currentId);
                    Node candidate = nodes.get(cid);
                    if (candidate.getDemand() <= remainingCap) {
                        double dist = current.distanceTo(candidate);
                        if (dist < minDist) {
                            minDist = dist;
                            nextId = cid;
                        }
                    }
                }

                if (nextId == null)
                    break;

                route.add(nextId);
                remainingCap -= nodes.get(nextId).getDemand();
                unvisited.remove(nextId);
                currentId = nextId;
            }

            route.add(depotId);
            routes.add(route);
        }

        return routes;
    }
}