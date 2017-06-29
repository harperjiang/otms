otms.namespace('otms.dashboardPage');

otms.dashboardPage.todoMessage = {
	"CLIENT_FEEDBACK" : "Comment on the lesson you have attended with <a onclick='tutor_clicked(event, {5})'>{1}</a>, {3} to {4}, {2}"
};

function todo_renderer(container, item) {
	item.context = otms.json(item.context);
	var msg = otms.dashboardPage.todoMessage[item.type];
	var date = new Date(item.context.lessonFrom);
	var dateTo = new Date(item.context.lessonTo);

	var data = otms.FormatUtil.format(msg, item.context.lessonTitle,
			item.context.lessonWith, otms.DateUtil.formatdate(date),
			otms.DateUtil.formattime(date), otms.DateUtil.formattime(dateTo),
			item.context.lessonTutorId);
	container.append(data);
};

function todoClicked(event, dataitem) {
	debugger;
	switch (dataitem.type) {
	case 'CLIENT_FEEDBACK':
		otms.setPageParam('otms.feedbackPage.itemId', dataitem.refId);
		window.location = 'feedback_client.html';
	default:
		break;
	}
};

$(function() {
	otms.namespace('otms.dashboardPage');

	var userType = otms.auth.userType();
	var lessonList = new otms.ui.list.List($('#lesson_list_container'));
	lessonList.setRenderer(lessonEventListRenderer());

	lessonList.rowClicked = lessonEventClick;

	lessonList.titleContainer = $('#lesson_title');
	lessonList.renderTitle = function(length) {
		var msgtop = "You have {0} lessons in the coming week.";
		var append = " You will receive notification 30 minutes before lesson starts."
		var result = otms.FormatUtil
				.format(msgtop, length == 0 ? 'no' : length);
		if (length > 0)
			result += append;
		return result;
	}

	var todoList = new otms.ui.list.List($('#todo_list_container'));
	todoList.setRenderer(todo_renderer);
	todoList.titleContainer = $('#todo_title');
	todoList.rowClicked = todoClicked;
	todoList.renderTitle = function(length) {
		var msgtop = "You have {0} todo events.";
		var result = otms.FormatUtil
				.format(msgtop, length == 0 ? 'no' : length);
		return result;
	}

	var lessonListCallback = function(success, data) {
		if (success) {
			lessonList.model.setData(data.events);
		}
	};

	var comingweek = otms.DateUtil.comingweek();
	CalendarService.getEvents(otms.auth.req({
		'fromDate' : comingweek.fromDate,
		'toDate' : comingweek.toDate
	}), otms.ui.MessageBox.shan(lessonListCallback));

	var todoCallback = function(success, data) {
		if (success) {
			todoList.model.setData(data.todos);
		}
	};
	TodoService.getTodos(otms.auth.req({}), otms.ui.MessageBox
			.shan(todoCallback));
});