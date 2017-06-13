function requestWithLang(lang) {
	// A flag for pages that cannot be executed twice
	otms.headerPage.needReload = true;
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

function changeLang(item) {
	var lang = $(item).val();
	requestWithLang(lang);
};

function handleLang() {
	// Set the language to be selected
	var lang = localStorage.getItem('otms.lang');
	var serverLang = $('meta[name=lang]').attr("content");

	if (otms.isEmpty(lang)) {
		lang = 'en_US';
	}
	if (otms.isEmpty(serverLang)) {
		serverLang = 'en_US';
	}

	if (lang != serverLang) {
		// Always use local lang, Resend current request
		requestWithLang(lang);
		return;
	}

	if (!otms.isEmpty(lang) && $('#language_select').val() != lang) {
		$("#language_select").val(lang);
	}
};