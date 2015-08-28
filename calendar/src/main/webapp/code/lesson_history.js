function query(pagingData) {

	var fromtime = undefined;
	var totime = new Date();

	var timevalue = $('#time_period').val();
	switch (timevalue) {
	case "0":
		fromtime = otms.DateUtil.offset(totime, -7);
		break;
	case "1":
		fromtime = otms.DateUtil.offset(totime, -30);
		break;
	case "2":
		fromtime = otms.DateUtil.offset(totime, -90);
		break;
	case "3":
		fromtime = null;
	default:
		break;
	}

	var callback = function(success, data) {
		if (success) {
			// Update data to page
			otms.lessonHistoryPage.list.model.setData(data.result);
			otms.lessonHistoryPage.paging.update(data.paging);
		}
	};

	LessonService.getLessonHistory(otms.auth.req({
		"fromTime" : fromtime,
		"toTime" : totime,
		"paging" : pagingData
	}), otms.ui.MessageBox.shan(callback));
};