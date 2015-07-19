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
	this.initialize();
};

otms.ui.list.List.prototype.initialize = function() {
	this.container.addClass('list_container');

	this.titlePanel = $(document.createElement('div'));
	this.titlePanel.addClass('list_title');
	this.container.append(this.titlePanel);

	this.contentPanel = $(document.createElement('div'));
	this.contentPanel.addClass('list_content');
	this.container.append(this.contentPanel);
};

otms.ui.list.List.prototype.setTitle = function(title) {
	this.titlePanel.empty();
	this.titlePanel.append(title);
};

otms.ui.list.List.prototype.setRenderer = function(renderer) {
	this.renderer = renderer;
};

otms.ui.list.List.prototype.refresh = function() {
	this.contentPanel.empty();

	var length = this.model.length();
	for (var i = 0; i < length; i++) {
		var item = this.model.get(i);
		var container = $(document.createElement('div'));
		container.addClass('list_row');
		if (this.renderer === undefined) {
			container.append(String(item));
		} else {
			this.renderer(container, item);
		}
		this.container.prop('dataItem', item);
		this.contentPanel.append(container);
	}
	var end = $(document.createElement('div'));
	end.addClass('list_row_end');
	this.contentPanel.append(end);
};