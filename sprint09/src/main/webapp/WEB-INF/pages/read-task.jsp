<%@ page import="com.softserve.itacademy.model.Task" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Read existing Task</title>
    <style>
        <%@include file="../styles/main.css"%>
        th, td {
            text-align: left;
        }
    </style>
</head>
<body>
<%@include file="header.html"%>
<h2>Read existing Task</h2>
<%
    Task task = (Task) request.getAttribute("task");
%>
<table>
    <tr>
        <td>Id:</td>
        <th><%=task.getId()%></th>
    </tr>
    <tr>
        <td>Name:</td>
        <th><%=task.getTitle()%></th>
    </tr>
    <tr>
        <td>Priority:</td>
        <th><%=task.getPriority()%></th>
    </tr>
</table>
</body>
</html>
