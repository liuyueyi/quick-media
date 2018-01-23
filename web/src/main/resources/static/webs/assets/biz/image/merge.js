$(function () {

    var app = new Vue({
        el: '#app',
        data: {
            message: 'Hello Vue!'
        }
    })


    $.ajax({
        type: "get",
        dataType: "json",
        url: '/wx/list',
        success: function (data) {
            if (data != "") {
                console.log(data);
                var html = mergeTemplates(data.result.list);
                console.log(html);
                $('#templateList').html(html)
            }
        }
    });


    function mergeTemplates(list) {
        console.log(list);
        var html = "";
        for (var i = 0; i < list.length; i++) {
            html += '<div class="item ' + (i == 0 ? 'active' : ' ') + '">';
            // html += '<img src="' + list[i].img + '" alt="" style="max-height: 400px;"/>';
            html += '<img src="' + 'http://f.hiphotos.baidu.com/image/h%3D300/sign=f5b8fcb9f2f2b211fb2e834efa816511/bd315c6034a85edf683164e940540923dc5475bf.jpg' + '" alt="" style="max-height: 400px;"/>';
            html += '<div class="carousel-caption">';
            html += '<p>' + list[i].name + '</p>';
            html += '</div></div>';
        }

        return html;
    }
});