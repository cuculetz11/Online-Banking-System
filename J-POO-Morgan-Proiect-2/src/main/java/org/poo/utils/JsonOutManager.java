package org.poo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public final class JsonOutManager {
    private static JsonOutManager instance = null;
    private ArrayNode output;

    private JsonOutManager() {
    }

    /**
     * Obtine instanta singleton a clasei JsonOutManager
     * @return instanta unica JsonOutManager
     */
    public static JsonOutManager getInstance() {
        if (instance == null) {
            instance = new JsonOutManager();
        }
        return instance;
    }

    public ArrayNode getOutput() {
        return output;
    }

    public void setOutput(final ArrayNode output) {
        this.output = output;
    }

    /**
     * Prin aceasta metoda adug direct obictul ce vreau sa-l pun in output
     * @param object obiectul de adugat la output
     */
    public void addToOutput(final Object object) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.valueToTree(object);
        this.output.add(node);
    }
}
