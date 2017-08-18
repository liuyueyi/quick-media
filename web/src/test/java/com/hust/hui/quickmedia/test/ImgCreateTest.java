package com.hust.hui.quickmedia.test;

import com.alibaba.fastjson.JSON;
import com.hust.hui.quickmedia.web.api.image.create.ImgCreateRequest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yihui on 2017/8/18.
 */
public class ImgCreateTest {

    @Test
    public void testImgCreate() {
        List<ImgCreateRequest.ContentInfo> cList = new ArrayList<>();
        ImgCreateRequest.ContentInfo cInfo = new ImgCreateRequest.ContentInfo();
        cInfo.setContent("招魂酹翁宾旸\n" +
                "郑起\n" +
                "\n" +
                "君之在世帝敕下，君之谢世帝敕回。\n" +
                "魂之为变性原返，气之为物情本开。\n" +
                "於戏龙兮凤兮神气盛，噫嘻鬼兮归兮大块埃。"
        );
        cInfo.setOrder(0);

        cList.add(cInfo);


        ImgCreateRequest.ContentInfo cInfo2 = new ImgCreateRequest.ContentInfo();
        cInfo2.setContent("身可朽名不可朽，骨可灰神不可灰。\n" +
                "采石捉月李白非醉，耒阳避水子美非灾。\n" +
                "长孙王吉命不夭，玉川老子诗不徘。\n" +
                "新城罗隐在奇特，钱塘潘阆终崔嵬。\n" +
                "阴兮魄兮曷往，阳兮魄兮曷来。\n" +
                "君其归来，故交寥落更散漫。\n" +
                "君来归来，帝城绚烂可徘徊。\n" +
                "君其归来，东西南北不可去。\n" +
                "君其归来。\n" +
                "春秋霜露令人哀。\n" +
                "花之明吾无与笑，叶之陨吾实若摧。\n" +
                "晓猿啸吾闻泪堕，宵鹤立吾见心猜。\n" +
                "玉泉其清可鉴，西湖其甘可杯。\n" +
                "孤山暖梅香可嗅，花翁葬荐菊之隈。\n" +
                "君其归来，可伴逋仙之梅，去此又奚之哉。");
        cInfo2.setOrder(2);
        cList.add(cInfo2);


        List<ImgCreateRequest.ImgInfo> iList = new ArrayList<>();
        ImgCreateRequest.ImgInfo img = new ImgCreateRequest.ImgInfo();
        img.setImg("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png");
        img.setOrder(1);
        iList.add(img);

        ImgCreateRequest.Infos info = new ImgCreateRequest.Infos();
        info.setContents(cList);
        info.setImgs(iList);

        System.out.println(JSON.toJSONString(info));
    }
}
