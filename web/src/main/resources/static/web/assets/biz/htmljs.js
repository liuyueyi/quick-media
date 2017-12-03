$(function() {

    // 点击了预览
    $('#preBtn').click(function() {
        var url = $('#htmlurl').val().trim();
        if(!url || !url.startsWith("http")) {
            alert("请输入合法的以http开头的url");
            return;
        }


        window.open(url, "_blank")
    });

    // parse2img
    $('#executeBtn').click(function() {
       var url = $('#htmlurl').val().trim();
        if(!url || !url.startsWith("http")) {
            alert("请输入合法的以http开头的url");
            return;
        }

        var params = {
            "token" : "0xdahdljk3u8eqhrjqwer90e",
            "url" : url,
            "type" : "url"
        };

        // 显示过度动画
        $('#data').attr('src', '/web/assets/img/loading.gif');
        $('#imgDetail').attr('href', '/web/assets/img/loading.gif');
        $('#imgLink').attr("href", '');

        $.post("/wx/html2img?", params, function (data, status) {
            console.log(data, status);

            if(data.status.code = 200) {
                // var img = data.result.prefix + data.result.base64result;
                var img = data.result.url;
                $('#data').attr("src", img);
                $('#imgDetail').attr('href', img);
                $('#imgLink').attr("href", url);
                $().toastmessage("showSuccessToast", "转换成功!");
            } else {
                $().toastmessage("showErrorToast", "转换失败： " + data.status.msg);
            }

        });



    });

});