<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Service.APIService" %>

<html>
<head>
    <title>와이파이 정보 구하기</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>

<%
    APIService apiService = new APIService();
    int count = apiService.getPublicWifiJson();
%>

<div>
    <% if (count > 0) {%>
    <div style="text-align: center;">
        <h1 style="margin: 20px 0"><%=count%>개의 WIFI 정보를 정상적으로 저장하였습니다.</h1>
        <a href="http://localhost:8080">홈으로 가기</a>
    </div>
    <% } else { %>
    <h1 style="text-align: center">데이터 저장 실패</h1>
    <% } %>
</div>

</body>
</html>
