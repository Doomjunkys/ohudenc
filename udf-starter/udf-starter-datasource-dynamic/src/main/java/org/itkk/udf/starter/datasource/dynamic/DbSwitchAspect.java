package org.itkk.udf.starter.datasource.dynamic;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class DbSwitchAspect {
    /**
     * 描述 : dbSwitchAspect
     */
    @Pointcut("@annotation(org.itkk.udf.starter.datasource.dynamic.DbSwitch)")
    public void pointCut() {

    }

    /**
     * 环绕通知
     * @param pjd pjd
     * @return Object
     * @throws Throwable Throwable
     */
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjd) throws Throwable {
        try {
            Signature signature = pjd.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            if (method.isAnnotationPresent(DbSwitch.class)) {
                DbSwitch dbSwitch = method.getAnnotation(DbSwitch.class);
                String code = dbSwitch.value();
                if (StringUtils.isNotBlank(code)) {
                    DbContextHolder.setDataSourceCode(code);
                }
            }
            return pjd.proceed();
        } finally {
            DbContextHolder.clearDataSourceCode();
        }
    }
}
