<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录成功</title>
<script src="<c:url value="/js/jquery-1.4.2.min.js"></c:url>" type="text/javascript"></script>
<script src="logout.js" type="text/javascript"></script>
</head>
<body>
<h1>恭喜您，登录成功！</h1>
<p>
	认证用户ID: ${authentication.principal.id}
</p>
<p>
认证时间：${authentication.authenticatedDate}
</p>
<p>
<a href="javascript:logout();">统一注销</a>&nbsp;&nbsp;&nbsp;
<a href="login.do">返回登录</a>
</p>
</body>
</html>