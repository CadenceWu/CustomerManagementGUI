package com.springbootaop.customermanagement.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

	@Before("execution(* com.springbootaop.customermanagement.service.CustomerService.*(..))")
	public void beforeMethodExecution(JoinPoint joinPoint) {
		System.out.println("Before executing: " + joinPoint.getSignature().getName());
	}

	@After("execution(* com.springbootaop.customermanagement.service.CustomerService.*(..))")
	public void afterMethodExecution(JoinPoint joinPoint) {
		System.out.println("After executing: " + joinPoint.getSignature().getName());
	}

	@AfterThrowing(pointcut = "execution(* com.springbootaop.customermanagement.service.CustomerService.*(..))", throwing = "error")
	public void afterThrowingAdvice(JoinPoint joinPoint, Throwable error) {
		System.out.println("Method " + joinPoint.getSignature().getName() + " threw exception: " + error.getMessage());
	}
}
