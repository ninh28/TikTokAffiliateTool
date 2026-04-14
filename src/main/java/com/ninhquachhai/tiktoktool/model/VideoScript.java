package com.ninhquachhai.tiktoktool.model;

public record VideoScript(
    String type, 
    String hook, 
    String script, 
    String cta, 
    String videoPath,
    boolean isSynced
) {
    // Constructor cũ để tương thích ngược nếu cần
    public VideoScript(String type, String hook, String script, String cta, String videoPath) {
        this(type, hook, script, cta, videoPath, false);
    }
}
