$(function() {
	var calendar = new otms.ui.calendar.Calendar($('#container'));

	calendar.onAddButton = function() {

		switch (otms.auth.userType()) {
		case 'client':
		case 'admin':
			// Go to setup meeting page
			otms.clearPageParam('eventId');
			otms.clearPageParam('lessonId');
			window.location = 'lesson.html';
			break;
		case 'tutor':
			otms.ui.MessageBox.warn($('#errmsg_panel'),
					'You are not allowed to schedule new lesson');
			break;
		}
	};

	calendar.eventRenderer = function(eventDiv, eventObj) {
		var eventContent = '';

		switch (otms.auth.userType()) {
		case 'tutor':
			eventContent = otms.DateUtil.formattime(eventObj.fromTime)
					+ " with " + eventObj.clientName;
			break;
		case 'client':
			eventContent = otms.DateUtil.formattime(eventObj.fromTime)
					+ " with " + eventObj.tutorName;
			break;
		case 'admin':
			eventContent = otms.DateUtil.formattime(eventObj.fromTime) + ", "
					+ eventObj.tutorName + " with " + eventObj.clientName;
			break;
		default:
			break;
		}
		eventDiv.append(eventContent);
		eventDiv.prop('title', eventContent);
		// For event in the past, update background color
		if (eventObj.past) {
			eventDiv.addClass('calendar_event_past');
		}
	};

	calendar.onEventClick = function(obj, event) {
		// Show a dialog
		var dialog = new otms.ui.calendar.ChooseEventDialog($('#container'));
		var event = obj.eventData;
		if (event.type === 'lesson') {
			dialog.onConfirm = function() {
				var checked = dialog.panel.find(
						'input[name="modify_type"]:checked').val();
				if (checked == 'meeting') {
					sessionStorage.setItem('otms.lessonPage.id', event.id);
					window.location = 'lesson.html';
				} else {
					sessionStorage.setItem('otms.lessonItemPage.lessonId',
							event.id);
					sessionStorage.setItem('otms.lessonItemPage.lessonDate',
							event.date.getTime());
					window.location = 'lesson_item.html';
				}
			};
			dialog.show();
		} else {
			// Show lesson_item page
			sessionStorage.setItem('otms.lessonItemPage.id', event.id);
			window.location = 'lesson_item.html';
		}
	};

	calendar.model.load = function(fromDate, toDate) {
		var model = this;
		var calendar = this.calendar;
		var callback = function(success, data) {
			if (success) {
				model.setData(data);
			} else {
				// Make sure a correct calendar is displayed even on error
				model.setData([]);
			}
		};
		CalendarService.getEvents(otms.auth.req({
			'fromDate' : fromDate,
			'toDate' : toDate
		}), otms.ui.MessageBox.errhandler($('#errmsg_panel'), callback));

	};

	calendar.model.setDate(new Date());
});