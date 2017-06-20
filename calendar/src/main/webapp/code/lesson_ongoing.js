function refreshList() {
	var callback = function(success, data) {
		if (success) {
			// Update data to page
			otms.ongoingLessonPage.list.model.setData(data.lessons);
		}
	};
	LessonService.getOngoingLessons(otms.auth.req({}), otms.ui.MessageBox
			.shan(callback));
};
$(function() {
	otms.namespace('otms.ongoingLessonPage');

	var list = new otms.ui.list.List($('#lesson_list_container'));
	list.setRenderer(lessonListRenderer());
	list.rowClicked = viewLessonDetail;

	list.titleContainer = $('#title_container');
	list.renderTitle = function(length) {
		var msg = 'You have {0} on-going lessons';
		return otms.FormatUtil.format(msg, length == 0 ? 'no' : length);
	};

	otms.ongoingLessonPage.list = list;
	refreshList();
});