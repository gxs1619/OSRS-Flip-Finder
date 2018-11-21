import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLPopulater {

    private Connection connect() {
        String url = "jdbc:sqlite:C:/Users/Griffin Seibold/Desktop/osrsflipfinderdb.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insert(int Id, String name, int sellprice, int sellquant, int buyprice, int buyquant, int margin) {
        String sql = "INSERT INTO Items(Id,name,SellPrice, SellQuantity, BuyPrice, BuyQuantity, Profit) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Id);
            pstmt.setString(2, name);
            pstmt.setInt(3, sellprice);
            pstmt.setInt(4, sellquant);
            pstmt.setInt(5, buyprice);
            pstmt.setInt(6, buyquant);
            pstmt.setInt(7, margin);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
