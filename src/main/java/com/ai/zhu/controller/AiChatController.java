package com.ai.zhu.controller;

import com.ai.zhu.config.Assistant;
import com.ai.zhu.service.AiCodeGeneratorService;
import com.ai.zhu.service.HtmlCodeResult;
import com.ai.zhu.tools.TimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * <br/>
 * Created by cl_zhu on 2025/7/30
 */
@RestController
@RequestMapping("/ai/chat")
public class AiChatController {

    private final ChatClient chatClient;


    @Autowired
    private Assistant assistant;

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
        return assistant.chat(query);
    }

    @GetMapping("/html")
    public HtmlCodeResult generateHtml(@RequestParam(value = "query", defaultValue = "创建一个小网站") String query) {
        return aiCodeGeneratorService.generateHtmlCode(query);
    }


    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？") String query) {
        return chatClient.prompt(query).stream().content();
    }

}
