# Welcome to the Gather Platform

------

[Readme in Chinese](https://github.com/gsh199449/spider/tree/master/README.md)

[![Build Status](https://travis-ci.org/gsh199449/spider.svg?branch=master)](https://travis-ci.org/gsh199449/spider)

Gather Platform is a Web spider console based on [Webmagic](https://github.com/code4craft/webmagic). It's can edit a task configuration and search the data which is collected by the web spider.

> * Gather the data from web page accroding to the configuration
> * Do the nature language process on the web page data, like: extract keywords, extract summary, extract entity words
> * Auto detect the main content of the web page without any configuration of the spider
> * Dynamic field extraction from the web page
> * Manage the collected data, such as: search, delete and so on

## Windows/Mac/Linux all platform support

Dependencies:

 - JDK 8 above
 - Tomcat 8.3 above
 - Elasticsearch 5.0

## Deploy

The platform provide two ways to deploy, one is download the pre-complie package and the other is complie by yourself

### 1. Use the pre-complie package to gather web page data

 - Download re-complie package and dependencies from the [link](https://pan.baidu.com/s/1i4IoEhB) password: v3jm, for *nix users please download  `elasticsearch-5.0.0.zip` ,for windows users please download  `elasticsearch-5.0.0-win.zip` 
 - install JDK 8 ,[ORACLE](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
 - unzip the elasticsearch5.0.0.zip
 - Go into the `bin` directory, for *nix users run `elasticsearch` ,for windows users run  `elasticsearch.bat`
 - Use the browser open `http://localhost:9200`, if the web page is as fellow means the elasticsearch has been successfully installed

 ```json
    {
      "name" : "AQYRo1f",
      "cluster_name" : "elasticsearch",
      "cluster_uuid" : "0LJm-YogQ2qgLLznrlvWwQ",
      "version" : {
        "number" : "5.0.0",
        "build_hash" : "080bb47",
        "build_date" : "2016-11-11T22:08:49.812Z",
        "build_snapshot" : false,
        "lucene_version" : "6.2.1"
      },
      "tagline" : "You Know, for Search"
    }
 ```
 - unzip the `apache-tomcat-8.zip` ,put `spider.war` into `Tomcat/webapp` directory
 - Go into `tomcat/bin` directory,for *nix users run  `startup.sh` ,for windows users run  `startup.bat`
 - Use the browser open `http://localhost:8080/spider` to use the console of the Gather Platform

### 2. Build by yourself

 - Install JDK 8 , [ORACLE](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
 - Down and install Elasticsearch 5.0, [elastic.co](https://www.elastic.co/downloads/past-releases/elasticsearch-5-0-0)
 - Install the `ansj-elasticsearch` plugin, [github](https://github.com/NLPchina/elasticsearch-analysis-ansj)
 - Strat Elasticsearch
 - Install Tomcat 8, [Apache Tomcat](http://tomcat.apache.org)
 - Clone this project source code
 - Use `mvn package` to complie and package
 - Put `spider.war` into `Tomcat/webapp` directory
 - Start tomcat

## Usage

  After deploy open the browser , go `http://localhost:8080/spider` ,click `普通网页抓取` to show the menu.

  ![数据采集平台首页](https://raw.githubusercontent.com/gsh199449/spider/master/doc/imgs/home.png)

### Configure the spider

  Click the `编辑模板` button from the menu, in this page you can configure the spider

![编辑模板](https://raw.githubusercontent.com/gsh199449/spider/master/doc/imgs/spiderinfo.png)

  爬虫模板配置完成后,点击下面的 `采集样例数据` 按钮,稍等片刻即可在下方展示根据刚刚配置的模板抓取的数据,如果数据有误在上面的模板中进行修改,然后再次点击 `采集样例数据` 按钮即可重新抓取.

  ![采集样例数据](https://raw.githubusercontent.com/gsh199449/spider/master/doc/imgs/testSpiderinfo.png)

  注意,在对于爬虫模板没有完全的把握之前请勿选择爬虫模板下方的几个 `是否网页必须有XXX` 的配置项.以文章的标题为例,因为如果文章标题的配置项(即为title)配置有误,爬虫就无法抓取到网页的标题,如果这时再选中了 `是否网页必须有标题` 的话,就会导致爬虫无限制的进行抓取.

  ![needXXX](https://raw.githubusercontent.com/gsh199449/spider/master/doc/imgs/need.png)

  当模板配置完毕,即可点击下方的 `导出模板` 按钮,这时下方的大输入框中显示的Json格式的文字即为爬虫模板,可以将段文字保存到文本文件中,以便以后使用,也可以点击 `存储此模板` 对这个模板进行存储,以后可在本平台的 `爬虫模板管理系统` 中查找.

### 快速上手

  本平台在examples文件夹中给出了两个抓取腾讯新闻的示例,这两个一个是使用预定义的发布时间抓取规则,另外一个是使用系统自动探测文章的发布时间.
  以预定义的爬虫模板为例,打开[news.qq.com.json](https://github.com/gsh199449/spider/tree/master/examples/news.qq.com.json),将文件内容全部拷贝至爬虫模板编辑页面最下方的大输入框中,点击自动填充.这时爬虫配置文件中的爬虫模板信息就被自动填充进上面的表格了.然后点击抓取样例数据按钮,稍等片刻即可在当前页面下方看到通过这个模板抓取的新闻数据了.
  如果模板配置的有问题,导致长时间卡在获取数据页面,请转至爬虫监控页面,将刚刚提交的这个抓取任务停止即可.

### 爬虫监控

  在导航栏中点击查看进度就可以看到当前爬虫的运行状况,在这个界面中可以实现对爬虫的停止,删除,查看进度,查看已抓取的数据,查看模板等操作.

  ![spiderList](https://raw.githubusercontent.com/gsh199449/spider/master/doc/imgs/spiderList.png)

  注意,按照采集平台默认配置,这里的所有爬虫运行记录将在每两个小时对于已经完成的爬虫进行删除.如果不想让系统定时自动删除任何爬虫记录,或者改变删除记录的时间周期,请参阅高级配置中对于配置文件的解释部分.

### 数据管理与搜索

  点击导航栏下拉菜单中的搜索,即可看到目前Elasticsearch库中存储的所有网页数据,这些网页数据在默认情况下是按照抓取时间进行排序的,也就是说,在导航栏点击 `搜索` 之后,展示的第一条数据就是最新抓取的数据.

  ![search](https://raw.githubusercontent.com/gsh199449/spider/master/doc/imgs/search.png)

  在搜索页面是上方可以通过输入关键词对所有已抓取的网页数据进行搜索,也可以指定网站域名查看指定网站的所有数据.如果是指定关键词进行搜索则搜索结果是按照与输入的关键词的相关度进行排序的,如果是输入域名,查看某一网站的所有数据则按照抓取时间进行排序,最新抓取的数据在最上方.

  点击导航栏中的 `网站列表` 按钮即可查看目前已经抓取的数据中都是那些网站的信息,对于每个网站都可以点击 `查看数据列表` 按钮查看该网站的所有数据.点击 `删除网站数据` 即可删除该网站下的所有数据.

  ![domainList](https://raw.githubusercontent.com/gsh199449/spider/master/doc/imgs/domainList.png)


### 高级使用

#### 动态字段和静态字段

  在配置网页模板时,有一个 `添加动态字段` 按钮,这个功能是为了抓取那些不在预设字段里的其他字段所设计.举例来说,目前爬虫模板可以配置的预设字段有:标题,正文,发布时间等等.如果我们想抓取文章的作者或者文章的发布文号,这时就需要使用动态字段来实现.

  ![动态字段](https://raw.githubusercontent.com/gsh199449/spider/master/doc/imgs/dynamic.png)

  点击 `添加动态字段` 按钮,在弹出的输入框中输入要抓取的字段名称,我们以要抓取文章的作者为例,在框中输入author. 注意这个动态字段的名称必须使用英文名称.之后再模板编辑页面就多出来的两个输入框,一个是author Reg,一个是author XPath,其中一个是配置作者字段的正则表达式,另一个是配置作者字段的XPath表达式,这两个选其一即可.

  静态字段使用方法与动态字段类似,但是与动态字段不同的是,静态字段相对于爬虫模板来说是静态的.也就是说这个值在配置模板的阶段就是预设好的,通过这个模板抓取的所有数据里面都会带有这个字段和预设的这个值.这个功能主要是方便二次开发人员在数据存储于搜索时的使用.

#### 使用Lucene Query进行数据查询

在数据查询页面进行数据查询时,在关键词输入框中输入的检索词默认是在文章正文中进行检索.如果在这个框中输入 `title: 中国` 的含义是在所以文章的标题中检索带有中国的网页.支持的字段名称有(括号前为字段名称,括号内为字段的含义):

 - content(正文)
 - title(标题)
 - url(网页链接)
 - domain(网页域名)
 - spiderUUID(爬虫id)
 - keywords(文章关键词)
 - summary(文章摘要)
 - publishTime(文章发布时间)
 - category(文章类别)
 - dynamic_fields(动态字段)

#### 同一网站不同模板的情况

  针对同一网站可以有不同的抽取模板的问题,可以通过配置另外的模板进行解决.

### 高级配置

  项目的配置文件在spider.war/WEB-INF文件夹下

#### 输出网页数据至Redis Channel

  找到 `mvc-dispatcher-servlet.xml` 配置文件,增加redis数据输出管道:

  ```xml
    <property name="pipelineList">
        <list>
            <!--Redis输出-->
            <ref bean="commonWebpageRedisPipeline"/>
            <ref bean="commonWebpagePipeline"/>
        </list>
    </property>
  ```

  在 `staticvalue.json` 配置文件中,修改如下内容:

  ```
  "needRedis": false,
  "redisPort": 6379,
  "redisHost": "localhost",
  "webpageRedisPublishChannelName": "webpage",
  ```

 - needRedis设置为true将在系统启动时检查redis配置
 - redisPort redis的端口
 - redisHost redis的服务器地址
 - webpageRedisPublishChannelName redis发布不通道的名称

#### 输出网页数据至任意数据源

  写一个类实现 `Pipeline` 接口,然后在 `mvc-dispatcher-servlet.xml` 配置文件中配置这个数据处理类, 爬虫框架会把每一个采集并处理好的的网页传入 `process` 方法,然后通过自己的代码将这些网页数据存储至你想要的位置即可,可以参考本平台实现的[Redis pipeline](https://github.com/gsh199449/spider/blob/master/src/main/java/com/gs/spider/dao/CommonWebpageRedisPipeline.java).本平台默认的ES输出[CommonWebpagePipeline](https://github.com/gsh199449/spider/blob/master/src/main/java/com/gs/spider/dao/CommonWebpagePipeline.java)中有一个 `convertResultItems2Webpage` 便利方法,可以将Webmagic框架的 `ResultItems` 对象转换为一个 `Webpage` 对象方便处理.

#### 配置文件解释

  本系统配置文件名称为 `staticvalue.json` :

  ```json
    {
      "esHost": "localhost",
      "esClusterName": "elasticsearch",
      "commonsIndex": "commons",
      "maxHttpDownloadLength": 1048576,
      "commonsSpiderDebug": false,
      "taskDeleteDelay": 1,
      "taskDeletePeriod": 2,
      "limitOfCommonWebpageDownloadQueue": 100000,
      "needRedis": false,
      "redisPort": 6379,
      "redisHost": "localhost",
      "webpageRedisPublishChannelName": "webpage",
      "commonsWebpageCrawlRatio": 2
    }
  ```

 - esHost:es服务器地址
 - esClusterName:es集群名称
 - commonsIndex:网页数据在es中存储的index名称
 - maxHttpDownloadLength: 如果网页超过这个大小则不再下载这个网页
 - commonsSpiderDebug:置为true则在爬虫管理系统的爬虫运行日志中可以看到更多的错误信息
 - taskDeleteDelay: 自动删除任务记录延时几小时启动
 - taskDeletePeriod: 每几小时删除一次已完成的任务记录
 - limitOfCommonWebpageDownloadQueue: 最大网页下载队列长度
 - commonsWebpageCrawlRatio: 如果抓取的网页超过需要网页commonsWebpageCrawlRatio倍,爬虫退出

### 二次开发接口

## 联系我

邮箱: 63388@qq.com
