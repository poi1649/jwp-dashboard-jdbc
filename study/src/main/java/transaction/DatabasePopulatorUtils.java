package transaction;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabasePopulatorUtils {

    private static final Logger log = LoggerFactory.getLogger(DatabasePopulatorUtils.class);

    public static void execute(final DataSource dataSource) {
        Connection connection = null;
        Statement statement = null;
        try {
            final var url = DatabasePopulatorUtils.class.getClassLoader().getResource("schema.sql");
            final var file = new File(url.getFile());
            final var fileString = Files.readString(file.toPath());
            final var sqls = Arrays.stream(fileString.split(";")).collect(Collectors.toList());
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            for (final var sql : sqls) {
                statement.execute(sql);
            }
        } catch (NullPointerException | IOException | SQLException e) {
            log.error(e.getMessage(), e.getCause());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ignored) {}

            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ignored) {}
        }
    }

    private DatabasePopulatorUtils() {}
}
