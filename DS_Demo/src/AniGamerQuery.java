import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
//import java.util.HashMap;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AniGamerQuery 
{
	public String searchKeyword;
	public String url;
	public String content;
	public static ArrayList<String>titles = new ArrayList<String>();		
	public static ArrayList<String>utitles = new ArrayList<String>();
	public static ArrayList<String>urls = new ArrayList<String>();
	public static ArrayList<String>uurls = new ArrayList<String>();

	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("請輸入你的關鍵字:");
		String k1 = sc.next();
		String k2 = sc.next();
		String k3 = sc.next();
		try {
			AniGamerQuery q1 = new AniGamerQuery(k1);
			AniGamerQuery q2 = new AniGamerQuery(k2);
			AniGamerQuery q3 = new AniGamerQuery(k3);
			
			q1.query();
			q2.query();
			q3.query();
			
			for (int i = 0; i < utitles.size(); i++) {
				System.out.println(utitles.get(i));
				System.out.println(uurls.get(i));
			}
			
			for (int i = 0; i < titles.size(); i++) {
				System.out.println(titles.get(i));
				System.out.println(urls.get(i));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sc.close();
	}
	public AniGamerQuery(String searchKeyword)
	{
		this.searchKeyword = searchKeyword;
		
		try
		{
			String encodeKw = java.net.URLEncoder.encode(searchKeyword, "utf-8");
			this.url = "https://ani.gamer.com.tw/animeList.php?tags=" + encodeKw;
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	private String fetchContent() throws IOException
	{
		String retVal = "";

		URL u = new URL(url);
		URLConnection conn = u.openConnection();
		//set HTTP header
		conn.setRequestProperty("User-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
		InputStream in = conn.getInputStream();

		InputStreamReader inReader = new InputStreamReader(in, "utf-8");
		BufferedReader bufReader = new BufferedReader(inReader);
		String line = null;

		while((line = bufReader.readLine()) != null)
		{
			retVal += line;
		}
		return retVal;
	}
	
	public void query() throws IOException
	{
		if(content == null)
		{
			content = fetchContent();
		}


		//using Jsoup analyze html string
		Document doc = Jsoup.parse(content);
		
		Elements animes = doc.select("div.theme-list-block a.theme-list-main");
		
		for (Element ani : animes) {
			
			try {
				String citeUrl = ani.attr("href");
				citeUrl = "https://ani.gamer.com.tw/" + citeUrl;
				String title = ani.select("p.theme-name").text();				
				
				if (title.equals("")||titles.contains(title)) {
					if (titles.contains(title)) {
						utitles.add(title);
						uurls.add(citeUrl);
						titles.remove(title);
						urls.remove(citeUrl);
					}
					continue;
				}
				if(!utitles.contains(title)) {
					titles.add(title);
					urls.add(citeUrl);
				}
			}catch(IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			
			
		}
		
	}
	
	public void sort() {
		// here is your code
	}
}