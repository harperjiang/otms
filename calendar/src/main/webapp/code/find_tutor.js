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

	var infoPanel = $(document.createElement('div'));
	infoPanel.addClass('tutor_info_panel');
	block.append(infoPanel);

	var wrapper = $(document.createElement('div'));
	wrapper.addClass('tutor_info_wrapper');
	infoPanel.append(wrapper);

	var namePanel = $(document.createElement('div'));
	namePanel.addClass('tutor_name_panel');
	wrapper.append(namePanel);

	var descPanel = $(document.createElement('div'));
	descPanel.addClass('tutor_desc_panel');
	wrapper.append(descPanel);

	var menuPanel = $(document.createElement('div'));
	menuPanel.addClass('tutor_block_menu');
	menuPanel.css('display', 'none');
	block.append(menuPanel);

	var favoriteLink = $(document.createElement('a'));
	favoriteLink.attr('itemId', 'favLink');
	favoriteLink.append('My Favorite');
	menuPanel.append(favoriteLink);

	var timesheetLink = $(document.createElement('a'));
	timesheetLink.attr('itemId', 'tsLink');
	timesheetLink.append('View Timesheet');
	menuPanel.append(timesheetLink);

	var scheduleLink = $(document.createElement('a'));
	scheduleLink.attr('itemId', 'sLink');
	scheduleLink.append('Schedule Lesson');
	menuPanel.append(scheduleLink);

	block.prop('dataItem', item);

	return block;
};

function finishRender() {
	$(document).on('click', '.tutor_block_menu a[itemId = "favLink"]',
			setFavorite);
	$(document).on('click', '.tutor_block_menu a[itemId = "tsLink"]',
			viewTimesheet);
	$(document).on('click', '.tutor_block_menu a[itemId = "sLink"]',
			scheduleLesson);
}

function setFavorite() {
	var block = $(this).parent('.tutor_block');
	var dataItem = block.prop('dataItem');

	alert('Not implemented');
};

function scheduleLesson() {
	var block = $(this).parent('.tutor_block');
	var dataItem = block.prop('dataItem');

	alert('Not implemented');
};

function showTimesheet() {
	var block = $(this).parent('.tutor_block');
	var dataItem = block.prop('dataItem');

	alert('Not implemented');
};