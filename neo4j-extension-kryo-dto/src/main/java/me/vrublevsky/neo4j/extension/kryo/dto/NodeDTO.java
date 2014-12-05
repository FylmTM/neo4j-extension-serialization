package me.vrublevsky.neo4j.extension.kryo.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeDTO {
    private long id;
    private List<String> labels;
    private Map<String, Object> properties;
    private boolean hasChildRelationships;

    public NodeDTO() {
        this.labels = new ArrayList<>();
        this.properties = new HashMap<>();
        this.hasChildRelationships = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void addLabel(String label) {
        labels.add(label);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void addProperty(String key, Object value) {
        properties.put(key, value);
    }

    public boolean isHasChildRelationships() {
        return hasChildRelationships;
    }

    public void setHasChildRelationships(boolean hasChildRelationships) {
        this.hasChildRelationships = hasChildRelationships;
    }

    @Override
    public int hashCode() {
        return (int) ((id >>> 32) ^ id);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof NodeDTO && this.getId() == ((NodeDTO) o).getId();
    }
}
