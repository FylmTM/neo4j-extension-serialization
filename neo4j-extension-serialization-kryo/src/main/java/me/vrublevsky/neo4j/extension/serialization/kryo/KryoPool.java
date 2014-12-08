package me.vrublevsky.neo4j.extension.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import me.vrublevsky.neo4j.extension.serialization.kryo.dto.NodeDTO;
import me.vrublevsky.neo4j.extension.serialization.kryo.dto.RelationshipDTO;
import me.vrublevsky.neo4j.extension.serialization.kryo.dto.RowDTO;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.ArrayList;
import java.util.HashMap;

public class KryoPool extends BasePooledObjectFactory<Kryo> {
    @Override
    public Kryo create() throws Exception {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(true);
        kryo.register(HashMap.class);
        kryo.register(ArrayList.class);
        kryo.register(String[].class);
        kryo.register(int[].class);
        kryo.register(long[].class);
        kryo.register(RowDTO.class);
        kryo.register(NodeDTO.class);
        kryo.register(RelationshipDTO.class);

        return kryo;
    }

    @Override
    public PooledObject<Kryo> wrap(Kryo kryo) {
        return new DefaultPooledObject<>(kryo);
    }
}

