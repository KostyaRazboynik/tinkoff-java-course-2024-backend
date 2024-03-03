package edu.java.data.client.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GithubRepositoryDTO {
    public String owner;
    @JsonProperty("name")
    public String name;
    @JsonProperty("updated_at")
    public OffsetDateTime updatedAt;

    @JsonProperty("owner")
    public void setOwner(Map<String, String> owner) {
        this.owner = owner.get("login");
    }
}
