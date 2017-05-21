<%--
  Created by IntelliJ IDEA.
  User: gaoshen
  Date: 16/6/30
  Time: 上午10:11
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>爬虫模板测试系统</title>
    <%@include file="../../commons/header.jsp" %>
    <%@include file="../../commons/allScript.jsp" %>
    <script>
        function jsonStringify(data, space) {
            var seen = [];
            return JSON.stringify(data, function (key, val) {
                if (!val || typeof val !== 'object') {
                    return val;
                }
                if (seen.indexOf(val) !== -1) {
                    return '[Circular]';
                }
                seen.push(val);
                return val;
            }, space);
        }
        function formToJson(form) {
            var result = {};
            var fieldArray = $("#" + form).serializeArray();
            for (var i = 0; i < fieldArray.length; i++) {
                var field = fieldArray[i];
                if (field.name in result) {
                    result[field.name] += ',' + field.value;
                } else {
                    result[field.name] = field.value;
                }
            }
            result['gatherFirstPage'] = $("#gatherFirstPage").prop('checked');
            result['doNLP'] = $("#doNLP").prop('checked');
            result['needTitle'] = $("#needTitle").prop('checked');
            result['needContent'] = $("#needContent").prop('checked');
            result['needPublishTime'] = $("#needPublishTime").prop('checked');
            result['startURL'] = eval(result['startURL']);
            result['callbackURL'] = eval(result['callbackURL']);
            result['saveCapture'] = $("#saveCapture").prop('checked');
            result['autoDetectPublishDate'] = $("#autoDetectPublishDate").prop('checked');
            result['ajaxSite'] = $("#ajaxSite").prop('checked');
            var dynamicFields = [];
            var fieldConfigList = $('.dynamicField');
            for (i = 0; i < fieldConfigList.length; i++) {
                var fieldDom = $('.dynamicField:eq(' + i + ')');
                var fieldId = fieldDom.attr('id');
                var fieldName = fieldDom.attr('name');
                var fieldConfig = {'regex': '', 'xpath': '', 'name': '', 'need': false};
                fieldConfig['name'] = fieldName;
                fieldConfig['regex'] = $('#' + fieldName + 'Reg').val();
                fieldConfig['xpath'] = $('#' + fieldName + 'XPath').val();
                fieldConfig['need'] = $('#need' + fieldName).prop('checked');
                dynamicFields.push(fieldConfig);
            }
            result['dynamicFields'] = dynamicFields;
            var staticFields = [];
            var staticFiledDomList = $('.staticField');
            for (i = 0; i < staticFiledDomList.length; i++) {
                var staticFieldDom = $('.staticField:eq(' + i + ')');
                var staticFieldName = staticFieldDom.attr('name');
                var staticFieldConfig = {'name': '', 'value': ''};
                staticFieldConfig['name'] = staticFieldName;
                staticFieldConfig['value'] = $('#static-' + staticFieldName + '-value').val();
                staticFields.push(staticFieldConfig);
            }
            result['staticFields'] = staticFields;
            return result;
        }
        function downloadFile(fileName, content) {
            var aLink = document.createElement('a');
            var blob = new Blob([content]);
            var evt = document.createEvent("HTMLEvents");
            evt.initEvent("click", false, false);//initEvent 不加后两个参数在FF下会报错, 感谢 Barret Lee 的反馈
            aLink.download = fileName;
            aLink.href = URL.createObjectURL(blob);
            aLink.dispatchEvent(evt);
        }
        function exportJson() {
            var result = formToJson("spiderInfoForm");
            var jsonResult = jsonStringify(result, 4);
            downloadFile(result['domain'] + '.json', jsonResult);
            $("#jsonSpiderInfo").html(jsonResult);
        }
        function getRandom(min, max) {
            var range = max - min;
            var rand = Math.random();
            return min + Math.round(rand * range);
        }
        /**
         * 检测content是否是英文文本
         * @param content 文章正文
         * @returns {boolean}
         */
        function isEnglishWebpage(content) {
            var i, engChar = 0, max_sampling = 15, threshold = 0.5;
            if (content.length <= 0) return false;
            for (i = 0; i <= max_sampling; i++) {
                var char = content.charAt(getRandom(0, content.length));
                if ((char >= 'a' && char <= 'z') || (char >= 'A' && char <= 'Z')) {
                    engChar++;
                }
            }
            return engChar / max_sampling > threshold;
        }
        /**
         * 添加动态抽取字段
         */
        function addDynamicField() {
            var fieldName, count, need, reg, xpath;
            inputModal("字段名称(必须为英文)", function (data) {
                count = $('.dynamicField').length + 1;
                fieldName = data;
                $('#dynamicFields').append('<div id="dynamicField' + count + '" class="dynamicField" name=' + fieldName + '>\
                            <button class="btn btn-danger" type="button" onclick="$(\'#dynamicField' + count + '\').remove();">删除动态字段' + fieldName + '</button>\
                    <div class="form-group">\
                    <label for="' + fieldName + 'Reg"><span class="label label-warning">动态</span>' + fieldName + '正则</label>\
                    <input type="text" class="form-control reg" id="' + fieldName + 'Reg" name="' + fieldName + 'Reg"\
            placeholder="' + fieldName + '正则">\
                    </div>\
                    <div class="form-group">\
                    <label for="' + fieldName + 'XPath"><span class="label label-warning">动态</span>' + fieldName + 'XPath</label>\
                    <input type="text" class="form-control xpath" id="' + fieldName + 'XPath"\
            name="' + fieldName + 'XPath"\
            placeholder="' + fieldName + 'XPath">\
                    </div>\
                    <div class="checkbox">\
                    <label>\
                    <input type="checkbox" name="need' + fieldName + '" id="need' + fieldName + '">\
                    是否网页必须有' + fieldName + '\
                    </label>\
                    </div>\
                    </div>');
                $('#confirmModal').modal('hide');
            });

        }
        function addStaticField() {
            var fieldName;
            inputModal("字段名称(必须为英文)", function (data) {
                count = $('.staticField').length + 1;
                fieldName = data;
                $('#staticFields').append('<div id="staticField' + count + '" class="staticField" name="' + fieldName + '">\
                                    <button class="btn btn-danger" type="button"\
                            onclick="$(\'#staticField' + count + '\').remove();">删除静态字段' + fieldName + '</button>\
                                    <div class="form-group">\
                                    <label for="static-' + fieldName + '-value"><span class="label label-warning">静态</span>' + fieldName + ' Value</label>\
                                    <input type="text" class="form-control" id="static-' + fieldName + '-value"\
                            name="' + fieldName + '-value"\
                            placeholder="' + fieldName + 'value">\
                                    </div>\
                                    </div>');
                $('#confirmModal').modal('hide');
            });

        }
        var dynamicFieldList = {};
        function showDynamicFields(id) {
            var fields = dynamicFieldList[id];
            tableModal(fields, id + "动态字段");
        }
        $(function () {
            var validate = $("#spiderInfoForm").validate({
                submitHandler: function (form) {   //表单提交句柄,为一回调函数，带一个参数：form
                    var result = formToJson("spiderInfoForm");
                    console.log(result);
                    $("#confirmModalTitle").text("正在抓取");
                    $("#confirmModalBody").html("正在抓取,请稍后");
                    $('#confirmModal').modal({
                        keyboard: false,
                        backdrop: "static"
                    });
                    $.getJSON('${pageContext.request.contextPath}/commons/spider/testSpiderInfo', {spiderInfoJson: JSON.stringify(result)}, function (data) {
                        $("#webpageTableBody").html("");
                        var modalBody = $("#confirmModalBody");
                        var modalTitle = $("#confirmModalTitle");
                        if (data.count <= 0) {
                            modalTitle.text("错误!");
                            modalBody.html("无法采集到数据");
                            return;
                        }
                        modalTitle.text("成功!");
                        modalBody.html("已抓取数据,正在渲染,请稍后");
                        var webpageTable = $("#webpageTable");
                        webpageTable.show("slow");
                        $('#taskId').html(data.resultList[0].spiderUUID);
                        $('#openTaskDetail').attr('href', '${pageContext.request.contextPath}/commons/spider/getTaskById?taskId=' + data.resultList[0].spiderUUID);
                        $('#taskIdDiv').show();
                        var engContentCount = 0, threshold = 0.8;
                        var tempContent = "";
                        $.each(data.resultList, function (i, item) {
                            if (isEnglishWebpage(item.content)) {
                                engContentCount++;
                            }
                            if(item.content.length > 50){
                                tempContent = item.content.substring(0, 51) + "····[省略" + (item.content.length - 50) + "字]";
                            }
                            dynamicFieldList[i] = item.dynamicFields;
                            $('<tr style="word-break:break-all; word-wrap:break-word;">' +
                                    '<th scope="row">' + i + '</th>' +
                                    '<td>' + item.title + '</td>' +
                                    '<td>' + item.summary + '</td>' +
                                    '<td>' + tempContent + '</td>' +
                                    '<td>' + item.publishTime + '</td>' +
                                    '<td>' + item.keywords + '</td>' +
                                    '<td>' + item.category + '</td>' +
                                    '<td><a class="btn btn-info" href="' + item.url + '">Go</a></td>' +
                                    '<td><button class="btn btn-info" onclick="showDynamicFields(' + i + ')">动态字段</button></td>' +
                                    '</tr>')
                                    .appendTo("#webpageTableBody");
                        });
                        if (engContentCount / data.count >= threshold) {
                            $('<span class="label label-pill label-warning">可能是西文网页,请勿使用自动抽取正文</span>').appendTo('#taskId');
                        }
                        setTimeout("$('#confirmModal').modal('hide')", 2000);
                    });
                },
                rules: {
                    thread: {
                        required: true,
                        digits: true,
                        min: 1
                    },
                    retry: {
                        required: true,
                        digits: true,
                        max: 5
                    },
                    sleep: {
                        required: true,
                        digits: true
                    },
                    maxPageGather: {
                        required: true,
                        digits: true,
                        maxGather: 0
                    },
                    timeout: {
                        required: true,
                        digits: true,
                        min: 1000
                    },
                    siteName: {
                        required: true
                    },
                    domain: {
                        required: true,
                        domain: 0
                    },
                    startURL: {
                        required: true,
                        json: 0
                    },
                    callbackURL: {
                        required: false,
                        json: 0
                    }
                },
                highlight: function (element) {
                    $(element).closest('.form-group').addClass('has-error');
                },
                success: function (label) {
                    label.closest('.form-group').removeClass('has-error');
                    label.remove();
                },
                errorPlacement: function (error, element) {
                    element.parent('div').append(error);
                }
            });

        });
        $.validator.addMethod("maxGather", function (value, element, params) {
            return !(value < 0 && !$("#gatherFirstPage").prop("checked"));
        }, "最大抓取页数与只抓取首页必选其一");
        $.validator.addMethod("domain", function (value, element, params) {
            return value.indexOf('/') == -1;
        }, "域名不可包含/");
        $.validator.addMethod("json", function (value, element, params) {
            var urlList;
            var validate = true;
            try {
                urlList = eval(value);
                if (urlList.length < 0) return false;
                $.each(urlList, function (i, item) {
                    if (item.substring(0, 4) != "http") {
                        validate = false;
                    }
                });
            } catch (e) {
                return false;
            }
            return validate;
        }, "url列表必须有至少一个url,必须为json格式,且每个链接必须使用http或者https开头");
        function submitTask() {
            rpcAndShowData("${pageContext.request.contextPath}/commons/spider/start", {spiderInfoJson: JSON.stringify(formToJson("spiderInfoForm"))});
        }
        function save() {
            rpcAndShowData("${pageContext.request.contextPath}/commons/spiderinfo/save", {spiderInfoJson: JSON.stringify(formToJson("spiderInfoForm"))});
        }
        function onDomainBlur() {
            var domain = $('#domain').val();
            $('#startURL').val('[\'http://' + domain + '/\']');
            $.getJSON('${pageContext.request.contextPath}/commons/spiderinfo/getByDomain', {domain: domain}, function (data) {
                if (data.count > 0) {
                    showModal("请确认", "服务器中已包含此网站的模板,确定要继续配置吗?",
                            function () {//cancel
                                var result = formToJson("spiderInfoForm");
                                localStorage["spiderInfo"] = jsonStringify(result, 0);
                                window.location.href = "${pageContext.request.contextPath}/panel/commons/listSpiderInfo?domain=" + domain;
                            },
                            function () {//confirm
                                $('#confirmModal').modal('hide');
                            });
                }
            })
        }
        function showAdvancedSetting() {
            $('#advancedSetting').show("normal");
            $('#showAdvancedSettingBtn').hide();
        }
    </script>
