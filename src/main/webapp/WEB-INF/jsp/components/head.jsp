<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- 引入 Bootstrap -->
    <link href="/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="/resources/css/app.css" rel="stylesheet" type="text/css">
    <script>
        var _hmt = _hmt || [];
        (function () {
            var hm = document.createElement("script");
            hm.src = "https://hm.baidu.com/hm.js?e988748e1cd0adcffabdb560cc3df84d";
            var s = document.getElementsByTagName("script")[0];
            s.parentNode.insertBefore(hm, s);
        })();
    </script>
</head>
<body>
<nav class="navbar navbar-default"
     style="border-radius: 0; margin-bottom: 10px;">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" style="font-weight: 700; font-size: 27px;" href="/">Roothub</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse header-navbar">
            <form class="navbar-form navbar-left hidden-xs hidden-sm"
                  role="search" action="/search" method="get" onsubmit="return checkSearch()">
                <div class="form-group has-feedback">
                    <input type="text" class="form-control" name="s" value="" id="sea"
                           style="width: 270px;" placeholder="回车搜索">
                </div>
            </form>
            <ul class="nav navbar-nav navbar-right">
                <li class="hidden-xs" id="shouye">
                    <a href="/">首页</a>
                </li>
                <li id="biaoqian">
                    <a href="/tags">标签</a>
                </li>
                <li id="loginli" style="display:none">
                    <a href="/login">登录</a>
                </li>
                <li id="zhuceli" style="display:none">
                    <a href="/register">注册</a>
                </li>
                <li class="hidden-md hidden-lg">
                    <a href="/topic/create">发布话题</a>
                </li>
                <li id="loginuser" style="display:none">
                    <a href="/user/public" id="current-loger"><span class="badge" id="badge"></span></a>
                </li>
                <li id="shezhili" style="display:none">
                    <a href="/user/settings/profile">设置</a>
                </li>
                <li id="tuichuli" style="display:none">
                    <a href="javascript:if(confirm('确定要登出Roothub吗？'))location.href='/logout'">退出</a>
                </li>
            </ul>
        </div>
    </div>
</nav>
<div class="container" style="padding: 0 25px;">

    <!-- jQuery (Bootstrap 的 JavaScript 插件需要引入 jQuery) -->
    <script src="/resources/js/jquery.js"></script>
    <!-- 引入 Bootstrap -->
    <script src="/resources/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        $(function () {
            $.ajax({
                type: "get",
                url: "/session",
                dataType: "json",
                success: function (data) {
                    // console.log(JSON.stringify(data));
                    if (data.success != null && data.success == true) {
                        $("#loginuser").show();
                        $("#loginuser a").text(data.user);
                        $("#loginuser a").attr("href", "/user/" + data.user);
                        
                        $("#shezhili").show();
                        $("#tuichuli").show();
                    }
                    if (data.success != null && data.success == false) {
                        $("#loginli").show();
                        $("#zhuceli").show();
                        $("#nologin").show();
                    }
                },
                error: function (data) {

                }
            });
        });
    </script>

    <script type="text/javascript">
        function checkSearch() {
            if ($("#sea").val() == "" || $('#sea').val() == null) {
                alert("请输入搜索内容！");
                return false;
            }
            return true;
        }
    </script>

</body>
</html>