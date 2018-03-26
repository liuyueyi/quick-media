## quick-media
> 多媒体处理web服务
>
> mult-media process Web Service by FFMPEG & ImageMagic & SpringMVC
 
本项目为一个提供图片 + 音频 + 视频处理的Web项目，我们的目标是封装一套多媒体文件处理的公共类库，简化各种复杂的调用

利用 spring-boot 来提供http接口实现多媒体的操作


### 技术栈

- spring-boot 
- ffmpeg
- imageMagic
- zxing
- batik


### 已支持服务

#### 1. 音频转码
   - [x] 音频不同格式的相互转码

#### 2. 二维码生成 & 解析
- [x] 二维码生成
- [x] 个性二维码生成
    - 支持logo
    - 支持logo样式 （圆角logo， 边框）
    - 支持二维码颜色设置
    - 支持探测图形颜色设置
    - 支持背景图
    - 支持base64格式的二维码图片
    - 支持二维码定制绘制信息样式
     - 三角形
     - 矩形
     - 五边形 （五角星待支持）
     - 六边形
     - 八边形
     - 圆
     - 自定义图片
- [x] 二维码信息解析

[查看更多二维码详情](doc/qrcode/QrGuide.md)



#### 3. 图片

- [x] 长图文生成
   - [水平文字，上下布局长图文生成](doc/images/imgGenV1.md)
   - [垂直文字，左右布局长图文生成](doc/images/imgGenV2.md)
   - 第三方字体支持
- [x] markdown 转 image
    - [markdown 转 html](doc/md/md2html.md)
    - [html 转 image](doc/md/html2image.md)
    - [利用phantomjs实现html转image](doc/images/html2img.md)
- [x] gif图生成
- [x] 合成
    - [图片合成支持](doc/images/imgMerge.md)
- [x] 水印
- [x] svg渲染
- [ ] 裁剪
- [ ] 压缩
- [ ] 旋转
- [ ] 缩放
- [ ] 格式转换


[查看更多图片服务详情](doc/images/ImgGuide.md)
    
#### 4. 视频相关
- [ ] 视频压缩
- [ ] 转码
- [ ] 截取
- [ ] 码率调整
- [ ] 生成gif


### tag 记录

1. [v0.001](https://github.com/liuyueyi/quick-media/releases/tag/v0.001)

    - 实现音频转码服务
    - 实现二维码基础服务，完成基于zxing的二维码深度定制
 
2. [v0.002](https://github.com/liuyueyi/quick-media/releases/tag/v0.002)

    - 重写zxing的二维码渲染逻辑，只使用二维码矩阵的生成逻辑，支持二维码的全面定制化

3. [v0.003](https://github.com/liuyueyi/quick-media/releases/tag/v0.003)

    - 长图文生成的支持

4. [v0.004](https://github.com/liuyueyi/quick-media/releases/tag/v0.004)
   
   - markdown 语法文本转html， 转image

5. [v0.005](https://github.com/liuyueyi/quick-media/releases/tag/v0.005)

    - fix markdown 转图片中文乱码
    - 图片合成服务支持
    - 微信小程序（图文小工具）服务端源码

6. [v0.006](https://github.com/liuyueyi/quick-media/releases/tag/v0.006)

    - svg渲染支持
    - 利用phantomjs实现html渲染
    - 实现应用网站搭建

### 文档

- [音频转码服务说明](doc/audio.md)
- [二维码生成解析服务说明](doc/qrcode/QrGenV1.md)
- [二维码服务拓展说明](doc/qrcode/QrGenV2.md)
- [二维码生成深度定制](doc/qrcode/QrGenV3.md)
- [长图文生成支持](doc/images/imgGenV1.md)
- [竖排长图文生成支持](doc/images/imgGenV2.md)
- [markdown 转 html](doc/md/md2html.md)
- [html 转 image](doc/md/html2image.md)
- [图片合成支持](doc/images/imgMerge.md)
- [利用phantomjs实现html转image](doc/images/html2img.md)

### 问题记录汇总

- [图片旋转不生效问题](doc/questions/ImgRotate.md)
- [markdonw转图片中文乱码问题](doc/questions/md2imgChineseMessyCode.md)
- [兼容ImageIO读取jpeg图片变红](https://zbang.online/hexblog/public/2018/01/22/%E5%85%BC%E5%AE%B9ImageIO%E8%AF%BB%E5%8F%96jpeg%E5%9B%BE%E7%89%87%E5%8F%98%E7%BA%A2/)
- [Batik渲染png图片异常的bug修复](https://zbang.online/hexblog/public/2018/01/20/Batik%E6%B8%B2%E6%9F%93png%E5%9B%BE%E7%89%87%E5%BC%82%E5%B8%B8%E7%9A%84bug%E4%BF%AE%E5%A4%8D/)


### 声明

尽信书则不如，已上内容，纯属一家之言，因本人能力一般，见识有限，如发现bug或者有更好的建议，随时欢迎批评指正，我的微博地址: [小灰灰Blog](https://weibo.com/p/1005052169825577/home)

### 扫描关注

公众号&博客

![QrCode](https://s17.mogucdn.com/mlcdn/c45406/180209_74fic633aebgh5dgfhid2fiiggc99_1220x480.png)


打赏码

![pay](https://s3.mogucdn.com/mlcdn/c45406/180211_3a9igegd1bghf1dl26f3777aldijk_1218x478.png)


---

### 实例演示

#### 0. 应用网站

Z+ : [https://zbang.online/webs](https://zbang.online/webs)

#### 1. 小程序

以本项目提供的基本服务为蓝本，写了一个简单的小程序，欢迎各位小伙伴尝鲜

后端服务都在本项目中，包目录 : 

`com.hust.hui.quickmedia.web.wxapi`

有很多东西直接写死了，不太友好，欢迎板砖

<img src="http://s2.mogucdn.com/mlcdn/c45406/171123_3i44i038f68gebkdk523021864f6l_320x320.jpg" style="max-width:160px"/>