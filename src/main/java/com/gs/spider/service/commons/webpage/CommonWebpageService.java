package com.gs.spider.service.commons.webpage;

import com.google.gson.Gson;
import com.gs.spider.dao.CommonWebpageDAO;
import com.gs.spider.gather.commons.CommonSpider;
import com.gs.spider.model.commons.SpiderInfo;
import com.gs.spider.model.commons.Webpage;
import com.gs.spider.model.utils.ResultBundle;
import com.gs.spider.model.utils.ResultBundleBuilder;
import com.gs.spider.model.utils.ResultListBundle;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CommonWebpageService
 *
 * @author Gao Shen
 * @version 16/4/19
 */
@Component
public class CommonWebpageService {
    private static final Gson gson = new Gson();
    private Logger LOG = LogManager.getLogger(CommonWebpageService.class);
    @Autowired
    private CommonWebpageDAO commonWebpageDAO;
    @Autowired
    private ResultBundleBuilder bundleBuilder;
    @Autowired
    private CommonSpider commonSpider;

    /**
     * 根据spiderUUID返回该spider抓取到的文章
     *
     * @param spiderUUID
     * @return
     */
    public ResultListBundle<Webpage> getWebpageListBySpiderUUID(String spiderUUID, int size, int page) {
        return bundleBuilder.listBundle(spiderUUID, () -> commonWebpageDAO.getWebpageBySpiderUUID(spiderUUID, size, page));
    }

    /**
     * 根据domain获取结果,按照抓取时间排序
     *
     * @param domain 网站域名
     * @param size   每页数量
     * @param page   页码
     * @return
     */
    public ResultListBundle<Webpage> getWebpageByDomain(String domain, int size, int page) {
        return bundleBuilder.listBundle(domain, () -> commonWebpageDAO.getWebpageByDomain(domain, size, page));
    }

    /**
     * 根据domain列表获取结果
     *
     * @param domain 网站域名列表
     * @param size   每页数量
     * @param page   页码
     * @return
     */
    public ResultListBundle<Webpage> getWebpageByDomains(Collection<String> domain, int size, int page) {
        return bundleBuilder.listBundle(domain.toString(), () -> commonWebpageDAO.getWebpageByDomains(domain, size, page));
    }

    /**
     * 根据关键词搜索网页
     *
     * @param query 关键词
     * @param size  每页数量
     * @param page  页码
     * @return
     */
    public ResultListBundle<Webpage> searchByQuery(String query, int size, int page) {
        return bundleBuilder.listBundle(query, () -> commonWebpageDAO.searchByQuery(query, size, page));
    }

    /**
     * 根据ES中的id获取网页
     *
     * @param id 网页id
     * @return
     */
    public ResultBundle<Webpage> getWebpageById(String id) {
        return bundleBuilder.bundle(id, () -> commonWebpageDAO.getWebpageById(id));
    }

    /**
     * 根据id删除网页
     *
     * @param id 网页id
     * @return 是否删除
     */
    public ResultBundle<Boolean> deleteById(String id) {
        return bundleBuilder.bundle(id, () -> commonWebpageDAO.deleteById(id));
    }

    /**
     * 获取所有网页,并按照抓取时间排序
     *
     * @param size 每页数量
     * @param page 页码
     * @return
     */
    public ResultListBundle<Webpage> listAll(int size, int page) {
        return bundleBuilder.listBundle(null, () -> commonWebpageDAO.listAll(size, page));
    }

    /**
     * 聚合所有网页的Domain信息
     *
     * @param size 大小
     * @return
     */
    public ResultBundle<Map<String, Long>> countDomain(int size) {
        return bundleBuilder.bundle(null, () -> commonWebpageDAO.countDomain(size));
    }

    /**
     * 聚合所有网页的Domain信息
     *
     * @return
     */
    public ResultBundle<Map<String, Long>> countWordByDomain(String domain) {
        return bundleBuilder.bundle(null, () -> commonWebpageDAO.countWordByDomain(domain));
    }

    /**
     * 根据网站的文章ID获取相似网站的文章
     *
     * @param id   文章ID
     * @param size 页面容量
     * @param page 页码
     * @return
     */
    public ResultListBundle<Webpage> moreLikeThis(String id, int size, int page) {
        return bundleBuilder.listBundle(id, () -> commonWebpageDAO.moreLikeThis(id, size, page));
    }

