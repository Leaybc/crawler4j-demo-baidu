package com.data.crawler;

import com.csvreader.CsvWriter;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


public class MyCrawler extends WebCrawler {

    /**
     * 正则匹配指定的后缀文件
     */
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp3|zip|gz))$");
    private final static String CSV_PATH = "C://info.csv";
    private CsvWriter cw;
    private File csv;

    public MyCrawler() throws IOException {
        csv = new File(CSV_PATH);

        if (csv.isFile()) {
            csv.delete();
        }
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(CSV_PATH, true), "GBK"));
        cw = new CsvWriter(writer, ',');
        cw.write("URL");
        cw.write("标题");
        cw.write("来源");
        cw.write("时间");
        cw.write("分词");
        cw.endRecord();
        cw.close();
    }

    /**
     * 这个方法主要是决定哪些url我们需要抓取，返回true表示是我们需要的，返回false表示不是我们需要的Url
     * 第一个参数referringPage封装了当前爬取的页面信息
     * 第二个参数url封装了当前爬取的页面url信息
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();  // 得到小写的url
        return !FILTERS.matcher(href).matches()   // 正则匹配，过滤掉我们不需要的后缀文件
                && href.startsWith("http://open.toutiao.com");  // url必须是http://www.java1234.com/开头，规定站点
    }

    /**
     * 当我们爬到我们需要的页面，这个方法会被调用，我们可以尽情的处理这个页面
     * page参数封装了所有页面信息
     */
    @Override
    public void visit(Page page) {
        ToAnalysis.parse("");
        SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd");
        if (page.getParseData() instanceof HtmlParseData) {  // 判断是否是html数据
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData(); // 强制类型转换，获取html数据对象
            Document doc = Jsoup.parse(htmlParseData.getHtml());

            Elements news = doc.select("a[mon=ph]");
            for (Element c : news) {
                String url = c.attr("href");
                System.out.println("url" + url);
                String title = c.text();
                Element sourceDate = c.nextElementSibling();
                String sourceDateStr = sourceDate.text();
                String[] sourceDateArray = sourceDateStr.split(" ");
                String source = sourceDateArray[0];
                String date = myFmt2.format(new Date()) + " " + sourceDateArray[1];
                System.out.println("title" + title);
                System.out.println("source:" + source);
                System.out.println("date:" + date);
                System.out.println("");

                Result result = ToAnalysis.parse(title); //分词结果的一个封装，主要是一个List<Term>的terms
                List<Term> terms = result.getTerms(); //拿到terms
                System.out.println("总条数：" + terms.size() + " 分词结果：");
                String resultStr = "";
                for (int i = 0; i < terms.size(); i++) {
                    resultStr = resultStr.concat(terms.get(i).getName()).concat(","); //拿到词
                    System.out.print(terms.get(i).getName() + "   ");
                }
                if (resultStr.length() > 1) {
                    resultStr = resultStr.substring(0, resultStr.length() - 1);
                }
                try {
                    Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(CSV_PATH, true), "GBK"));
                    cw = new CsvWriter(writer, ',');
                    cw.write(url);
                    cw.write(title);
                    cw.write(source);
                    cw.write(date);
                    cw.write(resultStr);
                    cw.endRecord();
                    cw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }



           /* String title=htmlParseData.getTitle();
            System.out.println("title:"+title);
            Result result = ToAnalysis.parse(title); //分词结果的一个封装，主要是一个List<Term>的terms
            List<Term> terms = result.getTerms(); //拿到terms
            System.out.println("总条数："+terms.size()+" 分词结果：");
            for(int i=0; i<terms.size(); i++) {
                String word = terms.get(i).getName(); //拿到词
                    System.out.print(word + "   ");
            }
            System.out.println("");*/

        }
    }
}