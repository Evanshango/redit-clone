package com.evans.redditclone.repository;

import com.evans.redditclone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
