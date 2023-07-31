<%@ page import="com.softserve.itacademy.model.Task" %>
<%@ page import="com.softserve.itacademy.model.Priority" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit existing Task</title>

    <style>
        <%@include file="../styles/main.css"%>

        .disable {
            background-color: rgba(248,248,248,255);
            border-color: rgba(223,223,223,255);
            border-width: 0.4mm;
            border-radius: 0.5mm;
            color: gray;
        }
    </style>

</head>
<body>
<%@include file="header.html" %>
<h2>Create new Task</h2>
<form action="/edit-task" method="post">
    <c:if test="${not empty errorMessage}">
        <p>${errorMessage}</p>
    </c:if>
    <table>
        <tr>
            <td><label for="id">Id: </label></td>
            <td><input type="text" id="id" name="id" class="disable"
                       value="<%=request.getAttribute("task") == null ? "" :
                       ((Task) request.getAttribute("task")).getId()%>" readonly>
            </td>
        </tr>
        <tr>
            <td><label for="title">Name: </label></td>
            <td><input type="text" id="title" name="title"
                       value="<%=request.getAttribute("task") == null ? "" :
                       ((Task) request.getAttribute("task")).getTitle()%>">
            </td>
        </tr>
        <tr>
            <td><label for="priority">Priority: </label></td>
            <td>
                <select name="priority" id="priority">
                    <%
                        for (Priority priority : Priority.values()) {
                            String pr = priority.toString().substring(0, 1).toUpperCase() +
                                    priority.toString().substring(1).toLowerCase();
                            if (request.getAttribute("task") == null ||
                                    !priority.equals(((Task) request.getAttribute("task")).getPriority())) { %>
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
            <td><input type="submit" value="Update"></td>
            <td><input type="reset" value="Clear"></td>
        </tr>
    </table>
</form>
</body>
</html>