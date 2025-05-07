package cvrp;

import java.io.*;
import java.util.*;
import cvrp.Heuristics.GreedyHeuristic;

public class SimpleCVRPHeuristic {

    static int capacity;
    static Map<Integer, Node> nodes = new HashMap<>();
    static List<Integer> customerIds = new ArrayList<>();
    static int depotId = 1;

    public static void main(String[] args) throws IOException {
        VRPUtils.parseVRPFile("C:\\Users\\tomju\\OneDrive\\Desktop\\uni\\ae\\input.vrp", nodes, customerIds, depotId);
        Heuristic heuristic = new GreedyHeuristic();
        List<List<Integer>> routes = heuristic.calculateRoutes(nodes, customerIds, depotId, capacity);
        printRoutes(routes);
        double totalDistance = calculateTotalDistance(routes);
        System.out.printf("Total distance: %.2f\n", totalDistance);
    }

    static double calculateTotalDistance(List<List<Integer>> routes) {
        double total = 0;
        for (List<Integer> route : routes) {
            for (int i = 0; i < route.size() - 1; i++) {
                Node a = nodes.get(route.get(i));
                Node b = nodes.get(route.get(i + 1));
                total += a.distanceTo(b);
            }
        }
        return total;
    }

    static void printRoutes(List<List<Integer>> routes) {
        int routeNum = 1;
        for (List<Integer> route : routes) {
            System.out.print("Route " + routeNum++ + ": ");
            for (int id : route)
                System.out.print(id + " ");
            System.out.println();
        }
    }
}