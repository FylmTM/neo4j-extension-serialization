package me.vrublevsky.neo4j.extension.serialization.kryo.dto;

import java.util.HashMap;
import java.util.Map;

public class RelationshipDTO {
    private long id;
    private String type;

    private long startNodeId;
    private long endNodeId;
    private Map<String, Object> properties;

    public RelationshipDTO() {
        this.properties = new HashMap<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getStartNodeId() {
        return startNodeId;
    }

    public void setStartNodeId(long startNodeId) {
        this.startNodeId = startNodeId;
    }

    public long getEndNodeId() {
        return endNodeId;
    }

    public void setEndNodeId(long endNodeId) {
        this.endNodeId = endNodeId;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void addProperty(String key, Object value) {
        properties.put(key, value);
    }

    @Override
    public int hashCode() {
        return (int) (( id >>> 32 ) ^ id );
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RelationshipDTO && this.getId() == ((RelationshipDTO) o).getId();
    }
}
