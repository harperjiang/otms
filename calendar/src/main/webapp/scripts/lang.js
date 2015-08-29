otms.namespace('otms.lang');

otms.lang.setLang = function(lang) {
	// The language setting is stored in local storage
	localStorage.setItem("otms.lang", lang);
};

otms.lang.load = function(key) {
	var lang = localStorage.getItem('otms.lang');
	if (otms.isEmpty(lang)) {
		lang = 'default';
	}
	if (otms.lang[lang] !== undefined) {
		return otms.lang[lang][key];
	}

	var url = "../lang/lang_{0}.json"
	url = otms.FormatUtil.format(url, lang);

	$.ajax(url, {
		'async' : false,
		'complete' : function(data) {
			debugger;
			
		}
	});
};