package cvrp.Heuristics;

import java.util.*;

import cvrp.Heuristic;
import cvrp.Node;

public class RandomHeuristic implements Heuristic {

    @Override
    public List<List<Integer>> calculateRoutes(Map<Integer, Node> nodes, List<Integer> customerIds, int depotId,
            int capacity) {
        List<List<Integer>> routes = new ArrayList<>();
        List<Integer> shuffledCustomers = new ArrayList<>(customerIds);
        Random random = new Random();
        Collections.shuffle(shuffledCustomers, random);

        while (!shuffledCustomers.isEmpty()) {
            List<Integer> route = new ArrayList<>();
            route.add(depotId);
            int remainingCap = capacity;

            Iterator<Integer> iterator = shuffledCustomers.iterator();
            while (iterator.hasNext()) {
                int cid = iterator.next();
                Node candidate = nodes.get(cid);
                if (candidate.getDemand() <= remainingCap) {
                    route.add(cid);
                    remainingCap -= candidate.getDemand();
                    iterator.remove();
                }
            }

            route.add(depotId);
            routes.add(route);
        }

        return routes;
    }
}