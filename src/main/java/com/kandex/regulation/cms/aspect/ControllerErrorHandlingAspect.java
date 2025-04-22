package com.kandex.regulation.cms.aspect;


import com.kandex.regulation.cms.exception.checked.BaseCheckedException;
import com.kandex.regulation.cms.exception.unchecked.BaseUncheckedException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ControllerErrorHandlingAspect {

    @AfterThrowing(pointcut = "execution(* com.kandex.regulation.cms.controller.*.*(..))", throwing = "exception")
    public void handleError(BaseCheckedException exception) {
        log.error("Exception caught in aspect. Exception: ", exception);
        throw new BaseUncheckedException(exception);
    }
}

