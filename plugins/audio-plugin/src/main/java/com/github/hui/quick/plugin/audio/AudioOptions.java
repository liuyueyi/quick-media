package com.github.hui.quick.plugin.audio;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yihui on 2017/7/13.
 */
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
