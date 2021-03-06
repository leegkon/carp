package com.github.rxyor.redis.redisson.factory;

import com.github.rxyor.common.core.exception.ReadFileException;
import com.github.rxyor.common.util.io.FileUtil;
import com.github.rxyor.redis.redisson.codec.FastJsonCodec;
import com.github.rxyor.redis.redisson.config.RedisConfig;
import com.github.rxyor.redis.redisson.exception.RedissonLackConfigExecption;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

/**
 *<p>
 *
 *</p>
 *
 * @author liuyang
 * @date 2019-06-04 Tue 10:37:00
 * @since 1.0.0
 */
public class CarpRedissonFactory {

    @Getter
    private Config config;

    @Getter
    private RedisConfig redisConfig;

    @Getter
    private String yaml;

    @Getter
    private String json;

    private CarpRedissonFactory(Config config, RedisConfig redisConfig, String yaml, String json) {
        this.config = config;
        this.redisConfig = redisConfig;
        this.yaml = yaml;
        this.json = json;
    }

    public RedissonClient createRedissonClient() {
        return Redisson.create(buildConfig());
    }

    private Config buildConfig() {
        if (config != null) {
            return config;
        }
        if (redisConfig != null) {
            return buildRedisBeanConfig();
        }
        if (yaml != null) {
            return buildYamlConfig();
        }
        if (json != null) {
            return buildJsonConfig();
        }
        throw new RedissonLackConfigExecption("you must config redis for factory");
    }

    private Config buildRedisBeanConfig() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(this.buildAddress(redisConfig.getHost(), redisConfig.getPort()));
        Codec codec = Optional.ofNullable(redisConfig).map(RedisConfig::getCodec).orElse(new FastJsonCodec());
        config.setCodec(codec);
        return config;
    }

    private Config buildYamlConfig() {
        Objects.requireNonNull(this.yaml, "yaml can't be null");
        InputStream is = null;
        try {
            is = FileUtil.readInputStream(this.yaml);
            Config config = Config.fromYAML(is);
            return configCodec(config);
        } catch (IOException e) {
            throw new ReadFileException(e);
        } finally {
            FileUtil.close(is);
        }
    }

    private Config buildJsonConfig() {
        Objects.requireNonNull(this.json, "json can't be null");
        InputStream is = null;
        try {
            is = FileUtil.readInputStream(this.json);
            Config config = Config.fromJSON(is);
            return configCodec(config);
        } catch (IOException e) {
            throw new ReadFileException(e);
        } finally {
            FileUtil.close(is);
        }
    }

    private Config configCodec(Config config) {
        if (config == null || config.getCodec() != null) {
            return config;
        }
        config.setCodec(new FastJsonCodec());
        return config;
    }

    private String buildAddress(String host, Integer port) {
        Objects.requireNonNull(host, "host can't be null");
        Objects.requireNonNull(port, "port can't be null");
        return "redis://" + host + ":" + port;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Config config;

        private RedisConfig redisConfig;

        private String yaml;

        private String json;

        public Builder config(Config config) {
            Objects.requireNonNull(config, "config can't be null");
            this.config = config;
            return this;
        }

        public Builder redisConfig(RedisConfig redisConfig) {
            Objects.requireNonNull(redisConfig, "redisConfig can't be null");
            this.redisConfig = redisConfig;
            return this;
        }

        public Builder yaml(String yamlPath) {
            Objects.requireNonNull(yamlPath, "yaml path can't be null");
            this.yaml = yamlPath;
            return this;
        }

        public Builder json(String jsonPath) {
            Objects.requireNonNull(jsonPath, "json path can't be null");
            this.json = jsonPath;
            return this;
        }

        public CarpRedissonFactory build() {
            return new CarpRedissonFactory(config, redisConfig, yaml, json);
        }
    }
}
