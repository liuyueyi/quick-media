package com.hust.hui.quickmedia.web.entity;

import lombok.*;

/**
 * Created by yihui on 2017/7/13.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Status {

    private int code;

    private String msg;

    public Status(StatusEnum statusEnum) {
        this(statusEnum.getCode(), statusEnum.getMsg());
    }


    public enum StatusEnum {
        /**
         * 成功
         */
        SUCCESS(200, "SUCCESS"),

        /**
         * 失败
         */
        FAIL(500, "FAIL"),


        ILLEGAL_PARAMS(401, "参数错误"),


        // 业务异常
        BIZ_MARK2HTML_ERROR(700, "markdown内容转html失败"),
        BIZ_HTML2IMAGE_ERROR(701, "html转image失败"),;


        private int code;

        private String msg;

        StatusEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }


        public Status getStatus() {
            return new Status(code, msg);
        }
    }

}
