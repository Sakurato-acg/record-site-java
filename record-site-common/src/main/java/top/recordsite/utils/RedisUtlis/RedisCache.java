package top.recordsite.utils.RedisUtlis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
@Component
public abstract class RedisCache<T> {

    @Autowired
    protected RedisTemplate redisTemplate;

    /**
     * exists key
     * 判断key是否存在
     *
     * @param key
     * @return boolean
     */
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * del key
     * 删除key
     *
     * @param key
     * @return boolean
     */
    public boolean del(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * del key
     *
     * @param keys
     * @return
     */
    public long del(Collection keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * expire key seconds
     * 设置过期时间
     *
     * @param key
     * @param timeout  过期时间
     * @param timeUnit 时间颗粒度
     * @return
     */
    public boolean expire(String key, long timeout, TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * @param key
     * @param timeout seconds
     * @return
     */
    public boolean expire(String key, long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

//    public boolean ttl(String key){
//        redisTemplate.expire
//    }

    /**
     * 1、不存在的key：返回 -2
     * 2、key存在，但没有设置剩余生存时间：返回 -1
     * 3、有剩余生存时间的key：返回key的剩余时间（以秒为单位）
     * @param key
     * @return
     */
    public long ttl(String key){
        return redisTemplate.getExpire(key);
    }

//    public void setrange(String key){
//        redisTemplate.set
//    }

    public Set keys(String pattern){
        return redisTemplate.keys(pattern);
    }
}




