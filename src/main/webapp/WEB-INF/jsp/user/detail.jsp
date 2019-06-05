<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Roothub-${user.userName}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="/resources/css/app.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="/resources/layui/css/layui.css" media="all">
    <link rel="shortcut icon" href="/resources/images/favicon.ico">
</head>
<body>
<div>
    <input type="hidden" id="currentUser" value="${user2.userName}"/>
</div>
<div class="wrapper">
    <jsp:include page="../components/head.jsp"></jsp:include>
    <div class="row">
        <!-- 小屏幕显示 -->
        <div class="col-md-9">
            <div class="box">
                <div class="cell">
                    <table cellpadding="0" cellspacing="0" border="0" width="100%">
                        <tbody>
                        <tr>
                            <td width="73" valign="top" align="center">
                                <img src="${user.avatar}" border="0" align="default" style="border-radius: 4px;"
                                     width="73" height="73px"/>
                                <div class="sep10"></div>
                            </td>
                            <td width="10"></td>
                            <td width="auto" valign="top" align="left">
                                <c:if test="${user2 != null && user2.userId != user.userId}">
                                    <div class="fr">
                                        <button class="btn btn-follow" onclick="save()" id="follow">加入特别关注</button>
                                        <div class="sep10"></div>
                                        <button class="btn btn-warning">Block</button>
                                    </div>
                                </c:if>
                                <h1 title="${user.userId}" id="user_id" class="user_id">${user.userName}</h1>
                                <span class="gray" style="font-size: 14px;">
                                    Roothub 第 ${user.userId} 号会员，加入于 <fmt:formatDate type="both" value="${user.createDate}"/>
                                    <div class="sep5"></div>
                                </span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <div class="sep5"></div>
                </div>
            </div>
            <div class="sep20"></div>
            <div class="panel panel-default">
                <div class="cell_tabs">
                    <div class="fl">
                        <img src="${user.avatar}" width="24" style="border-radius: 24px; margin-top: -2px;" border="0">
                    </div>
                    <%--href=”javascript:void(0);”这个的含义是，让超链接去执行一个js函数，而不是去跳转到一个地址--%>
                    <a href="javascript:void(0);" onclick="topicList()" class="cell_tab_current">主题</a>
                    <a href="javascript:void(0);" onclick="replyList()" class="cell_tab">评论</a>
                    <a href="javascript:void(0);" onclick="collectList()" class="cell_tab">收藏</a>
                    <a href="javascript:void(0);" onclick="followList()" class="cell_tab">关注</a>
                    <a href="javascript:void(0);" onclick="fansList()" class="cell_tab">粉丝</a>
                    <%--<a href="javascript:void(0);" onclick="topicQnaList()" class="cell_tab">提问</a></div>--%>
                    <a href="javascript:void(0);" onclick="shield()" class="cell_tab">被屏蔽</a></div>
                <div class="itemList"></div>
            </div>
        </div>
    </div>
</div>
<div id="back2Top" class="backTop___6Q-ki" style="display:none">
    <div class="line___F1WY0"></div>
    <div class="arrow___3UCwo"></div>
</div>
</div>
<jsp:include page="../components/foot.jsp"></jsp:include>
<script src="/resources/js/jquery.js"></script>
<script src="/resources/js/bootstrap.min.js"></script>
<script src="/resources/js/goTop.js"></script>
<script src="/resources/layui/layui.js"></script>
<script src="/resources/layui/layui-paginate.js"></script>
<script src="/resources/js/user/detail.js"></script>
<script src="/resources/js/formatDate.js"></script>
</body>
</html>