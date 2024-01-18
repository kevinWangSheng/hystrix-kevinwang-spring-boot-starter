package cn.kevinwang.aop;

import cn.kevinwang.hystrix.annotation.DoHystrix;
import cn.kevinwang.hystrix.value.IValueService;
import cn.kevinwang.hystrix.value.impl.HystrixValueImpl;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author wang
 * @create 2024-01-18-11:00
 */
@Aspect
@Component
public class DoHystrixPoint {

    @Pointcut("@annotation(cn.kevinwang.hystrix.annotation.DoHystrix)")
    public void pointCut(){}

    @Around("pointCut() && @annotation(doHystrix))")
    public Object doHystrix(ProceedingJoinPoint jp, DoHystrix doHystrix) throws Throwable {
        IValueService valueService = new HystrixValueImpl();
        return valueService.access(jp, getMethod(jp), doHystrix, jp.getArgs());
    }

    public Method getMethod(ProceedingJoinPoint jp) throws NoSuchMethodException {
        Signature signature = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return getClass(jp).getMethod(methodSignature.getName(),methodSignature.getParameterTypes());
    }

    public Class getClass(ProceedingJoinPoint jp){
        return jp.getTarget().getClass();
    }
}
