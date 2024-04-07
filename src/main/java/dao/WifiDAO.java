package dao;

import Database.DBConnect;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dto.WifiDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static dao.HistoryDAO.searchHistory;

public class WifiDAO {
    public static Connection conn;
    public static PreparedStatement ps;
    public static ResultSet rs;

    public WifiDAO() {
    }

    public static int insertPublicWifi(JsonArray jsonArray) {
        conn = null;
        ps = null;
        rs = null;

        int count = 0;

        try {
            conn = DBConnect.connectDB();
            conn.setAutoCommit(false);

            String sql = "insert into public_wifi "
                    + "( x_swifi_mgr_no, x_swifi_wrdofc, x_swifi_main_nm, x_swifi_adres1, x_swifi_adres2, "
                    + "x_swifi_instl_floor, x_swifi_instl_ty, x_swifi_instl_mby, x_swifi_svc_se, x_swifi_cmcwr, "
                    + "x_swifi_cnstc_year, x_swifi_inout_door, x_swifi_remars3, lat, lnt, work_dttm) "
                    + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ";

            ps = conn.prepareStatement(sql);

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject data = (JsonObject) jsonArray.get(i).getAsJsonObject();

                ps.setString(1, data.get("X_SWIFI_MGR_NO").getAsString());
                ps.setString(2, data.get("X_SWIFI_WRDOFC").getAsString());
                ps.setString(3, data.get("X_SWIFI_MAIN_NM").getAsString());
                ps.setString(4, data.get("X_SWIFI_ADRES1").getAsString());
                ps.setString(5, data.get("X_SWIFI_ADRES2").getAsString());
                ps.setString(6, data.get("X_SWIFI_INSTL_FLOOR").getAsString());
                ps.setString(7, data.get("X_SWIFI_INSTL_TY").getAsString());
                ps.setString(8, data.get("X_SWIFI_INSTL_MBY").getAsString());
                ps.setString(9, data.get("X_SWIFI_SVC_SE").getAsString());
                ps.setString(10, data.get("X_SWIFI_CMCWR").getAsString());
                ps.setString(11, data.get("X_SWIFI_CNSTC_YEAR").getAsString());
                ps.setString(12, data.get("X_SWIFI_INOUT_DOOR").getAsString());
                ps.setString(13, data.get("X_SWIFI_REMARS3").getAsString());
                ps.setString(14, data.get("LAT").getAsString());
                ps.setString(15, data.get("LNT").getAsString());
                ps.setString(16, data.get("WORK_DTTM").getAsString());

                ps.addBatch();
                ps.clearParameters();

                if ((i + 1) % 1000 == 0) {
                    int[] result = ps.executeBatch();
                    count += result.length;
                    conn.commit();
                }
            }
            int[] result = ps.executeBatch();
            count += result.length;
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnect.close(conn, ps, rs);
        }

        return count;
    }

    public List<WifiDTO> getNearestWifiList(String lat, String lnt) {
        conn = null;
        ps = null;
        rs = null;

        List<WifiDTO> list = new ArrayList<>();

        try {
            conn = DBConnect.connectDB();

            String sql = "SELECT *, " +
                    "round(6371*acos(cos(radians(?))*cos(radians(LAT))*cos(radians(LNT) " +
                    "-radians(?))+sin(radians(?))*sin(radians(LAT))), 4) " +
                    "AS distance " +
                    "FROM public_wifi " +
                    "ORDER BY distance " +
                    "LIMIT 20;";

            ps = conn.prepareStatement(sql);
            ps.setDouble(1, Double.parseDouble(lat));
            ps.setDouble(2, Double.parseDouble(lnt));
            ps.setDouble(3, Double.parseDouble(lat));

            rs = ps.executeQuery();

            while (rs.next()) {
                WifiDTO wifiDTO = WifiDTO.builder()
                        .distance(rs.getDouble("distance"))
                        .xSwifiMgrNo(rs.getString("x_swifi_mgr_no"))
                        .xSwifiWrdofc(rs.getString("x_swifi_wrdofc"))
                        .xSwifiMainNm(rs.getString("x_swifi_main_nm"))
                        .xSwifiAdres1(rs.getString("x_swifi_adres1"))
                        .xSwifiAdres2(rs.getString("x_swifi_adres2"))
                        .xSwifiInstlFloor(rs.getString("x_swifi_instl_floor"))
                        .xSwifiInstlTy(rs.getString("x_swifi_instl_ty"))
                        .xSwifiInstlMby(rs.getString("x_swifi_instl_mby"))
                        .xSwifiSvcSe(rs.getString("x_swifi_svc_se"))
                        .xSwifiCmcwr(rs.getString("x_swifi_cmcwr"))
                        .xSwifiCnstcYear(rs.getString("x_swifi_cnstc_year"))
                        .xSwifiInoutDoor(rs.getString("x_swifi_inout_door"))
                        .xSwifiRemars3(rs.getString("x_swifi_remars3"))
                        .lat(rs.getString("lat"))
                        .lnt(rs.getString("lnt"))
                        .workDttm(String.valueOf(rs.getTimestamp("work_dttm").toLocalDateTime()))
                        .build();

                list.add(wifiDTO);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnect.close(conn, ps, rs);
        }
        searchHistory(lat, lnt);

        return list;
    }
}
