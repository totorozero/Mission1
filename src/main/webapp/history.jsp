<%@ page import="dao.HistoryDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="dto.HistoryDTO" %>
<%@ page import="dao.WifiDAO" %>
<%@ page import="dto.WifiDTO" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>

<html>
<head>
    <title>와이파이 정보 구하기</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<h1>와이파이 정보 구하기</h1>
<a href="http://localhost:8080">홈</a>
<a href="#">|</a>
<a href="http://localhost:8080/history.jsp">위치 히스토리 목록</a>
<a href="#">|</a>
<a href="http://localhost:8080/load-wifi.jsp">Open API 와이파이 정보 가져오기</a>
<br>

<div>
    <%
        HistoryDAO historyDAO = new HistoryDAO();
        List<HistoryDTO> historyList = historyDAO.searchHistoryList();

        String strID = request.getParameter("id");
        if (strID != null) {
            historyDAO.deleteHistoryList(strID);
        }
    %>
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>x좌표</th>
            <th>y좌표</th>
            <th>조회일자</th>
            <th>비고</th>
        </tr>
        </thead>
        <tbody>
        <% if (historyList.isEmpty()) {%>
        <tr>
            <td colspan="5">위치 정보를 조회하신 이력이 없습니다.</td>
        </tr>
        <% } else { %>
        <% for (HistoryDTO historyDTO : historyList) { %>
        <tr>
            <td><%=historyDTO.getId()%>
            </td>
            <td><%=historyDTO.getLat()%>
            </td>
            <td><%=historyDTO.getLnt()%>
            </td>
            <td><%=historyDTO.getSearchDttm()%>
            </td>
            <td>
                <button onclick="deleteHistory(<%=historyDTO.getId()%>)">삭제</button>
            </td>
        </tr>
        <% }
        } %>
        </tbody>
    </table>
</div>
<script>
    function deleteHistory(ID) {
        if (confirm("데이터를 삭제하시겠습니까?")) {
            $.ajax({
                url: "http://localhost:8080/history.jsp",
                data: {id: ID},
                success: function () {
                    location.reload();
                }
            })
        }
    }
</script>
</body>
</html>
