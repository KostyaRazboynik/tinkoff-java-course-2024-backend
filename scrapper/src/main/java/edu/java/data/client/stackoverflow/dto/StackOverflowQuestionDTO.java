package edu.java.data.client.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StackOverflowQuestionDTO {
    @JsonProperty("items")
    public List<QuestionResponse> questions;

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class QuestionResponse {
        public String owner;
        @JsonProperty("title")
        public String title;
        @JsonProperty("last_activity_date")
        public OffsetDateTime updatedAt;

        @JsonProperty("owner")
        public void setOwner(Map<String, String> owner) {
            this.owner = owner.get("display_name");
        }
    }
}
