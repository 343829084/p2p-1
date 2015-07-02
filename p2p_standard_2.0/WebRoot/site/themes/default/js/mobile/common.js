artDialog.tips = function(content, time, isLock, icon) {
	return $.dialog({
		id : 'Tips',
		icon : icon,
		title : false,
		fixed : true,
		lock : isLock ? isLock : false,
		opacity : .5
	}).content('<div style="padding: 0 1em;">' + content + '</div>').time(
			time || 10);
};
//tab页切换
function zahe_1(name, cursel, n) {
	for (i = 1; i <= n; i++) {
		var menu = document.getElementById(name + i);
		var con = document.getElementById("con_" + name + "_" + i);
		if (i == cursel) {
			$(menu).addClass("hover");
			$(con).show();
		} else {
			$(menu).removeClass("hover");			
			$(con).hide();
		}
	}
}

$(function(){
	//投资列表
	$(".content_ul").on("click",".con_ul a",function(){
		$(this).closest(".con_ul").find(".hover").removeClass("hover");
		$(this).addClass("hover");
		clearPage();
	});
});