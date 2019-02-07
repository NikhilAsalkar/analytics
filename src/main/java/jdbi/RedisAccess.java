package jdbi;

import redis.clients.jedis.Jedis;
import service.Analytics;

import java.util.Map;
import java.util.Set;

public class RedisAccess {

    Jedis jedis = Analytics.connection;
    Set keys;
    String keyPattern;
    String time1;
    Map<String ,String> data;
    public Set getkeys(String apiName)
    {
        if(apiName == "")
            keyPattern = "*";
        else
            keyPattern = apiName+":*";
        keys = jedis.keys(keyPattern);
        return keys;
    }

    public void createKey(String key,String time)
    {
        if(time == "")
            jedis.hset(key,"count","1");
        else
            jedis.hset(key,time,"1");
    }

    public String getData(String key,String field)
    {
        String value =  jedis.hget(key,field);
        return value;
    }

    public void incrementField(String key,String field)
    {
        jedis.hincrBy(key,field,1);
    }


    public Map<String,String> getAllData(String key)
    {
       data = jedis.hgetAll(key);
       return data;
    }
}
