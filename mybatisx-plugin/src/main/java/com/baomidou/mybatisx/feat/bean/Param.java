package com.baomidou.mybatisx.feat.bean;

import com.baomidou.mybatisx.model.ParamDataType;

import java.util.regex.Pattern;

public class Param {
    static String L_BRACKET = "(";
    static String R_BRACKET = ")";
    static Pattern END_WITH_PAREN = Pattern.compile("\\((.*?)\\)$");
    private final String value;
    private final ParamDataType type;

    public Param(String value, ParamDataType type) {
        this.value = value;
        this.type = type;
    }

    public static Param of(String param) {
        if (!END_WITH_PAREN.matcher(param).find()) {
            return new Param(param.trim(), ParamDataType.STRING);
        }
        String value = param.substring(0, param.indexOf(L_BRACKET)).trim();
        ParamDataType type = ParamDataType.UNKNOWN;
        try {
            type = ParamDataType.valueOf(param.substring(param.indexOf(L_BRACKET) + 1, param.indexOf(R_BRACKET)).toUpperCase());
        } catch (IllegalArgumentException ignored) {

        }
        return new Param(value, type);
    }

    public String getValue() {
        return type.decorate(value);
    }
}
