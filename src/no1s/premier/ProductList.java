package no1s.premier;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ProductList {	
	public static void main(String... args) throws IOException {
		String url = "https://premier.no1s.biz/";
		String email = "micky.mouse@no1s.biz";	
		String password = "micky";
		String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";

		Response formRes = Jsoup.connect(url).userAgent(userAgent)
				.method(Method.GET).execute();
				
		HashMap<String, String> dataMap = new HashMap<>();
		dataMap.put("_method", "POST");
		dataMap.put("_csrfToken", formRes.cookie("csrfToken"));
		dataMap.put("email", email);
		dataMap.put("password", password);
		
		Response loginRes = Jsoup.connect(url).userAgent(userAgent)
				.cookies(formRes.cookies())
				.data(dataMap)
				.method(Method.POST)
				.execute();
		
		Response adminRes = Jsoup.connect(url + "admin").userAgent(userAgent)
				.cookies(loginRes.cookies())
				.method(Method.GET).execute();
		
		Document adminDoc = adminRes.parse();
		Elements adminTable = adminDoc.body().children().select("table");
		StringBuilder sb = new StringBuilder();		
		for (Element table : adminTable) {
			sb.append(table.select("tr").select("td").text());
		}

		Response page2Res = Jsoup.connect(url + "admin").userAgent(userAgent)
				.cookies(loginRes.cookies())
				.data("page", "2")
				.method(Method.GET).execute();
		
		Document page2Doc = page2Res.parse();
		Elements page2Table = page2Doc.body().children().select("table");
		for (Element table : page2Table) {
			sb.append(" ");
			sb.append(table.select("tr").select("td").text());
		}
		
		Response page3Res = Jsoup.connect(url + "admin").userAgent(userAgent)
				.cookies(loginRes.cookies())
				.data("page", "3")
				.method(Method.GET).execute();
		
		Document page3Doc = page3Res.parse();
		Elements page3Table = page3Doc.body().children().select("table");
		for (Element table : page3Table) {
			sb.append(" ");
			sb.append(table.select("tr").select("td").text());
		}
		
		String strTdList = sb.toString();
		String[] tdList = strTdList.split(" ");
		
		// csvèoóÕ
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("productList.csv")),"UTF-8")));
		for(int i = 0; i < tdList.length; i++) {
			pw.write("\"" + tdList[i] + "\",");
			if(i % 3 == 2) {
				pw.write("\n");
			}
		}
		pw.close();
	}
}
