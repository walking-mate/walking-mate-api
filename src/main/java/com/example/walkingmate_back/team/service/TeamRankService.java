package com.example.walkingmate_back.team.service;

import com.example.walkingmate_back.main.entity.Message;
import com.example.walkingmate_back.main.entity.StatusEnum;
import com.example.walkingmate_back.team.dto.TeamRankResponseDTO;
import com.example.walkingmate_back.team.entity.TeamRank;
import com.example.walkingmate_back.team.repository.TeamRankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 *    팀 전체 랭킹 조회
 *
 *   @version          1.00 / 2023.07.15
 *   @author           전우진
 */

@Service
@RequiredArgsConstructor
@Transactional
public class TeamRankService {

    private final TeamRankRepository teamRankRepository;

    /**
     * 전체 랭킹 조회
     * - 전우진 2023.07.15
     */
    public ResponseEntity<Message> getAllRank() {
        List<TeamRank> teamRanks = teamRankRepository.findAll();
        List<TeamRankResponseDTO> result = new ArrayList<>();

        for(TeamRank teamRank : teamRanks) {
            TeamRankResponseDTO rankResponseDTO = new TeamRankResponseDTO(
                    teamRank.getTeam().getId(),
                    teamRank.getCoin(),
                    teamRank.getTear()
            );
            result.add(rankResponseDTO);
        }
        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");
        message.setData(result);

        return ResponseEntity.ok().body(message);
    }
}