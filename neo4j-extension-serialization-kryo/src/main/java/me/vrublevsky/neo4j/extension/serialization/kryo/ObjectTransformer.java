package me.vrublevsky.neo4j.extension.serialization.kryo;

import me.vrublevsky.neo4j.extension.serialization.kryo.dto.*;
import me.vrublevsky.neo4j.extension.serialization.kryo.dto.NodeDTO;
import me.vrublevsky.neo4j.extension.serialization.kryo.dto.RelationshipDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class ObjectTransformer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectTransformer.class);

    public static Object transform(Object value) {
        // ToDo: Find better way to do this.
        if (value instanceof List) return extractList((List) value);
        if (value instanceof Node) return transformNode((Node) value);
        if (value instanceof Relationship) return transformRelationship((Relationship) value);
        return value;
    }

    private static NodeDTO transformNode(Node node) {
        NodeDTO nodeDao = new NodeDTO();
        nodeDao.setId(node.getId());

        for (Label label : node.getLabels()) {
            nodeDao.addLabel(label.name());
        }

        for (String key : node.getPropertyKeys()) {
            nodeDao.addProperty(key, node.getProperty(key));
        }

        return nodeDao;
    }

    private static RelationshipDTO transformRelationship(Relationship rel) {
        RelationshipDTO relDao = new RelationshipDTO();
        relDao.setId(rel.getId());
        relDao.setType(rel.getType().name());
        relDao.setStartNodeId(rel.getStartNode().getId());
        relDao.setEndNodeId(rel.getEndNode().getId());

        for (String key : rel.getPropertyKeys()) {
            relDao.addProperty(key, rel.getProperty(key));
        }

        return relDao;
    }

    private static List extractList(List list) {
        List<Object> listDTO = new ArrayList<>();
        for (Object listElement : list) {
            listDTO.add(transform(listElement));
        }
        return listDTO;
    }
}
