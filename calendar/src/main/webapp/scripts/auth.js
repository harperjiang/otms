otms.namespace('otms.auth');

otms.auth.validateToken = function(token) {
	// validate the timestamp
	// 30 min
	return (Date.now() - token.timestamp >= 1800000) ? undefined : token;
}

otms.auth.token = function(data) {
	if (data === undefined) {
		var token = undefined;

		var jsonToken = localStorage.getItem('otms.token');
		if (!otms.isEmpty(jsonToken))
			token = otms.json(jsonToken);

		if (!otms.isEmpty(token)) {
			token = otms.auth.validateToken(token);
			return token;
		}
		return undefined;
	} else {
		localStorage.setItem("otms.token", otms.json({
			"userId" : data.userId,
			"username" : data.username,
			"type" : data.type,
			"token" : data.token,
			"timestamp" : Date.now()
		}));
	}
};

otms.auth.cleartoken = function() {
	localStorage.removeItem('otms.token');
	otms.auth.tokenCache = undefined;
};

otms.auth.req = function(request) {
	var token = otms.auth.token();
	if (token !== undefined) {
		request['currentUser'] = token.userId;
		request['token'] = token.token;
		request['userType'] = token.type;
	}
	if (request['currentUser'] == undefined)
		request['currentUser'] = 0;
	if (request['token'] == undefined)
		request['token'] = '';
	if (request['userType'] == undefined)
		request['userType'] = '';
	// Convert all date to string in format yyyy-MM-dd HH:mm:ss
	otms.auth.filter(request);

	// renew local token
	var newtoken = otms.auth.token();
	if (!otms.isEmpty(newtoken)) {
		newtoken.timestamp = Date.now();
		otms.auth.token(newtoken);
	}
	return request;
};

otms.auth.filter = function(bean) {
	for (key in bean) {
		if (Object.prototype.toString.call(bean[key]) === '[object Date]') {
			bean[key] = otms.DateUtil.serverformatdate(bean[key]);
		}
		if (Object.prototype.toString.call(bean[key]) == '[object Object]') {
			otms.auth.filter(bean[key]);
		}
	}
};

otms.auth.isLoggedin = function() {
	return (otms.auth.token() !== undefined);
};

otms.auth.currentUser = function() {
	var token = otms.auth.token();
	if (token != undefined && token != null) {
		return token.userId;
	}
	return undefined;
};

otms.auth.userType = function() {
	var token = otms.auth.token();
	if (token != undefined && token != null) {
		return token.type;
	}
	return undefined;
};

otms.auth.username = function(val) {
	var token = otms.auth.token();
	if (val === undefined) {
		if (token != undefined && token != null) {
			return token.username;
		}
		return undefined;
	} else {
		token['username'] = val;
		otms.auth.token(token);
	}
};