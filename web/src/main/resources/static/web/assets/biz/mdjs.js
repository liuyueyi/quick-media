$(function() {

    $('#md2pdf').click(function() {
        parse("pdf");
    });

    $('#md2img').click(function() {
        parse("img");
    });

    function parse(type) {
        var content = $('#mdContent').val();
        console.log("the markdown content: ", content, type);
        $().toastmessage("showNoticeToast", "开始导出...")

        var params = {
            "content" : content,
            "token" : "0xdahdljk3u8eqhrjqwer90e",
            "type" : "img"
        };

        $.post("/wx/md2img?", params, function (data, status) {
            console.log(data, status);
            if(data.status.code = 200) {
                var img = data.result.prefix + data.result.base64result;
                // var img = data.result.url;
                $('#preData').show();
                $('#data').attr("src", img);
                $().toastmessage("showSuccessToast", "转换成功!");
            } else {
                $('#preData').hide();
                $().toastmessage("showErrorToast", "转换失败： " + data.status.msg);
            }

        });
    }
});