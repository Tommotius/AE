package cvrp;

import java.io.*;
import java.util.*;
import cvrp.Heuristics.GreedyHeuristic;
import cvrp.Heuristics.RandomHeuristic;

public class CVRP {

    private static int capacity;
    private static Map<Integer, Node> nodes = new HashMap<>();
    private static List<Integer> customerIds = new ArrayList<>();
    private static int depotId = 1;

    public static void main(String[] args) throws IOException {
        VRPUtils.parseVRPFile("C:\\Users\\tomju\\OneDrive\\Desktop\\uni\\ae\\input.vrp", nodes, customerIds, depotId);
        Heuristic heuristic = new RandomHeuristic();
        List<List<Integer>> routes = heuristic.calculateRoutes(nodes, customerIds, depotId, capacity);
        printRoutes(routes);
        double totalDistance = calculateTotalDistance(routes);
        System.out.printf("Total distance: %.2f\n", totalDistance);
    }

    public static int getCapacity() {
        return capacity;
    }

    public static void setCapacity(int capacity) {
        CVRP.capacity = capacity;
    }

    public static Map<Integer, Node> getNodes() {
        return nodes;
    }

    public static void setNodes(Map<Integer, Node> nodes) {
        CVRP.nodes = nodes;
    }

    public static List<Integer> getCustomerIds() {
        return customerIds;
    }

    public static void setCustomerIds(List<Integer> customerIds) {
        CVRP.customerIds = customerIds;
    }

    public static int getDepotId() {
        return depotId;
    }

    public static void setDepotId(int depotId) {
        CVRP.depotId = depotId;
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