package top.recordsite.utils.RedisUtlis;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
@Component
public class RedisString<T> extends RedisCache<T> {

    /**
     * set key value
     * 设置指定key的值
     *
     * @param <T>
     * @param key
     * @param value
     */
    public <T> void set(String key, T value) {
         redisTemplate.opsForValue().set(key, value);
    }

    /**
     * set key value expire
     *
     * @param key
     * @param value
     * @param time
     * @param timeUnit
     * @param <T>
     */
    public <T> void set(String key, T value, long time, TimeUnit timeUnit) {
         redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    /**
     * setnx key value
     * 只有在可以 不存在时设置key
     *
     * @param key
     * @param value
     * @param <T>
     */
    public <T> void setnx(String key, T value) {
         redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * get key
     * 获取指定的key
     *
     * @param <T>
     * @param key
     * @return
     */
    public T get(String key) {
        return (T)  redisTemplate.opsForValue().get(key);
    }

    /**
     * mset key1 value1 key2 value2
     * 设置一个或多个指定key的值
     *
     * @param map
     */
    public void mset(Map map) {
         redisTemplate.opsForValue().multiSet(map);
    }

    /**
     * mget key1 key2
     * 获取一个或多个指定key的值
     *
     * @param keys
     * @param <T>
     * @return
     */
    public List<T> mget(Collection keys) {
        return  redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * strlen key
     * 获取key所存储的字符串值的长度
     *
     * @param key
     * @return long
     */
    public Long strlen(String key) {
        return  redisTemplate.opsForValue().size(key);
    }

    /**
     * incr key
     * 增量key
     *
     * @param key
     */
    public void incr(String key) {
         redisTemplate.opsForValue().increment(key);
    }

    public void incr(String key, double incr) {
         redisTemplate.opsForValue().increment(key, incr);
    }

    public void incr(String key, long incr) {
         redisTemplate.opsForValue().increment(key, incr);
    }

    /**
     * decr key
     * 减量key
     *
     * @param key
     */
    public void decr(String key) {
         redisTemplate.opsForValue().decrement(key);
    }

    public void decr(String key, long decr) {
         redisTemplate.opsForValue().decrement(key, decr);
    }

    /**
     * 获取原来的key的值后在后面新增上新的字符串
     *
     * @param key
     * @param value
     * @param <T>
     */
    public <T> void appendKey(String key, T value) {
         redisTemplate.opsForValue().append(key, String.valueOf(value));
    }
}