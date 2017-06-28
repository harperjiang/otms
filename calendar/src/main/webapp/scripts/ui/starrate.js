otms.ui.StarRate = function(container) {
	this.container = container;

	new otms.ui.StarRateValidator(container);

	this.fixed = false;
	this.rdonly = false;
	this.container.addClass('starrate_panel');
	this.container.prop('starRateObj', this);

	this.items = [];
	for (var i = 0; i < 5; i++) {
		var item = $(document.createElement('i'));
		item.addClass('fa');
		item.addClass('fa-star-o');
		item.attr('aria-hidden', "true");
		item.prop('starIndex', i + 1);
		this.container.append(item);
		this.items.push(item);
	}

	this.container.mousemove(this.refresh);
	this.container.mouseleave(this.clear);
	this.container.click(this.chooseRate);

	this.setRate(0);
};

otms.ui.StarRate.prototype.readonly = function() {
	this.rdonly = true;
	this.fixed = true;
}

otms.ui.StarRate.prototype.setRate = function(rate) {
	this.rate = rate;

	for (var i = 0; i < 5; i++) {
		this.items[i].removeClass();
		this.items[i].addClass('fa');
	}

	var full = Math.floor(rate);
	var half = (rate - full) >= 0.5 ? 1 : 0;
	var empty = 5 - full - half;

	for (var i = 0; i < full; i++) {
		this.items[i].addClass('fa-star');
	}
	for (var i = full; i < full + half; i++) {
		this.items[i].addClass('fa-star-half-o');
	}
	for (var i = full + half; i < full + half + empty; i++) {
		this.items[i].addClass('fa-star-o');
	}
	this.container.attr('title', otms.FormatUtil.format('{0}/5', rate));
};

otms.ui.StarRate.prototype.reset = function() {
	if (this.rdonly)
		return;
	this.fixed = false;
	this.setRate(0);
};

otms.ui.StarRate.index = function(event) {
	var target = $(event.target);
	var tagname = target.prop('tagName');
	if (target.prop('tagName') == 'I')
		return target.prop('starIndex');
	else {
		var step = Math.floor(target.width() / 5);
		return Math.min(Math.ceil(event.offsetX / step), 5);
	}

}

otms.ui.StarRate.prototype.refresh = function(event) {
	var rateobj = this.starRateObj;
	if (rateobj.fixed == true)
		return;

	rateobj.setRate(otms.ui.StarRate.index(event));
};

otms.ui.StarRate.prototype.clear = function(event) {
	var rateobj = this.starRateObj;
	if (rateobj.fixed == true)
		return;
	rateobj.reset();
};

otms.ui.StarRate.prototype.chooseRate = function(event) {
	var rateobj = this.starRateObj;
	if (rateobj.rdonly)
		return;
	rateobj.setRate(otms.ui.StarRate.index(event));
	rateobj.fixed = true;
};

otms.ui.StarRateValidator = otms.extend(otms.validator.ValidatorBase, function(
		val) {
	otms.validator.ValidatorBase.call(this, val);
}, {
	trigger : function() {
		this.success = true;
	},
	value : function(val) {
		if (val === undefined) {
			var rateobj = this.control.prop('starRateObj');
			if (rateobj.fixed)
				return rateobj.rate;
			return 0;
		} else {
			return true;
		}
	},
	assign : function(newval) {
		if (otms.isEmpty(newval))
			return;
		this.control.prop('starRateObj').setRate(newval);
	}
});