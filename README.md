# quick-media

[![Join the chat at https://gitter.im/quick-media/Lobby](https://badges.gitter.im/quick-media/Lobby.svg)](https://gitter.im/quick-media/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Builder](https://travis-ci.org/liuyueyi/quick-media.svg?branch=master)](https://travis-ci.org/liuyueyi/quick-media)
[![codecov](https://codecov.io/gh/liuyueyi/quick-media/branch/master/graph/badge.svg)](https://codecov.io/gh/liuyueyi/quick-media)
[![Average time to resolve an issue](http://isitmaintained.com/badge/resolution/liuyueyi/quick-media.svg)](http://isitmaintained.com/project/liuyueyi/quick-media "Average time to resolve an issue")
[![Percentage of issues still open](http://isitmaintained.com/badge/open/liuyueyi/quick-media.svg)](http://isitmaintained.com/project/liuyueyi/quick-media "Percentage of issues still open")

> 多媒体处理web服务
>
> mult-media process Web Service by FFMPEG & ImageMagic & SpringMVC
 
本项目为一个提供图片 + 音频 + 视频处理的Web项目，我们的目标是封装一套多媒体文件处理的公共类库，简化各种复杂的调用；利用 spring-boot 来提供http接口实现多媒体的操作实例功能

整理了下QuickMedia的使用与技术文档，可以通过下面的链接进行查看

- [http://liuyueyi.gitee.io/quick-media/#/](http://liuyueyi.gitee.io/quick-media/#/)
- [https://liuyueyi.github.io/quick-media/#/](https://liuyueyi.github.io/quick-media/#/)

线上体验地址

- Z+ | web : [http://media.hhui.top](http://media.hhui.top)

### 使用说明

在下载本项目之后，有些常见事项需要注意一二

- 工程中使用lombok简化大量的代码，因此使用idea的童鞋请装一下lombok的插件
- 运行时，如果报某些依赖找不到，请在父pom文件中添加源

    ```xml
    <repositories>
        <repository>
            <id>yihui-maven-repo</id>
            <url>https://raw.githubusercontent.com/liuyueyi/maven-repository/master/repository</url>
        </repository>
    </repositories>
    ```
- 部分插件依赖第三方库，如 ffmpge, phantomjs, image-magic，请确保已经安装

## I. 项目分析

### 1. 技术栈

- spring-boot 
- ffmpeg
- ImageMagic
- zxing
- batik
- flexmark
- phantomjs

### 2. 结构分析

目前项目主要结构区分为web/plugins两个模块，

#### web

- 根据spring-boot可以迅速搭建一个web服务，提供http接口用于功能测试
- 内部集成了一个简单的web网站，打开: [http://media.hhui.top:8080/media/webs](http://media.hhui.top:8080/media/webs) 查看
- 使用ReactJS，前后端分离，写了一个更友好的网站，打开: [https://zweb.hhui.top/#/index](https://zweb.hhui.top/#/index) 查看
- 内部实现了小程序【图文小工具】的后端逻辑


#### plugins 

插件工程，根据不同的场景，支持不同的服务功能，目前将所有的插件抽象出来，可以独立作为工具包提供给第三方依赖，外部使用方式

添加源：

```xml
<repositories>
    <repository>
        <id>yihui-maven-repo</id>
        <url>https://raw.githubusercontent.com/liuyueyi/maven-repository/master/repository</url>
    </repository>
</repositories>
```

**audio-plugin**

提供音频转码服务，使用依赖如下，详细查看: [audio-plugin说明](plugins/audio-plugin/readme.md)

```xml
<dependency>
    <groupId>com.github.hui.media</groupId>
    <artifactId>audio-plugin</artifactId>
    <version>2.0</version>
</dependency>
```

**date-plugin**

提供时间戳、日期转换为农历日期，详细查看：[date-plugin说明](plugins/date-plugin/readme.md)

```xml
<dependency>
    <groupId>com.github.hui.media</groupId>
    <artifactId>date-plugin</artifactId>
    <version>2.0</version>
</dependency>
```


**image-plugin**

提供图片合成，提供gif图片生成等图片操作的封装类，详细查看： [image-plugin说明](plugins/image-plugin/readme.md)

```xml
<dependency>
    <groupId>com.github.hui.media</groupId>
    <artifactId>image-plugin</artifactId>
    <version>2.0</version>
</dependency>
```

**markdown-plugin** 

markdown转html，转图片的封装类， 详细内容查看: [markdown-plugin](plugins/markdown-plugin/readme.md)

```xml
<dependency>
    <groupId>com.github.hui.media</groupId>
    <artifactId>markdown-plugin</artifactId>
    <version>2.0</version>
</dependency>
```

**phantom-plugin**

提供根据phantomjs渲染html的封装

```xml
<dependency>
    <groupId>com.github.hui.media</groupId>
    <artifactId>phantom-plugin</artifactId>
    <version>2.0</version>
</dependency>
```

**qrcode-plugin**

提供二维码生成和解析的封装，详细查看: [qrcode-plugin使用说明](plugins/qrcode-plugin/readme.md)

```xml
<dependency>
    <groupId>com.github.hui.media</groupId>
    <artifactId>qrcode-plugin</artifactId>
    <version>2.0</version>
</dependency>
```

**svg-plugin**

提供svg文档的渲染，输出图片的封装，详细查看: [svg-plugin使用说明](plugins/svg-plugin/readme.md)

```xml
<dependency>
    <groupId>com.github.hui.media</groupId>
    <artifactId>svg-core</artifactId>
    <version>2.0</version>
</dependency>
```


**imagic-plugin**

基于imagic-magic实现的图片编辑插件封装，详细查看: [imagic-plugin使用说明](plugins/imagic-plugin/readme.md)

```xml
<dependency>
    <groupId>com.github.hui.media</groupId>
    <artifactId>imagic-core</artifactId>
    <version>2.0</version>
</dependency>
```


## II. 已支持服务

### 1. 音频转码
   - [x] 音频不同格式的相互转码

### 2. 二维码生成 & 解析

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
- [x] 动态二维码生成支持
- [x] 二维码信息解析

[查看更多二维码详情](https://liuyueyi.github.io/quick-media/#/插件/二维码/二维码插件概览)

给出一个实际生成的case如下:

<img src="http://ww1.sinaimg.cn/large/8154e929gy1g8wadvkt56g20pz08zwl5.gif"/>

### 3. 图片

- [x] 长图文生成
   - [水平文字，上下布局长图文生成](https://liuyueyi.github.io/quick-media/#/插件/image/Java实现长图文生成)
   - [垂直文字，左右布局长图文生成](https://liuyueyi.github.io/quick-media/#/插件/image/Java实现竖排长图文生成)
   - 第三方字体支持
- [x] markdown 转 image
    - [markdown 转 html](https://liuyueyi.github.io/quick-media/#/插件/markdown/markdown转html)
    - [html 转 image](https://liuyueyi.github.io/quick-media/#/markdown转image)
    - [利用phantomjs实现html转image](https://liuyueyi.github.io/quick-media/#/插件/phantom/Java&PhantomJs实现html输出图片)
- [x] gif图生成
- [x] 合成
    - [图片合成支持](https://liuyueyi.github.io/quick-media/#/插件/image/图片合成)
- [x] 水印
- [x] svg渲染
- [x] 裁剪
- [x] 压缩
- [x] 旋转
- [x] 缩放
- [ ] 格式转换


[查看更多图片服务详情](https://liuyueyi.github.io/quick-media/#/插件/image/概览)
    
### 4. 视频相关
- [ ] 视频压缩
- [ ] 转码
- [ ] 截取
- [ ] 码率调整
- [ ] 生成gif


## III. 阶段记录

详情查看: [quick-media更新迭代日志](https://liuyueyi.github.io/quick-media/#/%E8%BF%AD%E4%BB%A3/%E6%9B%B4%E6%96%B0%E6%97%A5%E5%BF%97)

## IV. 文档

所有使用以及技术文档，开发过程中一些常见问题汇总，可以点击👉: [quick-media文档](https://liuyueyi.github.io/quick-media/#/) 

### 问题记录汇总

- [图片旋转不生效问题](https://liuyueyi.github.io/quick-media/#/采坑/图片旋转问题修复)
- [markdonw转图片中文乱码问题](https://liuyueyi.github.io/quick-media/#/采坑/markdown转图片中文乱码)
- [兼容ImageIO读取jpeg图片变红](https://liuyueyi.github.io/quick-media/#/采坑/Batik渲染png图片异常的bug修复)
- [Batik渲染png图片异常的bug修复](https://liuyueyi.github.io/quick-media/#/采坑/兼容ImageIO读取jpeg图片变红)


## V. 其他

### 其他

看到下面的star走势图，难道真的没有动心点点🖱小小的👍一下么 

[![Stargazers over time](https://starchart.cc/liuyueyi/quick-media.svg)](https://starchart.cc/liuyueyi/quick-media)

### 声明

尽信书则不如，已上内容，一家之言，因个人能力有限，难免有疏漏和错误之处，如发现bug或者有更好的建议，欢迎批评指正，不吝感激

- 微博地址: 小灰灰Blog
- QQ： 一灰灰/3302797840
- WeChat: 一灰/liuyueyi25

### 扫描关注

公众号&博客

![QrCode](https://gitee.com/liuyueyi/Source/raw/master/img/info/blogInfoV2.png)


打赏码

![pay](https://gitee.com/liuyueyi/Source/raw/master/img/pay/pay.png)


---

### 实例演示

#### 0. 应用网站

项目本身提供一个控制台，基于reactjs搭建，在console模块下，启动即可

- [http://localhost:8089](http://localhost:8089)


使用react.js重构后的前端网站，实现前后端分离，前端网页借助gitee的pages直接部署，测试链接

- Z+ | web : [http://liuyueyi.gitee.io/zweb](http://liuyueyi.gitee.io/zweb)
- Z+ | web : [https://media.hhui.top/#/index](https://media.hhui.top/#/index)


web实际演示图: 

![demo](doc/img/demo/zwebdemo.gif)
