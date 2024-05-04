package org.mai24028.redis_assignment_uom.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@AllArgsConstructor
@RedisHash("users")
public class User implements Serializable {
    private String username;
}
