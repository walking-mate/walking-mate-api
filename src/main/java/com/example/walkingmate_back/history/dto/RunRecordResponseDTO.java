package com.example.walkingmate_back.history.dto;

import com.example.walkingmate_back.user.entity.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
public class RunRecordResponseDTO {

    private String userId;  // 사용자 id

    private String date;  // 러닝 날짜

    private int step;  // 걸음 수

    private double distance;  // 거리

    public RunRecordResponseDTO(String userId, String date, int step, double distance, LocalDateTime regTime, LocalDateTime updateTime) {
        this.userId=userId;
        this.date=date;
        this.step=step;
        this.distance=distance;
    }
}
