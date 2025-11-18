package com.ai.zhu.service;

import dev.langchain4j.service.SystemMessage;
import reactor.core.publisher.Flux;

/**
 * <br/>
 * Created by cl_zhu on 2025/10/30
 */
public interface AiLangChainGeneratorService {

    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    HtmlCodeResult generateHtmlCode(String userMessage);

    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    Flux<String> generateHtmlCodeStream(String userMessage);

}
