package top.recordsite.utils.RedisUtlis;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Component
@Data
@Accessors(chain = true)
public class RedisUtils<T> extends RedisCache<T> {

    @Autowired
    private RedisString<T> redisString;

    @Autowired
    private RedisList<T> redisList;

}
