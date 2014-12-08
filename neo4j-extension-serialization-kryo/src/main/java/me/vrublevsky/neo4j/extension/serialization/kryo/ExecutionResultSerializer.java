package me.vrublevsky.neo4j.extension.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import me.vrublevsky.neo4j.extension.serialization.kryo.dto.RowDTO;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.SoftReferenceObjectPool;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExtendedExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.server.rest.transactional.Statement;
import org.neo4j.server.rest.transactional.StatementDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.StreamingOutput;
import java.io.OutputStream;
import java.util.Map;

public class ExecutionResultSerializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionResultSerializer.class);
    public static final ObjectPool<Kryo> POOL = new SoftReferenceObjectPool<>(new KryoPool());

    private final StatementDeserializer statements;
    private final GraphDatabaseService db;
    private final ExecutionEngine executionEngine;

    public ExecutionResultSerializer(StatementDeserializer statements, GraphDatabaseService db, ExecutionEngine executionEngine) {
        this.statements = statements;
        this.db = db;
        this.executionEngine = executionEngine;
    }

    public StreamingOutput serialize() {
        return new StreamingOutput() {
            @Override
            public void write(OutputStream outputStream) {
                while (statements.hasNext()) {
                    Statement statement = statements.next();

                    // TODO: Simplify this. And error handling. Yes. Errors.
                    try (Transaction tx = db.beginTx()) {
                        Kryo kryo = POOL.borrowObject();
                        Output output = new Output(outputStream);

                        ExtendedExecutionResult statementResult = executionEngine.execute(statement.statement(), statement.parameters());
                        serializeStatementResult(kryo, output, statementResult);

                        POOL.returnObject(kryo);
                        tx.success();
                    } catch (Exception e) {
                        LOGGER.error("Serialize statement exception {}", e);
                    }
                }
            }
        };
    }

    private void serializeStatementResult(Kryo kryo, Output output, ExtendedExecutionResult statementResult) {
        // TODO: Top-level statement DTO here?
        // We should wrap statement in its own DTO. But if we do, we can't serialize by rows.
        // Maybe custom Kryo serializer???
        for (Map<String, Object> data : statementResult) {
            RowDTO row = new RowDTO();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                row.put(entry.getKey(), ObjectTransformer.transform(entry.getValue()));
            }
            kryo.writeObject(output, row);
        }
    }
}
