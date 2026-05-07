package com.health.ai.impl;

import com.health.ai.AIConfig;
import com.health.ai.AIProvider;
import org.springframework.stereotype.Component;

@Component
public class QwenAdapter extends AbstractOpenAICompatibleAdapter {

    private final AIConfig.QwenConfig config;

    public QwenAdapter(AIConfig aiConfig) {
        this.config = aiConfig.getQwen();
    }

    @Override public AIProvider getProvider() { return AIProvider.QWEN; }
    @Override protected String getApiKey() { return config.getApiKey(); }
    @Override protected String getBaseUrl() { return config.getBaseUrl(); }
    @Override protected String getModel() { return config.getModel(); }
    @Override protected double getTemperature() { return config.getTemperature(); }
    @Override
    protected String getSystemPrompt() {
        return "你是一个专业的健康顾问AI助手。请基于提供的用户健康数据，给出个性化、具体、可操作的健康分析和建议。如果数据中存在异常指标，请明确指出并给出改善建议。请用简洁明了的语言回答。";
    }
}
