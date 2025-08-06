package educative.VendingMachine;

import java.util.*;

public class Inventory {
    private final Map<Integer, Rack> racks = new HashMap<>();

    public void addRack(Rack rack) {
        racks.put(rack.getRackNumber(), rack);
    }

    public Rack getRack(int rackNumber) {
        return racks.get(rackNumber);
    }

    public Collection<Rack> allRacks() {
        return racks.values();
    }
}
