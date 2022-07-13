# QuickMedia 项目介绍

## 1.1 概述

QuickMedia项目，主要基于一些开源的项目实现复杂的图片、音频、视频、二维码等多媒体文件处理的二次封装类库，主要借助Builder模式简化各种复杂的调用方式，供使用者最低成本的实现开箱即用

## 1.2 特性

QuickMedia项目根据不同的应用场景，以独立的二方包方式提供第三方直接引入使用。目前的项目结构主要是以`plugin`方式进行管理，每个plugin表示提供一项对应的服务，使用者可以按需引入，无需导入整个项目工程

### 插件列表

- 音频处理： [audio-plugin](https://github.com/liuyueyi/quick-media/blob/master/plugins/audio-plugin)
- 基于JDK-AWT图片处理: [image-plugin](https://github.com/liuyueyi/quick-media/blob/master/plugins/image-plugin)
- 基于ImageMagic图片处理: [imagic-plugin](https://github.com/liuyueyi/quick-media/blob/master/plugins/imagic-plugin)
- PhantomJs渲染html插件: [phantom-plugin](https://github.com/liuyueyi/quick-media/blob/master/plugins/phantom-plugin)
- svg渲染插件: [svg-plugin](https://github.com/liuyueyi/quick-media/blob/master/plugins/svg-plugin)
- 基于jdk的markdonw/html渲染插件: [markdown-plugin](https://github.com/liuyueyi/quick-media/blob/master/plugins/markdown-plugin)
- 酷炫二维码生成/解码插件: [qrcode-plugin](https://github.com/liuyueyi/quick-media/blob/master/plugins/qrcode-plugin)

### 功能支持

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


#### 3. 图片

- [x] 长图文生成
   - 水平文字，上下布局长图文生成
   - 垂直文字，左右布局长图文生成
   - 第三方字体支持
- [x] markdown 转 image
    - markdown 转 html
    - html 转 image
    - 利用phantomjs实现html转image
- [x] gif图生成
- [x] 合成
    - 图片合成支持
- [x] 水印
- [x] svg渲染
- [x] 裁剪
- [x] 压缩
- [x] 旋转
- [x] 缩放
- [ ] 格式转换


### 4. 图像

- [x] 图像转素描风格

### 5. 视频相关

- [ ] 视频压缩
- [ ] 转码
- [ ] 截取
- [ ] 码率调整
- [ ] 生成gif


## 1.3 下载

### 文档地址

目前QuickMeida相关的文档会收集在这个网站下面，如果有兴趣了解更多的，可以到个人博客站上查看

- [QuickMedia相关文档](#)
- [一灰灰Blog个人博客站点](https://blog.hhui.top) ([备用地址](https://liuyueyi.github.io/hexblog/))
- [一灰灰Spring系列专题博文](http://spring.hhui.top)([备用地址](https://liuyueyi.gitee.io/spring-blog/))

### 源码地址

目前项目源码完全托管与Github/Gitee，可以直接下载

| 仓库地址 | release | 
| --- | --- |
| [https://github.com/liuyueyi/quick-media](https://github.com/liuyueyi/quick-media) | [release-page](https://github.com/liuyueyi/quick-media/releases) |
| [https://gitee.com/liuyueyi/quick-media](https://gitee.com/liuyueyi/quick-media) | [release-page](https://gitee.com/liuyueyi/quick-media/releases) |


### 仓库&插件

推荐直接从中央仓库获取最新的依赖包，无需额外的配置

- 直达地址：[https://mvnrepository.com/artifact/com.github.liuyueyi.media](https://mvnrepository.com/artifact/com.github.liuyueyi.media)


<del>

QuickMeida的Plugin以二方包的方式提供服务，正式版的jar包，部署在以github构建的一个私人maven仓库中，优点是简单易控，缺点是没有中央仓库正规易用、而且国内网络可能也不咋地。(关于如何使用github搭建一个私人maven仓库，有兴趣的可以参考博文: [借助GitHub搭建属于自己的maven仓库教程](https://blog.hhui.top/hexblog/2018/02/12/%E5%80%9F%E5%8A%A9GitHub%E6%90%AD%E5%BB%BA%E5%B1%9E%E4%BA%8E%E8%87%AA%E5%B7%B1%E7%9A%84maven%E4%BB%93%E5%BA%93%E6%95%99%E7%A8%8B/) )

> 私服仓库地址: [https://github.com/liuyueyi/maven-repository/](https://github.com/liuyueyi/maven-repository/)

maven的引入方式需要先添加一下`repository`

```xml
<repositories>
    <repository>
        <id>yihui-maven-repo</id>
        <url>https://raw.githubusercontent.com/liuyueyi/maven-repository/master/repository</url>
    </repository>
</repositories>
```

</del>

接下来可以按需引入对应的二方包，在引入之前，如果需要确定最新的jar版本，请到更新日志这一章节进行确认，或者直接进入[私服仓库](https://github.com/liuyueyi/maven-repository/tree/master/repository/com/github/hui/media)进行查看

**基于FFmpeg 音频处理二方包**

```xml
<!-- 基于github私服的引入方式 -->
<dependency>
    <groupId>com.github.hui.media</groupId>
    <artifactId>audio-plugin</artifactId>
    <version>x.x</version>
</dependency>

<!-- 推荐使用中央仓库直接引入方式 -->
<dependency>
    <groupId>com.github.liuyueyi.media</groupId>
    <artifactId>audio-plugin</artifactId>
    <version>x.x</version>
</dependency>
```

**日期农历与公历互转二方包**

```xml
<!-- 基于github私服的引入方式 -->
<dependency>
    <groupId>com.github.hui.media</groupId>
    <artifactId>date-plugin</artifactId>
    <version>x.x</version>
</dependency>

<!-- 推荐使用中央仓库直接引入方式 -->
<dependency>
    <groupId>com.github.liuyueyi.media</groupId>
    <artifactId>date-plugin</artifactId>
    <version>x.x</version>
</dependency>
```

**基于JDK AWT的图片处理二方包**

```xml
<!-- 基于github私服的引入方式 -->
<dependency>
    <groupId>com.github.hui.media</groupId>
    <artifactId>image-plugin</artifactId>
    <version>x.x</version>
</dependency>

<!-- 推荐使用中央仓库直接引入方式 -->
<dependency>
    <groupId>com.github.liuyueyi.media</groupId>
    <artifactId>image-plugin</artifactId>
    <version>x.x</version>
</dependency>
```

**基于flexmark，xhtmlrenderer实现markdown/html渲染输出图片二方包**

```xml
<!-- 基于github私服的引入方式 -->
<dependency>
    <groupId>com.github.hui.media</groupId>
    <artifactId>markdown-plugin</artifactId>
    <version>x.x</version>
</dependency>

<!-- 推荐使用中央仓库直接引入方式 -->
<dependency>
    <groupId>com.github.liuyueyi.media</groupId>
    <artifactId>markdown-plugin</artifactId>
    <version>x.x</version>
</dependency>
```

**phantomjs封装二方包**

```xml
<!-- 基于github私服的引入方式 -->
<dependency>
    <groupId>com.github.hui.media</groupId>
    <artifactId>phantom-plugin</artifactId>
    <version>x.x</version>
</dependency>

<!-- 推荐使用中央仓库直接引入方式 -->
<dependency>
    <groupId>com.github.liuyueyi.media</groupId>
    <artifactId>phantom-plugin</artifactId>
    <version>x.x</version>
</dependency>
```

**基于zxing深度定制的二维码编解码二方包**

```xml
<!-- 基于github私服的引入方式 -->
<dependency>
    <groupId>com.github.hui.media</groupId>
    <artifactId>qrcode-plugin</artifactId>
    <version>x.x</version>
</dependency>

<!-- 推荐使用中央仓库直接引入方式 -->
<dependency>
    <groupId>com.github.liuyueyi.media</groupId>
    <artifactId>qrcode-plugin</artifactId>
    <version>x.x</version>
</dependency>
```

**基于batik SVG渲染二方包**

```xml
<!-- 基于github私服的引入方式 -->
<dependency>
    <groupId>com.github.hui.media</groupId>
    <artifactId>svg-core</artifactId>
    <version>x.x</version>
</dependency>

<!-- 推荐使用中央仓库直接引入方式 -->
<dependency>
    <groupId>com.github.liuyueyi.media</groupId>
    <artifactId>svg-core</artifactId>
    <version>x.x</version>
</dependency>
```

**ImageMagic封装二方包**

```xml
<!-- 基于github私服的引入方式 -->
<dependency>
    <groupId>com.github.hui.media</groupId>
    <artifactId>imagic-core</artifactId>
    <version>x.x</version>
</dependency>

<!-- 推荐使用中央仓库直接引入方式 -->
<dependency>
    <groupId>com.github.liuyueyi.media</groupId>
    <artifactId>imagic-core</artifactId>
    <version>x.x</version>
</dependency>
```

## 1.4 实例操作

在QuickMedia项目内部集成了一个建议的控制台，在直接捞出源码进行测试时，可以启动进行测试；此外也支持在线测试

基于thymeleaf实现的前端演示网站，相关代码写在本工程中：

- Z+ : [http://media.hhui.top:8080/media/webs](http://media.hhui.top:8080/media/webs)

使用react.js重构后的前端网站，实现前后端分离，前端网页借助gitee的pages直接部署，测试链接

- Z+ | web : [http://liuyueyi.gitee.io/zweb](http://liuyueyi.gitee.io/zweb)
- Z+ | web : [https://zweb.hhui.top/#/index](https://zweb.hhui.top/#/index)

web演示demo如

![](https://raw.githubusercontent.com/liuyueyi/quick-media/master/doc/img/demo/zwebdemo.gif)

小程序测试demo（较长时间没有更新了...)

![](https://camo.githubusercontent.com/010cbb585ddcd073df7e3cfdad02860ce5eed7fe/687474703a2f2f73322e6d6f677563646e2e636f6d2f6d6c63646e2f6334353430362f3137313132335f33693434693033386636386765626b646b35323330323138363466366c5f333230783332302e6a7067)



