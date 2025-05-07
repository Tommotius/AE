package cvrp;

import java.io.*;
import java.util.*;

public class VRPUtils {

    public static void parseVRPFile(String filename, Map<Integer, Node> nodes, List<Integer> customerIds, int depotId)
            throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        boolean inCoordSection = false, inDemandSection = false;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("CAPACITY")) {
                CVRP.setCapacity(Integer.parseInt(line.split(":")[1].trim()));
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
                nodes.put(id, new Node(id, x, y, 0));
            } else if (inDemandSection && !line.isEmpty()) {
                String[] parts = line.split("\\s+");
                int id = Integer.parseInt(parts[0]);
                int demand = Integer.parseInt(parts[1]);
                nodes.get(id).setDemand(demand);
                if (id != depotId)
                    customerIds.add(id);
            }
        }

        reader.close();
    }
}