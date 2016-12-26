package main;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class DocPage {

	private Document doc;

	public DocPage(String docname) {
		try {
			doc = Jsoup.parse(new File(docname), "utf-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getCol(int rown, int coln) // PersonRec
	{
		Element title = null;
		PersonRec person = new PersonRec();
		String rowclass = rown == 1 ? "row-1 row-first" : "row-2 row-last";
		Element table = doc.getElementsByClass("views-view-grid grid-4")
				.first();
		Element row = table.getElementsByClass(rowclass).first();
		Element col = row.getElementsByClass("col-" + coln).first();
		title = col.getElementsByClass("views-field-title").first();
		if (title != null) {
			Element field_content = title.getElementsByClass("field-content")
					.first();
			Element href = field_content.getElementsByTag("a").first();
			System.out.println(href.text());
		}

		title = col.getElementsByClass("views-field-field-photo-fid").first();
		if (title != null) {
			Element field_content = title.getElementsByClass("field-content")
					.first();
			Element src = field_content.getElementsByTag("img").first();
			String imgref = src.attr("src");
			if (imgref != null);
		}
		title = col.getElementsByClass("views-field-field-date-of-birth-value")
				.first();
		if (title != null) {
			Element field_content = title.getElementsByClass("field-content")
					.first();
			System.out.println(field_content.text());
		}
		title = col.getElementsByClass("views-field-phpcode").first();
		if (title != null) {
			Element field_content = title.getElementsByClass("b-money").first();
			System.out.println(field_content.text());
		}

	}

}
