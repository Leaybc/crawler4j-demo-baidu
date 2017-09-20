package com.data.controller;
import com.data.crawler.MyCrawler;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "c:/crawl"; // 定义爬虫数据存储位置
        int numberOfCrawlers = 1; // 定义7个爬虫，也就是7个线程

        CrawlConfig config = new CrawlConfig(); // 定义爬虫配置
        config.setCrawlStorageFolder(crawlStorageFolder); // 设置爬虫文件存储位置

        /*
         * 实例化爬虫控制器
         */
        PageFetcher pageFetcher = new PageFetcher(config); // 实例化页面获取器
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig(); // 实例化爬虫机器人配置 比如可以设置 user-agent
        robotstxtConfig.setUserAgentName("Baiduspider");
        // 实例化爬虫机器人对目标服务器的配置，每个网站都有一个robots.txt文件 规定了该网站哪些页面可以爬，哪些页面禁止爬，该类是对robots.txt规范的实现
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        // 实例化爬虫控制器
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /**
         * 配置爬虫种子页面，就是规定的从哪里开始爬，可以配置多个种子页面
         */
        controller.addSeed("http://news.baidu.com/n?cmd=4&class=civilnews&pn=1");
        /*controller.addSeed("http://open.toutiao.com/a6332618777879904513/?utm_campaign=open&utm_medium=webview&utm_source=samsung_llq_mz");
        controller.addSeed("http://open.toutiao.com/a6357111520753713409/?utm_campaign=open&utm_medium=webview&utm_source=weimiliulanqi");
        controller.addSeed("http://open.toutiao.com/a4437650566/?utm_campaign=open&utm_source=jinlisl_wap&union_ad_enabled_by_lua=1&utm_medium=webview&guid=2041707241546510000&disable_antispam_by_lua=0");
        controller.addSeed("http://open.toutiao.com/a6320502929095262465/?utm_campaign=open&utm_medium=webview&utm_source=samsung_llq_api&gy=204605c7560fee9c9896574b7ded474c21fb411948fbf8c460edc59bbeceb2e556eb0a7226c2765e9bc00a596cff593433384d503c12f8b5ffc7167bb1f2a31c&crypt=8040&ac=unknown&item_id=6320505053064987137&behot_time=1504865449&label=related_news");*/
        /**
         * 启动爬虫，爬虫从此刻开始执行爬虫任务，根据以上配置
         */
        controller.start(MyCrawler.class, numberOfCrawlers);
    }
}
