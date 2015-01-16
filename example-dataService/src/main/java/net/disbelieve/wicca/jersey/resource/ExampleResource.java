package net.disbelieve.wicca.jersey.resource;

import com.google.common.base.Optional;
import net.disbelieve.wicca.cassandra.dao.ExampleDAO;
import net.disbelieve.wicca.model.WiccaData;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by kmatth002c on 12/10/2014.
 */
@Singleton
@Path("/example")
public class ExampleResource {
    private ExampleDAO hydraDAO;

    public ExampleResource() {
        hydraDAO = new ExampleDAO();
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{accountId}/{deviceId}")
    public void getOne(@Suspended final AsyncResponse asyncResponse, @PathParam("accountId") final String accountId,
                       @PathParam("deviceId") final String deviceId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WiccaData hydraData = null;
                try {
                    BlockingQueue<Optional> queue = new ArrayBlockingQueue<Optional>(2);

                    try {
                        Optional optional = hydraDAO.getOne(queue, accountId, deviceId).take();
                        if (optional.isPresent()) {
                            hydraData = (WiccaData) optional.get();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    /**MUST have finally block resume or any uncaught exceptions will leave the browser request hung*/
                } finally {
                    asyncResponse.resume(hydraData);
                }
            }
        }).start();
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{accountId}")
    public void getAll(@Suspended final AsyncResponse asyncResponse,
                       @PathParam("accountId") final String accountId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<WiccaData> hydraDatum = new ArrayList<WiccaData>();
                try {
                    BlockingQueue<Optional> queue = new ArrayBlockingQueue<Optional>(2);
                    WiccaData hydraData = new WiccaData();
                    hydraData.setAccountId(accountId);
                    Optional optional;

                    try {
                        optional = hydraDAO.getAll(queue, hydraData).take();

                        if (optional.isPresent()) {
                            hydraDatum = (List<WiccaData>) optional.get();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    /**MUST have finally block resume or any uncaught exceptions will leave the browser request hung*/
                } finally {
                    GenericEntity<List<WiccaData>> entity = new GenericEntity<List<WiccaData>>(hydraDatum) {};
                    asyncResponse.resume(entity);
                }
            }
        }).start();
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    // do not require id info in the path. its in the request body anyway and can lead to
    // confusion over the source of truth; for the resource and the client
    public void post(@Suspended final AsyncResponse asyncResponse, final WiccaData data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BlockingQueue<Optional> queue = new ArrayBlockingQueue<Optional>(2);

                    // cassandra makes no distinction between insert and update so put is reused
                    queue = hydraDAO.put(queue, data);
                    //TODO: read queue to test for error
                    /**MUST have finally block resume or any uncaught exceptions will leave the browser request hung*/
                } finally {
                    asyncResponse.resume(null);
                }
            }
        }).start();
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    // do not require id info in the path. its in the request body anyway and can lead to
    // confusion over the source of truth; for the resource and the client
    public void put(@Suspended final AsyncResponse asyncResponse, final WiccaData data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BlockingQueue<Optional> queue = new ArrayBlockingQueue<Optional>(2);

                    queue = hydraDAO.put(queue, data);
                    //TODO: read queue to test for error
                    /**MUST have finally block resume or any uncaught exceptions will leave the browser request hung*/
                } finally {
                    asyncResponse.resume(null);
                }

            }
        }).start();
    }

    @DELETE
    @Path("{accountId}/{deviceId}")
    public void deleteOne(@Suspended final AsyncResponse asyncResponse, @PathParam("accountId") final String accountId,
                          @PathParam("deviceId") final String deviceId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BlockingQueue<Optional> queue = new ArrayBlockingQueue<Optional>(2);

                    queue = hydraDAO.deleteOne(queue, accountId, deviceId);
                    //TODO: read queue to test for error
                    /**MUST have finally block resume or any uncaught exceptions will leave the browser request hung*/
                } finally {
                    asyncResponse.resume(null);
                }
            }
        }).start();
    }

    @DELETE
    @Path("{accountId}")
    public void deleteAll(@Suspended final AsyncResponse asyncResponse, @PathParam("accountId") final String accountId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BlockingQueue<Optional> queue = new ArrayBlockingQueue<Optional>(2);
                    WiccaData data = new WiccaData();

                    data.setAccountId(accountId);
                    queue = hydraDAO.deleteAll(queue, data);
                    //TODO: read queue to test for error
                    /**MUST have finally block resume or any uncaught exceptions will leave the browser request hung*/
                } finally {
                    asyncResponse.resume(null);
                }
            }
        }).start();
    }
}
