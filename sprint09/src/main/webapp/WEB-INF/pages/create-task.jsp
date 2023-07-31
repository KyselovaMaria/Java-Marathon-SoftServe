<%@ page import="com.softserve.itacademy.model.Task" %>
<%@ page import="com.softserve.itacademy.model.Priority" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<head>
    <title>Create new Task</title>
</head>
<body>
<%@include file="header.html"%>
<h2>Create new Task</h2>
<%
    Task task = (Task) request.getAttribute("task");
%>
<form action="/create-task" method="post">
    <c:if test="${not empty message}">
        <p>${message}</p>
    </c:if>
    <table>
        <tr>
            <td><label for="title">Name: </label></td>
            <td><input type="text" id="title" name="title"
                       value="<%=task == null ? "" :
                       task.getTitle()%>">
            </td>
        </tr>
        <tr>
            <td><label for="priority">Priority:</label></td>
            <td>
                <select name="priority" id="priority">
                    <%
                        for (Priority priority : Priority.values()) {
                            String pr = priority.toString().substring(0, 1).toUpperCase() +
                                    priority.toString().substring(1).toLowerCase();
                            if (task == null ||
                                    !priority.equals(task.getPriority())) { %>
                    <option value="<%=priority%>"><%=pr%>
                    </option>
                    <%
                    } else {
                    %>
                    <option value="<%=priority%>" selected><%=pr%>
                    </option>
                    <%
                            }
                        }
                    %>
                </select>
            </td>
        </tr>
        <tr>
            <td><input type="submit" value="Create"></td>
            <td><input type="reset" value="Reset"></td>
        </tr>
    </table>
</form>
</body>
</html>