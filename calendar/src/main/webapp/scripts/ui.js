if (window.otms === undefined) {
	window.otms = {};
}

otms.loadScript = function(url) {
	var head = document.getElementsByTagName('head')[0];
	var script = document.createElement('script');
	script.type = 'text/javascript';
	script.src = url;
	head.appendChild(script);
};

otms.namespace = function(namespace) {
	var parts = namespace.split('.');
	var current = window;
	for (var i = 0; i < parts.length; i++) {
		if (current[parts[i]] === undefined) {
			current[parts[i]] = {};
		}
		current = current[parts[i]];
	}
};

otms.extend = function(base, cons, extend) {
	cons.prototype = Object.create(base.prototype);
	cons.prototype.constructor = cons;
	for ( var key in extend) {
		cons.prototype[key] = extend[key];
	}
	return cons;
};

otms.isEmpty = function(str) {
	return str == undefined || str == null || str === '';
};

otms.UIUtil = {};

otms.UIUtil.hidden = function(element) {
	return (element.offsetParent === null);

};

otms.DateUtil = {};

otms.DateUtil.time = function(hour, min, am) {
	if (am != undefined) {
		if (hour == 12 && am) {
			hour = 0;
		} else if (!am) {
			hour += 12;
		}
	}
	return {
		'hour' : hour,
		'minute' : min,
		'total' : hour * 60 + min
	};
};
otms.DateUtil.date = function(month, date, year) {
	if (year === undefined) {
		year = new Date().getFullYear();
	}
	return new Date(year, month - 1, date);
};