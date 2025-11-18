package com.ai.zhu.controller;

import cn.hutool.json.JSONUtil;
import com.ai.zhu.service.AiCodeGeneratorService;
import com.ai.zhu.service.HtmlCodeResult;
import com.ai.zhu.tools.TimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * <br/>
 * Created by cl_zhu on 2025/7/30
 */
@RestController
@RequestMapping("/ai/chat")
public class AiChatController {

    private final ChatClient chatClient;


    @Autowired
    private AiCodeGeneratorService aiCodeGeneratorService;

    private final MessageWindowChatMemory messageWindowChatMemory;


    public AiChatController(ChatClient.Builder builder) {
        messageWindowChatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(5)
                .build();
        this.chatClient = builder.defaultAdvisors(
                MessageChatMemoryAdvisor.builder(messageWindowChatMemory)
                        .build()
        ).build();
    }


    @GetMapping("/call")
    public String callChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？") String query) {
        return chatClient.prompt(query).tools(new TimeTools()).call().content();
    }

    @GetMapping("/langChan/call")
    public String langChanCallChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？") String query) {
        return "";
    }


    @GetMapping("/html")
    public HtmlCodeResult generateHtml(@RequestParam(value = "query", defaultValue = "创建一个小网站") String query) {
        return aiCodeGeneratorService.generateHtmlCode(query);
    }

    @GetMapping("/langChain/html")
    public HtmlCodeResult generateLangChainHtml(@RequestParam(value = "query", defaultValue = "创建一个小网站") String query) {
        query = "创建一个html代码生成网站" +
                "后端接口 localhost:9999/ai/chat/langChan/stream/html?query=" +
                "接口流式返回Flux<String>";
        return aiCodeGeneratorService.generateHtmlCodeLangChain(query);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/langChan/stream/html", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>>  langChanCallChatStream(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？") String query) {
        Flux<String> contentFlux = aiCodeGeneratorService.generateHtmlCodeLangChainStream(query);
        return contentFlux
                .map(chunk -> {
                    Map<String, String> wrapper = Map.of("d", chunk);
                    String jsonData = JSONUtil.toJsonStr(wrapper);
                    return ServerSentEvent.<String>builder()
                            .data(jsonData)
                            .build();
                })
                .concatWith(Mono.just(
                        // 发送结束事件
                        ServerSentEvent.<String>builder()
                                .event("done")
                                .data("")
                                .build()
                ));
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？") String query) {
        return chatClient.prompt(query).stream().content();
    }

}
