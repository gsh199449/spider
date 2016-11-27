package com.gs.spider.service.commons.spiderinfo;

import com.gs.spider.dao.SpiderInfoDAO;
import com.gs.spider.model.commons.SpiderInfo;
import com.gs.spider.model.utils.ResultBundle;
import com.gs.spider.model.utils.ResultBundleBuilder;
import com.gs.spider.model.utils.ResultListBundle;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * SpiderInfoService
 *
 * @author Gao Shen
 * @version 16/7/18
 */
@Component
public class SpiderInfoService {
    private final static Logger LOG = LogManager.getLogger(SpiderInfoService.class);
    @Autowired
    private SpiderInfoDAO spiderInfoDAO;
    @Autowired
    private ResultBundleBuilder bundleBuilder;

    /**
     * 列出库中所有爬虫模板
     *
     * @param size 页面容量
     * @param page 页码
     * @return
     */
    public ResultListBundle<SpiderInfo> listAll(int size, int page) {
        return bundleBuilder.listBundle(null, () -> spiderInfoDAO.listAll(size, page));
    }

    /**
     * 根据domain获取结果
     *
     * @param domain 网站域名
     * @param size   每页数量
     * @param page   页码
     * @return
     */
    public ResultListBundle<SpiderInfo> getByDomain(String domain, int size, int page) {
        return bundleBuilder.listBundle(domain, () -> spiderInfoDAO.getByDomain(domain, size, page));
    }

    /**
     * 索引爬虫模板
     *
     * @param spiderInfo 爬虫模板
     * @return 如果爬虫模板索引成功则返回模板id, 否则返回null
     */
    public ResultBundle<String> index(SpiderInfo spiderInfo) {
        return bundleBuilder.bundle(spiderInfo.getDomain(), () -> StringUtils.isBlank(spiderInfo.getId()) ? spiderInfoDAO.index(spiderInfo) : spiderInfoDAO.update(spiderInfo));
    }

    /**
     * 根据网站domain删除数据
     *
     * @param domain 网站域名
     * @return 是否全部数据删除成功
     */
    public ResultBundle<Boolean> deleteByDomain(String domain) {
        return bundleBuilder.bundle(domain, () -> spiderInfoDAO.deleteByDomain(domain));
    }

    /**
     * 根据id删除网页模板
     *
     * @param id 网页模板id
     * @return 是否删除
     */
    public ResultBundle<Boolean> deleteById(String id) {
        return bundleBuilder.bundle(id, () -> spiderInfoDAO.deleteById(id));
    }

    /**
     * 根据爬虫模板id获取指定爬虫模板
     *
     * @param id 爬虫模板id
     * @return
     */
    public ResultBundle<SpiderInfo> getById(String id) {
        return bundleBuilder.bundle(id, () -> spiderInfoDAO.getById(id));
    }

    /**
     * 更新爬虫模板
     *
     * @param spiderInfo 爬虫模板实体
     * @return 爬虫模板id
     */
    public ResultBundle<String> update(SpiderInfo spiderInfo) {
        return bundleBuilder.bundle(spiderInfo.getId(), () -> spiderInfoDAO.update(spiderInfo));
    }
}
