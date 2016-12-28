package main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class BDwriter extends Thread {
	private String url;
	PersonRec precord;

	public BDwriter(String dbname, PersonRec prec) {
		url = "jdbc:sqlite:" + dbname;
		precord = prec;
	}

	/**
	 * Connect to the database. It'c created if not exists.
	 *
	 * @return the Connection object
	 * @throws Exception
	 */
	private Connection connect() throws Exception {
		Connection conn = DriverManager.getConnection(url);

		// SQL statement for creating a new table
		String sql = "CREATE TABLE IF NOT EXISTS persons (\n"
				+ "	id integer,\n" + "	name text NOT NULL,\n"
				+ "	summ text NOT NULL,\n" + "	picture blob,\n"
				+ "	picturelink text,\n"
				+ "	link text NOT NULL,\n" + "	age text NOT NULL\n" + ");";

		try (Statement stmt = conn.createStatement()) {
			// create a new table
			stmt.execute(sql);
		} catch (SQLException e) {
			throw e;
		}

		return conn;
	}

	@Override
	public void run() {
		String sql = "INSERT INTO persons(id, name, summ, picture, link, age, picturelink) VALUES(?,?,?,?,?,?,?)";
		try (Connection conn = this.connect();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, precord.id);
			pstmt.setString(2, precord.name);
			pstmt.setString(3, precord.summ);
			pstmt.setBytes(4, precord.picture);
			pstmt.setString(5, precord.link);
			pstmt.setString(6, precord.age);
			pstmt.setString(7, precord.pictureLink);
			pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
     * Read the file and returns the byte array
     * @param file
     * @return the bytes of the file
     */
    private static byte[] readFile(String file) {
        ByteArrayOutputStream bos = null;
        FileInputStream fis = null;
        try {
            File f = new File(file);
            fis = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            bos = new ByteArrayOutputStream();
            for (int len; (len = fis.read(buffer)) != -1;) {
                bos.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e2) {
            System.err.println(e2.getMessage());
        }
        finally
        {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
        }
        return bos != null ? bos.toByteArray() : null;
    }
    
    public static void main(String[] args) {
		String dbname = "D:/Work/J2EE/Beginning/TestSQLite/test.db";
		String picfile = "D:/Work/J2EE/Beginning/TestSQLite/pic.jpg";
	
		PersonRec precord = new PersonRec(); 
		precord.id = 1;
		precord.name = "q1";
		precord.summ = "";
		precord.picture = readFile(picfile);
		precord.link = "href1";
		precord.age = "6 лет";
		BDwriter mconn = new BDwriter(dbname, precord);
		mconn.run();
/*
		for (int i = 2; i < 50; i++) {
			PersonRec precord = new PersonRec(); 
			precord.id = i;
			precord.name = "q1";
			precord.summ = "";
			precord.picture = "ww";
			precord.link = "href1";
			BDwriter mconn = new BDwriter(dbname, precord);
			mconn.run();			
		}
*/
		// logger.info("TWClient debug main");
	}

}
