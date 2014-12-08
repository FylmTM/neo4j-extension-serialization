package me.vrublevsky.neo4j.extension.serialization.bson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PropertyContainer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Neo4jBsonCodec extends ObjectMapper {
    @Override
    public void writeValue( JsonGenerator out, Object value ) throws IOException
    {
        if ( value instanceof PropertyContainer )
        {
            writePropertyContainer( out, (PropertyContainer) value );
        }
        else if ( value instanceof Path )
        {
            writePath( out, ((Path) value).iterator() );
        }
        else if (value instanceof Iterable)
        {
            writeIterator( out, ((Iterable) value).iterator() );
        }
        else if ( value instanceof byte[] )
        {
            writeByteArray( out, (byte[]) value );
        }
        else if ( value instanceof Map )
        {
            writeMap(out, (Map) value );
        }
        else
        {
            super.writeValue( out, value );
        }
    }

    private void writeMap( JsonGenerator out, Map value ) throws IOException
    {
        out.writeStartObject();
        try
        {
            Set<Map.Entry> set = value.entrySet();
            for ( Map.Entry e : set )
            {
                out.writeFieldName( e.getKey().toString() );
                writeValue( out, e.getValue() );
            }
        }
        finally
        {
            out.writeEndObject();
        }
    }

    private void writeIterator( JsonGenerator out, Iterator value ) throws IOException
    {
        out.writeStartArray();
        try
        {
            while ( value.hasNext() )
            {
                writeValue( out, value.next() );
            }
        }
        finally
        {
            out.writeEndArray();
        }
    }

    private void writePath( JsonGenerator out, Iterator<PropertyContainer> value ) throws IOException
    {
        out.writeStartArray();
        try
        {
            while ( value.hasNext() )
            {
                writePropertyContainer( out, value.next() );
            }
        }
        finally
        {
            out.writeEndArray();
        }
    }

    private void writePropertyContainer( JsonGenerator out, PropertyContainer value ) throws IOException
    {
        out.writeStartObject();
        try
        {
            for ( String key : value.getPropertyKeys() )
            {
                out.writeObjectField( key, value.getProperty( key ) );
            }
        }
        finally
        {
            out.writeEndObject();
        }
    }

    private void writeByteArray( JsonGenerator out, byte[] bytes ) throws IOException
    {
        out.writeStartArray();
        try
        {
            for ( byte b : bytes )
            {
                out.writeNumber( (int) b );
            }
        }
        finally
        {
            out.writeEndArray();
        }
    }
}
