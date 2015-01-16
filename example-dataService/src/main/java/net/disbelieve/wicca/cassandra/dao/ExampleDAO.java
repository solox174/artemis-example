package net.disbelieve.wicca.cassandra.dao;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.google.common.base.Optional;
import net.disbelieve.wicca.model.WiccaData;

import java.util.concurrent.BlockingQueue;

/**
 * Created by kmatth002c on 12/11/2014.
 */
public class ExampleDAO extends CassandraDAO<WiccaData> {
    private PreparedStatement getAllPrepared;
    private BoundStatement getAll;

    private PreparedStatement deleteAllPrepared;
    private BoundStatement deleteAll;

    public ExampleDAO() {
        super(WiccaData.class);
        getAllPrepared = session.prepare("select * from hydra.exampleservice where accountId = ?");
        getAll = new BoundStatement(getAllPrepared);
        deleteAllPrepared = session.prepare("delete from hydra.exampleservice where accountId = ?");
        deleteAll = new BoundStatement(deleteAllPrepared);
    }

    @Override
    public BlockingQueue<Optional> getAll(BlockingQueue<Optional> queue, WiccaData data) {
        getAll.bind(data.getAccountId());

        return getAll(queue, getAll);
    }

    @Override
    public BlockingQueue<Optional> deleteAll(BlockingQueue<Optional> queue, WiccaData data) {
        deleteAll.bind(data.getAccountId());

        return deleteAll(queue, deleteAll);
    }
}
