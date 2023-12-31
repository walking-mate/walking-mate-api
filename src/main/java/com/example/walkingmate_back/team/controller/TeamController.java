package com.example.walkingmate_back.team.controller;

import com.example.walkingmate_back.main.response.DefaultRes;
import com.example.walkingmate_back.main.response.ResponseMessage;
import com.example.walkingmate_back.main.response.StatusEnum;
import com.example.walkingmate_back.team.dto.TeamRequestDTO;
import com.example.walkingmate_back.team.dto.TeamResponseDTO;
import com.example.walkingmate_back.team.dto.TeamSearchDTO;
import com.example.walkingmate_back.team.entity.Team;
import com.example.walkingmate_back.team.entity.TeamMember;
import com.example.walkingmate_back.team.service.TeamMemberService;
import com.example.walkingmate_back.team.service.TeamRankService;
import com.example.walkingmate_back.team.service.TeamService;
import com.example.walkingmate_back.user.entity.UserEntity;
import com.example.walkingmate_back.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 *    팀 생성, 삭제, 단일 조회, 전체 조회, 가입된 팀 정보 조회, 팀 검색 조회
 *
 *   @version          1.00 / 2023.08.09
 *   @author           전우진
 */

//@Controller
@RestController
@RequestMapping("/team")
@Slf4j
public class TeamController {

    private final TeamService teamService;
    private final UserService userService;
    private final TeamMemberService teamMemberService;
    private final TeamRankService teamRankService;

    public TeamController(TeamService teamService, UserService userService, TeamMemberService teamMemberService, TeamRankService teamRankService) {
        this.teamService = teamService;
        this.userService = userService;
        this.teamMemberService = teamMemberService;
        this.teamRankService = teamRankService;
    }

    // 팀 생성
    @PostMapping("/save")
    public ResponseEntity<DefaultRes<TeamResponseDTO>> saveTeam(@RequestBody TeamRequestDTO teamRequestDTO, Authentication authentication){
        log.info("현재 여기 지나가는 중");
        log.info(String.valueOf(teamRequestDTO));

        UserEntity user = userService.FindUser(authentication.getName());

        if(user == null) return new ResponseEntity<>(DefaultRes.res(StatusEnum.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER, null), HttpStatus.OK);

        TeamResponseDTO teamResponseDTO = teamService.saveTeam(teamRequestDTO, user);

        if(teamResponseDTO != null)
            return new ResponseEntity<>(DefaultRes.res(StatusEnum.OK, ResponseMessage.WRITE_TEAM, teamResponseDTO), HttpStatus.OK);
        else    // 현재 여기 통과함.
            return new ResponseEntity<>(DefaultRes.res(StatusEnum.BAD_REQUEST, ResponseMessage.NOT_WRITE_TEAM, null), HttpStatus.OK);
    }

    // 팀 삭제
    // TODO : 현재 대결 중인 상태에서 팀을 삭제하고자 한다면, 대결중이라고 막아야 합니다... (2023-09-16 이수) 
    @DeleteMapping("/{teamId}")
    public ResponseEntity<DefaultRes<TeamResponseDTO>> deleteTeam(@PathVariable Long teamId, Authentication authentication) {
        UserEntity user = userService.FindUser(authentication.getName());
        if(user == null)  return new ResponseEntity<>(DefaultRes.res(StatusEnum.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER, null), HttpStatus.OK);

        Team team = teamService.FindTeam(teamId);
        if(team == null) return new ResponseEntity<>(DefaultRes.res(StatusEnum.BAD_REQUEST, ResponseMessage.NOT_FOUND_TEAM, null), HttpStatus.OK);
        TeamResponseDTO teamResponseDTO = teamService.deleteTeam(team, user.getId());

        if(teamResponseDTO != null)
            return new ResponseEntity<>(DefaultRes.res(StatusEnum.OK, ResponseMessage.DELETE_TEAM, teamResponseDTO), HttpStatus.OK);
        else
            return new ResponseEntity<>(DefaultRes.res(StatusEnum.BAD_REQUEST, ResponseMessage.NOT_FOUND_TEAMLEADER, null), HttpStatus.OK);
    }

