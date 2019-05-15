package com.github.rxyor.distributed.lock;

import com.github.rxyor.redis.config.RedisConnectionProperties;
import com.github.rxyor.redis.config.RedisConnectionProperties.Pool;
import com.github.rxyor.redis.factory.RedisConnectionFactory;
import io.lettuce.core.RedisClient;
import java.time.Duration;
import org.junit.Before;

/**
 *<p>
 *
 *</p>
 *
 * @author liuyang
 * @date 2019-05-15 Wed 14:30:00
 * @since 1.0.0
 */
public class RedisDistributedLockTest {

    private RedisConnectionProperties redisConnectionProperties;

    private RedisClient redisClient;

    @Before
    public void initConnectionProperties() {
        redisConnectionProperties = new RedisConnectionProperties();
        redisConnectionProperties.setHost("127.0.0.1");
        redisConnectionProperties.setDatabase(0);
        redisConnectionProperties.setPort(6379);
        redisConnectionProperties.setTimeout(Duration.ofSeconds(180L));
        redisConnectionProperties.setPool(new Pool());

        RedisConnectionFactory factory = RedisConnectionFactory.builder()
            .redisConnectionProperties(redisConnectionProperties).build();
        redisClient = factory.createClient();
    }

    @org.junit.Test
    public void getLock() {
        RedisDistributedLock lock = new RedisDistributedLock(redisClient);
        System.out.println(lock.getLock("key","task_id",300L));
    }
}