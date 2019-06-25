# markdown to html
## 背景
将markdown文档转换为html，主要是web应用中有些场景会用到，如博客系统，支持markdown语法的评论功能等

要自己去实现这个功能，并没有那么简单，当然面向GitHub编程，就简单很多了

## 设计

### 1. markdown 转 html

在github上相关的开源包还是比较多的，选择了一个之前看 Solo （一个开源的java博客系统）源码时，接触到的辅助包 `flexmark`

因为`flexmark` 工程比较庞大，我们这里只依赖其中的markdown转html的工具类，所以只需要添加下面的依赖即可

```java
<!--markdown to html-->
<dependency>
    <groupId>com.vladsch.flexmark</groupId>
    <artifactId>flexmark</artifactId>
    <version>0.26.4</version>
</dependency>
<dependency>
    <groupId>com.vladsch.flexmark</groupId>
    <artifactId>flexmark-util</artifactId>
    <version>0.26.4</version>
</dependency>
<!--表格渲染插件-->
<dependency>
    <groupId>com.vladsch.flexmark</groupId>
    <artifactId>flexmark-ext-tables</artifactId>
    <version>0.26.4</version>
</dependency>
```


使用姿势也比较简单，从demo中查看，下面给出一个从文件中读取内容并转换的过程

```java
// 从文件中读取markdown内容
InputStream stream = this.getClass().getClassLoader().getResourceAsStream("test.md");
BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));

List<String> list = reader.lines().collect(Collectors.toList());
String content = Joiner.on("\n").join(list);



// markdown to image
MutableDataSet options = new MutableDataSet();
options.setFrom(ParserEmulationProfile.MARKDOWN);
options.set(Parser.EXTENSIONS, Arrays.asList(new Extension[] { TablesExtension.create()}));
Parser parser = Parser.builder(options).build();
HtmlRenderer renderer = HtmlRenderer.builder(options).build();

Node document = parser.parse(content);
String html = renderer.render(document);
```

## 实现

> 上面给出了设计思路，主要是利用开源包进行转换，在此基础上进行封装，使得调用方式更加友好


### 0. 依赖

pom直接依赖即可

```java
 <!--markdown to html-->
<dependency>
    <groupId>com.vladsch.flexmark</groupId>
    <artifactId>flexmark</artifactId>
    <version>${flexmark.version}</version>
</dependency>
<dependency>
    <groupId>com.vladsch.flexmark</groupId>
    <artifactId>flexmark-util</artifactId>
    <version>${flexmark.version}</version>
</dependency>
<dependency>
    <groupId>com.vladsch.flexmark</groupId>
    <artifactId>flexmark-ext-tables</artifactId>
    <version>${flexmark.version}</version>
</dependency>
<!--markdown to html end-->
```

### 1. `MarkdownEntity` 

这个entity类除了markdown转换后的html内容之外，还增加了`css` 和 `divStyle` 属性

- `css` 属性，主要是用于美化输出html的展示样式
- `divStyle` 同样也是为了定义一些通用的属性，会在html内容外层加一个`<div>`标签，可以在其中进行统一的宽高设置，字体....

```java
@Data
public class MarkdownEntity {

    public static String TAG_WIDTH = "<style type=\"text/css\"> %s { width:85%%} </style>";

    // css 样式
    private String css;

    // 最外网的div标签， 可以用来设置样式，宽高，字体等
    private Map<String, String> divStyle = new ConcurrentHashMap<>();

    // 转换后的html文档
    private String html;

    public MarkdownEntity() {
    }

    public MarkdownEntity(String html) {
        this.html = html;
    }

    @Override
    public String toString() {
        return css + "\n<div " + parseDiv() + ">\n" + html + "\n</div>";
    }


    private String parseDiv() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : divStyle.entrySet()) {
            builder.append(entry.getKey()).append("=\"")
            .append(entry.getValue()).append("\" ");
        }
        return builder.toString();
    }


    public void addDivStyle(String attrKey, String value) {
        if (divStyle.containsKey(attrKey)) {
            divStyle.put(attrKey, divStyle.get(attrKey) + " " + value);
        } else {
            divStyle.put(attrKey, value);
        }
    }


    public void addWidthCss(String tag) {
        String wcss = String.format(TAG_WIDTH, tag);
        css += wcss;
    }
}
```

### 2. `MarkDown2HtmlWrapper`
> 操作封装类

