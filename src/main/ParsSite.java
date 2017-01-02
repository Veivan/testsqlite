package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class ParsSite {
	int cnt = 12;

	private final String dbname = "D:/Work/J2EE/Beginning/TestSQLite/test.db";

	public void DoParsing() throws Exception {
		URL url = null;
		HttpsURLConnection urlconn = null;
		String fname;
		int n = 0;
		while (n <= cnt) {
			if (n == 0) {
				url = new URL("https://podari-zhizn.ru/main/children");
				fname = "d:/temp/children.htm";
			} else {
				url = new URL("https://podari-zhizn.ru/main/children?page=" + n);
				fname = "d:/temp/children" + n + ".htm";
			}

			System.out.println("Starting page: " + Integer.toString(n));
			urlconn = (HttpsURLConnection) url.openConnection();
			urlconn.setRequestMethod("GET");
			urlconn.connect();
			System.out.println("Response Code : " + urlconn.getResponseCode());
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlconn.getInputStream()));

			StringBuffer text = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				text.append(line);
			}

			Save2file(text.toString(), fname);

			n++;
		}
	}

	public void DoParsingFromFiles() throws Exception {
		String fname;
		int n = 0;
		while (n <= cnt) {
			if (n == 0) {
				fname = "d:/temp/children.htm";
			} else {
				fname = "d:/temp/children" + n + ".htm";
			}
			DocPage doc = new DocPage(fname);
			for (int i = 1; i <= 2; i++)
				for (int j = 1; j <= 4; j++) {
					PersonRec person = doc.getCol(i, j);
					person.link = "https://podari-zhizn.ru/main/node/"
							+ person.id;
					person.picture = GetPicture(person.pictureLink);
					BDwriter mconn = new BDwriter(dbname, person);
					mconn.run();
				}
			n++;
		}

	}

	public static byte[] GetPicture(String pictureLink) throws Exception {
		URL connection = new URL(pictureLink);
		HttpsURLConnection urlconn = (HttpsURLConnection) connection
				.openConnection();
		urlconn.setRequestMethod("GET");
		urlconn.connect();

		try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
			byte[] buffer = new byte[1024];
			int length;
			while ((length = urlconn.getInputStream().read(buffer)) != -1) {
				result.write(buffer, 0, length);
			}
			return result.toByteArray();
		} catch (Exception e) {

			System.out.println(e.getMessage());
		} finally {
			urlconn.disconnect();
		}
		return null;
	}

	// HTTP GET request
	public static void DoHttpGet() throws Exception {
		URL connection = new URL("https://podari-zhizn.ru/main/children");
		HttpsURLConnection urlconn = (HttpsURLConnection) connection
				.openConnection();
		urlconn.setRequestMethod("GET");
		urlconn.connect();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				urlconn.getInputStream()));

		StringBuffer text = new StringBuffer();
		String line = "";
		while ((line = reader.readLine()) != null) {
			text.append(line);
		}
		Save2file(text.toString(), "d:/temp/children0.htm");
		urlconn.disconnect();
	}

	// HttpClient GET request
	public static void DoHttClientpGet() throws Exception {
		HttpClient client = HttpClientBuilder.create().build();
		String url = "https://podari-zhizn.ru/main/children";
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);

		System.out.println("Response Code : "
				+ response.getStatusLine().getStatusCode());

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent()));

		StringBuffer text = new StringBuffer();
		String line = "";
		while ((line = reader.readLine()) != null) {
			text.append(line);
		}

		Save2file(text.toString(), "d:/temp/children02.htm");
	}

	private static void Save2file(String buffer, String filename)
			throws Exception {
		BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(
				filename)));
		// write contents of StringBuffer to a file
		bwr.write(buffer);
		// flush the stream
		bwr.flush();
		// close the stream
		bwr.close();
	}

	public static void readHTML(String html) {
		DocPage doc = new DocPage(html);
		// DocPage doc = new DocPage();
		doc.getCol(1, 1);
	}

	public static void main(String[] args) {
		ParsSite parser = new ParsSite();
		try {
			parser.DoParsing(); // Getting pages 2 files
			parser.DoParsingFromFiles();

			// readHTML("d:/temp/children.htm");
			// DoHttpGet();
			// DoHttClientpGet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
