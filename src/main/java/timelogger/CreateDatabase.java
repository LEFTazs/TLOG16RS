package timelogger;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import timelogger.entities.*;

@Slf4j
public class CreateDatabase {
    private DataSourceConfig dataSourceConfig;
    private ServerConfig serverConfig;
    @lombok.Getter
    private EbeanServer ebeanServer;
    
    public CreateDatabase(TLOG16RSConfiguration config) {
        updateSchema(config);
        
        initDataSourceConfig(config);
        
        initServerConfig(config);

        initServer();
    }
    
    private void initDataSourceConfig(TLOG16RSConfiguration config) {
        DataSourceConfig newDataSourceConfig = new DataSourceConfig();
        newDataSourceConfig.setDriver(config.getDriver());
        newDataSourceConfig.setUrl(config.getUrl());
        newDataSourceConfig.setUsername(config.getUsr());
        newDataSourceConfig.setPassword(config.getPassword());
        this.dataSourceConfig = newDataSourceConfig;
    }
    
    private void initServerConfig(TLOG16RSConfiguration config) {
        ServerConfig newServerConfig = new ServerConfig();
        newServerConfig.setName(config.getUsr());
        newServerConfig.setDdlGenerate(false);
        newServerConfig.setDdlRun(false);
        newServerConfig.setRegister(true);
        newServerConfig.setDataSourceConfig(this.dataSourceConfig);
        newServerConfig.setClasses(Arrays.asList(
                Task.class, WorkDay.class, WorkMonth.class, TimeLogger.class));
        newServerConfig.setDefaultServer(true);
        this.serverConfig = newServerConfig;
    }
    
    private void initServer() {
        EbeanServer newEbeanServer = EbeanServerFactory.create(this.serverConfig);
        
        this.ebeanServer = newEbeanServer;
    }
    
    private void updateSchema(TLOG16RSConfiguration config) {
        try (Connection connection = DriverManager.getConnection(
                    config.getUrl(),
                    config.getUsr(),
                    config.getPassword())) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(
                    config.getConfigfile(),
                    new FileSystemResourceAccessor(), 
                    database
            );
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (LiquibaseException | SQLException e) {
            log.error(e.getLocalizedMessage());
        }
    }
}
