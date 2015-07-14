package org.harper.otms.common.service;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.harper.otms.common.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandler {
	Logger logger = LoggerFactory.getLogger(getClass());

	public Object checkError(ProceedingJoinPoint pjp) throws Throwable {
		try {
			Object result = pjp.proceed();
			return result;
		} catch (Exception e) {
			logger.warn("Error captured in service method", e);
			MethodSignature signature = (MethodSignature) pjp.getSignature();
			Method method = signature.getMethod();
			Object dto = method.getReturnType().newInstance();
			if (dto instanceof ResponseDto) {
				((ResponseDto) dto).setErrorMessage(e.getMessage());
				((ResponseDto) dto).setSuccess(false);
				return dto;
			}
			throw new RuntimeException("Return type is not a ResultDto:"
					+ dto.getClass());
		}
	}
}
