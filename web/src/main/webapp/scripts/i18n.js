otms.namespace('otms.i18n');

otms.i18n.langs = {};

otms.i18n.langs.en_US = {
	'weekday' : [ 'Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday',
			'Friday', 'Saturday' ],
	'monthName' : [ 'January', 'February', 'March', 'April', 'May', 'June',
			'July', 'August', 'Sepetember', 'October', 'November', 'December' ],
	'calendarBtn' : [ 'Prev Month', 'Today', 'Next Month','Book New Lesson' ]
};

otms.i18n.langs.zh_CN = {
	'weekday' : [ '周日', '周一', '周二', '周三', '周四',
			'周五', '周六' ],
	'monthName' : [ 'January', 'February', 'March', 'April', 'May', 'June',
			'July', 'August', 'Sepetember', 'October', 'November', 'December' ],
	'calendarBtn' : [ '上个月', '本日', '下个月','添加课程' ]
};

otms.i18n.get = function(key, lang) {
	return otms.i18n.langs[lang][key];
};