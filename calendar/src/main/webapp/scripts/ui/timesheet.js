otms.namespace('otms.ui.timesheet');

otms.ui.timesheet.TimesheetModel= function() {

	this.data = [];
	for (var i = 0; i < 7; i++) {
		this.data[i] = [];
	}

	this.get = function(i, j) {
		return this.data[i][j];
	};

	this.set = function(i, j, value) {
		this.data[i][j] = value;
	};

	this.setWithKey = function(key, value) {
		var i = parseInt(key.substr(0, 1));
		var j = parseInt(key.substr(2));
		this.set(i, j, value);
	};

	// Convert data to string expressions
	this.getData = function() {
		var exps = [];
		for (var i = 0; i < 7; i++) {
			var exp = "";
			for (var j = 0; j < 48; j++) {
				exp += get(i, j) ? '1' : '0';
			}
			exps[i] = exp;
		}
		return exps;
	};

	// Load data from string expressions
	this.setData = function(exps) {
		for (var i = 0; i < 7; i++) {
			for (var j = 0; j < 48; j++) {
				this.set(i, j, exps[i].charAt(j) == '1');
			}
		}
	};
};

otms.ui.timesheet.Timesheet = function(container) {
	this.container = container;
	this.model = new otms.ui.timesheet.TimesheetModel();
	this.timeFormat = function(input) {
		var am = input < 12;
		var display = input % 12;
		if (display == 0)
			display = 12;
		return {
			'display' : display,
			'am' : am
		};
	};

	this.setItem = function(i, j, value) {
		var local = $('#timesheet_item_' + i + '_' + j);
		setItemInternal(local, value);
	};

	this.setItemInternal = function(local, value) {
		local.removeClass('timesheet_item_a');
		local.removeClass('timesheet_item_na');
		if (value) {
			local.addClass('timesheet_item_a');
		} else {
			local.addClass('timesheet_item_na');
		}
		// Update model
		var key = local.attr('id').substr(15);
		this.model.setWithKey(key, value);
	};

	this.refresh = function() {
		for (var i = 0; i < 7; i++) {
			for (var j = 0; j < 48; j++) {
				setItem(i, j, this.model.get(i, j));
			}
		}
	};

	// Initialize
	this.initialize = function() {
		this.container.addClass("timesheet_container");

		var topleft = $(document.createElement('div'));
		topleft.addClass('timesheet_topleft');
		topleft.append('&nbsp;');
		container.append(topleft);

		var weekday = [ 'Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat' ];
		for (var i = 0; i < 7; i++) {
			var title = weekday[i];
			var column = $(document.createElement("div"));
			column.addClass("timesheet_column");
			column.addClass('timesheet_title');
			if (i == 6) {
				column.addClass('timesheet_end');
			}
			column.append(title);
			container.append(column);
		}

		container.append('<div style="clear:both;"></div>');

		var panel = $(document.createElement("div"));
		panel.addClass('timesheet_frame');
		container.append(panel);

		var leftcol = $(document.createElement("div"));
		leftcol.addClass('timesheet_leftcol');
		panel.append(leftcol);

		for (var i = 0; i < 24; i++) {
			var timeDiv = $(document.createElement('div'));
			timeDiv.addClass('timesheet_lefttime');

			var timeFormat = this.timeFormat(i);

			var am = timeFormat.am;
			var display = timeFormat.display;
			timeDiv.append('<span>' + display + '</span>');

			var flag = $(document.createElement('span'));
			flag.addClass('timesheet_leftflag');
			flag.append(am ? 'AM' : 'PM');
			timeDiv.append(flag);
			leftcol.append(timeDiv);
		}

		for (var i = 0; i < 7; i++) {
			var column = $(document.createElement('div'));
			column.addClass('timesheet_column');
			panel.append(column);
			for (var j = 0; j < 48; j++) {
				var item = $(document.createElement('div'));
				item.addClass('timesheet_item');
				item.append('&nbsp;');
				item.attr('id', 'timesheet_item_' + i + '_' + j);

				var timeFormat = this.timeFormat(((j / 2) | 0));
				item.addClass('timesheet_item_na');
				item.attr('title', weekday[i] + ' ' + timeFormat.display
						+ (j % 2 == 1 ? ':30' : ':00')
						+ (timeFormat.am ? 'AM' : 'PM'));
				item.prop('timesheet', this);
				item.click(function(event) {
					this.timesheet.setItemInternal($(this), $(this).hasClass(
							'timesheet_item_na'));
				});

				column.append(item);
			}
		}
	};

	this.initialize();

}