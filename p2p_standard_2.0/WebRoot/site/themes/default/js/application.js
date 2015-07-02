// JavaScript Document

$(document).ready(function(){    

    $("#top_btn").click(function(){if(scroll=="off") return;$("html,body").animate({scrollTop: 0}, 600);});

	    //右侧导航 - 二维码
        $(".youhui").mouseover(function(){
            $(this).children(".2wm").show();
        })
        $(".youhui").mouseout(function(){
            $(this).children(".2wm").hide();
        });


});
$(document).ready(function(){    

    $("#top_btn").click(function(){if(scroll=="off") return;$("html,body").animate({scrollTop: 0}, 600);});

	    //右侧导航 - 二维码
        $(".youhui1").mouseover(function(){
            $(this).children(".2wm1").show();
        })
        $(".youhui1").mouseout(function(){
            $(this).children(".2wm1").hide();
        });


});