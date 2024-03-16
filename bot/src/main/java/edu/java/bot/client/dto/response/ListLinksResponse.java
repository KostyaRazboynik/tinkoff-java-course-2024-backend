package edu.java.bot.client.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ListLinksResponse {
    public int size;
    public List<LinkResponse> links;
}
