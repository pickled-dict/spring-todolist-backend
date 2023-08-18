package com.pickleddict.springtodolistbackend.annotations;

import com.pickleddict.springtodolistbackend.errors.ApiError;
import com.pickleddict.springtodolistbackend.http.response.MessageResponse;
import com.pickleddict.springtodolistbackend.security.jwt.JwtUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Aspect
@Component
public class ValidJwtAspect {
    @Autowired
    JwtUtils jwtUtils;

    @Around("@annotation(ValidJwt)")
    public Object checkJwtIsValid(ProceedingJoinPoint joinPoint) throws Throwable {
        String jwtToken = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest()
                .getHeader("Authorization");

        if (StringUtils.hasText(jwtToken) && jwtToken.startsWith("Bearer ")) {
            jwtUtils.hardValidate(jwtToken.substring(7));
            return joinPoint.proceed();
        }

        throw new Exception("a badly designed authentication prcedure");
    }
}
