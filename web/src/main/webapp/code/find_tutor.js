function appendMenu() {
	$(document).on('mouseenter', '.tutor_block', function(block) {
		// debugger;
		$(this).find('.tutor_block_menu').css('display', 'block');
	});

	$(document).on('mouseleave', '.tutor_block', function(block) {
		$(this).find('.tutor_block_menu').css('display', 'none');
	});
};

function renderTutorBlock(item) {
	var block = $(document.createElement('div'));
	block.addClass('tutor_block');

	var photoPanel = $(document.createElement('div'));
	photoPanel.addClass('tutor_photo_panel');
	block.append(photoPanel);

	var photoImg = $(document.createElement("img"));
	photoImg.attr('src', item.pictureUrl);
	photoPanel.append(photoImg);

	var infoPanel = $(document.createElement('div'));
	infoPanel.addClass('tutor_info_panel');
	block.append(infoPanel);

	var wrapper = $(document.createElement('div'));
	wrapper.addClass('tutor_info_wrapper');
	infoPanel.append(wrapper);

	var namePanel = $(document.createElement('div'));
	namePanel.addClass('tutor_name_panel');
	namePanel.append(item.name);
	wrapper.append(namePanel);

	var descPanel = $(document.createElement('div'));
	descPanel.addClass('tutor_desc_panel');
	descPanel.append(item.description);
	wrapper.append(descPanel);

	var menuPanel = $(document.createElement('div'));
	menuPanel.addClass('tutor_block_menu');
	menuPanel.css('display', 'none');
	block.append(menuPanel);

	var favoriteLink = $(document.createElement('a'));
	favoriteLink.addClass('favorite');
	favoriteLink.attr('itemId', 'favLink');
	favoriteLink.append('我对这个教师感兴趣');
	menuPanel.append(favoriteLink);

	var timesheetLink = $(document.createElement('a'));
	timesheetLink.addClass('timesheet');
	timesheetLink.attr('itemId', 'tsLink');
	timesheetLink.append('查看可以上课的时间');
	menuPanel.append(timesheetLink);

	var scheduleLink = $(document.createElement('a'));
	scheduleLink.addClass('schedule');
	scheduleLink.attr('itemId', 'sLink');
	scheduleLink.append('向教师请求试听课');
	menuPanel.append(scheduleLink);

	block.prop('dataItem', item);

	return block;
};

function finishRender() {
	appendMenu();
	$(document).on('click', '.tutor_block_menu a[itemId = "favLink"]',
			setFavorite);
	$(document).on('click', '.tutor_block_menu a[itemId = "tsLink"]',
			viewTimesheet);
	$(document).on('click', '.tutor_block_menu a[itemId = "sLink"]',
			scheduleLesson);

	$(document).on('click', '.tutor_block', showTutor);
}

function setFavorite(event) {
	var block = $(this).parent('.tutor_block');
	var dataItem = block.prop('dataItem');
	event.stopPropagation();
	alert('Not implemented');
};

function scheduleLesson(event) {
	var block = $(this).parent('.tutor_block');
	var dataItem = block.prop('dataItem');
	event.stopPropagation();
	alert('Not implemented');
};

function viewTimesheet(event) {
	var block = $(this).parent('.tutor_block');
	var dataItem = block.prop('dataItem');
	event.stopPropagation();
	alert('Not implemented');
};

function showTutor() {
	var dataItem = $(this).prop('dataItem');

	sessionStorage.setItem('otms.tutorInfoPage.tutorId', dataItem.tutorId);
	window.location = 'tutor_info.html';
};

$(function() {

	otms.namespace('otms.findTutorPage');

	var searchbm = new otms.validator.BeanManager();
	searchbm.reg('keyword', $('#keyword_input'));
	searchbm.reg('favorite', $('#favorite_check'));

	otms.findTutorPage.searchbm = searchbm;

	var list = new otms.ui.list.SimpleList($('#tutor_container'));
	list.renderItem = renderTutorBlock;
	list.finishRender = finishRender;

	var callback = function(success, data) {
		if (success) {
			list.model.setData(data.tutors);
		}
	};

	TutorService.getPopularTutors(otms.auth.req({}), otms.ui.MessageBox
			.shan(callback));

	$('#find-btn').click(function() {

	});

	$('#lucky-btn').click(function() {

	});
});