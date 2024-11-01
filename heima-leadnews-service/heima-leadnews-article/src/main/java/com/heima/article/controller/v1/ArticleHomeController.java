package com.heima.article.controller.v1;

import com.heima.article.service.ApArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.common.dtos.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/article")
public class ArticleHomeController {

    private final ApArticleService articleService;

    /**
     * 加载
     * @param articleHomeDto
     * @return
     */
    @PostMapping("/load")
    public ResponseResult load(@RequestBody ArticleHomeDto articleHomeDto) {
        return articleService.load(articleHomeDto, ArticleConstants.LOADTYPE_LOAD_MORE);
    }


    /**
     * 加载更多
     * @param articleHomeDto
     * @return
     */
    @PostMapping("/loadmore")
    public ResponseResult loadMore(@RequestBody ArticleHomeDto articleHomeDto) {
        return articleService.load(articleHomeDto, ArticleConstants.LOADTYPE_LOAD_MORE);
    }


    /**
     * 加载更新
     * @param articleHomeDto
     * @return
     */
    @PostMapping("/loadnew")
    public ResponseResult loadNew(@RequestBody ArticleHomeDto articleHomeDto) {
        return articleService.load(articleHomeDto, ArticleConstants.LOADTYPE_LOAD_NEW);
    }

}
