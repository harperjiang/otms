function CalendarModel() {
	this.getEvents = function(date) {
		return new Array();
	};
};

function Calendar(container) {

	this.container = container;
	this.container.addClass('calendar_container');
	this.date = new Date();
	this.monthName = [ 'January', 'February', 'March', 'April', 'May', 'June',
			'July', 'August', 'Sepetember', 'October', 'November', 'December' ];
	this.weekName = [ 'Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat' ];
	this.model = new CalendarModel();

	// Create Structure
	this.initialize = function() {

		var titleDiv = $(document.createElement("div"));
		titleDiv.attr('itemId', 'titleDiv');
		titleDiv.addClass('calendar_title');

		var prevButton = $(document.createElement('button'));
		prevButton.append('<');
		prevButton.addClass('calendar_btn');
		prevButton.prop('calendar', this);
		prevButton.click(function() {
			var curdate = this.calendar.date;
			curdate.setMonth(curdate.getMonth() - 1);
			this.calendar.refresh();
		});
		titleDiv.append(prevButton);

		var nextButton = $(document.createElement('button'));
		nextButton.append('>');
		nextButton.addClass('calendar_btn');
		nextButton.prop('calendar', this);
		nextButton.click(function() {
			var curdate = this.calendar.date;
			curdate.setMonth(curdate.getMonth() + 1);
			this.calendar.refresh();
		});
		titleDiv.append(nextButton);

		var todayButton = $(document.createElement('button'));
		todayButton.append('Today');
		todayButton.prop('calendar', this);
		todayButton.addClass('calendar_btn');
		todayButton.click(function() {
			this.calendar.date = new Date();
			this.calendar.refresh();
		});
		titleDiv.append(todayButton);

		var titleLabel = $(document.createElement('span'));
		titleLabel.attr('itemId', 'titleLabel');
		titleDiv.append(titleLabel);

		this.container.append(titleDiv);

		// Generate Column Headers
		for (var i = 0; i < 7; i++) {
			var columnDiv = $(document.createElement('div'));
			columnDiv.attr('itemId', 'column_' + i);
			columnDiv.addClass('calendar_column');
			this.container.append(columnDiv);

			var columnTitleDiv = $(document.createElement('div'));
			columnTitleDiv.addClass('calendar_column_header');
			columnTitleDiv.append(this.weekName[i]);
			columnDiv.append(columnTitleDiv);

			if (i == 6) {
				columnTitleDiv.addClass('calendar_item_end');
			}

			for (var j = 0; j < 6; j++) {
				var itemDiv = $(document.createElement('div'));
				itemDiv.addClass('calendar_item');
				itemDiv.attr('itemId', 'item_' + i + '_' + j);
				itemDiv.prop('calendar', this);
				// Display a popup window for event list
				itemDiv
						.click(function(event) {
							// Display the popup div at the location of current
							// item
							var visible = this.calendar.popupDiv
									.css('visibility');
							if (visible == 'visible') {
								this.calendar.popupDiv.css('visibility',
										'hidden');
							} else {
								var popupDiv = this.calendar.popupDiv;

								popupDiv.css('left', this.offsetLeft);
								popupDiv.css('top', this.offsetTop);
								var titleDiv = popupDiv
										.find('[itemId="popupTitleDiv"]');
								var contentDiv = popupDiv
										.find('[itemId="popupContentDiv"]');

								titleDiv.empty();
								titleDiv.append(this.date.toDateString());

								contentDiv.empty();
								var todayEvents = this.calendar.model
										.getEvents(this.date);
								if (todayEvents.length == 0) {
									contentDiv.append('&nbsp;');
								} else {
									for (var e = 0; e < todayEvents.length; e++) {
										var eventObj = todayEvents[e];
										var eventDiv = $(document
												.createElement('div'));
										eventDiv.addClass('calendar_event');
										eventDiv.append(eventObj.text);
										contentDiv.append(eventDiv);
									}
								}

								popupDiv.css('visibility', 'visible');
							}
						});
				var itemDateDiv = $(document.createElement('div'));
				itemDateDiv.addClass('calendar_item_date');

				itemDateDiv.attr('itemId', 'dateDiv');

				itemDiv.append(itemDateDiv);
				if (i == 6) {
					itemDiv.addClass('calendar_item_end');
				}

				columnDiv.append(itemDiv);
			}
		}
		this.popupDiv = $(document.createElement('div'));
		this.popupDiv.addClass('calendar_popup');
		this.popupDiv.attr('itemId', 'popupDiv');
		this.popupDiv.css('z-index', '1');
		this.popupDiv.css('visibility', 'hidden');
		this.container.append(this.popupDiv);

		var popupTitleDiv = $(document.createElement('div'));
		popupTitleDiv.attr('itemId', 'popupTitleDiv');
		popupTitleDiv.addClass('calendar_popup_title');
		popupTitleDiv.append('Abc');
		this.popupDiv.append(popupTitleDiv);

		var popupContentDiv = $(document.createElement('div'));
		popupContentDiv.attr('itemId', 'popupContentDiv');
		popupContentDiv.addClass('calendar_popup_content');
		this.popupDiv.append(popupContentDiv);
	};

	this.dateText = function(weekday, j) {
		var dateref = new Date(this.date);
		dateref.setDate(1);
		// The first date of the month
		var week = dateref.getDay();
		// Day 1 is (0, week)
		dateref.setMonth(dateref.getMonth() + 1, 0);
		var dateIndex = j * 7 + weekday - week + 1;
		dateref.setDate(dateIndex);
		dateIndex = dateref.getDate();
		return {
			'text' : dateIndex + '',
			'date' : new Date(dateref),
			'notcurrent' : dateref.getMonth() != this.date.getMonth()
		};
	};

	this.refresh = function() {
		var month = this.monthName[this.date.getMonth()];
		var year = this.date.getFullYear();
		var title = month + ' ' + year;
		var titleLabel = this.container.find('[itemId="titleLabel"]');
		titleLabel.empty();
		titleLabel.append(title);

		for (var i = 0; i < 7; i++) {
			for (var j = 0; j < 6; j++) {
				var dtext = this.dateText(i, j);
				var itemDiv = this.container.find('[itemId="item_' + i + '_'
						+ j + '"]');
				var dateTextDiv = itemDiv.find('[itemId="dateDiv"]');
				dateTextDiv.empty();
				dateTextDiv.append(dtext.text);
				itemDiv.prop('date', dtext.date);

				itemDiv.empty();
				itemDiv.append(dateTextDiv);

				var todayEvents = this.model.getEvents(this.date);
				if (todayEvents.length == 0) {
					itemDiv.append('&nbsp;');
				} else {
					for (var e = 0; e < todayEvents.length; e++) {
						var eventObj = todayEvents[e];
						var eventDiv = $(document.createElement('div'));
						eventDiv.addClass('calendar_event');
						eventDiv.append(eventObj.text);
						itemDiv.append(eventDiv);
					}
				}

				if (dtext.notcurrent) {
					dateTextDiv.addClass('calendar_item_notcurrent');
				} else {
					dateTextDiv.removeClass('calendar_item_notcurrent');
				}
				// Today
				if (dtext.date.toDateString() == new Date().toDateString()) {
					itemDiv.addClass('calendar_item_today');
				} else {
					itemDiv.removeClass('calendar_item_today');
				}
			}
		}
		this.popupDiv.css('visibility', 'hidden');
	};

	// Display the calendar
	this.initialize();
	this.date = new Date();
	this.refresh();

	/*
	 * // Test Event Display var eventDiv = $(document.createElement('div'));
	 * eventDiv.addClass('calendar_event'); eventDiv.append("3pm Meet with
	 * Josh"); eventDiv.draggable(); this.container.find('[itemId =
	 * "item_2_1"]').append(eventDiv);
	 * 
	 * var eventDiv2 = $(document.createElement('div'));
	 * eventDiv2.addClass('calendar_event'); eventDiv2 .append("3pm This is a
	 * really long title of event. It cannot display all of it so we will have
	 * to truncate"); eventDiv2.draggable(); this.container.find('[itemId =
	 * "item_2_1"]').append(eventDiv2);
	 */
}