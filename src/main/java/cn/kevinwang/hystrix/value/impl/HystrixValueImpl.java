package cn.kevinwang.hystrix.value.impl;

import cn.hutool.json.JSONUtil;
import cn.kevinwang.hystrix.annotation.DoHystrix;
import cn.kevinwang.hystrix.common.Constance;
import cn.kevinwang.hystrix.value.IValueService;
import com.alibaba.fastjson2.JSON;
import com.netflix.hystrix.*;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author wang
 * @create 2024-01-18-10:49
 */
public class HystrixValueImpl extends HystrixCommand<Object> implements IValueService {
    private ProceedingJoinPoint jp;

    private Method method;

    private DoHystrix doHystrix;
    public HystrixValueImpl() {
        /*********************************************************************************************
         * 置HystrixCommand的属性
         * GroupKey：            该命令属于哪一个组，可以帮助我们更好的组织命令。
         * CommandKey：          该命令的名称
         * ThreadPoolKey：       该命令所属线程池的名称，同样配置的命令会共享同一线程池，若不配置，会默认使用GroupKey作为线程池名称。
         * CommandProperties：   该命令的一些设置，包括断路器的配置，隔离策略，降级设置，以及一些监控指标等。
         * ThreadPoolProperties：关于线程池的配置，包括线程池大小，排队队列的大小等
         *********************************************************************************************/
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(Constance.Hystrix.HYSTRIX_GROUP_KEY))
                .andCommandKey(HystrixCommandKey.Factory.asKey(Constance.Hystrix.HYSTRIX_COMMAND_KEY))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(Constance.Hystrix.HYSTRIX_THREAD_POOL_KEY))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(Constance.Hystrix.HYSTRIX_DEFAULT_POOL_SIZE))
        );
    }

    @Override
    public Object access(ProceedingJoinPoint jp, Method method, DoHystrix doHystrix, Object[] args) throws Throwable {
        this.jp = jp;
        this.method = method;
        this.doHystrix = doHystrix;

        // 设置熔断超时时间
        Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(Constance.Hystrix.HYSTRIX_GROUP_KEY))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(doHystrix.timeoutValue()));

        return this.execute();
    }

    // 这里是设置熔断返回的默认值
    @Override
    protected Object run() throws Exception {
        try {
            return jp.proceed();
        } catch (Throwable e) {
            return null;
        }
    }

    @Override
    protected Object getFallback() {
        return JSONUtil.toBean(doHystrix.returnJson(),method.getReturnType());
    }
}
