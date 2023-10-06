package org.springframework.transaction.support;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DataSourceUtils;

public class TransactionManager {

    private final DataSource dataSource;

    public TransactionManager(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void doInTransaction(Runnable runnable) {
        TransactionSynchronizationManager.initSynchronization();
        final var connection = DataSourceUtils.getConnection(dataSource);
        try {
            connection.setAutoCommit(false);
            runnable.run();
            connection.commit();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }
}