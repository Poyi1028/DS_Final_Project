import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Scanner;

public class test {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("請輸入要搜索的關鍵字: ");
        String keyword = scanner.nextLine();

        try {
            // 將關鍵字進行 URL 編碼
            String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");

            // 构建巴哈姆特動畫瘋搜尋的 URL
            String bahamutSearchUrl = "https://ani.gamer.com.tw/animeList.php?tags=" + encodedKeyword;

            // 发送 HTTP 請求並取得搜尋結果頁面
            Document document = Jsoup.connect(bahamutSearchUrl).get();

            // 解析搜索結果頁面，提取動畫標題和相關資訊
            Elements animeResults = document.select("div.search_list");
            for (Element animeResult : animeResults) {
                Element titleElement = animeResult.selectFirst("a.title");
                Element infoElement = animeResult.selectFirst("div.info");

                if (titleElement != null && infoElement != null) {
                    String title = titleElement.text();
                    String info = infoElement.text();

                    System.out.println("動畫標題: " + title);
                    System.out.println("相關資訊: " + info);
                    System.out.println("------");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        scanner.close();
    }
}
