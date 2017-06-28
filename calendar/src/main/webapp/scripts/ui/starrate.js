otms.ui.StarRate = function(container) {
	this.container = container;

	new otms.ui.StarRateValidator(container);

	this.fixed = false;
	this.rdonly = false;
	// this.container.append("&nbsp;");
	this.container.addClass('starrate_panel');
	// this.container.addClass('starrate_0');
	this.container.prop('starRateObj', this);

	this.container.mousemove(this.refresh);
	this.container.mouseleave(this.clear);
	this.container.click(this.chooseRate);

};

otms.ui.StarRate.prototype.readonly = function() {
	this.rdonly = true;
	this.fixed = true;
}

otms.ui.StarRate.prototype.setRate = function(rate) {
	this.rate = rate;
	// var clazz = otms.FormatUtil.format('starrate_{0}', rate);
	// this.container.removeClass();
	// this.container.addClass('starrate_panel');
	// this.container.addClass(clazz);
	this.container.empty();
	for (var i = 0; i < 5; i++) {
		if (i < rate) {
			this.container
					.append('<i class="fa fa-star" aria-hidden="true"></i>');
		} else {
			this.container
					.append('<i class="fa fa-star-o" aria-hidden="true"></i>');
		}
	}

};

otms.ui.StarRate.prototype.reset = function() {
	if (this.readonly)
		return;
	this.fixed = false;
	//	
	this.setRate(0);
	// this.container.removeClass();
	// this.container.addClass('starrate_panel');
	// this.container.addClass('starrate_0');
};

otms.ui.StarRate.index = function(offsetX) {
	var offset = Math.ceil(offsetX / 20);
	if (offset > 5)
		offset = 5;
	return offset;
};

otms.ui.StarRate.prototype.refresh = function(event) {
	var rateobj = this.starRateObj;
	if (rateobj.fixed == true)
		return;
	rateobj.setRate(otms.ui.StarRate.index(event.offsetX));
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
	rateobj.setRate(otms.ui.StarRate.index(event.offsetX));
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