    /**
     * 统计指定网站每天抓取数量
     *
     * @param domain 网站域名
     * @return
     */
    public ResultBundle<Map<Date, Long>> countDomainByGatherTime(String domain) {
        return bundleBuilder.bundle(domain, () -> commonWebpageDAO.countDomainByGatherTime(domain));
    }

    /**
     * 根据网站domain删除数据
     *
     * @param domain 网站域名
     * @return 删除任务ID
     */
    public ResultBundle<String> deleteByDomain(String domain) {
        return bundleBuilder.bundle(domain, () -> commonSpider.deleteByDomain(domain));
    }

    /**
     * 开始滚动数据
     *
     * @return 滚动id
     */
    public ResultBundle<Pair<String, List<Webpage>>> startScroll() {
        return bundleBuilder.bundle(null, () -> commonWebpageDAO.startScroll());
    }

    /**
     * 根据scrollId获取全部数据
     *
     * @param scrollId scrollId
     * @return 网页列表
     */
    public ResultListBundle<Webpage> scrollAllWebpage(String scrollId) {
        return bundleBuilder.listBundle(scrollId, () -> commonWebpageDAO.scrollAllWebpage(scrollId));
    }

    /**
     * 根据spiderinfoID更新数据
     *
     * @param spiderInfoIdUpdateBy 待更新网站模板编号
     * @param callbackUrls         回调地址
     * @param spiderInfoJson       新的网页抽取模板JSON
     * @return 是否全部数据删除成功
     */
    public ResultBundle<String> updateBySpiderInfoID(String spiderInfoIdUpdateBy, String spiderInfoJson, List<String> callbackUrls) {
        SpiderInfo spiderInfo = gson.fromJson(spiderInfoJson, SpiderInfo.class);
        return bundleBuilder.bundle(spiderInfoIdUpdateBy, () -> commonSpider.updateBySpiderinfoID(spiderInfoIdUpdateBy, spiderInfo, callbackUrls));
    }

    /**
     * 获取query的关联信息
     *
     * @param query 查询queryString
     * @param size  结果集数量
     * @return 相关信息
     */
    public ResultBundle<Pair<Map<String, List<Terms.Bucket>>, List<Webpage>>> relatedInfo(String query, int size) {
        return bundleBuilder.bundle(query, () -> commonWebpageDAO.relatedInfo(query, size));
    }

    /**
     * 根据爬虫id导出 标题-正文 对
     *
     * @param uuid         爬虫id
     * @param outputStream 文件输出流
     */
    public void exportTitleContentPairBySpiderUUID(String uuid, OutputStream outputStream) {
        commonWebpageDAO.exportTitleContentPairBySpiderUUID(uuid, outputStream);
    }

    /**
     * 根据爬虫id导出 webpage的JSON对象
     *
     * @param uuid         爬虫id
     * @param includeRaw   是否包含网页快照
     * @param outputStream 文件输出流
     */
    public void exportWebpageJSONBySpiderUUID(String uuid, Boolean includeRaw, OutputStream outputStream) {
        commonWebpageDAO.exportWebpageJSONBySpiderUUID(uuid, includeRaw, outputStream);
    }

    /**
     * 根据域名导出 webpage的JSON对象
     *
     * @param domain       域名
     * @param includeRaw   是否包含网页快照
     * @param outputStream 文件输出流
     */
    public void exportWebpageJSONByDomain(String domain, Boolean includeRaw, OutputStream outputStream) {
        commonWebpageDAO.exportWebpageJSONByDomain(domain, includeRaw, outputStream);
    }

	/**
	 * 根据关键词和域名分页查找
	 * @param query
	 * @param domain
	 * @param size
	 * @param page
	 * @return
	 */
    public ResultBundle<Pair<List<Webpage>, Long>> getWebPageByKeywordAndDomain(String query, String domain, int size, int page) {
        return bundleBuilder.bundle(query, () -> commonWebpageDAO.getWebpageByKeywordAndDomain(query, domain, size, page));
    }
}
