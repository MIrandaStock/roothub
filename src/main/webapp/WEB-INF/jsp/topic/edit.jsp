<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>更新话题</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="/resources/css/app.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="/resources/wangEditor/wangEditor.min.css">
    <link rel="shortcut icon" href="/resources/images/favicon.ico">
    <script src="/resources/js/logout.js"></script>
</head>
<body>
<div class="wrapper">
    <jsp:include page="../components/head.jsp"></jsp:include>
    <div class="row">
        <div class="col-md-9">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <a href="/">主页</a> / 编辑话题
                </div>
                <div class="panel-body">
                    <form id="form">
                        <div class="form-group">
                            <label for="title">标题</label>
                            <input type="text" class="form-control" id="title" value="${topic.title}" name="title" placeholder="标题"/>
                        </div>
                        <div class="form-group">
                            <label for="editor">内容</label>
                            <div id="editor" style="margin-bottom: 10px;"></div>
                        </div>
                        <button type="submit" class="btn btn-default">更新话题</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
<jsp:include page="../components/foot.jsp"></jsp:include>
<script src="/resources/js/jquery.js"></script>
<script src="/resources/js/bootstrap.min.js"></script>
<script src="/resources/wangEditor/wangEditor.min.js"></script>
<!-- <script src="/resources/js/topic/node.js"></script> -->
<script type="text/javascript">
    $(function () {
        var E = window.wangEditor;
        var editor = new E('#editor');
        editor.customConfig.uploadFileName = 'file';
        editor.customConfig.uploadImgServer = '/common/wangEditorUpload';
        editor.customConfig.menus = [
            'head',  // 标题
            'bold',  // 粗体
            'italic',  // 斜体
            'underline',  // 下划线
            'strikeThrough',  // 删除线
            'link',  // 插入链接
            'list',  // 列表
            'quote',  // 引用
            'emoticon',  // 表情
            'image',  // 插入图片
            'table',  // 表格
            'code',  // 插入代码
            'undo',  // 撤销
            'redo'  // 重复
        ];
        editor.create();
        editor.txt.html('${fn:replace(topic.content,vEnter,'')}');
        $("#form").submit(function () {
            if (confirm("确定编辑此话题吗？")) {
                var title = $("#title").val();
                var contentHtml = editor.txt.html();
                if (!title || title.length > 120) {
                    alert('请输入标题，且最大长度在120个字符以内');
                    return false;
                }  else {
                    $.ajax({
                        url: '/api/user/edit',
                        type: 'post',
                        async: false,
                        cache: false,
                        dataType: 'json',
                        data: {
                            id: ${topic.topicId},
                            title: title,
                            content: contentHtml
                        },
                        success: function (data) {
                            if (data.success != null && data.success === true) {
                                alert("更新成功！");
                                window.location.href = "/topic/"+${topic.topicId};
                            } else {
                                toast(data.error);
                            }
                        },
                        error: function (data) {
                            toast(data.error);
                        }
                    })
                }
            }
            return false;
        });
    })
</script>
</body>
</html>
