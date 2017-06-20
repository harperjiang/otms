otms.helpPage.histStack = [];
otms.helpPage.currentItem = undefined;

$(onload);
function onload() {
	otms.helpPage.histStack.push(otms.helpPage.topicRoot);
	otms.helpPage.currentItem = otms.helpPage.topicRoot;
	refresh();
	$(document).on('click', '.help_header a', histClick);
	$(document).on('click', '.help_menu a', menuClick);
};

function refresh() {
	renderHist();
	renderMenu();
	renderContent();
};

function renderMenu() {
	$('#menu_ul').empty();

	var currentRoot = otms.helpPage.histStack[otms.helpPage.histStack.length - 1];
	if (currentRoot !== otms.helpPage.topicRoot) {
		// Add a return item
		var menuItem = $(document.createElement("li"));
		var menuItemLink = $(document.createElement('a'));
		menuItemLink.append('返回上级');
		menuItem.append(menuItemLink);
		$('#menu_ul').append(menuItem);
		menuItemLink.prop('dataItem', 'return');
	}
	for ( var i in currentRoot.children) {
		var item = currentRoot.children[i];
		var menuItem = $(document.createElement("li"));
		var menuItemLink = $(document.createElement('a'));
		menuItemLink.append(item.topic);
		menuItem.append(menuItemLink);
		$('#menu_ul').append(menuItem);
		menuItemLink.prop('dataItem', item);
	}

	var menuItem = $(document.createElement("li"));
	var menuItemLink = $(document.createElement('a'));
	menuItemLink.append('联系我们');
	menuItem.append(menuItemLink);
	$('#menu_ul').append(menuItem);
	menuItemLink.prop('dataItem', 'contact');
};

function menuClick(event) {
	var item = $(this).prop('dataItem');
	switch (item) {
	case 'return':
		otms.helpPage.histStack.pop();
		otms.helpPage.currentItem = otms.helpPage.histStack[otms.helpPage.histStack.length - 1];
		break;
	case 'contact':
		window.location = 'contact.html';
		break;
	default:
		if (item.end != true && item.children !== undefined) {
			otms.helpPage.histStack.push(item);
		}
		otms.helpPage.currentItem = item;
		break;
	}
	refresh();
};

function renderHist() {
	$('#hist_ul').empty();

	for ( var index in otms.helpPage.histStack) {
		var item = otms.helpPage.histStack[index];
		var hist = $(document.createElement('li'));
		var histLink = $(document.createElement('a'));
		histLink.append(item.topic);
		hist.append(histLink);
		histLink.prop('dataItem', item);

		$('#hist_ul').append(hist);
	}

};

function histClick(event) {
	var item = $(this).prop('dataItem');
	while (otms.helpPage.histStack[otms.helpPage.histStack.length - 1] !== item) {
		otms.helpPage.histStack.pop();
	}
	refresh();
};

function contentClick(link, event) {
	debugger;
	otms.helpPage.currentItem = $(link).prop('dataItem');
	renderContent();
};

function contentByIdClick(item, id) {
	var titlePanel = $('#title_panel');
	var dataPanel = $('#data_panel');

	titlePanel.empty();
	titlePanel.append(item.innerText);

	dataPanel.empty();
	// Load page URL
	var url = otms.FormatUtil.format('help/{0}.html', id);

	$.ajax(url, {
		'complete' : function(xhr, status) {
			switch (xhr.status) {
			case 200:
				// Update content
				dataPanel.append(xhr.responseText);
				break;
			default:
				dataPanel.append('这部分帮助内容暂时无法显示');
				break;
			}
		}
	});
};

function renderContent() {
	var item = otms.helpPage.currentItem;
	var titlePanel = $('#title_panel');
	var dataPanel = $('#data_panel');
	if (item === otms.helpPage.topicRoot) {
		titlePanel.empty();
		titlePanel.append('常见问题列表');

		dataPanel.empty();
		// Load page URL
		var url = 'help/default.html';

		$.ajax(url, {
			'complete' : function(xhr, status) {
				switch (xhr.status) {
				case 200:
					// Update content
					dataPanel.append(xhr.responseText);
					break;
				default:
					dataPanel.append('这部分帮助内容暂时无法显示');
					break;
				}
			}
		});
	} else if (item.end === true) {
		titlePanel.empty();
		titlePanel.append(item.topic);

		dataPanel.empty();
		// Display a link for each item
		for ( var index in item.children) {
			var child = item.children[index];
			var link = $(document.createElement('a'));
			link.append(child.topic);
			link.attr('onclick', 'contentClick(this, event)');
			link.prop('dataItem', child);
			dataPanel.append(link);
		}
	} else if (item.children === undefined) {
		titlePanel.empty();
		titlePanel.append(item.topic);

		dataPanel.empty();
		// Load page URL
		var url = otms.FormatUtil.format("help/{0}.html", item.id);

		$.ajax(url, {
			'complete' : function(xhr, status) {
				switch (xhr.status) {
				case 200:
					// Update content
					dataPanel.append(xhr.responseText);
					break;
				default:
					dataPanel.append('这部分帮助内容暂时无法显示');
					break;
				}
			}
		});
	}
};