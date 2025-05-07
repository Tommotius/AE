package cvrp;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import cvrp.Heuristic;

public class CVRP {

    private static int capacity;
    private static Map<Integer, Node> nodes = new HashMap<>();
    private static List<Integer> customerIds = new ArrayList<>();
    private static int depotId = 1;

    public static void main(String[] args) throws IOException {
        // Easily changeable test folder path
        String testFolderPath = "c:\\Users\\tomju\\OneDrive\\Desktop\\uni\\AE\\test_instances";

        File testFolder = new File(testFolderPath);

        if (!testFolder.exists() || !testFolder.isDirectory()) {
            System.out.println("Invalid test folder path: " + testFolderPath);
            return;
        }

        File[] testFiles = testFolder.listFiles((dir, name) -> name.endsWith(".vrp"));
        if (testFiles == null || testFiles.length == 0) {
            System.out.println("No test instances found in folder: " + testFolderPath);
            return;
        }

        // Create results folder if it doesn't exist
        String resultsFolderPath = "c:\\Users\\tomju\\OneDrive\\Desktop\\uni\\AE\\results";
        File resultsFolder = new File(resultsFolderPath);
        if (!resultsFolder.exists()) {
            resultsFolder.mkdirs();
        }

        // Generate a timestamp for the results file
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File csvFile = new File(resultsFolder, "results_" + timestamp + ".csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            writer.write("Instance,Heuristic,TotalDistance,Routes\n");

            // Dynamically load all heuristics in the cvrp.Heuristics package
            List<Class<? extends Heuristic>> heuristics = getHeuristics();

            for (Class<? extends Heuristic> heuristicClass : heuristics) {
                String heuristicName = heuristicClass.getSimpleName();
                System.out.println("Testing heuristic: " + heuristicName);

                for (File testFile : testFiles) {
                    System.out.println("Processing instance: " + testFile.getName());
                    nodes.clear();
                    customerIds.clear();

                    VRPUtils.parseVRPFile(testFile.getAbsolutePath(), nodes, customerIds, depotId);

                    Heuristic heuristic = heuristicClass.getDeclaredConstructor().newInstance();
                    List<List<Integer>> routes = heuristic.calculateRoutes(nodes, customerIds, depotId, capacity);
                    double totalDistance = calculateTotalDistance(routes);

                    // Write results to CSV
                    writer.write(testFile.getName() + "," + heuristicName + "," + totalDistance + ",\"");
                    for (List<Integer> route : routes) {
                        writer.write(route.toString().replace(",", " ").replace("[", "").replace("]", "") + " | ");
                    }
                    writer.write("\"\n");

                    System.out.printf("Total distance for %s using %s: %.2f\n", testFile.getName(), heuristicName,
                            totalDistance);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Results written to: " + csvFile.getAbsolutePath());

        try {
            String pythonScriptPath = "c:\\Users\\tomju\\OneDrive\\Desktop\\uni\\AE\\cvrp\\plot_results.py";
            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Plotting completed successfully.");
            } else {
                System.out.println("Plotting script failed with exit code: " + exitCode);
            }
        } catch (Exception e) {
            System.err.println("Failed to execute the plotting script.");
            e.printStackTrace();
        }
    }

    private static List<Class<? extends Heuristic>> getHeuristics() {
        List<Class<? extends Heuristic>> heuristics = new ArrayList<>();
        try {
            heuristics.add(cvrp.Heuristics.RandomHeuristic.class);
            heuristics.add(cvrp.Heuristics.GreedyHeuristic.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return heuristics;
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