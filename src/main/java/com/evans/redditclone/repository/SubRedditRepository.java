package com.evans.redditclone.repository;

import com.evans.redditclone.model.SubReddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubRedditRepository extends JpaRepository<SubReddit, Long> {
}
