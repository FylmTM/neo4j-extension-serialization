package me.vrublevsky.neo4j.extension.serialization.bson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import de.undercouch.bson4jackson.BsonFactory;
import de.undercouch.bson4jackson.BsonGenerator;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.cypher.javacompat.ExtendedExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.server.rest.transactional.ExecutionResultSerializer;
import org.neo4j.server.rest.transactional.Statement;
import org.neo4j.server.rest.transactional.StatementDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class BsonSerialize {
    private static final Logger LOGGER = LoggerFactory.getLogger(BsonSerialize.class);
    public static final BsonFactory bsonFactory = new BsonFactory(new Neo4jBsonCodec())
            .enable(BsonGenerator.Feature.ENABLE_STREAMING);
    private final StatementDeserializer statements;
    private final GraphDatabaseService db;
    private final ExecutionEngine executionEngine;

    public BsonSerialize(StatementDeserializer statements, GraphDatabaseService db, ExecutionEngine executionEngine) {
        this.statements = statements;
        this.db = db;
        this.executionEngine = executionEngine;
    }

    public StreamingOutput serialize() {
        return new StreamingOutput() {
            @Override
            public void write(OutputStream outputStream) {
                try {
                    BsonGenerator g = bsonFactory.createJsonGenerator(outputStream);

                    g.writeStartObject();
                    g.writeArrayFieldStart("results");
                    while (statements.hasNext()) {
                        Statement statement = statements.next();

                        // TODO: Simplify this. And error handling. Yes. Errors.
                        try (Transaction tx = db.beginTx()) {

                            ExtendedExecutionResult statementResult = executionEngine.execute(statement.statement(), statement.parameters());
                            serializeStatementResult(g, outputStream, statementResult);

                            statementResult.iterator().close();
                            tx.success();
                        } catch (Exception e) {
                            Throwables.propagate(e);
                        }
                    }
                    g.writeEndArray();
                    g.writeEndObject();
                    g.close();
                } catch (IOException e) {
                    LOGGER.error("Serialize statement exception {}", e);
                }
            }
        };
    }

    private void serializeStatementResult(BsonGenerator g, OutputStream outputStream, ExtendedExecutionResult statementResult) throws IOException {
        for (Map<String, Object> data : statementResult) {
            g.writeStartObject();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                g.writeObjectField(entry.getKey(), entry.getValue());
            }
            g.writeEndObject();
        }
    }
}
