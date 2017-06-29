otms.namespace('otms.ui.paging');

otms.ui.paging.PagingControl = function(container) {
	this.container = container;
	this.container.prop('pagingControl', this);
	this.pageSize = 20;
	this.currentPage = 0;
	this.totalPage = 0;
	this.init();
};

otms.ui.paging.PagingControl.prototype.init = function() {
	this.container.append();

	var prevButton = $(document.createElement('button'));
	prevButton.append('&#9664;');
	prevButton.addClass('btn_small');
	prevButton.addClass('paging-control');
	prevButton.prop('id', 'paging_btn_prev');
	this.container.append(prevButton);

	var input = $(document.createElement('input'));
	input.prop('type', 'text');
	input.addClass('paging-input');
	this.container.append(input);

	this.currentPageInput = input;

	this.container.append(" of ");

	var totalSpan = $(document.createElement('span'));
	totalSpan.addClass('paging-total');
	this.container.append(totalSpan);

	this.totalSpan = totalSpan;

	var goButton = $(document.createElement('button'));
	goButton.append("Go");
	goButton.addClass('btn_small');
	goButton.addClass('paging-control');
	goButton.prop('id', 'paging_btn_go');
	this.container.append(goButton);

	var nextButton = $(document.createElement('button'));
	nextButton.append('&#9654;');
	nextButton.addClass('btn_small');
	nextButton.addClass('paging-control');
	nextButton.prop('id', 'paging_btn_next');
	this.container.append(nextButton);

	$(document).on('click', '#paging_btn_prev', this.prev);
	$(document).on('click', '#paging_btn_next', this.next);
	$(document).on('click', '#paging_btn_go', this.go);
};

otms.ui.paging.PagingControl.prototype.go = function(event) {
	debugger;
	var control = $(this).parent().prop('pagingControl');

	var newindex = parseInt(control.currentPageInput.val());
	if (isNaN(newindex))
		return;
	control.currentPage = newindex - 1;
	control.changePage();
};

otms.ui.paging.PagingControl.prototype.prev = function(event) {
	debugger;
	var control = $(this).parent().prop('pagingControl');
	control.currentPage--;
	control.changePage();
};

otms.ui.paging.PagingControl.prototype.next = function(event) {
	debugger;
	var control = $(this).parent().prop('pagingControl');
	control.currentPage++;
	control.changePage();
};

otms.ui.paging.PagingControl.prototype.changePage = function() {
	if (this.currentPage < 0) {
		this.currentPage = 0;
	} else if (this.totalPage > 0 && this.currentPage >= this.totalPage) {
		this.currentPage = this.totalPage - 1;
	} else {
		this.currentPage = 0;
	}
	this.callback({
		"pageSize" : this.pageSize,
		"currentPage" : this.currentPage
	});
};

otms.ui.paging.PagingControl.prototype.update = function(data) {
	this.totalPage = data.totalPage;
	this.currentPage = data.currentPage;

	this.totalSpan.empty();
	this.totalSpan.append(this.totalPage);

	var page = this.currentPage;
	if (this.totalPage != 0) {
		page += 1;
	}

	this.currentPageInput.val(page);
};

otms.ui.paging.PagingControl.prototype.callback = function(data) {
	debugger;
};