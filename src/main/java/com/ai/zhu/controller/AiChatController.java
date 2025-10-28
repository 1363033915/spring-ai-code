package com.ai.zhu.controller;

import com.ai.zhu.tools.TimeTools;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
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

    public AiChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/call")
    public String callChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？")String query) {
        return chatClient.prompt(query).tools(new TimeTools()).call().content();
    }


    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？")String query) {
        return chatClient.prompt(query).stream().content();
    }

}
