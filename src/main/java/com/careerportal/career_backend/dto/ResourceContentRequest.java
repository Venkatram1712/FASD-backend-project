package com.careerportal.career_backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ResourceContentRequest {
    @Pattern(regexp = "^(?i)(text|video)?$", message = "Type must be text or video")
    private String type;

    @JsonAlias({"content", "contentText"})
    @Size(max = 5000, message = "Content text cannot exceed 5000 characters")
    private String contentText;

    @JsonAlias({"videoUrl", "url", "link"})
    @Pattern(regexp = "^(https?://.*)?$", message = "Video URL must start with http:// or https://")
    private String videoUrl;

    @AssertTrue(message = "For text type, contentText is required. For video type, videoUrl is required")
    public boolean isValidByType() {
        String normalizedType = type == null ? "text" : type.trim().toLowerCase();

        if ("video".equals(normalizedType)) {
            return videoUrl != null && !videoUrl.trim().isEmpty();
        }

        return contentText != null && !contentText.trim().isEmpty();
    }

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
