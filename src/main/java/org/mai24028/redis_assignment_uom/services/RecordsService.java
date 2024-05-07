package org.mai24028.redis_assignment_uom.services;

import org.mai24028.redis_assignment_uom.models.RecordItem;
import org.mai24028.redis_assignment_uom.models.UserEntries;
import org.mai24028.redis_assignment_uom.repositories.RecordsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordsService {

    private final RecordsRepository recordsRepository;

    public RecordsService(RecordsRepository recordsRepository) {
        this.recordsRepository = recordsRepository;
    }


    public void storeRecord(String username, String recordName) throws Exception {
        recordsRepository.addRecord(username, recordName);
    }

    public List<RecordItem> findRecordsByUsername(String username) {
        return recordsRepository.findRecordsByUsername(username);
    }

    public String searchRecord(String recordName) {

        return recordsRepository.findRecordByRecordName(recordName);

    }

    public List<UserEntries> getEntriesPerUser() {
        return recordsRepository.getEntriesPerUser();
    }

    public String getAverageQueries() {
        return recordsRepository.getAverageQueries();
    }
}
