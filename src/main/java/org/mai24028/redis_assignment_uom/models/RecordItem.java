package org.mai24028.redis_assignment_uom.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;


@Data
@AllArgsConstructor
public class RecordItem implements Serializable {
    private String recordName;
    private Integer recordQueries;
}
