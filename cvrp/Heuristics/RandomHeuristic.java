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

        System.out.println("Shuffled Customers: " + shuffledCustomers);
        Set<Integer> unvisited = new HashSet<>(shuffledCustomers);

        while (!unvisited.isEmpty()) {
            List<Integer> route = new ArrayList<>();
            route.add(depotId);
            int currentId = depotId;
            int remainingCap = capacity;

            Iterator<Integer> iterator = unvisited.iterator();
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