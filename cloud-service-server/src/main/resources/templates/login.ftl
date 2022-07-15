<#assign base=request.contextPath>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no"/>
    <title>登录</title>
    <link rel="stylesheet" href="${base}/static/css/common.css" />
    <link rel="stylesheet" href="${base}/static/css/MMJY.css" />
    <style>
        .erweima2 {
            display: inline-block;
            text-align: center;
            vertical-align: bottom;
        }
    </style>
</head>
<body>
<!--登录框-->
<div >
    <form class="login-form">
        <div class="tab clear">
            <a id="accountLoginWay" href="javascript:;" class="on" data-left="55">账号登录(未实现)</a>
            <a id="qrcodeTab" href="javascript:;" data-left="265">二维码登录</a>
            <img src="${base}/static/images/tabs-line.png" class="tab-line"/>
        </div>
        <!--密码登录-->
        <div class="tab-content pt30" >
            <div class="group">
                <label>用户名</label>
                <div class="fluid">
                    <input type="text" class="block" name="username" id="username" placeholder="请输入账号／登录名／手机号" required/>
                </div>
            </div>
            <div class="group">
                <label>密&nbsp;&nbsp;码</label>
                <div class="fluid">
                    <input type="password" class="block" name="password" id="password" required/>
                </div>
            </div>
            <div id="errorMsg" class="error-msg tc" style="color:red;"></div>
            <div class="group" style="padding-top: 20px;">
                <p class="tr pb10"><a href="#" class="t-grey">忘记密码</a></p>
                <input type="button" class="btn-block" id="submit" value="登&nbsp;&nbsp;&nbsp;&nbsp;录"/>
            </div>
        </div>
        <!--二维码登录-->
        <div class="tab-content tc" hidden >
            <div id="qrcodeDiv" class="erweima2">
                <img id="qrcodeImg" src="" width="200">
                <input id="qrcodeId" type="hidden"/>
            </div>
            <p class="mt20">请使用手机扫描二维码登录<a id="refeshQrcode" href="#" class="t-blue ml20">
                    <img src="${base}/static/images/icon-updata.png"/>刷新</a>
            </p>

            <p class="mt20"><a id="btnPopupQrloginCode" href="#" class="t-blue ml20">模拟手机扫码登录</a></p>
        </div>
    </form>


</div>

<!--模拟二维码登录-->
<div id="qrcodeLoginForm" hidden>
    <div >
        <div class="group">
            <label style="width:160px;">已登录的用户Id</label>
            <div class="fluid">
                <input type="text" class="block" name="userId" id="userId" value="demo001" required/>
            </div>
        </div>
        <div class="group">
            <label style="width:160px;">已登录的用户姓名</label>
            <div class="fluid">
                <input type="password" class="block" name="personName" id="personName" value="测试用户" required/>
            </div>
        </div>
        <div class="group" style="padding-top: 20px;">
            <input type="button" class="btn-block" id="btnQrcodeLogin" value="模拟手机扫码登录"/>
        </div>
    </div>
</div>
<script src="${base}/static/js/jquery-1.11.3/jquery.min.js"></script>
<script src="${base}/static/js/layer-v3.1.1/layer/layer.js"></script>
<script src="${base}/static/js/login/login.js"></script>
</body>
</html>