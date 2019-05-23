package com.github.rxyor.distributed.delay;

import com.github.rxyor.common.util.SnowFlakeHelper;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *<p>
 *
 *</p>
 *
 * @author liuyang
 * @date 2019-05-21 Tue 14:04:00
 * @since 1.0.0
 */
public abstract class AbstractDelayJobHandler implements DelayJobHandler {

    protected List<String> topics = new ArrayList<>(4);

    private static SnowFlakeHelper snowFlakeHelper = SnowFlakeHelper.builder().datacenterId(3L).machineId(2L).build();

    @Override
    public String getId() {
        return snowFlakeHelper.nextHexId();
    }

    @Override
    public List<String> getProcessibleTopics() {
        return topics;
    }

    public void addTopic(String topic) {
        if (StringUtils.isEmpty(topic)) {
            throw new IllegalArgumentException("topic can't be empty");
        }
        topics.add(topic);
    }
}