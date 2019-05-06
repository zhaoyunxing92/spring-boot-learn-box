/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.sentinel.service;

/**
 * @author zhaoyunxing
 * @date: 2019-05-05 11:22
 * @des:
 */
public interface HelloService {
    /**
     * 默认10
     *
     * @param arg sleep time
     * @return 如果没有大于10 hello sentinel ,大于等于10抛异常
     */
    String say(long arg);

    void test();

    String helloAnother(String name);
}
