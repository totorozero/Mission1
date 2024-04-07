package dao;

import Database.DBConnect;
import dto.HistoryDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryDAO {
    public static Connection conn;
    public static PreparedStatement ps;
    public static ResultSet rs;

    public static void searchHistory(String lat, String lnt) {
        conn = null;
        ps = null;
        rs = null;

        try {
            conn = DBConnect.connectDB();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String strDate = sdf.format(new Date());

            String sql = "insert into history (lat, lnt, search_dttm) values ( ?, ?, ? )";

            ps = conn.prepareStatement(sql);

            ps.setString(1, lat);
            ps.setString(2, lnt);
            ps.setString(3, strDate.toString());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnect.close(conn, ps, rs);
        }
    }

    public List<HistoryDTO> searchHistoryList() {
        conn = null;
        ps = null;
        rs = null;

        List<HistoryDTO> list = new ArrayList<>();

        try {
            conn = DBConnect.connectDB();
            String sql = "select * from history order by id desc";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                HistoryDTO historyDTO = new HistoryDTO(
                        rs.getInt("id")
                        , rs.getString("lat")
                        , rs.getString("lnt")
                        , rs.getString("search_dttm")
                );
                list.add(historyDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnect.close(conn, ps, rs);
        }

        return list;
    }

    public void deleteHistoryList(String id) {
        conn = null;
        ps = null;
        rs = null;

        try {
            conn = DBConnect.connectDB();
            String sql = "delete from history where id = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnect.close(conn, ps, rs);
        }
    }
}
