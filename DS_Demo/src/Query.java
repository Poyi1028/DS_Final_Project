import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Query 
{
	public String searchKeyword;
	public String url;
	public String content;
	
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("請輸入關鍵字:");
		String keyword = sc.nextLine();
		
		try {
			Query q = new Query(keyword);
			q.query();
		}catch (IOException e){
			e.printStackTrace();
		}
		
		sc.close();
	}
	
	public Query(String searchKeyword)
	{
		this.searchKeyword = searchKeyword;
		try
		{
			// This part has been specially handled for Chinese keyword processing. 
			// You can comment out the following two lines 
			// and use the line of code in the lower section. 
			// Also, consider why the results might be incorrect 
			// when entering Chinese keywords.
			String encodeKeyword=java.net.URLEncoder.encode(searchKeyword,"utf-8");
			this.url = "https://www.google.com/search?q="+encodeKeyword+"&oe=utf8&num=50";
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
	
	public HashMap<String, String> query() throws IOException
	{
		if(content == null)
		{
			content = fetchContent();
		}

		HashMap<String, String> retVal = new HashMap<String, String>();
		Set<String> filter = new HashSet<String>();
		filter.add("www.netflix.com");
		filter.add("ani.gamer.com.tw");
		filter.add("www.cartoonmad.com");
		filter.add("777tv.ai");
		filter.add("www.linetv.tw");
		filter.add("tw.iqiyi.com");
		filter.add("gimy.is");
		filter.add("gimy.ai");
		filter.add("gimytv.tw");
		filter.add("bilibli.com");
		filter.add("douban.com");
		filter.add("tv.apple.com");
		
		
		/* 
		 * some Jsoup source
		 * https://jsoup.org/apidocs/org/jsoup/nodes/package-summary.html
		 * https://www.1ju.org/jsoup/jsoup-quick-start
 		 */
		
		//using Jsoup analyze html string
		Document doc = Jsoup.parse(content);
		
		//select particular element(tag) which you want 
		Elements lis = doc.select("div");
		lis = lis.select(".kCrYT");
		
		for(Element li : lis)
		{
			try 
			{
				String citeUrl = li.select("a").get(0).attr("href").replace("/url?q=", "");
				String title = li.select("a").get(0).select(".vvjwJb").text();
				
				int saIndex = citeUrl.indexOf("&sa=");

		        // 如果找到了 "&sa="，則截取該位置之前的部分
		        if (saIndex != -1) {
		            citeUrl = citeUrl.substring(0, saIndex);
		        }
		        
				if(title.equals("")) 
				{
					continue;
				}
				
				for(String s : filter) {
					if(citeUrl.contains(s)) {
						System.out.println(title + "\n" + citeUrl);
						System.out.println("------------");
						//put title and pair into HashMap
						retVal.put(title, citeUrl);
					}
				}
				
				

			} catch (IndexOutOfBoundsException e) 
			{
//				e.printStackTrace();
			}
		}
		
		return retVal;
	}
}