package service;

import bean.RedisConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import resource.RecordData;
import redis.clients.jedis.*;
import db.RedisAdapter;

public class Analytics extends Application<RedisConfiguration> {

    public static Jedis connection;
    public static void main(String[] args)
    {

    }
    public void init(RedisConfiguration configuration)
    {
            connection = new RedisAdapter().getConnection();
    }

    public void run(RedisConfiguration redisConfiguration, Environment environment) throws Exception {
        init(redisConfiguration);
        environment.jersey().register(RecordData.class);

    }
}
