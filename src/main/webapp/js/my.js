/**
 * Created by gaoshen on 16/5/17.
 */
 var needShowResultModel = false;
function showModal(title, content, cancelAction, confirmAction) {
    $("#confirmModalTitle").text(title);
    $("#confirmModalBody").html(content);
    if (cancelAction != undefined) {
        $("#cancelButton").one("click", cancelAction);
    }
    if (confirmAction != undefined) {
        $("#confirmButton").one("click", confirmAction);
    }
    $('#confirmModal').modal('show');
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
        needShowResultModel = true;
    });
    $("#confirmModal").one('hidden.bs.modal', function () {
        if (needShowResultModel) {
            $.getJSON(url, pram, callback);
        }
    });
    needShowResultModel = false;
    $('#confirmModal').modal('show');
}
function rpcAndShowData(url, pram) {
    rpc(url, pram, function (data) {
        needShowResultModel = false;
        if (data.success) {
            showModal("成功", data.result != undefined ? data.result : data.resultList, function  () {
                $('#confirmModal').modal('hide');
            }, function  () {
                $('#confirmModal').modal('hide');
            });
        } else {
            showModal("失败", "请重试" + data.errorMsg, function  () {
                $('#confirmModal').modal('hide');
            }, function  () {
                $('#confirmModal').modal('hide');
            });
        }
    })
}
