package com.ninhquachhai.tiktoktool.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "video_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;
    private String productName;
    private String videoPath;
    private String brightnessMode;
    private String subtitle;
    
    private LocalDateTime createdAt;
}
