<%--
  Created by IntelliJ IDEA.
  User: Sarala Wimalratne
  Date: 08/03/12
  Time: 04:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Terms</title>
</head>
<body>
<table border=1>
    <thead><tr>
        <th>ID</th>
    </tr></thead>
    <c:forEach var="term" items="${terms.terms}">
        <tr>
            <td>${term.id}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>