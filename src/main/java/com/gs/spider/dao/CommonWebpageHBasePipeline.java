package com.gs.spider.dao;

import com.gs.spider.model.commons.Webpage;
import com.gs.spider.utils.StaticValue;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.IOException;

/**
 * Created by gsh199449 on 2016/10/24.
 */
@Component
public class CommonWebpageHBasePipeline implements Pipeline {
    private static Connection connection;
    private final String hbaseHost;
    private final boolean needHBase;
    private final String tableName = "webpage";
    private final String[] familiesName = new String[]{
            "content",
            "title",
            "domain",
            "category",
            "rawHTML",
            "spiderInfoId",
            "spiderUUID",
            "url",
            "gatherTime",
            "keywords",
            "publishTime",
            "summary"
    };
    private Logger LOG = LogManager.getLogger(CommonWebpageHBasePipeline.class);

    @Autowired
    public CommonWebpageHBasePipeline(StaticValue staticValue) {
        this.needHBase = staticValue.isNeedHBase();
        this.hbaseHost = staticValue.getHbaseHost();
        if (this.needHBase) {
            LOG.info("正在初始化HBase客户端,Host:{}", staticValue.getHbaseHost());
            try {
                Configuration conf = HBaseConfiguration.create();
                conf.set("hbase.zookeeper.quorum", this.hbaseHost);
                connection = ConnectionFactory.createConnection(conf);

                HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
                for (String familyName : familiesName) {
                    tableDescriptor.addFamily(new HColumnDescriptor(familyName).setCompressionType(Compression.Algorithm.NONE));
                }

                Admin admin = connection.getAdmin();
                if (admin.tableExists(TableName.valueOf(tableName))) {
                    System.out.println("table Exists!");
                } else {
                    admin.createTable(tableDescriptor);
                    System.out.println("create table Success!");
                }
            } catch (IOException e) {
                LOG.error("初始化HBase链接失败,{}", e.getLocalizedMessage());
            }
        } else {
            LOG.warn("未初始化HBase客户端");
        }
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        if (!needHBase) return;
        Webpage webpage = CommonWebpagePipeline.convertResultItems2Webpage(resultItems);
        Put put = new Put(Bytes.toBytes(webpage.getId()));
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            put.addColumn("content".getBytes(), null, webpage.getContent().getBytes());
            put.addColumn("title".getBytes(), null, webpage.getTitle().getBytes());
            put.addColumn("domain".getBytes(), null, webpage.getDomain().getBytes());
            put.addColumn("category".getBytes(), null, webpage.getCategory() == null ? "".getBytes() : webpage.getCategory().getBytes());
            put.addColumn("rawHTML".getBytes(), null, webpage.getRawHTML().getBytes());
            put.addColumn("spiderInfoId".getBytes(), null, webpage.getSpiderInfoId() == null ? "".getBytes() : webpage.getSpiderInfoId().getBytes());
            put.addColumn("spiderUUID".getBytes(), null, webpage.getSpiderUUID().getBytes());
            put.addColumn("url".getBytes(), null, webpage.getUrl().getBytes());
            put.addColumn("gatherTime".getBytes(), null, webpage.getGathertime().toString().getBytes());
            put.addColumn("keywords".getBytes(), null, webpage.getKeywords().toString().getBytes());
            put.addColumn("publishTime".getBytes(), null, webpage.getPublishTime() == null ? "".getBytes() : webpage.getPublishTime().toString().getBytes());
            put.addColumn("summary".getBytes(), null, webpage.getSummary().toString().getBytes());
            table.put(put);
            LOG.trace("add data Success!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
