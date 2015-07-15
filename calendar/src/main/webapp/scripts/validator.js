otms.namespace('otms.validator');

otms.validator.ValidatorBase = function(control) {
	var validator = this;
	control.prop('validator', this);
	this.control = control;
	this.checked = false;
	this.success = false;

	control.focusout(function(event) {
		validator.trigger();
	});
};

otms.validator.ValidatorBase.prototype.trigger = function() {
	var control = this.control;
	if (!this.check(control.val())) {
		control.addClass('validate_fail');
		this.oldTooltip = control.attr('title');
		control.attr('title', this.message());
		this.success = false;
	} else {
		control.removeClass('validate_fail');
		control.attr('title', this.oldTooltip);
		this.success = true;
	}
	this.checked = true;
};

otms.validator.ValidatorBase.prototype.force = function(message) {
	var control = $(this.control);
	control.addClass('validate_fail');
	control.prop('oldTooltip', control.attr('title'));
	control.attr('title', message);
	this.success = false;
	this.message(message);
};

otms.validator.ValidatorBase.prototype.check = function(value) {
	if(this.control.attr('type') == 'checkbox') {
		return this.value(this.control.is(':checked'));
	}
	
	return this.value(value);
};

otms.validator.ValidatorBase.prototype.value = function(arg) {
	if (arg === undefined) {
		if (this.checked) {
			return this.val;
		} else {
			return this.control.val();
		}
	}
	this.val = arg;
	return true;
};

otms.validator.ValidatorBase.prototype.message = function(arg) {
	if (arg === undefined)
		return this.msg;
	this.msg = arg;
	return false;
};

// Use Regular Expression to validate input
otms.validator.RegexValidator = function(target, regex) {
	var validator = this;
	this.target = target;
	this.regex = regex;

	this.check = function(event) {
		var cregex = new RegExp(regex);
		var content = this.target.val();

		if (!cregex.test(content)) {
			this.target.addClass('validate_fail');
		} else {
			this.target.removeClass('validate_fail');
		}
	};

	this.target.focusout(function(event) {
		validator.check(event);
	});
};

otms.validator.NonemptyValidator = otms.extend(otms.validator.ValidatorBase,
		function(val) {
			otms.validator.ValidatorBase.call(this, val);
		}, {
			check : function(content) {
				if (content == undefined || content == '') {
					return this.message('The field cannot be left empty');
				}
				return this.value(content);
			}
		});

// Accepted time format:
// 0 12 13 0:30 3am 5pm 3:01pm

otms.validator.TimeValidator = otms.extend(otms.validator.ValidatorBase,
		function(val) {
			otms.validator.ValidatorBase.call(this, val);
		}, {
			check : function(content) {
				if (otms.isEmpty(content)) {
					return this.message('The field cannot be left empty');
				}
				var apmMatch = content.toLowerCase().match('(am|pm)$');
				var withApm = (apmMatch != null);
				var am = false;
				if (withApm) {
					am = apmMatch[0] == 'am';
					content = content.substr(0, content.length - 2);
				} else {
					am = undefined;
				}
				var single = new RegExp('^(\\d{1,2})$');
				var twoparts = new RegExp('^(\\d{1,2}):(\\d{2})');

				var smresult = content.match(single);
				if (smresult != null) {
					var num = parseInt(smresult[1]);
					if (withApm ? (num <= 12) : (num <= 23)) {
						// Form a time
						return this.value(otms.DateUtil.time(num, 0, am));
					} else {
						return this.message('Incorrect time value');
					}
				}

				var tmresult = content.match(twoparts);
				if (tmresult != null) {
					var hour = parseInt(tmresult[1]);
					var min = parseInt(tmresult[2]);
					if ((withApm ? (hour <= 12) : (hour <= 23)) && (min < 60)) {
						// Form a time
						return this.value(otms.DateUtil.time(hour, min, am));
					} else {
						return this.message('Incorrect time value');
					}
				}
				return this.message('Incorrect time format');
			}
		});

// Accept 5/23 3/24/15 05/12/2015
otms.validator.DateValidator = otms.extend(otms.validator.ValidatorBase,
		function(val) {
			otms.validator.ValidatorBase.call(this, val);
		}, {
			check : function(content) {
				if (otms.isEmpty(content)) {
					return this.message('The field cannot be left empty');
				}
				var withyear = new RegExp(
						'^(\\d{1,2})/(\\d{1,2})/(\\d{2}|\\d{4})$');
				var monthdate = new RegExp('^(\\d{1,2})/(\\d{1,2})');

				var wyresult = content.match(withyear);
				if (wyresult != null) {
					var month = parseInt(wyresult[1]);
					var date = parseInt(wyresult[2]);
					var year = parseInt(wyresult[3]);

					if (month >= 13 && date >= 32) {
						return this.message('Incorrect date value');
					}
					// Form a date
					var result = otms.DateUtil.date(month, date, year);
					if (result == null)
						return this.message('Incorrect date value');
					return this.value(result);
				}
				var mdresult = content.match(monthdate);
				if (mdresult != null) {
					var month = parseInt(mdresult[1]);
					var date = parseInt(mdresult[2]);

					if (month >= 13 && date >= 32) {
						return this.message('Incorrect date value');
					}
					var result = otms.DateUtil.date(month, date);
					if (result == null)
						return this.message('Incorrect date value');
					return this.value(result);
				}

				return this.message('Incorrect date format');
			}
		});

otms.validator.BeanManager = function() {
	this.components = {};
};

otms.validator.BeanManager.prototype.reg = function(key, component) {
	this.components[key] = component;
	if (component.prop('validator') == undefined) {
		// Install an empty validator
		new otms.validator.ValidatorBase(component);
	}
};

otms.validator.BeanManager.prototype.validate = function(bean, vresult) {
	return true;
};

otms.validator.BeanManager.prototype.fail = function(keys, message) {
	for ( var key in keys)
		this.components[keys[key]].prop('validator').force(message);
};

otms.validator.BeanManager.prototype.getBean = function() {
	var bean = {};
	var vresult = {};
	// Collect data from each validator
	for ( var key in this.components) {
		var comp = this.components[key];
		if (otms.UIUtil.hidden(comp.val()))
			continue;
		var validator = comp.prop('validator');
		if (!validator.checked) {
			validator.trigger();
		}
		otms.set(bean, key, validator.value());
		vresult[key] = validator.success;
	}
	return (this.validate(bean, vresult) ? bean : null);
};