package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
	
	public static void DoParsing() throws Exception {
		URL url = null;
		HttpsURLConnection urlconn = null;
		String fname;
		int n = 0;
		while (n <= 9) {
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

			/*
			 * /парсим текст страницы replacements =
			 * text.substring(text.indexOf(
			 * "<script src=\"http://samair.ru:81/js/m.js"
			 * type="text/javascript">) +
			 * "<script src=\"http://samair.ru:81/js/m.js"
			 * type="text/javascript">.length(),
			 * text.indexOf("</script></head>")).split(";"); //на самаире,
			 * возможно, в целях защиты от парсеров порты в списке выводятся
			 * javascript'ом //в начале страницы рандомом задаются 10 переменных
			 * для каждой цыфры, затем они скриптом же и выводятся в таблицу
			 * //replacements - как раз массив этих переменных cursor =
			 * text.indexOf("<tr><td>"); while (cursor != -1) { cursor +=
			 * "<tr><td>".length(); host = text.substring(cursor,
			 * text.indexOf("<script type=\"text/javascript\">", cursor));
			 * //host - адрес прокси сервера port =
			 * text.substring(text.indexOf(">document.write(\":\"+", cursor) +
			 * ">document.write(\":\"+".length(), text.indexOf(")</script>" ,
			 * cursor)); port = removeChar(port, '+'); for (int i = 0; i<10;
			 * i++) { port = port.replaceAll(replacements[i].split("=")[0],
			 * replacements[i].split("=")[1]); //подставляем вместо букв циферки
			 * } //port - порт сервера cursor = text.indexOf("</td><td>",
			 * cursor) + "</td><td>".length(); anon_level =
			 * text.substring(cursor, text.indexOf("</td><td>", cursor)); cursor
			 * = text.indexOf("</td><td>", cursor) + "</td><td>".length();
			 * cursor = text.indexOf("</td><td>", cursor) +
			 * "</td><td>".length(); country = text.substring(cursor,
			 * text.indexOf("</td></tr>", cursor)); //получаем остальную лабуду
			 * - тип сервера и страна, не пропадать же траффику зря) хотя они и
			 * вряд ли понадобятся ResultSet rs =
			 * st.executeQuery("select host, port from proxies where host = '"
			 * +host+"' and port = '"+port+"'"); if (!rs.next()) {
			 * st.executeUpdate
			 * ("INSERT INTO proxies (host, port, anon_level, country) VALUES ('"
			 * +host+"', '"+port+"', '"+anon_level+"', '"+country+"')");
			 * System.out.println("Added: "+host+":"+port); //Если такого хоста
			 * и порта в базе еще нету, то вносим его туда } cursor =
			 * text.indexOf("<tr><td>", cursor); }
			 */

			n++;

		}

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
		doc.getCol(1, 1);
	}

	public static void main(String[] args) {
		try {
			 DoParsing();

			// readHTML("d:/temp/children.htm");
			//DoHttpGet();
			//DoHttClientpGet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
