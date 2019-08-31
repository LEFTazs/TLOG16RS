package timelogger;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import timelogger.entities.TestEntity;
import org.avaje.datasource.DataSourceConfig;

public class CreateDatabase {
    @lombok.Getter
    private EbeanServer ebeanServer;
    
    public CreateDatabase() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriver("org.mariadb.jdbc.Driver");
        dataSourceConfig.setUrl("jdbc:mariadb://127.0.0.1:9001/timelogger"); //timelogger will be the name of the database
        dataSourceConfig.setUsername( "timelogger");
        dataSourceConfig.setPassword("633Ym2aZ5b9Wtzh4EJc4pANx");
        
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setName("timelogger");
        //serverConfig.setDdlGenerate(true);
        //serverConfig.setDdlRun(true); //if the last 2 property is true, the database will be generated automatically
        serverConfig.setRegister(true);
        serverConfig.setDataSourceConfig(dataSourceConfig);
        serverConfig.addClass(TestEntity.class); // (Now it is only the TestEntity, but here you can add the list of the annotated classes)
        serverConfig.setDefaultServer(true);

        EbeanServer ebeanServer = EbeanServerFactory.create(serverConfig);
        
        this.ebeanServer = ebeanServer;
    }
    
}