- 从git上找了一个简单`markdown.css`样式， 为了避免每次都去文件中读，这里定义一个静态变量 `MD_CSS`
- 为了利用css样式，需要给 `MarkdownEntity` 的 divStyle 新增一个 `class: markdown-body `样式
- markdown to html 的主要逻辑在 `parse` 方法中，注意下为了支持table，加载了对应的table插件

```java
public class MarkDown2HtmlWrapper {

    private static String MD_CSS = null;

    static {
        try {
            MD_CSS = FileReadUtil.readAll("md/huimarkdown.css");
            MD_CSS = "<style type=\"text/css\">\n" + MD_CSS + "\n</style>\n";
        } catch (Exception e) {
            MD_CSS = "";
        }
    }


    /**
     * 将本地的markdown文件，转为html文档输出
     *
     * @param path 相对地址or绝对地址 ("/" 开头)
     * @return
     * @throws IOException
     */
    public static MarkdownEntity ofFile(String path) throws IOException {
        return ofStream(FileReadUtil.getStreamByFileName(path));
    }


    /**
     * 将网络的markdown文件，转为html文档输出
     *
     * @param url http开头的url格式
     * @return
     * @throws IOException
     */
    public static MarkdownEntity ofUrl(String url) throws IOException {
        return ofStream(FileReadUtil.getStreamByFileName(url));
    }


    /**
     * 将流转为html文档输出
     *
     * @param stream
     * @return
     */
    public static MarkdownEntity ofStream(InputStream stream) {
        BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(stream, Charset.forName("UTF-8")));
        List<String> lines = bufferedReader.lines().collect(Collectors.toList());
        String content = Joiner.on("\n").join(lines);
        return ofContent(content);
    }


    /**
     * 直接将markdown语义的文本转为html格式输出
     *
     * @param content markdown语义文本
     * @return
     */
    public static MarkdownEntity ofContent(String content) {
        String html = parse(content);
        MarkdownEntity entity = new MarkdownEntity();
        entity.setCss(MD_CSS);
        entity.setHtml(html);
        entity.addDivStyle("class", "markdown-body ");
        return entity;
    }


    /**
     * markdown to image
     *
     * @param content markdown contents
     * @return parse html contents
     */
    public static String parse(String content) {
        MutableDataSet options = new MutableDataSet();
        options.setFrom(ParserEmulationProfile.MARKDOWN);

        // enable table parse!
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()));


        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(content);
        return renderer.render(document);
    }

}
```


## 测试

测试代码比较简单，下面三行即可

```java
@Test
public void markdown2html() throws IOException {
    String file = "md/tutorial.md";
    MarkdownEntity html = MarkDown2HtmlWrapper.ofFile(file);
    System.out.println(html.toString());
}
```


markdown 文件如下

```
Markdown cells support standard Markdown syntax as well as GitHub Flavored Markdown (GFM). Open the preview to see these rendered.

### Basics

# H1
## H2
### H3
#### H4
##### H5
###### H6

---

*italic*, **bold**, ~~Scratch this.~~

`inline code`

### Lists

1. First ordered list item
2. Another item
  * Unordered sub-list. 
1. Actual numbers don't matter, just that it's a number
  1. Ordered sub-list
4. And another item.

### Quote

> Peace cannot be kept by force; it can only be achieved by understanding.

### Links

[I'm an inline-style link](https://www.google.com)
http://example.com

You can also create a link to another note: (Note menu -> Copy Note Link -> Paste)
[01 - Getting Started](quiver-note-url/D2A1CC36-CC97-4701-A895-EFC98EF47026)

### Tables

| Tables        | Are           | Cool  |
| ------------- |:-------------:| -----:|
| col 3 is      | right-aligned | $1600 |
| col 2 is      | centered      |   $12 |
| zebra stripes | are neat      |    $1 |

### GFM Task Lists

- [ ] a task list item
- [ ] list syntax required
- [ ] normal **formatting**, @mentions, #1234 refs
- [ ] incomplete
- [x] completed

### Inline LaTeX

You can use inline LaTeX inside Markdown cells as well, for example, $x^2$.
```

测试示意图

![testCase](https://static.oschina.net/uploads/img/201709/11200153_lCgP.gif)

## 其他

项目地址：[https://github.com/liuyueyi/quick-media](https://github.com/liuyueyi/quick-media)

个人博客：[一灰的个人博客](http://blog.zbang.online:8080)

公众号获取更多:

![个人信息](https://static.oschina.net/uploads/img/201709/05212311_hPmi.png "个人信息")