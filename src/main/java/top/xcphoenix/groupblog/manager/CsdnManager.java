package top.xcphoenix.groupblog.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import top.xcphoenix.groupblog.model.dao.Blog;
import top.xcphoenix.groupblog.processor.Processor;
import top.xcphoenix.groupblog.service.blog.content.BlogContentService;
import top.xcphoenix.groupblog.service.blog.userzone.UserZoneService;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

/**
 * @author      xuanc
 * @date        2020/1/13 下午5:40
 * @version     1.0
 */
@Slf4j
@Component("manager-csdn")
public class CsdnManager {

    @Resource(name = "content-csdn")
    private BlogContentService blogContentService;

    @Resource(name = "zone-csdn")
    private UserZoneService userZoneService;

    @Resource(name = "selenium")
    private Processor processor;

    private UrlBuilder urlBuilder;

    public void setUrl(String url) {
        this.urlBuilder = new UrlBuilder(url);
    }

    public void exec() throws ParseException {
        if (this.urlBuilder == null) {
            throw new IllegalArgumentException("url not configure");
        }

        log.info("begin get blogs from url: " + this.urlBuilder);
        while (true) {
            String url = urlBuilder.nextUrl();
            List<Blog> blogList = userZoneService.getPageBlogUrls(url);
            if (blogList == null) {
                break;
            }
            for (Blog blog : blogList) {
                String webContent = processor.processor(blog.getOriginalLink());
                blogContentService.getBlogFromHtml(webContent, blog);
            }

            log.info("blogs >> " + JSON.toJSONString(blogList, SerializerFeature.PrettyFormat));
        }
        log.info("get blogs end");
    }

}

@PropertySource(value = "classpath:config/manager/csdnManager.properties", encoding = "utf-8")
class UrlBuilder {

    private String initUrl;
    private int initPageNum = 1;

    /*
     * props
     */

    @Value("${manager.original.only:false}")
    private boolean onlyOriginal;
    @Value("${manager.original.arg}")
    private String originalArg;
    @Value("${manager.original.value}")
    private String originalVal;

    UrlBuilder(String initUrl) {
        this.initUrl = initUrl;
    }

    String nextUrl() {
        String url = initUrl + "/" + initPageNum;
        if (onlyOriginal) {
            url += "?" + originalArg + "=" + originalVal;
        }
        initPageNum ++;
        return url;
    }

    @Override
    public String toString() {
        return initUrl;
    }

}