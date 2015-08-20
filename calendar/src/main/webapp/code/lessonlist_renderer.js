function client_clicked(client_id) {
	sessionStorage.setItem("otms.clientInfoPage.clientId", client_id);
	window.location = 'client_info.html';
};

function tutor_clicked(tutor_id) {
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
			link.attr('onclick', 'client_clicked(' + item.clientId + ')');
			link.append(item.clientName);
			content.append(link);

			content.append('. ');
		} else {
			content.append(", requesting ");

			var link = $(document.createElement('a'));
			link.attr('onclick', 'tutor_clicked(' + item.tutorId + ')');
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
			link.attr('onclick', 'client_clicked(' + event.clientId + ')');
			link.append(event.clientName);
			container.append(link);
			container.append('. ');
		} else {
			var link = $(document.createElement('a'));
			link.attr('onclick', 'tutor_clicked(' + event.tutorId + ')');
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