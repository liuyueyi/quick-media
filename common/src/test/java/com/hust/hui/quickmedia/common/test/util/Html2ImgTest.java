package com.hust.hui.quickmedia.common.test.util;


import com.github.jarlakxen.embedphantomjs.PhantomJSReference;
import com.github.jarlakxen.embedphantomjs.executor.PhantomJSConsoleExecutor;

import java.util.concurrent.ExecutionException;

/**
 * Created by yihui on 2017/9/3.
 */
public class Html2ImgTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        PhantomJSConsoleExecutor ex = new PhantomJSConsoleExecutor(PhantomJSReference.create().build());
        String output = ex.execute("console.log('TEST1');phantom.exit();").get();
        System.out.println(output);  // This prints "TEST1"
    }

}
