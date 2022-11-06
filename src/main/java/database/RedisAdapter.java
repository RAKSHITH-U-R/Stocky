package database;

import redis.clients.jedis.Jedis;

public class RedisAdapter {

    private Jedis jedis;

    public void connect () {
        try {
            jedis = new Jedis("localhost");
            System.out.println("Redis Connected"+jedis);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String get(String searchKey){
        return jedis.get(searchKey);
    }

    public void set(String key, String value){
        jedis.set(key, value);
    }

    public void expire(String key, int timeout){
        jedis.expire(key, timeout);
    }
}
