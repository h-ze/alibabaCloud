//登录轮询定时器Id
var isLoginTimerId;
//二维码过期定时器Id
var isExpireTimerId;
//二维码是否已过期
var qrcodeExpired = false;
var contextPath = window.location.pathname.split("/")[1];
$(function () {
    /**
     * 切换登录方式
     */
    $('.tab').on('click', 'a', function () {
        var $this = $(this), index = $this.index(), left = $this.data('left');
        $this.addClass('on').siblings().removeClass('on');
        $('.tab-content').hide().eq(index).show();
        $('.tab-line').animate({
            left: left
        }, 400)
        if (index == 0) {
            // 停止二维码登录
            clearInterval(isLoginTimerId);
            clearTimeout(isExpireTimerId);
        } else if (index == 1) {
            loadQrcode();
        }
    });

    /**
     * 绑定刷新二维码操作
     */
    $('body').on('click', '#refeshQrcode', loadQrcode);

    /**
     * 绑定回车键执行登陆操作
     */
    $('body').on('keydown', function (e) {
        if (e.keyCode == 13 && $submitBtn.is(':visible')) {
            $submitBtn.trigger('click');
        }
    });


    $('#btnPopupQrloginCode').on('click', openQrcodeLogin);
    $('#btnQrcodeLogin').on('click', qrcodeLogin);
});

/**
 * 加载登录二维码
 */
function loadQrcode() {
    var url = "/login/qrcode/create";
    var params = {};
    $.ajax({
        type: 'post',
        url: url,
        timeout: 5000,
        data: params,
        dataType: "json",
        success: function (response) {
            if (100000 == response.code) {
                var qrcodeInfo = response.data;
                var qrcodeId = qrcodeInfo.qrcodeId;
                var qrcodeImgUrl = qrcodeInfo.qrcodeImgUrl;
                $("#qrcodeImg").attr("src", qrcodeImgUrl);
                $("#qrcodeId").val(qrcodeId);
                //1.启动对应的二维码是否已登录的验证
                isLoginTimerId = setInterval(qrcodeIsLogined, 3000);
                //2.设置二维码过期时间
                isExpireTimerId = setTimeout(qrcodeIsExpired, 60000);
                qrcodeExpired = false;
            } else {
                layer.msg(response.msg, {icon: 5});
            }
        },
        error: function (msg) {
            layer.msg("网络异常，请稍后重试...", {icon: 5});
        }
    });
}

/**
 * 轮询二维码是否已登录
 */
function qrcodeIsLogined() {
    var qrcodeId = $("#qrcodeId").val();
    var url = "/login/qrcode/isLogined";
    $.ajax({
        type: 'post',
        url: url,
        timeout: 5000,
        data: {
            "qrcodeId": qrcodeId
        },
        dataType: "json",
        success: function (response) {
            if (100000 === response.code) {
                clearInterval(isLoginTimerId);
                clearTimeout(isExpireTimerId);
                window.location.href = "/user/index";
            }
        },
        error: function (msg) {
            layer.msg("网络异常，请稍后重试...", {icon: 5});
            clearInterval(isLoginTimerId);
            clearTimeout(isExpireTimerId);
        }
    });
}

/**
 * 二维码过期设置
 */
function qrcodeIsExpired() {
    $("#qrcodeImg").attr("src", "/static/images/qrcodeError.png");
    qrcodeExpired = true;
    clearInterval(isLoginTimerId);
    clearTimeout(isExpireTimerId);
}


function openQrcodeLogin(){
    layer.open({
        type : 1,
        title : false,
        content : $('#qrcodeLoginForm'),
        area : [ '480px', '360px' ]
    })
}

/**
 * 模拟二维码登录
 */
function qrcodeLogin(){
    var qrcodeId = $("#qrcodeId").val();
    var userId = $("#userId").val();
    var url = "/login/qrcode/login";
    $.ajax({
        type: 'post',
        url: url,
        timeout: 5000,
        data: {
            "qrcodeId": qrcodeId,
            "userId": userId
        },
        dataType: "json",
        success: function (response) {
            if ("2000" == response.code) {
                layer.msg(response.msg,{icon:1});
            }else{
                layer.msg(response.msg,{icon:5});
            }
        },
        error: function (msg) {
            layer.msg("网络异常，请稍后重试...", {icon: 5});
            clearInterval(isLoginTimerId);
            clearTimeout(isExpireTimerId);
        }
    });
}