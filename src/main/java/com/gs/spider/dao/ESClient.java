package com.gs.spider.dao;

import com.gs.spider.utils.StaticValue;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

/**
 * ESClient
 *
 * @author Gao Shen
 * @version 16/1/21
 */
@Component
@Scope("prototype")
public class ESClient {
    private final static String COMMON_INDEX_CONFIG = "commonIndex.json";
    private static final String COMMONS_INDEX_NAME = "commons";
    private static final String WEBPAGE_TYPE_NAME = "webpage";
    private static final String SPIDER_INFO_TYPE_NAME = "spiderinfo";
    private static final String SPIDER_INFO_INDEX_NAME = "spiderinfo";
    private Logger LOG = LogManager.getLogger(ESClient.class);
    private Client client;
    @Autowired
    private StaticValue staticValue;


    public boolean checkCommonsIndex() {
        return checkIndex(COMMONS_INDEX_NAME, COMMON_INDEX_CONFIG);
    }


    public boolean checkWebpageType() {
        return checkType(COMMONS_INDEX_NAME, WEBPAGE_TYPE_NAME, "webpage.json");
    }


    public boolean checkSpiderInfoIndex() {
        return checkIndex(SPIDER_INFO_INDEX_NAME, COMMON_INDEX_CONFIG);
    }

    public boolean checkSpiderInfoType() {
        return checkType(SPIDER_INFO_INDEX_NAME, SPIDER_INFO_TYPE_NAME, "spiderinfo.json");
    }

    public Client getClient() {
        if (!staticValue.isNeedEs()) {
            LOG.info("已在配置文件中声明不需要ES,如需要ES,请在配置文件中进行配置");
            return null;
        }
        if (client != null) return client;
        LOG.info("正在初始化ElasticSearch客户端," + staticValue.getEsHost());
        
        Settings settings = Settings.builder()
        		.put("cluster.name", staticValue.getEsClusterName()).build();
        try {
        	client = new PreBuiltTransportClient(settings)
        			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(staticValue.getEsHost()), staticValue.getEsPort()));
            final ClusterHealthResponse healthResponse = client.admin().cluster().prepareHealth()
                    .setTimeout(TimeValue.timeValueMinutes(1)).execute().actionGet();
            if (healthResponse.isTimedOut()) {
                LOG.error("ES客户端初始化失败");
            } else {
                LOG.info("ES客户端初始化成功");
            }
        } catch (IOException e) {
            LOG.fatal("构建ElasticSearch客户端失败!");
        }
        return client;
    }

    public boolean checkType(String index, String type, String mapping) {
        if (client == null) return false;
        if (!client.admin().indices().typesExists(new TypesExistsRequest(new String[]{index}, type)).actionGet().isExists()) {
            LOG.info(type + " type不存在,正在准备创建type");
            File mappingFile;
            try {
                mappingFile = new File(this.getClass().getClassLoader()
                        .getResource(mapping).getFile());
            } catch (Exception e) {
                LOG.fatal("查找ES mapping配置文件出错, " + e.getLocalizedMessage());
                return false;
            }
            LOG.debug(type + " MappingFile:" + mappingFile.getPath());
            PutMappingResponse mapPuttingResponse = null;

            PutMappingRequest putMappingRequest = null;
            try {
                putMappingRequest = Requests.putMappingRequest(index).type(type).source(FileUtils.readFileToString(mappingFile));
            } catch (IOException e) {
                LOG.error("创建 jvmSample mapping 失败," + e.getLocalizedMessage());
            }
            mapPuttingResponse = client.admin().indices().putMapping(putMappingRequest).actionGet();

            if (mapPuttingResponse.isAcknowledged()) LOG.info("创建" + type + "type成功");
            else {
                LOG.error("创建" + type + "type索引失败");
                return false;
            }
        } else LOG.debug(type + " type 存在");
        return true;
    }

    public boolean checkIndex(String index, String mapping) {
        if (client == null) return false;
        if (!client.admin().indices().exists(new IndicesExistsRequest(index)).actionGet().isExists()) {
            File indexMappingFile;
            try {
                indexMappingFile = new File(this.getClass().getClassLoader()
                        .getResource(mapping).getFile());
            } catch (Exception e) {
                LOG.fatal("查找" + index + "index mapping配置文件出错, " + e.getLocalizedMessage());
                return false;
            }
            LOG.debug(index + "index MappingFile:" + indexMappingFile.getPath());
            LOG.info(index + " index 不存在,正在准备创建index");
            CreateIndexResponse createIndexResponse = null;
            try {
                createIndexResponse = client.admin().indices()
                        .prepareCreate(index)
                        .setSettings(FileUtils.readFileToString(indexMappingFile))
                        .execute().actionGet();
            } catch (IOException e) {
                LOG.error("创建 " + index + " index 失败");
                return false;
            }
            if (createIndexResponse.isAcknowledged()) LOG.info(index + " index 成功");
            else {
                LOG.fatal(index + " index失败");
                return false;
            }
        } else LOG.debug(index + " index 存在");
        return true;
    }

    public StaticValue getStaticValue() {
        return staticValue;
    }

    public ESClient setStaticValue(StaticValue staticValue) {
        this.staticValue = staticValue;
        return this;
    }
}
