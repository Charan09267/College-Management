package com.CollegeHub.student.repository;

import com.CollegeHub.admin.entity.ProfileStatus;
import com.CollegeHub.student.entity.StudentInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudentInfoRepoCustomImpl
        implements StudentInfoRepoCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<StudentInfo> findMatchingStudents(
            String branch,
            Integer year,
            String section,
            String batch
    ) {

        Query query = new Query();

        // Only active student profiles
        query.addCriteria(
                Criteria.where("profileStatus").is(ProfileStatus.ACTIVE)
        );

        // Add filters only when supplied
        if (branch != null && !branch.isBlank()) {
            query.addCriteria(
                    Criteria.where("branch").is(branch)
            );
        }

        if (year != null) {
            query.addCriteria(
                    Criteria.where("year").is(year)
            );
        }

        if (section != null && !section.isBlank()) {
            query.addCriteria(
                    Criteria.where("section").is(section)
            );
        }

        if (batch != null && !batch.isBlank()) {
            query.addCriteria(
                    Criteria.where("batch").is(batch)
            );
        }

        return mongoTemplate.find(query, StudentInfo.class);
    }
}
