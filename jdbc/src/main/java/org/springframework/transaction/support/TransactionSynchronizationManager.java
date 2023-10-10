package org.springframework.transaction.support;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

public abstract class TransactionSynchronizationManager {

    private static final ThreadLocal<Map<DataSource, Connection>> resources = ThreadLocal.withInitial(HashMap::new);

    private TransactionSynchronizationManager() {
    }

    public static Connection getResource(final DataSource key) {
        return getThreadLocalResource().get(key);
    }

    static void bindResource(final DataSource key, final Connection value) {
        getThreadLocalResource().put(key, value);
    }

    static Connection unbindResource(final DataSource key) {
        return getThreadLocalResource().remove(key);
    }

    private static Map<DataSource, Connection> getThreadLocalResource() {
        return resources.get();
    }
}
