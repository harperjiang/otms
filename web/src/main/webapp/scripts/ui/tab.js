otms.namespace('otms.ui.tab');

function switchTab(tabctrl, index) {
	tabctrl.switchTab(index);
}

otms.ui.tab.TabControl = function(links, tabs) {
	this.links = links;
	this.tabs = tabs;
	var self = this;
	links.forEach(function(link, index, array) {
		link.click(function(event) {
			switchTab(self, index);
		});
	});
	links[0].addClass('active');

	tabs.forEach(function(tab, index, array) {
		tab.css('display', 'none');
	});
	tabs[0].css('display', 'block');
};

otms.ui.tab.TabControl.prototype.switchTab = function(index) {
	this.links.forEach(function(link, idx, array) {
		if (idx == index) {
			link.addClass('active');
		} else {
			link.removeClass('active');
		}
	});
	this.tabs.forEach(function(tab, idx, array) {
		if (idx == index) {
			tab.css('display', 'block');
		} else {
			tab.css('display', 'none');
		}
	});
};