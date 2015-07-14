/**
 * Event Example: {'id': 1, 'text':'abc'};
 */
otms.namespace('otms.ui.calendar');

otms.ui.calendar.CalendarModel = function() {
	this.getEvents = function(date) {
		var result = new Array();
		/*
		// Test
		if (date.getMonth() == new Date().getMonth()) {
			for (var i = 0; i < date.getDay(); i++) {
				var obj = {
					'id' : i,
					'text' : '3pm Go to hell'
				};
				result.push(obj);
			}
		}*/
		return result;
	};
};

otms.ui.calendar.ChooseEventDialog = otms.extend(otms.ui.dialog.Dialog,
		function(parent) {
			otms.ui.dialog.Dialog.call(this, parent);
			this.setSize(250, 0);
		}, {
			drawTitle : function(titlePanel) {
				titlePanel.append('What do you want to open?');
			},
			drawContent : function(contentPanel) {
				var label1 = $(document.createElement('label'));
				var input1 = $(document.createElement('input'));
				input1.attr('name', 'modify_type');
				input1.attr('value', 'meeting');
				input1.attr('type', 'radio');
				input1.attr('checked', 'true');
				label1.append(input1);
				label1.append('The lesson');
				contentPanel.append(label1);

				var label2 = $(document.createElement('label'));
				var input2 = $(document.createElement('input'));
				input2.attr('name', 'modify_type');
				input2.attr('value', 'item');
				input2.attr('type', 'radio');
				label2.append(input2);
				label2.append('This occurance');
				contentPanel.append(label2);

			}
		});

otms.ui.calendar.Calendar = function(container) {
	this.container = container;
	this.container.addClass('calendar_container');
	this.date = new Date();
	this.monthName = [ 'January', 'February', 'March', 'April', 'May', 'June',
			'July', 'August', 'Sepetember', 'October', 'November', 'December' ];
	this.weekName = [ 'Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat' ];
	this.model = new otms.ui.calendar.CalendarModel();

	// Create Structure
	this.initialize = function() {
		var calendar = this;
		var titleDiv = $(document.createElement("div"));
		titleDiv.attr('itemId', 'titleDiv');
		titleDiv.addClass('calendar_title');

		var prevButton = $(document.createElement('button'));
		prevButton.append('<');
		prevButton.addClass('calendar_btn');
		prevButton.click(function() {
			var curdate = calendar.date;
			curdate.setMonth(curdate.getMonth() - 1);
			calendar.refresh();
		});
		titleDiv.append(prevButton);

		var nextButton = $(document.createElement('button'));
		nextButton.append('>');
		nextButton.addClass('calendar_btn');
		nextButton.click(function() {
			var curdate = calendar.date;
			curdate.setMonth(curdate.getMonth() + 1);
			calendar.refresh();
		});
		titleDiv.append(nextButton);

		var todayButton = $(document.createElement('button'));
		todayButton.append('Today');
		todayButton.addClass('calendar_btn');
		todayButton.click(function() {
			calendar.date = new Date();
			calendar.refresh();
		});
		titleDiv.append(todayButton);

		var titleLabel = $(document.createElement('span'));
		titleLabel.attr('itemId', 'titleLabel');
		titleDiv.append(titleLabel);

		var addButton = $(document.createElement('button'));
		addButton.append('+');
		addButton.addClass('calendar_btn');
		addButton.css('float', 'right');
		addButton.click(function(event) {
			calendar.onAddButton();
		});
		titleDiv.append(addButton);

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
		var calendar = this;
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

				// Draw events
				var todayEvents = this.model.getEvents(dtext.date);
				if (todayEvents.length == 0) {
					itemDiv.append('&nbsp;');
				} else {
					var max = Math.min(3, todayEvents.length);

					for (var e = 0; e < max; e++) {
						var eventObj = todayEvents[e];
						var eventDiv = $(document.createElement('div'));
						eventDiv.prop('eventId', eventObj.id);
						eventDiv.addClass('calendar_event');
						eventDiv.click(function(event) {
							calendar.onEventClick(this, event);
						});
						eventDiv.append(eventObj.text);
						itemDiv.append(eventDiv);
					}
					// Add a 'View More' Link
					if (max < todayEvents.length) {
						var viewLink = $(document.createElement('a'));
						viewLink.append('+View ' + (todayEvents.length - max)
								+ ' More');
						viewLink.prop('calendarItem', itemDiv);
						viewLink.click(function(event) {
							calendar.onItemClick(this.calendarItem, event);
						});
						itemDiv.append(viewLink);
					}
				}

				// Not current month
				if (dtext.notcurrent) {
					dateTextDiv.addClass('calendar_item_notcurrent');
				} else {
					dateTextDiv.removeClass('calendar_item_notcurrent');
				}
				// Today
				var today = new Date();
				today.setHours(0, 0, 0, 0);
				if (dtext.date.toDateString() == today.toDateString()) {
					itemDiv.addClass('calendar_item_today');
				} else {
					itemDiv.removeClass('calendar_item_today');
				}
				// Old days
				if (today.getTime() > dtext.date.getTime()) {
					itemDiv.addClass('calendar_item_old');
				} else {
					itemDiv.removeClass('calendar_item_old');
				}
			}
		}
		this.popupDiv.css('visibility', 'hidden');
	};

	this.onItemClick = function(jqobj, event) {
		var obj = jqobj.get()[0];
		debugger;
		// Display the popup div at the location of current
		// item
		var visible = this.popupDiv.css('visibility');
		var calendar = this;
		if (visible == 'visible') {
			this.popupDiv.css('visibility', 'hidden');
		} else {
			var popupDiv = this.popupDiv;

			// Move to the clicked item
			popupDiv.css('left', obj.offsetLeft);
			popupDiv.css('top', obj.offsetTop);
			var titleDiv = popupDiv.find('[itemId="popupTitleDiv"]');
			var contentDiv = popupDiv.find('[itemId="popupContentDiv"]');

			titleDiv.empty();
			titleDiv.append(obj.date.toDateString());

			contentDiv.empty();
			var todayEvents = this.model.getEvents(obj.date);
			if (todayEvents.length == 0) {
				contentDiv.append('&nbsp;');
			} else {
				for (var e = 0; e < todayEvents.length; e++) {
					var eventObj = todayEvents[e];
					var eventDiv = $(document.createElement('div'));
					eventDiv.addClass('calendar_event');
					eventDiv.append(eventObj.text);
					eventDiv.prop('eventId', eventObj.id);
					eventDiv.click(function(event) {
						calendar.onEventClick(this, event);
					});
					contentDiv.append(eventDiv);
				}
			}
			popupDiv.css('visibility', 'visible');
		}
	};

	this.onEventClick = function(obj, event) {

	};

	this.onAddButton = function() {

	};

	// Display the calendar
	this.initialize();
	this.date = new Date();
	this.refresh();
};