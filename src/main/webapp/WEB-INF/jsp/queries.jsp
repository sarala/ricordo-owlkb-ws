<%--
  Created by IntelliJ IDEA.
  User: sarala
  Date: 28/06/12
  Time: 17:26
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
        <th>Query templates</th>
    </tr></thead>
    <c:forEach var="query" items="${queries.queries}">
        <tr>
            <td>${query.query}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>