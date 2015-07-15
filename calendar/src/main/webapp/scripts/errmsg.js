otms.namespace('otms.ErrorMsg');

otms.ErrorMsg.msg = function(code, locale) {
	return otms.ErrorMsg.data[local][code];
};

otms.ErrorMsg.data = {
	'en_US' : {
		"101" : "No such tutor"
	},
	'zh_CN' : {
		"101" : "没有找到这个教师"
	}
};