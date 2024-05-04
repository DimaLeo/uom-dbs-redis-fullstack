package org.mai24028.redis_assignment_uom.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserEntries {
    private String username;
    private Integer entries;
}
