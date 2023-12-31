package com.example.walkingmate_back.user.entity;

import com.example.walkingmate_back.board.entity.Board;
import com.example.walkingmate_back.board.entity.BoardComment;
import com.example.walkingmate_back.board.entity.Recommend;
import com.example.walkingmate_back.board.entity.RecommendComment;
import com.example.walkingmate_back.history.entity.BuyHistory;
import com.example.walkingmate_back.history.entity.CheckList;
import com.example.walkingmate_back.history.entity.RunRecord;
import com.example.walkingmate_back.team.entity.TeamMember;
import com.example.walkingmate_back.user.dto.UserUpdateDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
//@ToString  // 무한 루프 발생해서 주석처리함. <= 2023-09-15 이수 작성함.
@Builder
//@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@Slf4j
public class UserEntity {

    public UserEntity() {
        log.info("현재 UserEntity 건드림");
    }
    @Id
    @Column
    private String id; // 사용자 id

    @Column
    private String pw; // 사용자 pw

    @Column
    String name; // 사용자 이름

    @Column
    String phone; // 사용자 전화번호

    @Column
    LocalDate birth; // 사용자 생년월일

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserRank userRank;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserBody userBody;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TeamMember> teamMembers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Board> boards;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardComment> boardComments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Recommend> recommends;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RecommendComment> recommendComments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BuyHistory> buyHistorys;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CheckList> checkLists;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RunRecord> runRecords;

    public void update(UserUpdateDTO userUpdateDTO, LocalDate date) {
        this.birth=date;
        this.phone=userUpdateDTO.getPhone();
        this.name=userUpdateDTO.getName();
    }
}
