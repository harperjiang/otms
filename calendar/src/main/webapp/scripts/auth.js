otms.namespace('otms.auth');

otms.auth.token = function(data) {
	if (data === undefined) {
		if (otms.auth.tokenCache !== undefined) {
			return otms.auth.tokenCache;
		}
		var token = localStorage.getItem('otms.token');
		if (token != undefined && token != null) {
			token = otms.json(token);
			otms.auth.tokenCache = token;
			return token;
		}
		return undefined;
	} else {
		localStorage.setItem("otms.token", otms.json({
			"userId" : data.userId,
			"username" : data.username,
			"type" : data.type,
			"token" : data.token
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