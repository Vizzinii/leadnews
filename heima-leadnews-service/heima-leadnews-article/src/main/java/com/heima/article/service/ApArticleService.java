package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;

public interface ApArticleService extends IService<ApArticle> {

    /**
     * 根据参数加载文章列表
     * @param articleHomeDto
     * @param loadtype 1为加载更多  2为加载最新
     * @return
     */
    public ResponseResult load(ArticleHomeDto articleHomeDto ,Short loadtype);

}
