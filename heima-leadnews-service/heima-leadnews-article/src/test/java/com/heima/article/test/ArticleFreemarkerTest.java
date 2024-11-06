package com.heima.article.test;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.article.ArticleApplication;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.service.ApArticleService;
import com.heima.file.service.MinIOFileStorageService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleContent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = ArticleApplication.class)
@RunWith(SpringRunner.class)
public class ArticleFreemarkerTest {

    @Autowired
    private ApArticleContentMapper articleContentMapper;

    @Autowired
    private Configuration freemarkerConfig;

    @Autowired
    private MinIOFileStorageService minIOFileStorageService;

    @Autowired
    private ApArticleService apArticleService;

    @Test
    public void createStaticUrlTest() throws Exception {

        // 1. 根据已知的文章id获取文章内容，这需要我们新建一个查询文章内容的mapper，即 ApArticleContentMapper
        ApArticleContent apArticleContent = articleContentMapper.selectOne(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId, 1302862387124125698L));
        if(apArticleContent != null && StringUtils.isNotBlank(apArticleContent.getContent())){
            // 2. 文章内容通过 freemarker 生成 html 文件
            Template template = freemarkerConfig.getTemplate("article.ftl"); //首先找到用来生成 html 文件的模板，这个模板在 resource 下的 templates 中
            StringWriter out = new StringWriter();
            Map<String, Object> content = new HashMap<>();

            // "content" 是 map 对象 content 的键。用 ap_article_content 表中的 article_id 和 content 两列的值作为 map 对象 content 的值。
            // JSONArray.parseArray() 作用是把 String 对象转成 Object 对象
            content.put("content", JSONArray.parseArray(apArticleContent.getContent()));
            template.process(content, out);

            // 3， 把 html 文件上传到 minio 中
            InputStream inputStream = new ByteArrayInputStream(out.toString().getBytes()); // 把 StringWriter 对象转换成 InputStream 对象
            String uploadHtmlFile = minIOFileStorageService.uploadHtmlFile("", apArticleContent.getArticleId() + ".html", inputStream);

            // 4. 修改 ap_article 表，保存 static_url 字段
//            ApArticle apArticle = new ApArticle();
//            apArticle.setId(apArticleContent.getArticleId());
//            apArticle.setStaticUrl(uploadHtmlFile);
//            apArticleService.updateById(apArticle);
            apArticleService.update(Wrappers.<ApArticle>lambdaUpdate().eq(ApArticle::getId, apArticleContent.getArticleId())
                    .set(ApArticle::getStaticUrl, uploadHtmlFile));
        }
    }
}
