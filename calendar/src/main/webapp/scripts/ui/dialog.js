otms.namespace('otms.ui.dialog');

otms.ui.dialog.Dialog = function(parent) {

	this.parentContainer = parent;

	this.mask = $(document.createElement('div'));
	this.mask.addClass('dialog_mask');
	parent.append(this.mask);

	this.panel = $(document.createElement('div'));
	this.panel.addClass('dialog');
	parent.append(this.panel);

	this.titleDiv = $(document.createElement('div'));
	this.titleDiv.addClass('dialog_title');
	this.panel.append(this.titleDiv);

	this.contentDiv = $(document.createElement('div'));
	this.contentDiv.addClass('dialog_content');
	this.panel.append(this.contentDiv);

	this.buttonDiv = $(document.createElement('div'));
	this.buttonDiv.addClass('dialog_buttonpanel');
	this.panel.append(this.buttonDiv);
	
	this.drawTitle(this.titleDiv);
	this.drawContent(this.contentDiv);
	this.drawButton(this.buttonDiv);
};

otms.ui.dialog.Dialog.prototype.setSize = function(width, height) {
	this.panel.css('width', width);
	this.panel.css('min-height', height);
};

otms.ui.dialog.Dialog.prototype.show = function() {
	this.mask.css('display', 'initial');
	this.panel.css('display', 'initial');
	// center to parent
	var ptop = this.parentContainer.prop('offsetTop');
	var pleft = this.parentContainer.prop('offsetLeft');
	var pwidth = this.parentContainer.prop('offsetWidth');
	var pheight = this.parentContainer.prop('offsetHeight');

	var mywidth = this.panel.prop('offsetWidth');
	var myheight = this.panel.prop('offsetHeight');

	var myTop = ptop + (pheight - myheight) / 4;
	var myLeft = pleft + (pwidth - mywidth) / 2;

	this.panel.css('top', myTop);
	this.panel.css('left', myLeft);
};

otms.ui.dialog.Dialog.prototype.hide = function() {
	this.mask.css('display', 'none');
	this.panel.css('display', 'none');
};

otms.ui.dialog.Dialog.prototype.drawTitle = function(titlePanel) {

};

otms.ui.dialog.Dialog.prototype.drawContent = function(contentPanel) {

};

otms.ui.dialog.Dialog.prototype.onConfirm = function() {

};

otms.ui.dialog.Dialog.prototype.drawButton = function(buttonPanel) {
	var dialog = this;
	this.confirmButton = $(document.createElement('button'));
	this.confirmButton.append('Confirm');
	this.confirmButton.addClass('confirm');
	this.confirmButton.click(function(event){
		dialog.hide();
		dialog.onConfirm();
	});
	buttonPanel.append(this.confirmButton);
	
	this.cancelButton = $(document.createElement('button'));
	this.cancelButton.append('Cancel');
	this.cancelButton.click(function(event){
		dialog.hide();
	});
	buttonPanel.append(this.cancelButton);
};