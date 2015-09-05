otms.namespace('otms.ErrorMsg');

otms.ErrorMsg.msg = function(code, locale) {
	if (locale === undefined) {
		locale = 'zh_CN';
	}
	var result = otms.ErrorMsg.data[locale][String(code)];
	if (undefined == result) {
		result = otms.ErrorMsg.defaultmsg[locale] + code;
	}
	// If session related, clear old token
	if (code >= 200 && code < 300) {
		console.log("Session related error, clear token");
		otms.auth.cleartoken();
		otms.headerPage.refreshLoginOption();
	}

	return result;
};

otms.ErrorMsg.data = {
	'en_US' : {
		"101" : "The user id has been used",
		"102" : "The user email has been used",
		"103" : "Failed to login. Please check your username and password",
		"104" : "This user is not activated",
		"105" : "This user has been activated",
		"106" : "Unknown source",
		"201" : "Session has expired. Please re-login",
		"202" : "Please login first",
		"1101" : "Cannot find the tutor",
		"1401" : "Data not found",
		"1501" : "No such user",
		"1502" : "You are not allowed to access the information"
	},
	'zh_CN' : {
		"101" : "该用户名已被使用",
		"102" : "该电子邮件地址已被使用",
		"103" : "登录失败，请检查用户名和密码",
		"104" : "该用户未激活",
		"105" : "该用户已激活",
		"106" : "未知的来源",
		"201" : "您太长时间未操作，请重新登录",
		"202" : "请先登录系统再进行此操作",
		"1101" : "找不到指定的信息",
		"1401" : "找不到指定的信息",
		"1501" : "用户不存在",
		"1502" : "您不能查看该信息"
	}
};

otms.ErrorMsg.defaultmsg = {
	'en_US' : 'Server error: error code ',
	'zh_CN' : '服务器返回错误代码'
};

otms.ErrorMsg.invalidAccess = function() {
	// Display warning
	otms.ui.MessageBox.warning($('#errmsg_panel'),
			'Invalid access. Will now go to Index Page');
	setTimeout(function() {
		window.location = 'index.html';
	}, 1000);
};