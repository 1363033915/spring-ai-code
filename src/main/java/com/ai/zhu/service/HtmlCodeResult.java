package com.ai.zhu.service;

import lombok.Data;

/**
 * <br/>
 * Created by cl_zhu on 2025/10/29
 */
@Data
public class HtmlCodeResult {

    private String htmlCode;
    private String description;

    public HtmlCodeResult() {}

    public HtmlCodeResult(String htmlCode, String description) {
        this.htmlCode = htmlCode;
        this.description = description;
    }
}
