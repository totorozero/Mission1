<%@ page import="dao.WifiDAO" %>
<%@ page import="dto.WifiDTO" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>와이파이 정보 구하기</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<h1>와이파이 정보 구하기</h1>
<a href="http://localhost:8080">홈</a>
<a>|</a>
<a href="http://localhost:8080/history.jsp">위치 히스토리 목록</a>
<a>|</a>
<a href="http://localhost:8080/load-wifi.jsp">Open API 와이파이 정보 가져오기</a>
<br>
<br>

<%
    String lat = request.getParameter("lat") == null ? "0.0" : request.getParameter("lat");
    String lnt = request.getParameter("lnt") == null ? "0.0" : request.getParameter("lnt");
%>

<div class="head">
    <span>LAT:</span><input type="text" id="lat" value="<%=lat%>">
    <span>LNT:</span><input type="text" id="lnt" value="<%=lnt%>">

    <button id="btn_cur_position"><span>내 위치 가져오기</span></button>
    <button id="btn_nearest_wifi"><span>근처 Wifi 정보 보기</span></button>
</div>

<div>
    <table>
        <thead>
        <tr>
            <th>거리(km)</th>
            <th>관리번호</th>
            <th>자치구</th>
            <th>와이파이명</th>
            <th>도로명 주소</th>
            <th>상세 주소</th>
            <th>설치 위치(층)</th>
            <th>설치 기관</th>
            <th>설치 유형</th>
            <th>서비스 구분</th>
            <th>망 종류</th>
            <th>설치 년도</th>
            <th>실내 외 구분</th>
            <th>WIFI 접속 환경</th>
            <th>x좌표</th>
            <th>y좌표</th>
            <th>작업일자</th>
        </tr>
        </thead>
        <tbody>
        <%
            if (!("0.0").equals(lat) && !("0.0").equals(lnt)) {
                WifiDAO wifiDAO = new WifiDAO();
                List<WifiDTO> list = wifiDAO.getNearestWifiList(lat, lnt);

                if (list != null) {
                    for (WifiDTO wifiDTO : list) {
        %>
        <tr>
            <td><%=wifiDTO.getDistance()%></td>
            <td><%=wifiDTO.getXSwifiMgrNo()%></td>
            <td><%=wifiDTO.getXSwifiWrdofc()%></td>
            <td><a href="detail_wifi.jsp?mgrNo=<%= wifiDTO.getXSwifiMgrNo() %>&distance=<%=wifiDTO.getDistance()%>&wifiname=<%=wifiDTO.getXSwifiMainNm()%>"><%= wifiDTO.getXSwifiMainNm() %></a></td>
            <td><%=wifiDTO.getXSwifiAdres1()%></td>
            <td><%=wifiDTO.getXSwifiAdres2()%></td>
            <td><%=wifiDTO.getXSwifiInstlFloor()%></td>
            <td><%=wifiDTO.getXSwifiInstlMby()%></td>
            <td><%=wifiDTO.getXSwifiInstlTy()%></td>
            <td><%=wifiDTO.getXSwifiSvcSe()%></td>
            <td><%=wifiDTO.getXSwifiCmcwr()%></td>
            <td><%=wifiDTO.getXSwifiCnstcYear()%></td>
            <td><%=wifiDTO.getXSwifiInoutDoor()%></td>
            <td><%=wifiDTO.getXSwifiRemars3()%></td>
            <td><%=wifiDTO.getLat()%></td>
            <td><%=wifiDTO.getLnt()%></td>
            <td><%=wifiDTO.getWorkDttm()%></td>
        </tr>
        <% } %>
        <% } %>
        <% } else { %>
        <td colspan='20'> 위치 정보를 입력하신 후에 조회해 주세요. </td>
        <% } %>
        </tbody>
    </table>
</div>

<script>
    document.getElementById("btn_cur_position").addEventListener("click", function () {
        if ('geolocation' in navigator) {
            navigator.geolocation.getCurrentPosition(function (position) {
                document.getElementById("lat").value = position.coords.latitude;
                document.getElementById("lnt").value = position.coords.longitude;
            })
        } else {
            alert("위치정보 확인 불가")
        }
    });

    document.getElementById("btn_nearest_wifi").addEventListener("click", function () {
        let latitude = document.getElementById("lat").value;
        let longitude = document.getElementById("lnt").value;

        if (latitude !== "" || longitude !== "") {
            window.location.assign("http://localhost:8080?lat=" + latitude + "&lnt=" + longitude);
        } else {
            alert("위치정보 확인 불가")
        }
    });
</script>
</body>
</html>