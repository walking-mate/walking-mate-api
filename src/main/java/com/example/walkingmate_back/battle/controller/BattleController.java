package com.example.walkingmate_back.battle.controller;

import com.example.walkingmate_back.battle.dto.BattleRequestDTO;
import com.example.walkingmate_back.battle.dto.BattleResponseDTO;
import com.example.walkingmate_back.battle.service.BattleService;
import com.example.walkingmate_back.main.entity.ResponseMessage;
import com.example.walkingmate_back.main.entity.DefaultRes;
import com.example.walkingmate_back.main.entity.StatusEnum;
import com.example.walkingmate_back.team.entity.TeamMember;
import com.example.walkingmate_back.team.service.TeamMemberService;
import com.example.walkingmate_back.user.entity.UserEntity;
import com.example.walkingmate_back.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.List;

/**
 *    대결 생성, 삭제, 단일 조회, 전체 조회
 *
 *   @version          1.00 / 2023.07.20
 *   @author           전우진
 */

//@Controller
@RestController
@RequestMapping("/battle")
public class BattleController {

    private final BattleService battleService;
    private final UserService userService;
    private final TeamMemberService teamMemberService;

    public BattleController(BattleService battleService, UserService userService, TeamMemberService teamMemberService) {
        this.battleService = battleService;
        this.userService = userService;
        this.teamMemberService = teamMemberService;
    }

    // 대결 생성
    @PostMapping("/new")
    public ResponseEntity<DefaultRes<BattleResponseDTO>> saveBattle(@RequestBody BattleRequestDTO battleRequestDTO) throws ParseException {
        String userId = "aaa";
        UserEntity user = userService.FindUser(userId); // 사용자 확인
        if(user == null) return new ResponseEntity<>(DefaultRes.res(StatusEnum.OK, ResponseMessage.NOT_FOUND_USER, null), HttpStatus.OK);

        // 팀 소속이 없는 경우
        if(user.getTeam() == null) return new ResponseEntity<>(DefaultRes.res(StatusEnum.OK, ResponseMessage.NOT_FOUND_TEAM, null), HttpStatus.OK);

        // 팀장이 아닌 경우
        TeamMember teamMember = teamMemberService.FindTeam(user.getId());
        if(teamMember.isTeamLeader() == false) return new ResponseEntity<>(DefaultRes.res(StatusEnum.OK, ResponseMessage.NOT_FOUND_TEAMLEADER, null), HttpStatus.OK);

        BattleResponseDTO battleResponseDTO = battleService.saveBattle(battleRequestDTO, user);

        if(battleResponseDTO != null)
            return new ResponseEntity<>(DefaultRes.res(StatusEnum.OK, ResponseMessage.WRITE_BATTLE, battleResponseDTO), HttpStatus.OK);
        else
            return new ResponseEntity<>(DefaultRes.res(StatusEnum.OK, ResponseMessage.NOT_WRITE_BATTLE, null), HttpStatus.OK);
    }

    // 대결 삭제
    @DeleteMapping("/{battleId}")
    public ResponseEntity<DefaultRes<BattleResponseDTO>> deleteBattle(@PathVariable Long battleId) {
        BattleResponseDTO battleResponseDTO = battleService.deleteBattle(battleId);

        if(battleResponseDTO != null)
            return new ResponseEntity<>(DefaultRes.res(StatusEnum.OK, ResponseMessage.DELETE_BATTLE, battleResponseDTO), HttpStatus.OK);
        else
            return new ResponseEntity<>(DefaultRes.res(StatusEnum.DB_ERROR, ResponseMessage.NOT_FOUND_BATTLE, null), HttpStatus.OK);
    }

    // 대결 전체 조회 - 대결 상대 포함
    @GetMapping("/list")
    public ResponseEntity<DefaultRes<List<BattleResponseDTO>>> listBatlle() {
        List<BattleResponseDTO> battleResponseDTO = battleService.getAllBattle();

        if(battleResponseDTO != null)
            return new ResponseEntity<>(DefaultRes.res(StatusEnum.OK, ResponseMessage.READ_SUCCESS, battleResponseDTO), HttpStatus.OK);
        else
            return new ResponseEntity<>(DefaultRes.res(StatusEnum.DB_ERROR, ResponseMessage.NOT_FOUND_BATTLE, null), HttpStatus.OK);
    }

    // 단일 대결 조회 - 대결 상대 포함
    @GetMapping("/{battleId}")
    public ResponseEntity<DefaultRes<BattleResponseDTO>> SpecificationBattle(@PathVariable Long battleId) {
        BattleResponseDTO battleResponseDTO = battleService.getBattle(battleId);

        if(battleResponseDTO != null)
            return new ResponseEntity<>(DefaultRes.res(StatusEnum.OK, ResponseMessage.READ_SUCCESS, battleResponseDTO), HttpStatus.OK);
        else
            return new ResponseEntity<>(DefaultRes.res(StatusEnum.DB_ERROR, ResponseMessage.NOT_FOUND_BATTLE, null), HttpStatus.OK);
    }

//    // 대결 검색 조회
//    @GetMapping("/list/search")
//    public ResponseEntity<Message> listSearchBatlle(@RequestBody BattleSearchDTO battleSearchDTO) {
//        return battleService.getSearchBattle(battleSearchDTO);
//    }

}
