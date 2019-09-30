package com.github.hui.media.console.annotation;


import com.github.hui.media.console.entity.IValidate;
import com.github.hui.media.console.exception.ParameterError;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 校验切面
 * <p>
 * Created by yihui on 2017/9/12.
 */
@Aspect
@Component
public class ValidateAspect {


    /**
     * 参数校验
     *
     * @param joinPoint
     * @param validate
     * @throws Throwable
     */
    @Before("@annotation(validate)")
    public void beforeExec(JoinPoint joinPoint, ValidateDot validate) throws ParameterError {
        Object[] args = joinPoint.getArgs();

        for (Object obj : args) {
            if (obj instanceof IValidate) {
                if (!((IValidate) obj).validate()) {
                    throw new ParameterError();
                }
            }
        }
    }


}
