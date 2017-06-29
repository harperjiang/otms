otms.namespace('otms.ui.list');

otms.ui.list.ListModel = function(list) {
	this.list = list;
};

otms.ui.list.ListModel.prototype.setData = function(data) {
	this.data = data;
	this.list.refresh();
};

otms.ui.list.ListModel.prototype.length = function() {
	return this.data.length;
};

otms.ui.list.ListModel.prototype.get = function(index) {
	return this.data[index];
};

otms.ui.list.List = function(container) {
	this.model = new otms.ui.list.ListModel(this);
	this.container = container;
	this.container.prop('listControl', this);
	this.initialize();
};

otms.ui.list.List.prototype.initialize = function() {
	this.container.addClass('list_container');

	this.contentPanel = $(document.createElement('div'));
	this.contentPanel.addClass('list_content');
	this.container.append(this.contentPanel);

	var list = this;

	$(document).on('mouseleave', '.list_content', function(event) {
		list.hidePopup();
	});
};

otms.ui.list.List.prototype.setRenderer = function(renderer) {
	this.renderer = renderer;
};

otms.ui.list.List.prototype.setPopupRenderer = function(popupr) {
	this.popupRenderer = popupr;
};

otms.ui.list.List.prototype.refresh = function() {
	var list = this;
	this.contentPanel.empty();

	var length = this.model.length();
	for (var i = 0; i < length; i++) {
		var item = this.model.get(i);
		var container = $(document.createElement('div'));
		container.addClass('list_row');

		var content = $(document.createElement('div'));
		content.addClass('list_row_content');
		container.append(content);

		if (this.renderer === undefined) {
			content.append(String(item));
		} else {
			this.renderer(content, item);
		}
		container.prop('dataItem', item);
		this.contentPanel.append(container);
	}

	$(this.container).on('mouseenter', '.list_row', function(event) {
		// display the popup
		list.currentItem = $(this).prop('dataItem');
		list.showPopup($(this));
	});

	$(this.container).on('click', '.list_row', function(event) {
		list.rowClicked(event, $(this).prop('dataItem'));
	});

	var end = $(document.createElement('div'));
	end.addClass('list_row_end');
	this.contentPanel.append(end);

	if (this.titleContainer !== undefined) {
		this.titleContainer.empty();
		this.titleContainer.append(this.renderTitle(length));
	}
};

otms.ui.list.List.prototype.rowClicked = function(event, itemdata) {
	// Do nothing
};

otms.ui.list.List.prototype.hasPopup = function(data) {
	return false;
};

otms.ui.list.List.prototype.showPopup = function(row) {
	if (this.popupDiv === undefined && this.popupRenderer === undefined)
		// No popup
		return;
	var hasPopup = this.hasPopup(row.dataItem);
	if (!hasPopup) {
		return;
	}
	if (this.popupDiv === undefined) {
		this.popupDiv = $(document.createElement('div'));
		this.popupDiv.attr('id', 'list_popup_div');
		this.popupDiv.addClass('list_popup_div');
		this.popupDiv.layout = function(height) {
		};

		this.contentPanel.append(this.popupDiv);

		this.popupRenderer(this.popupDiv);
	}

	// Show popup
	this.contentPanel.append(this.popupDiv);

	var stubwidth = row.prop('offsetWidth');
	var stubheight = row.prop('offsetHeight');
	var widthexp = this.popupDiv.prop('width');
	var stubpos = row.offset();
	var divpos = this.popupDiv.offset();
	if (stubpos.top === divpos.top && this.popupDiv.css('width') != '0px')
		// Already shown on the same location
		return;

	this.popupDiv.css('height', stubheight);

	this.popupDiv.layout(stubheight);

	this.popupDiv.css('top', stubpos.top);
	this.popupDiv.css('left', stubpos.left + stubwidth);

	this.popupDiv.css('width', 0);

	this.popupDiv.animate({
		'left' : stubpos.left - widthexp + stubwidth,
		'width' : widthexp
	}, 50);

};

otms.ui.list.List.prototype.hidePopup = function() {
	if (this.popupDiv != undefined) {
		this.popupDiv.detach();
		this.popupDiv.css('width', 0);
	}
	this.currentItem = null;
};

otms.ui.list.SimpleList = function(container) {
	this.container = container;
	this.model = new otms.ui.list.ListModel(this);
};

otms.ui.list.SimpleList.prototype.refresh = function() {
	for (var i = 0; i < this.model.length(); i++) {
		var dataitem = this.model.get(i);
		this.container.append(this.renderItem(dataitem));
	}
	this.finishRender();
};

otms.ui.list.SimpleList.prototype.renderItem = function(item) {
	return undefined;
};

otms.ui.list.SimpleList.prototype.finishRender = function() {
};