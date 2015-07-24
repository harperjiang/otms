package org.harper.otms.auth.service.impl;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.harper.otms.auth.service.ErrorCode;
import org.harper.otms.auth.service.TokenManager;
import org.harper.otms.common.dto.RequestDto;
import org.harper.otms.common.dto.ResponseDto;
import org.harper.otms.common.service.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenValidator {

	private TokenManager tokenManager;

	Logger logger = LoggerFactory.getLogger(getClass());

	public Object validateToken(ProceedingJoinPoint pjp) throws Throwable {
		if (pjp.getArgs() == null || pjp.getArgs().length == 0) {
			logger.warn("No parameter presents, cannot conduct validation");
		}

		Object firstParam = pjp.getArgs()[0];
		if (firstParam instanceof RequestDto) {
			RequestDto rqd = (RequestDto) firstParam;
			if (rqd.needValidation()) {
				if (rqd.getCurrentUser() == 0) {
					logger.warn("No user info provided.");
					int errCode = ErrorCode.SESSION_NOTLOGIN;
					ResponseDto resp = generateResponse(pjp);
					resp.setErrorCode(errCode);
					resp.setSuccess(false);
					return resp;
				}
				if (!tokenManager.checkToken(rqd.getCurrentUser(),
						rqd.getToken())) {
					// Token check failed
					logger.warn("Token validation failed:"
							+ rqd.getCurrentUser() + " " + rqd.getToken());
					int errCode = ErrorCode.SESSION_EXPIRED;
					ResponseDto resp = generateResponse(pjp);
					resp.setErrorCode(errCode);
					resp.setSuccess(false);
					return resp;
				}
			}
		}

		try {
			Object result = pjp.proceed();
			return result;
		} catch (DataException de) {
			logger.warn("DataException captured in service method", de);
			ResponseDto resp = generateResponse(pjp);
			resp.setErrorCode(de.getErrorCode());
			resp.setSuccess(false);
			return resp;
		} catch (Exception e) {
			logger.warn("Error captured in service method", e);
			ResponseDto resp = generateResponse(pjp);
			resp.setErrorMessage(e.getMessage());
			resp.setSuccess(false);
			return resp;
		}
	}

	private ResponseDto generateResponse(ProceedingJoinPoint pjp)
			throws InstantiationException, IllegalAccessException {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		Object dto = method.getReturnType().newInstance();
		if (dto instanceof ResponseDto) {
			ResponseDto resp = (ResponseDto) dto;
			return resp;
		}
		throw new RuntimeException("Return type is not a ResultDto:"
				+ dto.getClass());
	}

	public TokenManager getTokenManager() {
		return tokenManager;
	}

	public void setTokenManager(TokenManager tokenManager) {
		this.tokenManager = tokenManager;
	}

}
