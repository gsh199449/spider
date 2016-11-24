/**
 * Created by gaoshen on 16/5/17.
 */
function showModal(title, content) {
    $("#modalTitle").text(title);
    $("#modalBody").html(content);
    $('#myModal').modal('show');
}
function createRandomItemStyle() {
    return {
        normal: {
            color: 'rgb(' + [
                Math.round(Math.random() * 160),
                Math.round(Math.random() * 160),
                Math.round(Math.random() * 160)
            ].join(',') + ')'
        }
    };
}
var wordCloudChart;
// 使用
require(
    [
        'echarts',
        'echarts/chart/force', // 使用柱状图就加载bar模块，按需加载
        'echarts/chart/chord',
        'echarts/chart/wordCloud'
    ],
    function (ec) {
        // 基于准备好的dom，初始化echarts图表
        if (document.getElementById('wordCloudBody') != null) {
            wordCloudChart = ec.init(document.getElementById('wordCloudBody'));
        }
    }
);
function showWordCloud(url, para) {
    rpc(url, para, function (json) {
        var wordCloudOption = {
            title: {
                text: '词云图'
            },
            tooltip: {
                show: true
            },
            series: [{
                name: '主题词',
                type: 'wordCloud',
                size: ['95%', '95%'],
                textRotation: [0, 45, 90, -45],
                textPadding: 0,
                autoSize: {
                    enable: true,
                    minSize: 24
                },
                data: json.resultList
            }]
        };
        wordCloudChart.setOption(wordCloudOption, true);
    });
}
function inputModal(dataName, callback) {
    $('#data').val('');
    $("#inputModalTitle").text("请输入" + dataName);
    $("label[for='data']").html(dataName);
    $("#confirmInputButton").one("click", function () {
        $('#inputModal').modal('hide');
        callback($('#data').val());
    });
    $('#inputModal').modal('show');
}
function tableModal(data, title) {
    $("#tableModalTitle").text(title);
    var html = "";
    $.each(data, function (k, v) {
        html += '<tr>\
            <th scope="row">' + k + '</th>\
            <td>' + v + '</td>\
        </tr>';
    });
    $("#tableModalBody").html(html);
    $('#tableModal').modal('show');
}
function rpc(url, pram, callback) {
    $("#confirmModalTitle").text("确定?");
    $("#confirmModalBody").html("确定要执行" + url + "吗?");
    $("#confirmButton").one("click", function () {
        $('#confirmModal').modal('hide');
        $.getJSON(url, pram, callback);
    });
    $('#confirmModal').modal('show');
}
function rpcAndShowData(url, pram) {
    rpc(url, pram, function (data) {
        if (data.success) {
            showModal("成功", data.result != undefined ? data.result : data.resultList);
        } else {
            showModal("失败", "请重试" + data.errorMsg);
        }
    })
}
