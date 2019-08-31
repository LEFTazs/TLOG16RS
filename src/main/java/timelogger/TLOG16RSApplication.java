package timelogger;

import com.avaje.ebean.EbeanServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TLOG16RSApplication {

    public static void main(String[] args) {
        SpringApplication.run(TLOG16RSApplication.class, args);
    }
    
    @Bean
    public EbeanServer persistencyServer() { 
        CreateDatabase createDatabase = new CreateDatabase();
        return createDatabase.getEbeanServer();
    }
}