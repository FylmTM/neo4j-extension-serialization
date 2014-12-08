package me.vrublevsky.neo4j.extension.serialization.kryo.rest;

import me.vrublevsky.neo4j.extension.serialization.kryo.ExecutionResultSerializer;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.server.rest.transactional.TransactionFacade;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.InputStream;

@Path("/transaction")
public class TransactionalCypherResource {
    private final GraphDatabaseService db;
    private final ExecutionEngine executionEngine;
    private TransactionFacade facade;

    public TransactionalCypherResource(@Context GraphDatabaseService db,
                                       @Context ExecutionEngine executionEngine,
                                       @Context TransactionFacade facade) {
        this.db = db;
        this.executionEngine = executionEngine;
        this.facade = facade;
    }

    @POST
    @Path("/commit")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.APPLICATION_JSON)
    /**
     * TODO: Tests!
     */
    public Response commitNewTransaction(final InputStream input) {
        StreamingOutput streamingResults = executeStatementsAndCommit(input);
        return Response.ok()
                .entity(streamingResults)
                .build();
    }

    public StreamingOutput executeStatementsAndCommit(InputStream input) {
        ExecutionResultSerializer serializer = new ExecutionResultSerializer(facade.deserializer(input), db, executionEngine);
        return serializer.serialize();
    }
}
