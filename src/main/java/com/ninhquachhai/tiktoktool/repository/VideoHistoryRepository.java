package com.ninhquachhai.tiktoktool.repository;

import com.ninhquachhai.tiktoktool.model.VideoHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoHistoryRepository extends JpaRepository<VideoHistory, Long> {
    List<VideoHistory> findByUserEmailOrderByCreatedAtDesc(String userEmail);
}
