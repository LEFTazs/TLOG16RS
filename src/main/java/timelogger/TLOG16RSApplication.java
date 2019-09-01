package timelogger;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TLOG16RSApplication {

    @Autowired
    private TLOG16RSConfiguration config;
    
    public static void main(String[] args) {
        SpringApplication.run(TLOG16RSApplication.class, args);
    }
    
    @PostConstruct
    public void createDatabase() { 
        CreateDatabase createDatabase = new CreateDatabase(config);
    }
}