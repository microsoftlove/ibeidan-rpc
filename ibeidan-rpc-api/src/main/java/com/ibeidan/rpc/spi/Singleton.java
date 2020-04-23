package com.ibeidan.rpc.spi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author libeibei
 * 2020/4/22 15:53
 * 单例注解
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Singleton {
}
