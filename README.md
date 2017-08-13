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


### 已支持服务

#### 1. 音频转码
   - 音频不同格式的相互转码

#### 2. 二维码生成 & 解析
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
   - 支持二维码信息解析

**实例**

 基本的样式：前置色（宝蓝） + 三定位颜色（蓝色）+ logo + logo边框 + logo圆角 + 背景图 + 着色样式（三角形，矩形，五边形，六边形，八边形，圆形）
  
  ![styl1](doc/img/qrcode/styleQr1.png)
  
 
 自定义图片：使用自定义的图片来代替传统的纯色 （建议jpg格式非透明的图片）
  
  ![styl2](doc/img/qrcode/styleQr2.png)
  
 
 深度定制：二维码上的所有信息都可以进行自定义替换，如下
 
  ![style3](doc/img/qrcode/小灰灰blog.png)


---

### 待完成服务

1. 图片相关
    - 长图文
    - 裁剪
    - 压缩
    - 旋转
    - 合成
    - 水印
    - 缩放
    - 格式转换
    - xxx
    
2. 视频相关


### tag 记录

1. [v0.001](https://github.com/liuyueyi/quick-media/releases/tag/v0.001)

    - 实现音频转码服务
    - 实现二维码基础服务，完成基于zxing的二维码深度定制
 
2. [v0.002](https://github.com/liuyueyi/quick-media/releases/tag/v0.002)

    - 重写zxing的二维码渲染逻辑，只使用二维码矩阵的生成逻辑，支持二维码的全面定制化


### 文档

- [音频转码服务说明](doc/audio.md)
- [二维码生成解析服务说明](doc/qrcode.md)
- [二维码服务拓展说明](doc/qrcodeExtend.md)
- [二维码生成深度定制](doc/qrcodeSelfDesign.md)