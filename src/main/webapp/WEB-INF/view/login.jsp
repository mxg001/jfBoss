<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

 <c:if test="${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal != null}">
    <c:redirect url="welcome.do"/>
</c:if>   
    
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>登录</title>

    <link href="${ctx}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${ctx}/font-awesome/css/font-awesome.css" rel="stylesheet">

    <link href="${ctx}/css/animate.css" rel="stylesheet">
    <link href="${ctx}/css/style.css" rel="stylesheet">
</head>

<body class="loginbg">
    <div class="row login-bg">
    <h2 class="login-box-title"><span>欢迎登录移联会员系统</span></h2>
    <div class="ibox-content animated fadeInDown" style="width:390px; overflow: hidden; margin: 80px auto 0">
        <c:url value="/perform_login.do" var="loginUrl" />
        <form action="${loginUrl}" method="post">

            <c:if test="${param.logout != null || param.logout == null}">
                <p class="ibox-ctitle">欢迎登录</p>
            </c:if>
            <c:if test="${param.error != null}">
                <c:choose>
                    <c:when test="${SPRING_SECURITY_LAST_EXCEPTION.message == 'blocked'}">
                        <p>登录错误太多,30分钟后重试.</p>
                    </c:when>
                    <c:otherwise>
                        <p>用户名或密码错误.</p>
                    </c:otherwise>
                </c:choose>
            </c:if>
            <div class="form-group login-item">
                <span><img src="${ctx}/img/user.png"></span><input type="text" class="form-control" placeholder="请输入用户名" required=""  name="username">
            </div>
            <div class="form-group login-item">
                <span><img src="${ctx}/img/password.png"></span><input type="password" class="form-control" placeholder="请输入密码" required="" name="password">
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <button type="submit" class="login-btn">登录</button>

        </form>
        <!-- <p class="m-t">
            <small>Inspinia we app framework base on Bootstrap 3 &copy; 2016</small>
        </p> -->
    </div>
    <div class="loginColumns animated fadeInDown">



    </div>
    </div>
    <div style="position:fixed; bottom:10px; left:50%; width:1300px; margin-left:-600px; font-size:18px; text-align: center">
    <div class="col-md-12" style="color:#fff">
    Copyright 深圳市移付宝科技有限公司.   All Rights Reserved.&emsp;&emsp; 粤ICP备15076873号-1 © 2015-2016
    </div>
    </div>
</body>

</html>

