function client_clicked(event, client_id) {
	otms.UIUtil.stopMessage(event);
	otms.setPageParam("otms.clientInfoPage.clientId", client_id);
	window.location = 'info_client.html';
};

function tutor_clicked(event, tutor_id) {
	otms.UIUtil.stopMessage(event);
	otms.setPageParam("otms.tutorInfoPage.tutorId", tutor_id);
	window.location = 'info_tutor.html';
};

function lessonListRenderer() {
	var userType = otms.auth.userType();
	return function(content, item) {
		var titleSpan = $(document.createElement('span'));
		titleSpan.addClass('list_row_title');
		titleSpan.append(item.title);
		content.append(titleSpan);

		if (userType === 'tutor') {
			content.append(" with ");

			var link = $(document.createElement('a'));
			link
					.attr('onclick', 'client_clicked(event, ' + item.clientId
							+ ')');
			link.append(item.clientName);
			content.append(link);

			content.append('. ');
		} else if (userType === 'client') {
			content.append(" with ");

			var link = $(document.createElement('a'));
			link.attr('onclick', 'tutor_clicked(event, ' + item.tutorId + ')');
			link.append(item.tutorName);
			content.append(link);

			content.append('. ');
		} else if (userType === 'admin') {

			var clientLink = $(document.createElement('a'));
			clientLink.attr('onclick', 'client_clicked(event, ' + item.clientId
					+ ')');
			clientLink.append(item.clientName);
			content.append(clientLink);

			content.append(" with ");

			var tutorLink = $(document.createElement('a'));
			tutorLink.attr('onclick', 'tutor_clicked(event, ' + item.tutorId
					+ ')');
			tutorLink.append(item.tutorName);
			content.append(tutorLink);
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
		} else if (userType === 'client') {
			content.append(" with ");

			var link = $(document.createElement('a'));
			link.attr('onclick', 'tutor_clicked(event, ' + item.tutorId + ')');
			link.append(item.tutorName);
			content.append(link);

			content.append('. ');
		} else if (userType === 'admin') {
			var clientLink = $(document.createElement('a'));
			clientLink.attr('onclick', 'client_clicked(event, ' + item.clientId
					+ ')');
			clientLink.append(item.clientName);
			content.append(clientLink);

			content.append(" with ");

			var tutorLink = $(document.createElement('a'));
			tutorLink.attr('onclick', 'tutor_clicked(event, ' + item.tutorId
					+ ')');
			tutorLink.append(item.tutorName);
			content.append(tutorLink);
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

		if (userType === 'tutor') {
			container.append(" with ");
			var link = $(document.createElement('a'));
			link.attr('onclick', 'client_clicked(event, ' + event.clientId
					+ ')');
			link.append(event.clientName);
			container.append(link);
			container.append('. ');
		} else if (userType === 'client') {
			container.append(" with ");
			var link = $(document.createElement('a'));
			link.attr('onclick', 'tutor_clicked(event, ' + event.tutorId + ')');
			link.append(event.tutorName);
			container.append(link);
			container.append('. ');
		} else if (userType === 'admin') {
			var clientLink = $(document.createElement('a'));
			clientLink.attr('onclick', 'client_clicked(event, '
					+ event.clientId + ')');
			clientLink.append(event.clientName);
			container.append(clientLink);

			container.append(" with ");

			var tutorLink = $(document.createElement('a'));
			tutorLink.attr('onclick', 'tutor_clicked(event, ' + event.tutorId
					+ ')');
			tutorLink.append(event.tutorName);
			container.append(tutorLink);
			container.append(' ')
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

function lessonEventClick(event, itemdata) {
	otms.clearPageParam('otms.lessonItemPage');
	otms.clearPageParam('otms.lessonPage');
	
	switch (itemdata.type) {
	case 'lesson':
		otms.setPageParam('otms.lessonItemPage.lessonId', itemdata.id);
		otms.setPageParam('otms.lessonItemPage.lessonDate', itemdata.date);
		break;
	case 'lesson_item':
		otms.setPageParam('otms.lessonItemPage.id', itemdata.id)
		break;
	}
	window.location = 'lesson_item.html';
};

function viewLessonDetail(event, lesson) {
	otms.clearParameter('otms.lessonPage');
	otms.UIUtil.stopMessage(event);
	otms.setPageParam('otms.lessonPage.id', lesson.lessonId)
	window.location = 'lesson.html'
};

function viewLessonItemDetail(event, lessonItem) {
	otms.UIUtil.stopMessage(event);
	var usertype = otms.auth.userType();
	if (usertype == 'client' && lessonItem.feedbackStatus == 'NO_FEEDBACK') {
		otms.setPageParam('otms.feedbackPage.itemId', lessonItem.itemId);
		window.location = 'feedback_client.html';
	}
	if (usertype == 'tutor' && lessonItem.feedbackStatus == 'CLIENT_FEEDBACK') {
		otms.setPageParam('otms.feedbackPage.itemId', lessonItem.itemId);
		window.location = 'feedback_tutor.html';
	}
};