otms.namespace('otms.ErrorMsg');

otms.ErrorMsg.msg = function(code, locale) {
	if (locale === undefined) {
		locale = 'en_US';
	}
	var result = otms.ErrorMsg.data[locale][String(code)];
	if (undefined == result) {
		result = otms.ErrorMsg.defaultmsg[locale] + code;
	}
	// If session related, clear old token
	if (code >= 200 && code < 300) {
		console.log("Session related error, clear token");
		otms.auth.cleartoken();
	}

	return result;
};

otms.ErrorMsg.data = {
	'en_US' : {
		"101" : "The user id has been used",
		"102" : "The user email has been used",
		"103" : "User name doesn't exists",
		"201" : "Session has expired. Please re-login",
		"202" : "Please login first",
		"1101" : "Cannot find the tutor",
		"1401" : "Data not found",
		"1501" : "No such user",
		"1502" : "You are not allowed to access the information"
	},
	'zh_CN' : {
		"101" : "没有找到这个教师",
		"201" : "该用户名已被使用",
		"202" : "该电子邮件地址已被使用"
	}
};

otms.ErrorMsg.defaultmsg = {
	'en_US' : 'Server error: error code ',
	'zh_CN' : '服务器返回错误代码'
};