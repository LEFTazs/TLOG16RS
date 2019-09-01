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
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriver(config.getDriver());
        dataSourceConfig.setUrl(config.getUrl());
        dataSourceConfig.setUsername(config.getUsr());
        dataSourceConfig.setPassword(config.getPassword());
        this.dataSourceConfig = dataSourceConfig;
    }
    
    private void initServerConfig(TLOG16RSConfiguration config) {
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setName(config.getUsr());
        serverConfig.setDdlGenerate(false);
        serverConfig.setDdlRun(false);
        serverConfig.setRegister(true);
        serverConfig.setDataSourceConfig(this.dataSourceConfig);
        serverConfig.setClasses(Arrays.asList(
                Task.class, WorkDay.class, WorkMonth.class, TimeLogger.class));
        serverConfig.setDefaultServer(true);
        this.serverConfig = serverConfig;
    }
    
    private void initServer() {
        EbeanServer ebeanServer = EbeanServerFactory.create(this.serverConfig);
        
        this.ebeanServer = ebeanServer;
    }
    
    private void updateSchema(TLOG16RSConfiguration config) {
        try {
            Connection connection = DriverManager.getConnection(
                    config.getUrl(),
                    config.getUsr(),
                    config.getPassword()
            );
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