</head>
<body>
<%@include file="../../commons/head.jsp" %>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <form id="spiderInfoForm" action="${pageContext.request.contextPath}/commons/spider/testSpiderInfo">
                <div class="form-group">
                    <label for="siteName" >siteName</label>
                    <input type="text" class="form-control" id="siteName" name="siteName" placeholder="网站名称"
                           value="${spiderInfo.siteName}">
                </div>
                <div class="form-group">
                    <label for="domain">domain</label>
                    <input type="text" class="form-control" id="domain" name="domain" placeholder="域名"
                           value="${spiderInfo.domain}"
                           onblur="onDomainBlur()">
                </div>
                <div class="form-group">
                    <label for="startURL">startURL(多个起始地址请使用json格式书写,例如['http://news.qq.com','http://news.qq.com/dfs/dfs.fg'])</label>
                    <input type="text" class="form-control" id="startURL" name="startURL" placeholder="起始链接">
                </div>
                <div id="advancedSetting" style="display: none;">
                    <div class="form-group">
                        <label for="id">id</label>
                        <input type="text" class="form-control" id="id" name="id" placeholder="模板ID,系统自动填充,请勿手工赋值"
                               value="${spiderInfo.id}">
                    </div>
                    <div class="form-group">
                        <label for="thread">thread</label>
                        <input type="number" class="form-control" id="thread" name="thread" placeholder="抓取线程数"
                               value="${spiderInfo.thread}">
                    </div>
                    <div class="form-group">
                        <label for="retry">retry</label>
                        <input type="number" class="form-control" id="retry" name="retry" placeholder="失败的网页重试次数"
                               value="${spiderInfo.retry}">
                    </div>
                    <div class="form-group">
                        <label for="sleep">sleep</label>
                        <input type="number" class="form-control" id="sleep" name="sleep" placeholder="抓取每个网页睡眠时间"
                               value="${spiderInfo.sleep}">
                    </div>
                    <div class="form-group">
                        <label for="maxPageGather">maxPageGather(请在导出模板前将最大抓取数量设置为生产环境中正确的值)</label>
                        <input type="number" class="form-control" id="maxPageGather" name="maxPageGather"
                               placeholder="最大抓取网页数量,0代表不限制" value="${spiderInfo.maxPageGather}">
                    </div>
                    <div class="form-group">
                        <label for="timeout">timeout</label>
                        <input type="number" class="form-control" id="timeout" name="timeout" placeholder="HTTP链接超时时间"
                               value="${spiderInfo.timeout}">
                    </div>
                    <div class="form-group">
                        <label for="contentReg">contentReg</label>
                        <input type="text" class="form-control" id="contentReg" name="contentReg" placeholder="正文正则表达式"
                               value="${spiderInfo.contentReg}">
                    </div>
                    <div class="form-group">
                        <label for="contentXPath">contentXPath</label>
                        <input type="text" class="form-control" id="contentXPath" name="contentXPath"
                               placeholder="正文Xpath"
                               value="${spiderInfo.contentXPath}">
                    </div>
                    <div class="form-group">
                        <label for="titleReg">titleReg</label>
                        <input type="text" class="form-control" id="titleReg" name="titleReg" placeholder="标题正则"
                               value="${spiderInfo.titleReg}">
                    </div>
                    <div class="form-group">
                        <label for="titleXPath">titleXPath</label>
                        <input type="text" class="form-control" id="titleXPath" name="titleXPath" placeholder="标题xpath"
                               value="${spiderInfo.titleXPath}">
                    </div>
                    <div class="form-group">
                        <label for="categoryReg">categoryReg</label>
                        <input type="text" class="form-control" id="categoryReg" name="categoryReg" placeholder="分类信息正则"
                               value="${spiderInfo.categoryReg}">
                    </div>
                    <div class="form-group">
                        <label for="categoryXPath">categoryXPath</label>
                        <input type="text" class="form-control" id="categoryXPath" name="categoryXPath"
                               placeholder="分类信息XPath" value="${spiderInfo.categoryXPath}">
                    </div>
                    <div class="form-group">
                        <label for="defaultCategory">defaultCategory</label>
                        <input type="text" class="form-control" id="defaultCategory" name="defaultCategory"
                               placeholder="默认分类" value="${spiderInfo.defaultCategory}">
                    </div>
                    <div class="form-group">
                        <label for="urlReg">urlReg</label>
                        <input type="text" class="form-control" id="urlReg" name="urlReg" placeholder="url正则"
                               value="${spiderInfo.urlReg}">
                    </div>
                    <div class="form-group">
                        <label for="charset">charset</label>
                        <input type="text" class="form-control" id="charset" name="charset" placeholder="编码"
                               value="${spiderInfo.charset}">
                    </div>
                    <div class="form-group">
                        <label for="publishTimeXPath">publishTimeXPath</label>
                        <input type="text" class="form-control" id="publishTimeXPath" name="publishTimeXPath"
                               placeholder="发布时间xpath" value="${spiderInfo.publishTimeXPath}">
                    </div>
                    <div class="form-group">
                        <label for="publishTimeReg">publishTimeReg</label>
                        <input type="text" class="form-control" id="publishTimeReg" name="publishTimeReg"
                               placeholder="发布时间正则" value="${spiderInfo.publishTimeReg}">
                    </div>
                    <div class="form-group">
                        <label for="publishTimeFormat">publishTimeFormat</label>
                        <input type="text" class="form-control" id="publishTimeFormat" name="publishTimeFormat"
                               placeholder="发布时间模板" value="${spiderInfo.publishTimeFormat}">
                    </div>
                    <%--动态字段--%>
                    <div class="form-group" id="dynamicFields">
                        <button type="button" onclick="addDynamicField()" class="btn btn-info">添加动态字段
                        </button>
                        <c:forEach items="${spiderInfo.dynamicFields}" var="field" varStatus="index">
                            <div id="dynamicField${index.count}" class="dynamicField" name="${field.name}">
                                <button class="btn btn-danger" type="button"
                                        onclick="$('#dynamicField${index.count}').remove();">
                                    删除动态字段${field.name}</button>
                                <div class="form-group">
                                    <label for="${field.name}Reg"><span
                                            class="label label-warning">动态</span>${field.name}正则</label>
                                    <input type="text" class="form-control" id="${field.name}Reg"
                                           name="${field.name}Reg"
                                           placeholder="${field.name}正则" value="${field.regex}">
                                </div>
                                <div class="form-group">
                                    <label for="${field.name}XPath"><span
                                            class="label label-warning">动态</span>${field.name}XPath</label>
                                    <input type="text" class="form-control" id="${field.name}XPath"
                                           name="${field.name}XPath"
                                           placeholder="${field.name}XPath" value="${field.xpath}">
                                </div>
                                <div class="checkbox">
                                    <label>
                                        <c:if test="${field.need}">
                                            <input type="checkbox" name="need${field.name}" id="need${field.name}"
                                                   checked="checked">
                                        </c:if>
                                        <c:if test="${!field.need}">
                                            <input type="checkbox" name="need${field.name}" id="need${field.name}">
                                        </c:if> 是否网页必须有${field.name}
                                    </label>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <%--静态字段--%>
                    <div class="form-group" id="staticFields">
                        <button type="button" onclick="addStaticField()" class="btn btn-info">添加静态字段
                        </button>
                        <c:forEach items="${spiderInfo.staticFields}" var="field" varStatus="index">
                            <div id="staticField${index.count}" class="staticField" name="${field.name}">
                                <button class="btn btn-danger" type="button"
                                        onclick="$('#staticField${index.count}').remove();">删除静态字段${field.name}</button>
                                <div class="form-group">
                                    <label for="static-${field.name}-value"><span
                                            class="label label-warning">静态</span>${field.name} Value</label>
                                    <input type="text" class="form-control" id="static-${field.name}-value"
                                           name="${field.name}-value"
                                           placeholder="${field.name}value" value="${field.value}">
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="form-group">
                        <label for="lang">lang</label>
                        <input type="text" class="form-control" id="lang" name="lang"
                               placeholder="发布时间模板语言" value="${spiderInfo.lang}">
                    </div>
                    <div class="form-group">
                        <label for="country">country</label>
                        <input type="text" class="form-control" id="country" name="country"
                               placeholder="发布时间模板国家" value="${spiderInfo.country}">
                    </div>
                    <div class="form-group">
                        <label for="callbackURL">callbackURL(多个回调地址请使用json格式书写,例如['http://news.qq.com','http://news.qq.com/dfs/dfs.fg'])</label>
                        <input type="text" class="form-control" id="callbackURL" name="callbackURL" placeholder="回调url"
                               value="${spiderInfo.callbackURL}">
                    </div>
                    <div class="form-group">
                        <label for="userAgent">userAgent</label>
                        <input type="text" class="form-control" id="userAgent" name="userAgent" placeholder="userAgent"
                               value="${spiderInfo.userAgent}">
                    </div>
                    <div class="form-group">
                        <label for="proxyHost">proxyHost</label>
                        <input type="text" class="form-control" id="proxyHost" name="proxyHost"
                               placeholder="proxyHost"
                               value="${spiderInfo.proxyHost}">
                    </div>
                    <div class="form-group">
                        <label for="proxyPort">proxyPort</label>
                        <input type="number" class="form-control" id="proxyPort" name="proxyPort"
                               placeholder="proxyPort"
                               value="${spiderInfo.proxyPort}">
                    </div>
                    <div class="form-group">
                        <label for="proxyUsername">proxyUsername</label>
                        <input type="text" class="form-control" id="proxyUsername" name="proxyUsername"
                               placeholder="proxyUsername"
                               value="${spiderInfo.proxyUsername}">
                    </div>
                    <div class="form-group">
                        <label for="proxyPassword">proxyPassword</label>
                        <input type="text" class="form-control" id="proxyPassword" name="proxyPassword"
                               placeholder="proxyPassword"
                               value="${spiderInfo.proxyPassword}">
                    </div>
                    <div class="form-group">
                        <div class="checkbox">
                            <label>
                                <c:if test="${spiderInfo.doNLP}">
                                    <input type="checkbox" name="doNLP" id="doNLP" checked="checked">
                                </c:if>
                                <c:if test="${!spiderInfo.doNLP}">
                                    <input type="checkbox" name="doNLP" id="doNLP">
                                </c:if>是否进行nlp处理(包括摘要,关键词,命名实体抽取)
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="checkbox">
                            <label>
                                <c:if test="${spiderInfo.gatherFirstPage}">
                                    <input type="checkbox" name="gatherFirstPage" id="gatherFirstPage"
                                           checked="checked">
                                </c:if>
                                <c:if test="${!spiderInfo.gatherFirstPage}">
                                    <input type="checkbox" name="gatherFirstPage" id="gatherFirstPage">
                                </c:if> 是否只抓取首页
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="checkbox">
                            <label>
                                <c:if test="${spiderInfo.needTitle}">
                                    <input type="checkbox" name="needTitle" id="needTitle" checked="checked">
                                </c:if>
                                <c:if test="${!spiderInfo.needTitle}">
                                    <input type="checkbox" name="needTitle" id="needTitle">
                                </c:if> 是否网页必须有标题
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="checkbox">
                            <label>
                                <c:if test="${spiderInfo.needContent}">
                                    <input type="checkbox" name="needContent" id="needContent" checked="checked">
                                </c:if>
                                <c:if test="${!spiderInfo.needContent}">
                                    <input type="checkbox" name="needContent" id="needContent">
                                </c:if> 是否网页必须有正文
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="checkbox">
                            <label>
                                <c:if test="${spiderInfo.needPublishTime}">
                                    <input type="checkbox" name="needPublishTime" id="needPublishTime"
                                           checked="checked">
                                </c:if>
                                <c:if test="${!spiderInfo.needPublishTime}">
                                    <input type="checkbox" name="needPublishTime" id="needPublishTime">
                                </c:if> 是否网页必须有发布时间
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="checkbox">
                            <label>
                                <c:if test="${spiderInfo.saveCapture}">
                                    <input type="checkbox" name="saveCapture" id="saveCapture" checked="checked">
                                </c:if>
                                <c:if test="${!spiderInfo.saveCapture}">
                                    <input type="checkbox" name="saveCapture" id="saveCapture">
                                </c:if> 是否保存网页快照,默认保存
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="checkbox">
                            <label>
                                <c:if test="${spiderInfo.ajaxSite}">
                                    <input type="checkbox" name="ajaxSite" id="ajaxSite" checked="checked">
                                </c:if>
                                <c:if test="${!spiderInfo.ajaxSite}">
                                    <input type="checkbox" name="ajaxSite" id="ajaxSite">
                                </c:if> 是否是ajax网页
                            </label>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="checkbox">
                        <label>
                            <c:if test="${spiderInfo.autoDetectPublishDate}">
                                <input type="checkbox" name="autoDetectPublishDate" id="autoDetectPublishDate"
                                       checked="checked">
                            </c:if>
                            <c:if test="${!spiderInfo.autoDetectPublishDate}">
                                <input type="checkbox" name="autoDetectPublishDate" id="autoDetectPublishDate">
                            </c:if> 是否自动探测发布日期,尽量手工配置,自动探测费时,默认关闭
                        </label>
                    </div>
                </div>
                <button type="submit" class="btn btn-info">抓取样例数据</button>
                <button type="button" class="btn btn-primary" onclick="exportJson()">导出模板</button>
                <button type="button" class="btn btn-danger" onclick="submitTask()">提交抓取任务</button>
                <button type="button" class="btn btn-warning" onclick="save()">存储此模板</button>
                <button type="button" class="btn btn-info" id="showAdvancedSettingBtn" onclick="showAdvancedSetting()">
                    显示高级配置
                </button>
            </form>
        </div>
    </div>
