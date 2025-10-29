package com.ai.zhu.config;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

/**
 * <br/>
 * Created by cl_zhu on 2025/10/29
 */
@AiService
public interface Assistant {

    @SystemMessage("You are a polite assistant")
    String chat(String userMessage);
}
