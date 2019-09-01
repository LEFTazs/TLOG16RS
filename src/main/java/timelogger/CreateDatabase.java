package timelogger;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import timelogger.entities.TestEntity;

@Slf4j
public class CreateDatabase {
    @lombok.Getter
    private EbeanServer ebeanServer;
    
    public CreateDatabase() {
        this.updateSchema();
        
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriver("org.mariadb.jdbc.Driver");
        dataSourceConfig.setUrl("jdbc:mariadb://127.0.0.1:9001/timelogger"); //timelogger will be the name of the database
        dataSourceConfig.setUsername( "timelogger");
        dataSourceConfig.setPassword("633Ym2aZ5b9Wtzh4EJc4pANx");
        
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setName("timelogger");
        serverConfig.setDdlGenerate(false);
        serverConfig.setDdlRun(false); //if the last 2 property is true, the database will be generated automatically
        serverConfig.setRegister(true);
        serverConfig.setDataSourceConfig(dataSourceConfig);
        serverConfig.addClass(TestEntity.class); // (Now it is only the TestEntity, but here you can add the list of the annotated classes)
        serverConfig.setDefaultServer(true);

        EbeanServer ebeanServer = EbeanServerFactory.create(serverConfig);
        
        this.ebeanServer = ebeanServer;
    }
    
    private void updateSchema() {
        try {
            Connection connection = DriverManager.getConnection(
                    System.getProperty("url"),
                    //"jdbc:mariadb://127.0.0.1:9001/timelogger",
                    System.getProperty("username"),
                    //"timelogger", 
                    System.getProperty("password")
                    //"633Ym2aZ5b9Wtzh4EJc4pANx");
                    );
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(
                    System.getProperty("config"),
                    //".\\src\\main\\resources\\migrations.xml", 
                    new FileSystemResourceAccessor(), 
                    database);
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (LiquibaseException | SQLException e) {
            log.error(e.getLocalizedMessage());
        }
    }
}