</div>
<div class="container">
    <form id="import" action="${pageContext.request.contextPath}/panel/commons/editSpiderInfo" method="post">
        <fieldset class="form-group">
            <label for="jsonSpiderInfo">Json爬虫模板</label>
            <textarea class="form-control" id="jsonSpiderInfo" rows="10"
                      name="jsonSpiderInfo">${jsonSpiderInfo}</textarea>
        </fieldset>
        <button type="submit" class="btn btn-warning">自动填充</button>
    </form>
</div>
<div class="container">
    <div class="alert alert-success" style="display: none;" role="alert" id="taskIdDiv">
        <strong>Well done!</strong>抓取任务ID:<span id="taskId"></span>.
        <a class="btn btn-info" id="openTaskDetail" target="_blank">查看抓取详情</a>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
            <span class="sr-only">Close</span>
        </button>
    </div>
    <div class="row">
        <table class="table table-hover table-striped" style="display:none;" id="webpageTable">
            <thead class="thead-inverse">
            <tr>
                <th>#</th>
                <th>Title</th>
                <th>Summary</th>
                <th>Content</th>
                <th>PublishTime</th>
                <th>Keywords</th>
                <th>Category</th>
                <th>Go</th>
                <th>Full Data</th>
            </tr>
            </thead>
            <tbody id="webpageTableBody">

            </tbody>
        </table>
    </div>
</div>
 
</body>
<script>
    //JSON格式填充起始URL
    var startUrls = [];
    <c:forEach items="${spiderInfo.startURL}" var="url">
    startUrls.push('${url}');
    </c:forEach>
    $('#startURL').val(JSON.stringify(startUrls));
    //JSON格式填充回调地址
    var callbackURL = [];
    <c:forEach items="${spiderInfo.callbackURL}" var="url">
    callbackURL.push('${url}');
    </c:forEach>
    $('#callbackURL').val(JSON.stringify(callbackURL));
    // 检测上次编辑到一半的模板
    var spiderInfo = localStorage["spiderInfo"];
    if (spiderInfo != undefined) {
        showModal("请注意", "检测到有尚未保存的爬虫模板,是否要继续编辑?", function () {
            $('#confirmModal').modal('hide');
            localStorage.removeItem("spiderInfo");
        }, function () {
            window.location.href = "${pageContext.request.contextPath}/panel/commons/editSpiderInfo?jsonSpiderInfo=" + spiderInfo;
            localStorage.removeItem("spiderInfo");
        });
    }
</script>
</html>
