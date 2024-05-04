package top.recordsite.utils.RedisUtlis;


import org.springframework.stereotype.Component;

import java.util.List;

@SuppressWarnings("all")
@Component
public class RedisList<T> extends RedisCache<T> {


    /**
     * lpush key value1 value2
     *
     * @param key
     * @param dataList
     * @param <T>
     * @return
     */
    public <T> long lpush(String key, List<T> dataList) {
        Long count = redisTemplate.opsForList().leftPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * rpush key value1 value2
     *
     * @param key
     * @param dataList
     * @param <T>
     * @return
     */
    public <T> long rpush(String key, List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * lset key index value
     *
     * @param key
     * @param index
     * @param value
     * @param <T>
     */
    public <T> void lset(String key, long index, T value) {
        redisTemplate.opsForList().set(key, index, value);
    }


    /**
     * lrange key start end
     * end = -1 默认全部
     *
     * @param key
     * @param start
     * @param end
     * @param <T>
     * @return
     */
    public <T> List<T> lrange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * lpop key
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T lpop(String key) {
        return (T) redisTemplate.opsForList().leftPop(key);
    }

    /**
     * rpop key
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T rpop(String key) {
        return (T) redisTemplate.opsForList().rightPop(key);
    }

    public long llen(String key) {

        return redisTemplate.opsForList().size(key);

    }
}
