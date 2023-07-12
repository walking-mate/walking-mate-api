package com.example.walkingmate_back.history.dto;

import lombok.Data;
import java.util.Date;

@Data
public class RunRecordRequestDTO {

    private String userId;  // 사용자 id

    private String date;  // 러닝 날짜

    private int step;  // 걸음 수

    private double distance;  // 거리


}