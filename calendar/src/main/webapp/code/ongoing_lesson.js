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