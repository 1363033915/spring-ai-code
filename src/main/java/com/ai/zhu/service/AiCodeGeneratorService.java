package com.ai.zhu.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * <br/>
 * Created by cl_zhu on 2025/10/29
 */

@Service
public class AiCodeGeneratorService {

    private final ChatClient chatClient;

    @Value("classpath:prompt/codegen-html-system-prompt.txt")
    private Resource htmlSystemPromptResource;

    @Value("classpath:prompt/codegen-multi-file-system-prompt.txt")
    private Resource multiFileSystemPromptResource;

    @Value("classpath:prompt/codegen-vue-project-system-prompt.txt")
    private Resource vueProjectSystemPromptResource;


    public AiCodeGeneratorService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * 生成 HTML 代码
     */
    public HtmlCodeResult generateHtmlCode(String userMessage) {
        // 读取系统提示词
        SystemMessage systemMessage = loadSystemMessage(htmlSystemPromptResource);


        String systemPrompt = """
                你是一个 HTML 代码生成助手。
                请严格返回 html 格式： 完整HTML代码
                不要包含任何额外的文本、解释或代码块标记。
                """;

        UserMessage userMsg = new UserMessage(userMessage);

        Prompt prompt = new Prompt(List.of(
                systemMessage,
                new SystemMessage(systemPrompt),
                userMsg
        ));

        ChatResponse response = chatClient.prompt(prompt).call().chatResponse();

        String htmlCode = response.getResult().getOutput().getText();

        // 解析 AI 响应，这里假设 AI 返回 JSON 格式
        // 实际使用时可能需要更复杂的解析逻辑
        return parseHtmlResponse(htmlCode);
    }

    /**
     * 生成 HTML 代码（流式）
     */
    public Flux<String> generateHtmlCodeStream(String userMessage) {
        SystemMessage systemMessage = loadSystemMessage(htmlSystemPromptResource);
        UserMessage userMsg = new UserMessage(userMessage);

        Prompt prompt = new Prompt(List.of(systemMessage, userMsg));

        return chatClient.prompt(prompt).stream().content();
    }

    /**
     * 生成 Vue 项目代码（流式）
     */
    public Flux<String> generateVueProjectCodeStream(long appId, String userMessage) {
        SystemMessage systemMessage = loadSystemMessage(vueProjectSystemPromptResource);
        UserMessage userMsg = new UserMessage(userMessage);

        Prompt prompt = new Prompt(List.of(systemMessage, userMsg));

        return chatClient.prompt(prompt).stream().content();

    }

    /**
     * 加载系统提示词
     */
    private SystemMessage loadSystemMessage(Resource promptResource) {
        try {
            String promptText = new String(promptResource.getInputStream().readAllBytes());
            return new SystemMessage(promptText);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load system prompt", e);
        }
    }

    /**
     * 解析 HTML 响应
     */
    private HtmlCodeResult parseHtmlResponse(String htmlCode) {
        // 这里需要根据你的 AI 响应格式来实现解析逻辑
        // 假设 AI 返回 JSON 格式：{"htmlCode": "...", "description": "..."}
        try {
            // 使用 Jackson 或其他 JSON 库解析
            // 这里简化处理
            return new HtmlCodeResult(htmlCode, "Generated HTML code");
        } catch (Exception e) {
            // 如果解析失败，返回默认结果
            return new HtmlCodeResult(htmlCode, "Generated HTML code");
        }
    }

}
