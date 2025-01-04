package org.poo.entities.commerciant;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Commerciants {
    private Map<String, Commerciant> commerciants;
    private Map<String, HashSet<String>> usedCashback;
    public Commerciants() {
        commerciants = new HashMap<>();
        usedCashback = new HashMap<>();
    }
}
