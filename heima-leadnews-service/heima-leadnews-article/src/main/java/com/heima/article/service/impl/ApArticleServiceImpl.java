package com.heima.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;
import jdk.nashorn.api.scripting.ScriptUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService{

    // 单页最大加载的数字
    private final static short MAX_PAGE_SIZE = 50;
    // 单页默认加载的数字
    private final static short DEFAULT_PAGE_SIZE = 10;

    private final ApArticleMapper articleMapper;

    /**
     * 根据参数加载文章列表
     * @param articleHomeDto
     * @param loadtype 1为加载更多  2为加载最新
     * @return
     */
    @Override
    public ResponseResult load(ArticleHomeDto articleHomeDto, Short loadtype) {
        // 1. 校验参数
        // 分页条数的校验
        Integer size = articleHomeDto.getSize();
        if (size == null || size <= 0) {
            size = (int)DEFAULT_PAGE_SIZE;
        }
        size = (int)size > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : size;

        // 校验参数loadtype
        if(!loadtype.equals(ArticleConstants.LOADTYPE_LOAD_MORE) && !loadtype.equals(ArticleConstants.LOADTYPE_LOAD_NEW)){
            loadtype = ArticleConstants.LOADTYPE_LOAD_NEW;
        }

        // 校验频道参数
        if(StringUtils.isBlank(articleHomeDto.getTag())){
            articleHomeDto.setTag(ArticleConstants.DEFAULT_TAG);
        }

        // 时间校验
        if(articleHomeDto.getMaxBehotTime()==null){
            articleHomeDto.setMaxBehotTime(new Date());
        }
        if (articleHomeDto.getMinBehotTime()==null){
            articleHomeDto.setMinBehotTime(new Date());
        }

        // 2.查询
        List<ApArticle> articleList = articleMapper.loadArticleList(articleHomeDto, loadtype);

        // 3.结果封装返回
        return ResponseResult.okResult(articleList);
    }
}
