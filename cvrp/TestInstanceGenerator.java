package cvrp;

import java.io.*;
import java.util.Random;

public class TestInstanceGenerator {

    public static void generateTestInstances(String folderPath, int numInstances, int minNodes, int maxNodes,
            int maxCapacity) throws IOException {
        Random random = new Random();

        for (int i = 1; i <= numInstances; i++) {
            int numNodes = random.nextInt(maxNodes - minNodes + 1) + minNodes;
            int capacity = random.nextInt(maxCapacity - 50) + 50; // Ensure capacity is at least 50

            File file = new File(folderPath, "instance_" + i + ".vrp");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("NAME : Instance_" + i + "\n");
                writer.write("TYPE : CVRP\n");
                writer.write("DIMENSION : " + numNodes + "\n");
                writer.write("EDGE_WEIGHT_TYPE : EUC_2D\n");
                writer.write("CAPACITY : " + capacity + "\n");
                writer.write("NODE_COORD_SECTION\n");

                for (int j = 1; j <= numNodes; j++) {
                    int x = random.nextInt(100);
                    int y = random.nextInt(100);
                    writer.write(j + " " + x + " " + y + "\n");
                }

                writer.write("DEMAND_SECTION\n");
                for (int j = 1; j <= numNodes; j++) {
                    int demand = (j == 1) ? 0 : random.nextInt(20) + 1; // Depot has 0 demand
                    writer.write(j + " " + demand + "\n");
                }

                writer.write("DEPOT_SECTION\n");
                writer.write("1\n");
                writer.write("-1\n");
                writer.write("EOF\n");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String folderPath = "c:\\Users\\tomju\\OneDrive\\Desktop\\uni\\AE\\test_instances";
        int numInstances = 10;
        int minNodes = 10;
        int maxNodes = 50;
        int maxCapacity = 200;

        new File(folderPath).mkdirs(); // Create folder if it doesn't exist
        generateTestInstances(folderPath, numInstances, minNodes, maxNodes, maxCapacity);
        System.out.println("Test instances generated in folder: " + folderPath);
    }
}