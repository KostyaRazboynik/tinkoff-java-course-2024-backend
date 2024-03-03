package edu.java.bot.client.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiErrorResponse {
    public String description;
    public String code;
    public String exceptionName;
    public String exceptionMessage;
    public List<String> stacktrace;
}
