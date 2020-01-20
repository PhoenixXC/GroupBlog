package top.xcphoenix.groupblog;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.xcphoenix.groupblog.service.crawl.CrawlBlogService;

import javax.annotation.Resource;

/**
 * @author      xuanc
 * @date        2020/1/15 下午3:44
 * @version     1.0
 */
@SpringBootTest
public class CrawlTest {

    @Autowired
    private CrawlBlogService crawlBlogService;

    @Test
    @Ignore
    void testCrawlAll() throws Exception {
        int uid = 10074;
        crawlBlogService.crawlIncrement(uid);
    }

}