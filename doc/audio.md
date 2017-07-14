# 利用FFMPEG实现一个音频转码服务
> 提供一个音频转码服务，主要是利用ffmpeg实现转码，利用java web对外提供http服务接口

## 背景
> 音频转码服务算是比较基础的了，之前一直没做，最近有个需求背景，是将微信的amr格式音频，转换为mp3格式，否则h5页面的音频将无法播放

出于这个转码的场景，顺带着搭建一个多媒体处理服务应用(目标是图片的基本操作，音频、视频的常用操作等）

**拟采用的技术**

1. 图片
  - imageMagic/graphicMagic + im4java

2. 音频
  - ffmpeg + `Runtime.getRuntime().exec(cmd);`

3. Spring Boot + Spring Mvc 提供http服务接口


**本篇重点**

使用ffmpeg提供音频转码的服务接口

## 准备

### 1. ffmpeg 安装

#### 安装脚本如下

```sh
#!/bin/bash

## download ffmpge cmd
wget https://johnvansickle.com/ffmpeg/releases/ffmpeg-release-64bit-static.tar.xz


## exact package
xz -d ffmpeg-release-64bit-static.tar.xz
tar -xvf ffmpeg-release-64bit-static.tar
mv ffmpeg-release-64bit-static ffmpeg
cd ffmpeg
```

#### 测试

进入下载的目录，内部有一个 `ffmpeg` 的可执行文件，主要利用它来实现音频转码


`./ffmpeg -version` 查看ffmpeg的版本


**转码测试**

先准备一个测试文件 test.amr (不要直接从微信的文件夹中获取语音文件，微信做过处理，非标准的amr文件，如果手头没有，可以使用这个测试 [amrTestAudio.amr](http://git.oschina.net/liuyueyi/quicksilver/attach_files/download?i=86632&u=http%3A%2F%2Ffiles.git.oschina.net%2Fgroup1%2FM00%2F01%2F80%2FPaAvDFll2yKAZxSkAACkQOHKtLM001.amr%3Ftoken%3D409c7d98faa7a3ace3043f1e78c123b8%26ts%3D1499847458%26attname%3DamrTestAudio.amr) )

转码命令

```
./ffmpeg -i test.amr test.mp3
```

然后可以看到新增一个mp3文件，然后用播放器，打开确认是否有问题

### 2. 工程搭建

使用Spring-Boot 搭建一个Web工程

直接用官网的创建方式即可，这里不做叙述

### 3. 编码实现
> java利用命令行操作方式调用ffmpeg，实现音频转码，一个最简单的实现如下


```java
// cmd 为待执行的命令行
String cmd = "ffmpeg -i src.amr test.mp3";
Process process = Runtime.getRuntime().exec(cmd);
process.waitFor();
```

就这样就可以了么? 显然并没有这么简陋，先谈谈直接这么用有什么问题

- 扩展性，不好
- 命令行的输出流，异常流没有处理
- 对调用者而言不够友好
- 上面只适用于本地音频转码，如果是对远程的音频，数据流格式的音频就不怎么方便了

出于以上几点，着手实现我们的目标，先看最后的测试case:


```java
@Test
public void testAudioParse() {
    String[] arys = new String[]{
            "test.amr",
            "/Users/yihui/GitHub/quick-media/common/src/test/resources/test.amr",
            "http://s11.mogucdn.com/mlcdn/c45406/170713_3g25ec8fak8jch5349jd2dcafh61c.amr"
    };

    for (String src : arys) {
        try {
            String output = AudioWrapper.of(src)
                    .setOutputType("mp3")
                    .asFile();
            System.out.println(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

从使用的角度来看就很是简洁了,输出结果如下

```bash
/Users/yihui/GitHub/quick-media/common/target/test-classes/test_out.mp3
/Users/yihui/GitHub/quick-media/common/src/test/resources/test_out.mp3
/tmp/audio/170713_3g25ec8fak8jch5349jd2dcafh61c_out.mp3
```

## 实现

前面准备做好，测试的case也提前放出，那么可以看下如何实现了

### 配置类 `AudioOptions`
> 保存最终命令的配置相关信息，用于生成最终的执行命令行

对于音频转码，最终的cmd命令应该是: `ffmpeg -i source.amr output.mp3`，因此我们需要的参数有

- 源文件 source.mar
- 输出文件 output.mp3
- 执行命令 ffmpeg 
- 可选参数 （ffmpeg带的一些参数）

```java
public class AudioOptions {

    private String cmd = "ffmpeg -i ";

    private String src;


    private String dest;


    private Map<String, Object> options = new HashMap<>();


    public String getCmd() {
        return cmd;
    }

    public AudioOptions setCmd(String cmd) {
        this.cmd = cmd;
        return this;
    }

    public String getSrc() {
        return src;
    }

    public AudioOptions setSrc(String src) {
        this.src = src;
        return this;
    }

    public String getDest() {
        return dest;
    }

    public AudioOptions setDest(String dest) {
        this.dest = dest;
        return this;
    }

    public Map<String, Object> getOptions() {
        return options;
    }


    public AudioOptions addOption(String conf, Object value) {
        options.put("-" + conf, value);
        return this;
    }



    public String build() {
        StringBuilder builder = new StringBuilder(this.cmd);
        builder.append(" ").append(this.src);

        for (Map.Entry<String, Object> entry : options.entrySet()) {
            builder.append(entry.getKey().startsWith("-") ? " " : " -")
                    .append(entry.getKey())
                    .append(" ").append(entry.getValue());
        }

        builder.append(" ").append(this.dest);
        return builder.toString();
    }
}
```


### Audio处理封装类 `AudioWrapper`
> 对外暴露的接口，所有音频相关的操作都通过它来执行，正如上面的测试用例

1. 对输入源，我们预留三种调用方式

  - 传入path路径（相对路径，绝对路径，网络路径）
  - URI 方式 （即传入网络链接方式，等同于上面的网络路径方式）
  - InputStream （文件输入流）

2. 命令行调用，通常可选参数比较多，所以我们采用Builder模式来做参数的设置

3. 源码如下  

```java
@Slf4j
public class AudioWrapper {

    public static Builder<String> of(String str) {
        Builder<String> builder = new Builder<>();
        return builder.setSource(str);
    }


    public static Builder<URI> of(URI uri) {
        Builder<URI> builder = new Builder<>();
        return builder.setSource(uri);
    }


    public static Builder<InputStream> of(InputStream inputStream) {
        Builder<InputStream> builder = new Builder<>();
        return builder.setSource(inputStream);
    }


    private static void checkNotNull(Object obj, String msg) {
        if (obj == null) {
            throw new IllegalStateException(msg);
        }
    }

    private static boolean run(String cmd) {
        try {
            return ProcessUtil.instance().process(cmd);
        } catch (Exception e) {
            log.error("operate audio error! cmd: {}, e: {}", cmd, e);
            return false;
        }
    }


    public static class Builder<T> {
        /**
         * 输入源
         */
        private T source;


        /**
         * 源音频格式
         */
        private String inputType;


        /**
         * 输出音频格式
         */
        private String outputType;


        /**
         * 命令行参数
         */
        private Map<String, Object> options = new HashMap<>();


        /**
         * 临时文件信息
         */
        private FileUtil.FileInfo tempFileInfo;


        private String tempOutputFile;


        public Builder<T> setSource(T source) {
            this.source = source;
            return this;
        }

        public Builder<T> setInputType(String inputType) {
            this.inputType = inputType;
            return this;
        }

        public Builder<T> setOutputType(String outputType) {
            this.outputType = outputType;
            return this;
        }

        public Builder<T> addOption(String conf, Object val) {
            this.options.put(conf, val);
            return this;
        }


        private String builder() throws Exception {

            checkNotNull(source, "src file should not be null!");

            checkNotNull(outputType, "output Audio type should not be null!");


            tempFileInfo = FileUtil.saveFile(source, inputType);

            tempOutputFile = tempFileInfo.getPath() + "/" + tempFileInfo.getFilename() + "_out." + outputType;

            return new AudioOptions().setSrc(tempFileInfo.getAbsFile())
                    .setDest(tempOutputFile)
                    .addOption("y", "") // 覆盖写
                    .addOption("write_xing", 0) // 解决mac/ios 显示音频时间不对的问题
                    .addOption("loglevel", "quiet") // 不输出日志
                    .build();
        }


        public InputStream asStream() throws Exception {
            String output = asFile();

            if (output == null) {
                return null;
            }


            return new FileInputStream(new File(output));
        }


        public String asFile() throws Exception {
            String cmd = builder();
            return !run(cmd) ? null : tempOutputFile;
        }
    }

}
```


上面的逻辑还是比较清晰的，但是有几个地方需要注意

- 保存源文件到指定目录下 `tempFileInfo = FileUtil.saveFile(source, inputType);`
- 执行命令的生成 : 
    ```java
    new AudioOptions().setSrc(tempFileInfo.getAbsFile())
                  .setDest(tempOutputFile)
                  .addOption("y", "") // 覆盖写
                  .addOption("write_xing", 0) // 解决mac/ios 显示音频时间不对的问题
                  .addOption("loglevel", "quiet") // 不输出日志
                  .build();
    ```
- java执行cmd命令  `private static boolean run(String cmd)`


### 文件保存 `FileUtil`
> 这个工具类的目的比较清晰, 将源文件保存到指定的临时目录下，根据我们支持的三种方式，进行区分处理


我们定义一个数据结构 FileInfo 保存文件名相关信息

```java
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public static class FileInfo {
    /**
     * 文件所在的目录
     */
    private String path;


    /**
     * 文件名 （不包含后缀）
     */
    private String filename;


    /**
     * 文件类型
     */
    private String fileType;


    public String getAbsFile() {
        return path  + "/" + filename + "." + fileType;
    }
}
```

根据输入，选择不同的实现方式保存，并返回文件信息

```java
public static <T> FileInfo saveFile(T src, String inputType) throws Exception {
        if (src instanceof String) { // 给的文件路径，区分三中，本地绝对路径，相对路径，网络地址
            return saveFileByPath((String) src);
        } else if (src instanceof URI) { // 网络资源文件时，需要下载到本地临时目录下
            return saveFileByURI((URI) src);
        } else if (src instanceof InputStream) { // 输入流保存在到临时目录
            return saveFileByStream((InputStream) src, inputType);
        } else {
            throw new IllegalStateException("save file parameter only support String/URI/InputStream type! but input type is: " + (src == null ? null : src.getClass()));
        }
    }

```

#### 1. 输入源为String时

三种路径的区分，对于http的格式，直接走URI输入源的方式

相对路径时，需要优先获取文件的绝对路径

```java
/**
 * 根据path路径 生成源文件信息
 *
 * @param path
 * @return
 * @throws Exception
 */
private static FileInfo saveFileByPath(String path) throws Exception {
    if (path.startsWith("http")) {
        return saveFileByURI(URI.create(path));
    }


    String tmpAbsFile;
    if (path.startsWith("/")) { // 绝对路径
        tmpAbsFile = path;
    } else { // 相对路径转绝对路径
        tmpAbsFile = FileUtil.class.getClassLoader().getResource(path).getFile();
    }

    // 根据绝对路径，解析 目录 + 文件名  + 文件后缀
    return parseAbsFileToFileInfo(tmpAbsFile);
}

/**
 * 根据绝对路径解析出 目录 + 文件名 + 文件后缀
 *
 * @param absFile 全路径文件名
 * @return
 */
public static FileInfo parseAbsFileToFileInfo(String absFile) {
    FileInfo fileInfo = new FileInfo();
    extraFilePath(absFile, fileInfo);
    extraFileName(fileInfo.getFilename(), fileInfo);
    return fileInfo;
}


/**
 * 根据绝对路径解析 目录 + 文件名（带后缀）
 *
 * @param absFilename
 * @param fileInfo
 */
private static void extraFilePath(String absFilename, FileInfo fileInfo) {
    int index = absFilename.lastIndexOf("/");
    if (index < 0) {
        fileInfo.setPath(TEMP_PATH);
        fileInfo.setFilename(absFilename);
    } else {
        fileInfo.setPath(absFilename.substring(0, index));
        fileInfo.setFilename(index + 1 == absFilename.length() ? "" : absFilename.substring(index + 1));
    }
}


/**
 * 根据带后缀文件名解析 文件名 + 后缀
 *
 * @param fileName
 * @param fileInfo
 */
private static void extraFileName(String fileName, FileInfo fileInfo) {
    int index = fileName.lastIndexOf(".");
    if (index < 0) {
        fileInfo.setFilename(fileName);
        fileInfo.setFileType("");
    } else {
        fileInfo.setFilename(fileName.substring(0, index));
        fileInfo.setFileType(index + 1 == fileName.length() ? "" : fileName.substring(index + 1));
    }
}
```

#### 2. 输入源为URI时
> 网络资源，需要先把文件下载过来，所以就需要一个下载的工具类

一个非常初级的下载工具类： `HttpUtil.java`

```java
@Slf4j
public class HttpUtil {


    public static InputStream downFile(String src) throws IOException {
        return downFile(URI.create(src));
    }

    /**
     * 从网络上下载文件
     *
     * @param uri
     * @return
     * @throws IOException
     */
    public static InputStream downFile(URI uri) throws IOException {
        HttpResponse httpResponse;
        try {
            Request request = Request.Get(uri);
            HttpHost httpHost = URIUtils.extractHost(uri);
            if (StringUtils.isNotEmpty(httpHost.getHostName())) {
                request.setHeader("Host", httpHost.getHostName());
            }
            request.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

            httpResponse = request.execute().returnResponse();
        } catch (Exception e) {
            log.error("远程请求失败，url=" + uri, e);
            throw new FileNotFoundException();
        }

        int code = httpResponse.getStatusLine().getStatusCode();
        if (code != 200) {
            throw new FileNotFoundException();
        }

        return httpResponse.getEntity().getContent();
    }
}
```

具体的保存代码，比较简单，从网络上下载的InputStream直接转换第三种使用方式即可

```java
/**
 * 下载远程文件， 保存到临时目录, 病生成文件信息
 *
 * @param uri
 * @return
 * @throws Exception
 */
private static FileInfo saveFileByURI(URI uri) throws Exception {
    String path = uri.getPath();
    if (path.endsWith("/")) {
        throw new IllegalArgumentException("a select uri should be choosed! but input path is: " + path);
    }

    int index = path.lastIndexOf("/");
    String filename = path.substring(index + 1);

    FileInfo fileInfo = new FileInfo();
    extraFileName(filename, fileInfo);
    fileInfo.setPath(TEMP_PATH);

    try {
        InputStream inputStream = HttpUtil.downFile(uri);
        return saveFileByStream(inputStream, fileInfo);

    } catch (Exception e) {
        log.error("down file from url: {} error! e: {}", uri, e);
        throw e;
    }
}
```


#### 3. 输入源为InpuStream时
> 将输入流保存到文件

这是一个比较基础的功能了，但真正的实现起来，就没有那么顺畅了，需要注意一下几点

- 确保临时文件所在的目录存在
- 输入输出流的关闭，输出流的`flush()`方法不要忘记
- 保存的临时文件名为： 时间戳 + `[0-1000)随机数`
- 输出文件名为输入文件名的基础上加 + `"_out.输出格式"`

```java
public static FileInfo saveFileByStream(InputStream inputStream, String fileType) throws Exception {
    // 临时文件生成规则  当前时间戳 + 随机数 + 后缀
    return saveFileByStream(inputStream, TEMP_PATH, genTempFileName(), fileType);
}


/**
 * 将字节流保存到文件中
 *
 * @param stream
 * @param filename
 * @return
 */
public static FileInfo saveFileByStream(InputStream stream, String path, String filename, String fileType) throws FileNotFoundException {
    return saveFileByStream(stream, new FileInfo(path, filename, fileType));
}


public static FileInfo saveFileByStream(InputStream stream, FileInfo fileInfo) throws FileNotFoundException {
    if (!StringUtils.isBlank(fileInfo.getPath())) {
        mkDir(new File(fileInfo.getPath()));
    }

    String tempAbsFile = fileInfo.getPath() + "/" + fileInfo.getFilename() + "." + fileInfo.getFileType();
    BufferedOutputStream outputStream = null;
    InputStream inputStream = null;
    try {
        inputStream = new BufferedInputStream(stream);
        outputStream = new BufferedOutputStream(new FileOutputStream(tempAbsFile));
        int len = inputStream.available();
        //判断长度是否大于4k
        if (len <= 4096) {
            byte[] bytes = new byte[len];
            inputStream.read(bytes);
            outputStream.write(bytes);
        } else {
            int byteCount = 0;
            //1M逐个读取
            byte[] bytes = new byte[4096];
            while ((byteCount = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, byteCount);
            }
        }

        return fileInfo;
    } catch (Exception e) {
        log.error("save stream into file error! filename: {} e: {}", tempAbsFile, e);
        return null;
    } finally {
        try {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            log.error("close stream error! e: {}", e);
        }
    }
}


/**
 * 临时文件名生成： 时间戳 + 0-1000随机数
 *
 * @return
 */
private static String genTempFileName() {
    return System.currentTimeMillis() + "_" + ((int) (Math.random() * 1000));
}


/**
 * 递归创建文件夹
 *
 * @param file 由目录创建的file对象
 * @throws FileNotFoundException
 */
public static void mkDir(File file) throws FileNotFoundException {
    if (file.getParentFile().exists()) {
        if (!file.exists() && !file.mkdir()) {
            throw new FileNotFoundException();
        }
    } else {
        mkDir(file.getParentFile());
        if (!file.exists() && !file.mkdir()) {
            throw new FileNotFoundException();
        }
    }
}
```

#### 命令行执行封装工具类 `ProcessUtil`
> 这个就是将最上面的三行代码封装的工具类，基本上快两百行...

源码先贴出

```java
@Slf4j
public class ProcessUtil {

    /**
     * Buffer size of process input-stream (used for reading the
     * output (sic!) of the process). Currently 64KB.
     */
    public static final int BUFFER_SIZE = 65536;

    public static final int EXEC_TIME_OUT = 2;


    private ExecutorService exec;

    private ProcessUtil() {
        exec = new ThreadPoolExecutor(6,
                12,
                1,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(10),
                new CustomThreadFactory("cmd-process"),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }


    public static ProcessUtil instance() {
        return InputStreamConsumer.instance;
    }


    /**
     * 简单的封装， 执行cmd命令
     *
     * @param cmd 待执行的操作命令
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean process(String cmd) throws Exception {
        Process process = Runtime.getRuntime().exec(cmd);
        waitForProcess(process);
        return true;
    }

    /**
     * Perform process input/output and wait for process to terminate.
     *
     * 源码参考 im4java 的实现修改而来
     *
     */

    private int waitForProcess(final Process pProcess)
            throws IOException, InterruptedException, TimeoutException, ExecutionException {
        // Process stdout and stderr of subprocess in parallel.
        // This prevents deadlock under Windows, if there is a lot of
        // stderr-output (e.g. from ghostscript called by convert)
        FutureTask<Object> outTask = new FutureTask<Object>(() -> {
            processOutput(pProcess.getInputStream(), InputStreamConsumer.DEFAULT_CONSUMER);
            return null;
        });
        exec.submit(outTask);


        FutureTask<Object> errTask = new FutureTask<Object>(() -> {
            processError(pProcess.getErrorStream(), InputStreamConsumer.DEFAULT_CONSUMER);
            return null;
        });
        exec.submit(errTask);


        // Wait and check IO exceptions (FutureTask.get() blocks).
        try {
            outTask.get();
            errTask.get();
        } catch (ExecutionException e) {
            Throwable t = e.getCause();

            if (t instanceof IOException) {
                throw (IOException) t;
            } else if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new IllegalStateException(e);
            }
        }

        FutureTask<Integer> processTask = new FutureTask<Integer>(() -> {
            pProcess.waitFor();
            return pProcess.exitValue();
        });
        exec.submit(processTask);


        // 设置超时时间，防止死等
        int rc = processTask.get(EXEC_TIME_OUT, TimeUnit.SECONDS);


        // just to be on the safe side
        try {
            pProcess.getInputStream().close();
            pProcess.getOutputStream().close();
            pProcess.getErrorStream().close();
        } catch (Exception e) {
            log.error("close stream error! e: {}", e);
        }

        return rc;
    }


    //////////////////////////////////////////////////////////////////////////////

    /**
     * Let the OutputConsumer process the output of the command.
     * <p>
     * 方便后续对输出流的扩展
     */

    private void processOutput(InputStream pInputStream,
                               InputStreamConsumer pConsumer) throws IOException {
        pConsumer.consume(pInputStream);
    }

    //////////////////////////////////////////////////////////////////////////////

    /**
     * Let the ErrorConsumer process the stderr-stream.
     * <p>
     * 方便对后续异常流的处理
     */

    private void processError(InputStream pInputStream,
                              InputStreamConsumer pConsumer) throws IOException {
        pConsumer.consume(pInputStream);
    }


    private static class InputStreamConsumer {
        static ProcessUtil instance = new ProcessUtil();

        static InputStreamConsumer DEFAULT_CONSUMER = new InputStreamConsumer();

        void consume(InputStream stream) throws IOException {
            StringBuilder builder = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream), BUFFER_SIZE);

            String temp;
            while ((temp = reader.readLine()) != null) {
                builder.append(temp);
            }


            if (log.isDebugEnabled()) {
                log.debug("cmd process input stream: {}", builder.toString());
            }
            reader.close();
        }
    }


    private static class CustomThreadFactory implements ThreadFactory {

        private String name;

        private AtomicInteger count = new AtomicInteger(0);

        public CustomThreadFactory(String name) {
            this.name = name;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, name + "-" + count.addAndGet(1));
        }
    }

}
```

**说明**

- 内部类方式的单例模式
- 线程池，开独立的线程来处理命令行的输出流、异常流
  - 如果不清空这两个流，可能直接导致rt随着并发数的增加而线性增加
- 独立的线程执行命令行操作，支持超时设置
  - 超时设置，确保服务不会挂住
  - 异步执行命令行操作，可以并发执行后续的步骤


## 填坑之旅

上面实现了一个较好用的封装类，但是在实际的开发过程中，有些问题有必要单独的拎出来说一说

### 1. `-y` 参数
> 覆盖写，如果输出的文件名对应的文件已经存在，这个参数就表示使用新的文件覆盖老的

在控制台执行转码时，会发现这种场景会要求用户输入一个y/n来表是否继续转码，所以在代码中，如果不加上这个参数，将一直得不到执行


### 2. mac/ios 的音频长度与实际不符合

将 amr 音频转换 mp3 格式音频，如果直接使用命令`ffmpeg -i test.amr -y out.mp3`

会发现输出的音频时间长度比实际的小，但是在播放的时候又是没有问题的；测试在mac和iphone会有这个问题

解决方案，加一个参数  `write_xing 0`


### 3. 并发访问时，RT线性增加

执行命令： `ffmpeg -i song.ogg -y -write_xing 0 song.mp3`

当我们没有手动清空输出流，异常流时，会发现并发请求量越高，rt越高

主要原因是输出信息 & 异常信息没有被消费，而缓存这些数据的空间是有限制的，因此上面我们的`ProcessUtil`类中，有两个任务来处理输出流和异常流

还有一种方法就是加一个参数

`ffmpeg -i song.ogg -y -write_xing 0 song.mp3 -loglevel quiet`


## 其他

项目源码: [https://github.com/liuyueyi/quick-media](https://github.com/liuyueyi/quick-media)

公众号获取更多: ![https://static.oschina.net/uploads/img/201707/09205944_0PzS.jpg](https://static.oschina.net/uploads/img/201707/09205944_0PzS.jpg)

