package com.careerportal.career_backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ResourceContentRequest {
    private String type;

    @JsonAlias({"content", "contentText"})
    private String contentText;

    @JsonAlias({"videoUrl", "url", "link"})
    private String videoUrl;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
