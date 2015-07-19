package org.harper.otms.common.jpa;

import org.springframework.transaction.interceptor.TransactionInterceptor;

public class OTMSTransactionInterceptor extends TransactionInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3188140301472254337L;

	@Override
	protected void commitTransactionAfterReturning(TransactionInfo txInfo) {
		if (txInfo != null && txInfo.hasTransaction()) {
			if (logger.isTraceEnabled()) {
				logger.trace("Completing transaction for ["
						+ txInfo.getJoinpointIdentification() + "]");
			}
			if (txInfo.getTransactionAttribute().isReadOnly()) {
				txInfo.getTransactionManager().rollback(
						txInfo.getTransactionStatus());
			} else {
				txInfo.getTransactionManager().commit(
						txInfo.getTransactionStatus());
			}
		}
	}
}
