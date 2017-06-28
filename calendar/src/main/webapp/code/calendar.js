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
	};

	calendar.onEventClick = function(obj, event) {
		var event = obj.eventData;
		debugger;
		if (event.type === 'lesson') {
			var dialog = $('#open_lesson_dialog');
			dialog.prop('event', event);
			dialog.modal('show');
		} else {
			// Show lesson_item page
			otms.clearPageParam('otm.lessonItemPage');
			otms.setPageParam('otms.lessonItemPage.id', event.id);
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

	$('#create_lesson_dialog_confirm_btn')
			.click(
					function(e) {
						otms.clearPageParam('otms.lessonItemPage');
						otms.clearPageParam('otms.lessonPage');
						
						var dialog = $('#open_lesson_dialog');
						dialog.modal('hide');
						var event = dialog.prop('event');
						var checked = $('#open_lesson_dialog').find(
								'input[name="openType"]:checked').val();
						if (checked == 'lesson') {
							otms.setPageParam('otms.lessonPage.id', event.id);
							window.location = 'lesson.html';
						} else {
							otms.setPageParam('otms.lessonItemPage.lessonId',
									event.id);
							otms.setPageParam('otms.lessonItemPage.lessonDate',
									event.date.getTime());
							window.location = 'lesson_item.html';
						}
					});
});