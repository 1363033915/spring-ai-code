package com.ai.zhu.service;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * <br/>
 * Created by cl_zhu on 2025/10/29
 */
@Data
@Description("生成 HTML 代码文件的结果")
public class HtmlCodeResult {

    /**
     * HTML 代码
     */
    @Description("HTML代码")
    private String htmlCode;

    /**
     * 描述
     */
    @Description("生成代码的描述")
    private String description;


    public HtmlCodeResult() {}

    public HtmlCodeResult(String htmlCode, String description) {
        this.htmlCode = htmlCode;
        this.description = description;
    }
}
