function client_clicked(event, client_id) {
	otms.UIUtil.stopMessage(event);
	sessionStorage.setItem("otms.clientInfoPage.clientId", client_id);
	window.location = 'client_info.html';
};

function tutor_clicked(event, tutor_id) {
	debugger;
	otms.UIUtil.stopMessage(event);
	sessionStorage.setItem("otms.tutorInfoPage.tutorId", tutor_id);
	window.location = 'tutor_info.html';
};

function lessonListRenderer() {
	var userType = otms.auth.userType();
	return function(content, item) {
		var titleSpan = $(document.createElement('span'));
		titleSpan.addClass('list_row_title');
		titleSpan.append(item.title);
		content.append(titleSpan);

		if (userType === 'tutor') {
			content.append(", requested by ");

			var link = $(document.createElement('a'));
			link
					.attr('onclick', 'client_clicked(event, ' + item.clientId
							+ ')');
			link.append(item.clientName);
			content.append(link);

			content.append('. ');
		} else {
			content.append(" with ");

			var link = $(document.createElement('a'));
			link.attr('onclick', 'tutor_clicked(event, ' + item.tutorId + ')');
			link.append(item.tutorName);
			content.append(link);

			content.append('. ');
		}
		// Time info
		var timeSpan = $(document.createElement("span"));
		timeSpan.addClass('list_row_time');
		timeSpan.append(otms.DateUtil.formattime(item.fromTime));
		timeSpan.append(" to ");
		timeSpan.append(otms.DateUtil.formattime(item.toTime));
		timeSpan.append(", ");
		if (item.repeat) {
			timeSpan.append(otms.DateUtil.formatdate(item.repeatFromDate));
			timeSpan.append(" to ");
			timeSpan.append(otms.DateUtil.formatdate(item.repeatToDate));

			timeSpan.append(", every ");

			timeSpan.append(otms.DateUtil.formatweekday(item.weekrepeat));
		} else {
			timeSpan.append(otms.DateUtil.formatdate(item.oneoffDate));
		}
		content.append(timeSpan);
	};
};

function lessonItemListRenderer() {
	var userType = otms.auth.userType();
	return function(content, item) {
		var titleSpan = $(document.createElement('span'));
		titleSpan.addClass('list_row_title');
		titleSpan.append(item.title);
		content.append(titleSpan);

		if (userType === 'tutor') {
			content.append(", requested by ");

			var link = $(document.createElement('a'));
			link
					.attr('onclick', 'client_clicked(event, ' + item.clientId
							+ ')');
			link.append(item.clientName);
			content.append(link);

			content.append('. ');
		} else {
			content.append(" with ");

			var link = $(document.createElement('a'));
			link.attr('onclick', 'tutor_clicked(event, ' + item.tutorId + ')');
			link.append(item.tutorName);
			content.append(link);

			content.append('. ');
		}
		// Time info
		var timeSpan = $(document.createElement("span"));
		timeSpan.addClass('list_row_time');
		timeSpan.append(otms.DateUtil.formattime(item.fromTime));
		timeSpan.append(" to ");
		timeSpan.append(otms.DateUtil.formattime(item.toTime));
		timeSpan.append(", ");

		timeSpan.append(otms.DateUtil.formatdate(item.date));
		timeSpan.append('.');
		content.append(timeSpan);

		// Feedback status

		var feedbackSpan = $(document.createElement('span'));
		feedbackSpan.css('color', 'gray');
		if (userType == 'tutor') {
			switch (item.feedbackStatus) {
			case 'NO_FEEDBACK':
				feedbackSpan.append(' Feedback not given.');
				break;
			default:
				break;
			}
		} else {
			switch (item.feedbackStatus) {
			case 'NO_FEEDBACK':
				feedbackSpan.append(' Feedback needed.');
				break;
			default:
				break;
			}
		}
		content.append(feedbackSpan);
	};
};

function lessonEventListRenderer() {
	var userType = otms.auth.userType();
	return function(container, event) {
		var titleSpan = $(document.createElement('span'));
		titleSpan.addClass('list_row_title');
		titleSpan.append(event.title);
		container.append(titleSpan);

		container.append(", with ");
		if (userType === 'tutor') {
			var link = $(document.createElement('a'));
			link.attr('onclick', 'client_clicked(event, ' + event.clientId
					+ ')');
			link.append(event.clientName);
			container.append(link);
			container.append('. ');
		} else {
			var link = $(document.createElement('a'));
			link.attr('onclick', 'tutor_clicked(event, ' + event.tutorId + ')');
			link.append(event.tutorName);
			container.append(link);
			container.append('. ');
		}
		// Time info
		var timeSpan = $(document.createElement("span"));
		timeSpan.addClass('list_row_time');
		timeSpan.append(otms.DateUtil.formattime(event.fromTime));
		timeSpan.append(" to ");
		timeSpan.append(otms.DateUtil.formattime(event.toTime));
		timeSpan.append(", ");

		timeSpan.append(otms.DateUtil.formatdate(event.date));

		container.append(timeSpan);

		container.prop('dataItem', event);
	};
};

function viewLessonDetail(event, lesson) {
	otms.UIUtil.stopMessage(event);
	debugger;
};

function viewLessonItemDetail(event, lessonItem) {
	otms.UIUtil.stopMessage(event);
	var usertype = otms.auth.userType();
	if (usertype == 'client' && lessonItem.feedbackStatus == 'NO_FEEDBACK') {
		otms.setPageParam('otms.feedbackPage.itemId', lessonItem.itemId);
		window.location = 'client_feedback.html';
	} 
	if(usertype == 'tutor' && lessonItem.feedbackStatus == 'CLIENT_FEEDBACK') {
		otms.setPageParam('otms.feedbackPage.itemId', lessonItem.itemId);
		window.location = 'tutor_feedback.html';
	}
};