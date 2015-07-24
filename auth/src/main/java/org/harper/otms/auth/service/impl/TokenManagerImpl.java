package org.harper.otms.auth.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.harper.otms.auth.service.TokenManager;
import org.springframework.beans.factory.InitializingBean;

public class TokenManagerImpl implements TokenManager, InitializingBean {

	public class Token {
		int userId;
		String token;
		Date lastAccess;

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public Date getLastAccess() {
			return lastAccess;
		}

		public void setLastAccess(Date lastAccess) {
			this.lastAccess = lastAccess;
		}

	}

	private Map<String, Token> tokens = new HashMap<String, Token>();

	private ReadWriteLock lock = new ReentrantReadWriteLock();

	@Override
	public String acquireToken(int userId) {
		String newToken = UUID.randomUUID().toString();

		Token tokenObj = new Token();
		tokenObj.setUserId(userId);
		tokenObj.setToken(newToken);
		tokenObj.setLastAccess(new Date());

		lock.writeLock().lock();
		try {
			tokens.put(newToken, tokenObj);
		} finally {
			lock.writeLock().unlock();
		}
		return newToken;
	}

	@Override
	public boolean checkToken(int userId, String token) {
		Token tokenObj = null;
		lock.readLock().lock();
		try {
			tokenObj = tokens.get(token);
		} finally {
			lock.readLock().unlock();
		}
		if (tokenObj == null) {
			return false;
		}
		if (tokenObj.getUserId() != userId
				|| !tokenObj.getToken().equals(token)
				|| (new Date().getTime() - tokenObj.getLastAccess().getTime() > 6000000)) {
			lock.writeLock().lock();
			try {
				tokens.remove(token);
			} finally {
				lock.writeLock().unlock();
			}
			return false;
		}
		tokenObj.setLastAccess(new Date());
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		new TokenCleanerThread().start();
	}

	protected class TokenCleanerThread extends Thread {

		public TokenCleanerThread() {
			setName("Token Cleaner");
			setDaemon(true);
		}

		public void run() {
			while (true) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {

				}
				Date now = new Date();
				List<String> candidates = new ArrayList<String>();
				for (Entry<String, Token> entry : tokens.entrySet()) {
					if (entry.getValue().getLastAccess().getTime() <= (now
							.getTime() - 6000000)) {
						candidates.add(entry.getKey());
					}
				}
				lock.writeLock().lock();
				try {
					for (String ttm : candidates) {
						// Check again
						if (tokens.get(ttm) != null
								&& tokens.get(ttm).getLastAccess().getTime() <= now
										.getTime() - 1200000) {
							tokens.remove(ttm);
						}
					}
				} finally {
					lock.writeLock().unlock();
				}
			}
		}
	}
}
