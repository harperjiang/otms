$(function() {

	otms.namespace('otms.setting.timesheetPage');

	otms.setting.timesheetPage.mainTab = new otms.ui.tab.TabControl([
			$('#tab1'), $('#tab2') ], [ $('#regular_panel'),
			$('#specify_panel') ]);

	otms.setting.timesheetPage.regularTs = new otms.ui.timesheet.Timesheet(
			$('#regular_container'));
	otms.setting.timesheetPage.specifyTs = new otms.ui.timesheet.Timesheet(
			$('#specify_container'));
});