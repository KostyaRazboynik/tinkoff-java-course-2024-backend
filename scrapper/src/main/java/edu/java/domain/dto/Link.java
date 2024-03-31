package edu.java.domain.dto;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class Link {
    public String link;
    public int type;
    public OffsetDateTime checkedDate;
}
