package com.gs.spider.utils;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * NLPExtractor
 *
 * @author Gao Shen
 * @version 16/7/29
 */
@Component
public interface NLPExtractor {
    /**
     * 抽取命名实体
     *
     * @param content 文章正文
     * @return map的key是一下三种nr, ns, nt  其value就是对应的词表
     */
    Map<String, Set<String>> extractNamedEntity(String content);

    /**
     * 抽取摘要
     *
     * @param content 文章正文
     * @return 摘要句子列表
     */
    List<String> extractSummary(String content);

    /**
     * 抽取关键词
     *
     * @param content 文章正文
     * @return 关键词列表
     */
    List<String> extractKeywords(String content);
}