    // 단일 팀 조회 - 멤버 포함
    @GetMapping("/{teamId}")
    public ResponseEntity<DefaultRes<TeamResponseDTO>> SpecificationTeam(@PathVariable Long teamId) {
        log.info("현재 팀 검색이 실행되었습니다. => " + teamId);
        TeamResponseDTO teamResponseDTO = teamService.getTeam(teamId);
        log.info(teamResponseDTO.toString());

        if (teamResponseDTO != null) {
            log.info("팀 조회 성공함");
            return new ResponseEntity<>(DefaultRes.res(StatusEnum.OK, ResponseMessage.READ_SUCCESS, teamResponseDTO), HttpStatus.OK);
        } else {
            log.info("팀 조회 실패함");
            return new ResponseEntity<>(DefaultRes.res(StatusEnum.BAD_REQUEST, ResponseMessage.NOT_FOUND_TEAM, null), HttpStatus.OK);

        }
    }

    // 팀 전체 조회 - 멤버 포함
    @GetMapping("/list")
    public ResponseEntity<DefaultRes<List<TeamResponseDTO>>> listTeam() {
        List<TeamResponseDTO> teamResponseDTO = teamService.getAllTeam();

        if(teamResponseDTO != null)

            return new ResponseEntity<>(DefaultRes.res(StatusEnum.OK, ResponseMessage.READ_SUCCESS, teamResponseDTO), HttpStatus.OK);
        else
            return new ResponseEntity<>(DefaultRes.res(StatusEnum.BAD_REQUEST, ResponseMessage.NOT_FOUND_TEAM, null), HttpStatus.OK);
    }

    // 가입된 팀 정보 조회 - 랭킹 포함
    @GetMapping("/list/userTeam")
    public ResponseEntity<DefaultRes<TeamResponseDTO>> SpecificationUserTeam(Authentication authentication) {

        // 가입된 팀이 없는 경우 <= 우선 api 테스트를 위해 주석 처리가 필요함 (2023-09-12 이수 작성.)
        TeamMember teamMember = teamMemberService.FindTeam(authentication.getName());

        //if(teamMember == null) return new ResponseEntity<>(DefaultRes.res(StatusEnum.BAD_REQUEST, ResponseMessage.NOT_FOUND_TEAM, null), HttpStatus.OK);

        teamRankService.updateTeamRank(teamMember.getTeam().getId());

        TeamResponseDTO teamResponseDTO = teamService.getUserTeam(teamMember);

        // "데이터베이스 조회 성공",
        if (teamResponseDTO != null)
            return new ResponseEntity<>(DefaultRes.res(StatusEnum.OK, ResponseMessage.READ_SUCCESS, teamResponseDTO), HttpStatus.OK);
        else
            return new ResponseEntity<>(DefaultRes.res(StatusEnum.BAD_REQUEST, ResponseMessage.NOT_FOUND_TEAM, null), HttpStatus.OK);
    }

    // 팀 검색 조회
    @GetMapping("/list/search")
    public ResponseEntity<DefaultRes<List<TeamResponseDTO>>> searchTeam(@RequestBody TeamSearchDTO teamSearchDTO) {
        List<TeamResponseDTO> teamResponseDTO = teamService.getSearchTeam(teamSearchDTO);

        if(teamResponseDTO != null)
            return new ResponseEntity<>(DefaultRes.res(StatusEnum.OK, ResponseMessage.READ_SUCCESS, teamResponseDTO), HttpStatus.OK);
        else
            return new ResponseEntity<>(DefaultRes.res(StatusEnum.BAD_REQUEST, ResponseMessage.NOT_FOUND_TEAM, null), HttpStatus.OK);
    }
}
