package bean;

import io.dropwizard.Configuration;

public class RedisConfiguration extends Configuration {
    public String host;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }


}
