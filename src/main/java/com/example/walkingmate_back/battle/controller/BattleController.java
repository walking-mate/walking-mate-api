package com.example.walkingmate_back.battle.controller;

import com.example.walkingmate_back.battle.dto.BattleRequestDTO;
import com.example.walkingmate_back.battle.dto.BattleSearchDTO;
import com.example.walkingmate_back.battle.service.BattleService;
import com.example.walkingmate_back.main.entity.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;

/**
 *    대결 생성, 삭제, 단일 조회, 전체 조회
 *
 *   @version          1.00 / 2023.07.19
 *   @author           전우진
 */

//@Controller
@RestController
@RequestMapping("/battle")
public class BattleController {

    private final BattleService battleService;

    public BattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    // 대결 생성
    @PostMapping("/new")
    public ResponseEntity<Message> saveBattle(@RequestBody BattleRequestDTO battleRequestDTO) throws ParseException {
        String userId = "ccc";

        return battleService.saveBattle(battleRequestDTO, userId);
    }

    // 대결 삭제
    @DeleteMapping("/{battleId}")
    public ResponseEntity<Message> deleteBattle(@PathVariable Long battleId) {
        return battleService.deleteBattle(battleId);
    }

    // 대결 전체 조회 - 대결 상대 포함
    @GetMapping("/list")
    public ResponseEntity<Message> listBatlle() {
        return battleService.getAllBattle();
    }

    // 단일 대결 조회 - 대결 상대 포함
    @GetMapping("/{battleId}")
    public ResponseEntity<Message> SpecificationBattle(@PathVariable Long battleId) {

        return battleService.getBattle(battleId);
    }

//    // 대결 검색 조회
//    @GetMapping("/list/search")
//    public ResponseEntity<Message> listSearchBatlle(@RequestBody BattleSearchDTO battleSearchDTO) {
//        return battleService.getSearchBattle(battleSearchDTO);
//    }

}
