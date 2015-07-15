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

otms.namespace('otms.ui');

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

otms.get = function(object, key) {
	var parts = key.split('.');
	var current = object;
	for (var i = 0; i < parts.length; i++) {
		var part = parts[i];
		var index = parseInt(part);
		if (current === undefined)
			return undefined;
		// In get value case, keep going down
		if (isNaN(index)) {
			current = current[part];
		} else {
			current = current[index];
		}
	}
	return current;
};

otms.set = function(object, key, value) {
	// debugger;
	var parts = key.split('.');
	var current = object;
	for (var i = 0; i < parts.length; i++) {
		var part = parts[i];

		var index = parseInt(part);

		if (i == parts.length - 1) {
			// Set value and coming to the last one
			if (isNaN(index)) {
				current[part] = value;
			} else {
				current[index] = value;
			}
		} else {
			// In get value case, keep going down
			var next = isNaN(index) ? current[part] : current[index];
			if (next == undefined) {
				if (isNaN(parseInt(parts[i + 1]))) {
					// Next is a key
					next = {};
				} else {
					next = [];
				}
				if (isNaN(index)) {
					current[part] = next;
				} else {
					current[index] = next;
				}
			}
			current = next;
		}
	}
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
