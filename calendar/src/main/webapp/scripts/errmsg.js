otms.namespace('otms.ErrorMsg');

otms.ErrorMsg.msg = function(code, locale) {
	if (locale === undefined) {
		locale = 'en_US';
	}
	var result = otms.ErrorMsg.data[locale][String(code)];
	if (undefined == result) {
		result = otms.ErrorMsg.defaultmsg[locale] + code;
	}
	return result;
};

otms.ErrorMsg.data = {
	'en_US' : {
		"101" : "No such tutor",
		"201" : "User id has been used",
		"202" : "User email has been used"
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