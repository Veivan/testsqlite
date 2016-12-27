package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Testsqlite {

	static String dbname = "D:/Work/J2EE/Beginning/TestSQLite/test.db";
	// SQLite connection string
	static String url = "jdbc:sqlite:" + dbname;

	public static void main(String[] args) {
		/*
		 * createNewDatabase(dbname); createNewTable(); insert("Raw Materials",
		 * 3000); insert("Semifinished Goods", 4000); insert("Finished Goods",
		 * 5000); selectAll();
		 */

		selectAllpersons();
		
		//String picfile = "D:/Work/J2EE/Beginning/TestSQLite/picfromdb.jpg";
		String picfile = "D:/temp/";
		int id = 25672;
		readPicture(id, picfile + id + ".jpg") ;
	}

	/**
	 * Connect to a sample database
	 *
	 * @param fileName
	 *            the database file name : C:/sqlite/db/test.db
	 */
	public static void createNewDatabase(String fullfileName) {

		String url = "jdbc:sqlite:" + fullfileName;

		try (Connection conn = DriverManager.getConnection(url)) {
			if (conn != null) {
				DatabaseMetaData meta = conn.getMetaData();
				System.out
						.println("The driver name is " + meta.getDriverName());
				System.out.println("A new database has been created.");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Create a new table in the test database
	 *
	 */
	public static void createNewTable() {

		// SQL statement for creating a new table
		String sql = "CREATE TABLE IF NOT EXISTS warehouses (\n"
				+ "	id integer PRIMARY KEY,\n" + "	name text NOT NULL,\n"
				+ "	capacity real\n" + ");";

		try (Connection conn = DriverManager.getConnection(url);
				Statement stmt = conn.createStatement()) {
			// create a new table
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Connect to the test.db database
	 *
	 * @return the Connection object
	 */
	private static Connection connect() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	/**
	 * Insert a new row into the warehouses table
	 *
	 * @param name
	 * @param capacity
	 */
	public static void insert(String name, double capacity) {
		String sql = "INSERT INTO warehouses(name,capacity) VALUES(?,?)";

		try (Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, name);
			pstmt.setDouble(2, capacity);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * select all rows in the warehouses table
	 */
	public static void selectAll() {
		String sql = "SELECT id, name, capacity FROM warehouses";

		try (Connection conn = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			// loop through the result set
			while (rs.next()) {
				System.out.println(rs.getInt("id") + "\t"
						+ rs.getString("name") + "\t"
						+ rs.getDouble("capacity"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * select all rows in the persons table
	 */
	public static void selectAllpersons() {
		String sql = "SELECT id, name, summ, link, age FROM persons"; //picture, 

		try (Connection conn = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			// loop through the result set
			while (rs.next()) {
				System.out.println(rs.getInt("id") + "\t"
						+ rs.getString("name") + "\t" + rs.getString("summ")
						//+ "\t" + rs.getString("picture") + "\t"
						+ rs.getString("age") + rs.getString("link"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	

	/**
     * read the picture file and insert into the material master table
     *
     * @param materialId
     * @param filename
     */
    public static void readPicture(int materialId, String filename) {
        String selectSQL = "SELECT picture FROM persons WHERE id=?";
        ResultSet rs = null;
        FileOutputStream fos = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
 
        try {
            conn = connect();
            pstmt = conn.prepareStatement(selectSQL);
            pstmt.setInt(1, materialId);
            rs = pstmt.executeQuery();
 
            // write binary stream into file
            File file = new File(filename);
            fos = new FileOutputStream(file);
 
            System.out.println("Writing BLOB to file " + file.getAbsolutePath());
            while (rs.next()) {
                InputStream input = rs.getBinaryStream("picture");
                byte[] buffer = new byte[1024];
                while (input.read(buffer) > 0) {
                    fos.write(buffer);
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
 
                if (conn != null) {
                    conn.close();
                }
                if (fos != null) {
                    fos.close();
                }
 
            } catch (SQLException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
