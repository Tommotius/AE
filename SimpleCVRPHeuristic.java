import java.io.*;
import java.util.*;

public class SimpleCVRPHeuristic {

    static class Node {
        int id;
        double x, y;
        int demand;

        Node(int id, double x, double y, int demand) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.demand = demand;
        }

        double distanceTo(Node other) {
            return Math.hypot(this.x - other.x, this.y - other.y);
        }
    }

    static int capacity;
    static Map<Integer, Node> nodes = new HashMap<>();
    static List<Integer> customerIds = new ArrayList<>();
    static int depotId = 1;

    public static void main(String[] args) throws IOException {
        parseVRPFile("C:\\Users\\tomju\\OneDrive\\Desktop\\uni\\ae\\input.vrp");

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
                    if (candidate.demand <= remainingCap) {
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
                remainingCap -= nodes.get(nextId).demand;
                unvisited.remove(nextId);
                currentId = nextId;
            }

            route.add(depotId);
            routes.add(route);
        }

        printRoutes(routes);
        double totalDistance = calculateTotalDistance(routes);
        System.out.printf("Total distance: %.2f\n", totalDistance);
    }

    static void parseVRPFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        boolean inCoordSection = false, inDemandSection = false;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("CAPACITY")) {
                capacity = Integer.parseInt(line.split(":")[1].trim());
            } else if (line.equals("NODE_COORD_SECTION")) {
                inCoordSection = true;
                inDemandSection = false;
            } else if (line.equals("DEMAND_SECTION")) {
                inCoordSection = false;
                inDemandSection = true;
            } else if (line.equals("DEPOT_SECTION")) {
                break;
            } else if (inCoordSection && !line.isEmpty()) {
                String[] parts = line.split("\\s+");
                int id = Integer.parseInt(parts[0]);
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                nodes.put(id, new Node(id, x, y, 0)); // demand to be filled later
            } else if (inDemandSection && !line.isEmpty()) {
                String[] parts = line.split("\\s+");
                int id = Integer.parseInt(parts[0]);
                int demand = Integer.parseInt(parts[1]);
                nodes.get(id).demand = demand;
                if (id != depotId)
                    customerIds.add(id);
            }
        }

        reader.close();
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