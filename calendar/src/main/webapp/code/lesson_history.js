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
$(function() {
	otms.namespace('otms.lessonHistoryPage');

	var list = new otms.ui.list.List($('#lesson_list_container'));
	list.setRenderer(lessonItemListRenderer());
	list.rowClicked = viewLessonItemDetail;
	list.titleContainer = $('#title_container');
	list.renderTitle = function(length) {
		var msg = '{0} results found';
		return otms.FormatUtil.format(msg, length == 0 ? 'no' : length);
	};
	otms.lessonHistoryPage.list = list;

	var pagingControl = new otms.ui.paging.PagingControl($('#paging_control'));
	pagingControl.callback = query;
	otms.lessonHistoryPage.paging = pagingControl;

	$('#time_period').change(function() {
		pagingControl.changePage();
	});

	// Goto first page
	pagingControl.changePage();
});