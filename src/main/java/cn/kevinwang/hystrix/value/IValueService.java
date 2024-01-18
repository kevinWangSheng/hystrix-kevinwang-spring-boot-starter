package cn.kevinwang.hystrix.value;

import cn.kevinwang.hystrix.annotation.DoHystrix;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author wang
 * @create 2024-01-18-10:48
 */
public interface IValueService {
    Object access(ProceedingJoinPoint jp, Method method, DoHystrix doHystrix, Object[] args) throws Throwable;
}
