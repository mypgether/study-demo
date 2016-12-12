package com.gether.bigdata.util;

/**
 * @author: myp
 * @date: 16/8/25
 */
public class JobUtils {

    /**
     * job from的名称
     *
     * @param deviceId
     * @param quartzId
     * @return
     */
    public static String getJobNameFrom(String deviceId, String quartzId) {
        String prefix = String.format("%s-%s", deviceId, quartzId);
        String fromJobName = String.format("job-%s-from", prefix);
        return fromJobName;
    }

    /**
     * job to的名称
     *
     * @param deviceId
     * @param quartzId
     * @return
     */
    public static String getJobNameTo(String deviceId, String quartzId) {
        String prefix = String.format("%s-%s", deviceId, quartzId);
        String toJobName = String.format("job-%s-to", prefix);
        return toJobName;
    }

    /**
     * job group的名称
     *
     * @param deviceId
     * @param quartzId
     * @return
     */
    public static String getJobGroupName(String deviceId, String quartzId) {
        String prefix = String.format("%s-%s", deviceId, quartzId);
        String jobGroup = String.format("jobgrp-%s", prefix);
        return jobGroup;
    }

    /**
     * trigger from的名称
     *
     * @param deviceId
     * @param quartzId
     * @return
     */
    public static String getTriggerNameFrom(String deviceId, String quartzId, String scheduleId) {
        String prefix = String.format("%s-%s", deviceId, quartzId);
        String fromTriName = String.format("tri-%s-%s-from", prefix, scheduleId);
        return fromTriName;
    }

    /**
     * trigger to的名称
     *
     * @param deviceId
     * @param quartzId
     * @return
     */
    public static String getTriggerNameTo(String deviceId, String quartzId, String scheduleId) {
        String prefix = String.format("%s-%s", deviceId, quartzId);
        String fromTriName = String.format("tri-%s-%s-to", prefix, scheduleId);
        return fromTriName;
    }

    /**
     * trigger 通用的group的名称
     *
     * @param deviceId
     * @param quartzId
     * @return
     */
    public static String getTriggerCommonGroupName(String deviceId, String quartzId) {
        String prefix = String.format("%s-%s", deviceId, quartzId);
        String triGroup = String.format("trigrp-%s", prefix);
        return triGroup;
    }
}