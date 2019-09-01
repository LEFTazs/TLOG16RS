package timelogger;

import javax.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
@lombok.Getter
@lombok.Setter
public class TLOG16RSConfiguration {
    @NotEmpty
    protected String url;
    
    protected String usr;
    protected String password;
    
    @NotEmpty
    protected String configfile;

    @NotEmpty
    protected String driver;
}
