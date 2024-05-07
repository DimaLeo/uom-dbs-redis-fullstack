package org.mai24028.redis_assignment_uom.repositories;

import org.mai24028.redis_assignment_uom.models.RecordItem;
import org.mai24028.redis_assignment_uom.models.UserEntries;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class RecordsRepository {

    private static final String RECORDS_KEY_PREFIX = "records";
    private static final String QUERIES_KEY_PREFIX = "queries";
    private final RedisTemplate<String, String> redisTemplate;

    public RecordsRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addRecord(String username, String recordName) throws Exception {
        String key = buildRecord(username);

        if(Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, recordName))){
            throw new Exception("Record already exists");
        }

        redisTemplate.opsForSet().add(key, recordName);
        redisTemplate.opsForValue().set(buildQuery(recordName), String.valueOf(0));
    }



    public List<RecordItem> findRecordsByUsername(String username) {

        Set<String> recordNames = redisTemplate.opsForSet().members(buildRecord(username));
        return recordNames.stream()
                .map(recordName -> {
                    String queryValue = redisTemplate.opsForValue().get(buildQuery(recordName));
                    if (queryValue != null) {
                        return new RecordItem(recordName, Integer.parseInt(queryValue));
                    } else {
                        // Handle the case where queryValue is null, depending on your requirements.
                        // For now, returning null, but you might want to handle it differently.
                        return null;
                    }
                })
                .filter(Objects::nonNull) // Filter out null values
                .collect(Collectors.toList());
    }

    public String findRecordByRecordName(String recordName) {

        Set<String> keys = redisTemplate.keys("records:*");

        for (String key : keys) {
            if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, recordName))) {
                Integer counter = Integer.parseInt(redisTemplate.opsForValue().get(buildQuery(recordName)));
                redisTemplate.opsForValue().set(buildQuery(recordName), String.valueOf(counter+1));
                return key.split(":")[1];
            }
        }

        return null;
    }

    public List<UserEntries> getEntriesPerUser() {

        Set<String> keys = redisTemplate.keys("records:*");

        return keys.stream()
                .map(key -> {
                    String username = key.split(":")[1];
                    Integer entries = findRecordsByUsername(username).size();
                    return new UserEntries(username, entries);
                })
                .collect(Collectors.toList());

    }

    public String getAverageQueries() {
        Set<String> keys = redisTemplate.keys("queries:*");

        int sum = 0;

        for (String key : keys) {
            String value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                sum += Integer.parseInt(value);
            }
        }


        System.out.println(sum);
        System.out.println(keys.size());

        if(!keys.isEmpty() && sum>0){
            return String.valueOf(new DecimalFormat("#.##").format((double) sum /keys.size()));
        }
        else return "0";

    }

    private String buildRecord(String username) {
        return RECORDS_KEY_PREFIX + ":" + username;
    }

    private String buildQuery(String recordName) {
        return QUERIES_KEY_PREFIX + ":" + recordName;
    }

}
