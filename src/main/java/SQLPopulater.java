import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Written by: Griffin Seibold
 * 1/30/2019
 * This class handles all the interactions with the database. The key
 * methods in this class are connect which is used to connect to the database
 * and insert which is used to refresh or add items to the database.
 */
public class SQLPopulater {

    /**
     * Connect to the database and return that connection for use.
     * @return connection
     */
    private Connection connect() {
        String url = "jdbc:sqlite:/Users/griffin/Projects/OSRSFlipFinderDB.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Insert an item into the database, if it is already there it will
     * refresh the item in the database rather than make duplicates.
     * @param Id of item
     * @param name of item
     * @param sellprice selling price of item
     * @param sellquant selling quantity of item
     * @param buyprice buying price of item
     * @param buyquant buying quantity of item
     * @param margin profit margin of item
     * @param lastUpdated time last updated of item
     */
    public void insert(int Id, String name, int sellprice, int sellquant, int buyprice, int buyquant, int margin, String lastUpdated) {
        String sql = "INSERT OR REPLACE INTO Items(Id,name,SellPrice, SellQuantity, BuyPrice, BuyQuantity, Profit, LastUpdated) VALUES(?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Id);
            pstmt.setString(2, name);
            pstmt.setInt(3, sellprice);
            pstmt.setInt(4, sellquant);
            pstmt.setInt(5, buyprice);
            pstmt.setInt(6, buyquant);
            pstmt.setInt(7, margin);
            pstmt.setString(8, lastUpdated);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
