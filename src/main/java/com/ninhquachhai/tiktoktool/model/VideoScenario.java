package com.ninhquachhai.tiktoktool.model;

public class VideoScenario {
    public final String name;
    public final String prompt;
    public final String hook;
    public final String cta;
    
    public VideoScenario(String name, String prompt, String hook, String cta) {
        this.name = name;
        this.prompt = prompt;
        this.hook = hook;
        this.cta = cta;
    }
}