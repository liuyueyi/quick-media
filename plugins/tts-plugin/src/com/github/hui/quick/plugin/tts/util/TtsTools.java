package com.github.hui.quick.plugin.tts.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author zh-hq
 */
public class TtsTools {

    public static final Pattern NO_VOICE_PATTERN = Pattern.compile("[\\s\\p{C}\\p{P}\\p{Z}\\p{S}]");
    public static final String SDF = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z";
    public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    public static Logger log = LoggerFactory.getLogger(TtsTools.class);
    private static OkHttpClient client = new OkHttpClient();

    public static String httpGet(String url) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            log.info("response.toString():{}", response.toString());
            log.info("response.isSuccessful():{}", response.isSuccessful());
            if (response.isSuccessful()) {
                String body = response.body().string();
                // log.info("response.body:{}", body);
                return body;
            }
            throw new RuntimeException(String.format("request：%s fail, message:%s", url, response.code()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isNoVoice(CharSequence charSequence) {
        return NO_VOICE_PATTERN.matcher(charSequence).replaceAll("").isEmpty();
    }

    public static void sleep(int second) {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException ignore) {

        }
    }

    /**
     * 获取时间戳
     *
     * @return String time
     */
    public static String date() {
        return new SimpleDateFormat(SDF).format(new Date());
    }

    public static String localDateTime() {
        return LocalDateTime.now().format(DTF);
    }


    public static String localeToEmoji(Locale locale) {
        String countryCode = locale.getCountry();
        if ("TW".equals(countryCode) && Locale.getDefault().getCountry().equals("CN")) {
            return "";
        }
        int firstLetter = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6;
        int secondLetter = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
    }

    public static String getRandomId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
