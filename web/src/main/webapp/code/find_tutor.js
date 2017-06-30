function renderTutorBlock(item) {
	var container = $(document.createElement('div'));
	container.addClass('tutor-block');
	
	var block = $(document.createElement('div'));
	block.addClass('container');
	block.addClass('w-100');
	container.append(block);
	
	var row = $(document.createElement('div'));
	row.addClass('row');
	block.append(row);

	var photoPanel = $(document.createElement('div'));
	photoPanel.addClass('tutor-photo-panel');
	photoPanel.addClass('col-sm-2');
	row.append(photoPanel);

	var photoImg = $(document.createElement("img"));
	photoImg.attr('width', '100%');
	if (otms.isEmpty(item.pictureUrl)) {
		photoImg.attr('src', 'resource/default_profile.jpg');
	} else {
		photoImg.attr('src', item.pictureUrl);
	}
	photoPanel.append(photoImg);

	var centerCol = $(document.createElement('div'));
	centerCol.addClass('col-sm-10');
	row.append(centerCol);

	var infoPanel = $(document.createElement('div'));
	infoPanel.addClass('container');
	infoPanel.addClass('w-100');
	centerCol.append(infoPanel);

	var smrow = $(document.createElement('div'));
	smrow.addClass('row');
	infoPanel.append(smrow);

	var name = $(document.createElement('div'));
	name.addClass('col-sm-4');
	name.addClass('tutor-name');
	name.append(item.name);
	smrow.append(name);

	var ratePart = $(document.createElement('div'));
	ratePart.addClass('col-sm-3');
	ratePart.addClass('mt-2');
	smrow.append(ratePart);

	var rate = $(document.createElement('span'));
	ratePart.append(rate);
	var rateCtrl = new otms.ui.StarRate(rate);
	rateCtrl.setRate(item.rate);
	rateCtrl.readonly();

	var rightPanel = $(document.createElement('div'));
	rightPanel.addClass('col-sm-5');
	rightPanel.addClass('ml-auto');
	rightPanel.addClass('text-right');
	smrow.append(rightPanel);

	var dropdown = $(document.createElement('div'));
	dropdown.addClass('btn-group');
	rightPanel.append(dropdown);

	var btn = $(document.createElement('button'));
	var btnId = 'tutor_' + item.tutorId + '_btn';
	btn.attr('id', btnId);
	btn.addClass('btn btn-secondary');
	btn.append('查看上课时间');
	btn.click(function(event) {
		otms.setPageParam('otms.tutorInfoPage.tutorId', item.tutorId);
		window.location = "info_tutor2.html";
	});
	dropdown.append(btn);

	var btntoggle = $(document.createElement('button'));
	btntoggle.attr('id', btnId + '_toggle');
	btntoggle.addClass('btn btn-secondary dropdown-toggle');
	btntoggle.addClass('dropdown-toggle-split');
	btntoggle.attr('data-toggle', 'dropdown');
	btntoggle.attr('aria-haspopup', true);
	btntoggle.attr('aria-expanded', false);
	dropdown.append(btntoggle);

	var popup = $(document.createElement('div'));
	popup.addClass('dropdown-menu');
	popup.attr('aria-labelledby', btnId + "_toggle");

	var item1 = $(document.createElement('a'));
	item1.addClass('dropdown-item');
	item1.append('请求试听课');
	item1.click(function(event) {
		$('#trial_lesson_dialog').modal('show');
	});

	popup.append(item1);
	dropdown.append(popup);

	smrow = $(document.createElement('div'));
	smrow.addClass('row');
	infoPanel.append(smrow);

	var descPanel = $(document.createElement('div'));
	descPanel.addClass('tutor-desc');
	descPanel.addClass('col-sm-11');
	descPanel.append(item.statement);
	smrow.append(descPanel);

	block.prop('dataItem', item);

	return block;
};

function finishRender() {
	$(document).on('click', '.tutor-block', showTutor);
}

function showTutor() {
	var dataItem = $(this).prop('dataItem');

	sessionStorage.setItem('otms.tutorInfoPage.tutorId', dataItem.tutorId);
	window.location = 'info_tutor2.html';
};

$(function() {

	otms.namespace('otms.findTutorPage');

	var searchbm = new otms.validator.BeanManager();
	searchbm.reg('keyword', $('#keyword_input'));
	searchbm.reg('level', $('#level_select'));

	otms.findTutorPage.searchbm = searchbm;

	var list = new otms.ui.list.SimpleList($('#tutor_container'));
	list.renderItem = renderTutorBlock;
	list.finishRender = finishRender;

	var callback = function(success, data) {
		if (success) {
			list.model.setData(data.tutors);
		}
	};
	TutorService.getPopularTutors({}, otms.ui.MessageBox.shan(callback));

	$('#find_btn').click(
			function() {
				TutorService.getPopularTutors(searchbm.getBean(),
						otms.ui.MessageBox.shan(callback));
			});

	$('#lucky_btn').click(function() {

	});
});