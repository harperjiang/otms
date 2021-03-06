otms.namespace('otms.lang');

otms.lang.current = function(event) {
	var lang = localStorage.getItem('otms.lang');

	if (otms.isEmpty(lang)) {
		lang = 'en_US';
	}
	return lang;
};

otms.lang.requestWithLang = function(lang) {
	localStorage.setItem("otms.lang", lang);
	var locstring = window.location.toString().split('?');
	var url = locstring[0];
	if (locstring.length == 2) {
		// keep the parameter
		var params = locstring[1].split('&');
		var paramobj = {};
		for ( var index in params) {
			var param = params[index].split('=');
			paramobj[param[0]] = param[1];
		}
		paramobj['lang'] = lang;
		window.location = url + '?' + $.param(paramobj);
	} else {
		window.location = url + "?lang=" + lang;
	}
};

otms.lang.callbacks = [];

otms.lang.handleLang = function(lang) {
	// Set the language to be selected
	if (lang === undefined)
		lang = localStorage.getItem('otms.lang');

	var serverLang = $('meta[name=lang]').attr("content");

	if (otms.isEmpty(lang)) {
		lang = 'en_US';
	}
	if (otms.isEmpty(serverLang)) {
		serverLang = 'en_US';
	}

	if (lang != serverLang) {
		// Always use local lang, Resend current request
		otms.lang.requestWithLang(lang);
		return;
	}

	if (!otms.isEmpty(lang)) {
		for ( var i in otms.lang.callbacks)
			otms.lang.callbacks[i](lang);
	}
};