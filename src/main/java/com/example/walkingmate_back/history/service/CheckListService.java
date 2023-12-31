package com.example.walkingmate_back.history.service;

import com.example.walkingmate_back.history.dto.CheckListRequestDTO;
import com.example.walkingmate_back.history.dto.CheckListResponseDTO;
import com.example.walkingmate_back.history.entity.CheckList;
import com.example.walkingmate_back.history.repository.CheckListRepository;
import com.example.walkingmate_back.user.entity.UserEntity;
import com.example.walkingmate_back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *    체크리스트 등록, 수정, 삭제, 체크 및 해제, 조회
 *    - 서비스 로직
 *
 *   @version          1.00 / 2023.07.24
 *   @author           전우진
 */

@Service
@RequiredArgsConstructor
@Transactional
public class CheckListService {

    private final CheckListRepository checkListRepository;
    private final UserRepository userRepository;

    /**
     * 사용자 확인 후 체크리스트 저장
     * - 전우진 2023.07.12
     */

    // 수정사항: 체크리스트를 작성할 때, 사용자가 원하는 날짜에 일정을 기입할 수 있도록 수정하였습니다. (2023-09-11 이수 수정함.)
    public CheckListResponseDTO saveCheckList(CheckListRequestDTO checkListRequestDTO, String userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);

        if(user != null) { // 사용자가 존재하는 경우
            CheckList checkList = new CheckList(user,LocalDate.parse(checkListRequestDTO.getDate()) , checkListRequestDTO.getContent(), false);
            checkListRepository.save(checkList);

            return CheckListResponseDTO.builder()
                    .listId(checkList.getListId())
                    .userId(checkList.getUser().getId())
                    .date(String.valueOf(checkList.getDate()))
                    .checked(checkList.isChecked())
                    .content(checkList.getContent())
                    .build();
        } else {
            // 사용자가 존재하지 않는 경우
            return null;
        }
    }

    /**
     * 체크리스트 탐색 후 체크리스트 수정
     * - 전우진 2023.07.12
     */
    public CheckListResponseDTO updateCheckList(Long listId, CheckListRequestDTO checkListRequestDTO) {
        CheckList checkList = checkListRepository.findById(listId).orElse(null);

        if(checkList == null) {
            // 체크리스트가 존재하지 않는 경우
            return null;
        }

        checkList.update(checkListRequestDTO);

        checkListRepository.save(checkList);

        String date = checkList.getDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return CheckListResponseDTO.builder()
                .listId(checkList.getListId())
                .userId(checkList.getUser().getId())
                .date(date)
                .checked(checkList.isChecked())
                .content(checkList.getContent())
                .build();
    }

    /**
     * 체크리스트 탐색 후 체크리스트 체크 및 해제
     * - 전우진 2023.07.12
     */
    public CheckListResponseDTO updateCheckd(Long listId) {
        CheckList checkList = checkListRepository.findById(listId).orElse(null);

        if(checkList == null) {
            // 체크리스트가 존재하지 않는 경우
            return null;
        }

        boolean checked = checkList.isChecked();

        if(checked == true) {  // 체크리스트가 체크되어 있는 경우
            checkList.updateCheckd(false);

            checkListRepository.save(checkList);

            String date = checkList.getDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            return CheckListResponseDTO.builder()
                    .listId(checkList.getListId())
                    .userId(checkList.getUser().getId())
                    .date(date)
                    .checked(checkList.isChecked())
                    .content(checkList.getContent())
                    .build();
        } else {  // 체크리스트가 체크되어 있지 않는 경우
            checkList.updateCheckd(true);

            checkListRepository.save(checkList);

            String date = checkList.getDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            return CheckListResponseDTO.builder()
                    .listId(checkList.getListId())
                    .userId(checkList.getUser().getId())
                    .date(date)
                    .checked(checkList.isChecked())
                    .content(checkList.getContent())
                    .build();
        }
    }

    /**
     * 체크리스트 탐색 후 체크리스트 삭제
     * - 전우진 2023.07.12
     */
    public CheckListResponseDTO deleteCheckList(Long listId) {
        CheckList checkList = checkListRepository.findById(listId).orElse(null);

        if (checkList == null) {
            // 체크리스트가 존재하지 않는 경우
            return null;
        }
        checkListRepository.delete(checkList);

        String date = checkList.getDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return CheckListResponseDTO.builder()
                .listId(checkList.getListId())
                .userId(checkList.getUser().getId())
                .date(date)
                .checked(checkList.isChecked())
                .content(checkList.getContent())
                .build();
    }

    /**
     * 체크리스트 탐색 후 체크리스트 조회
     * - 전우진 2023.07.13
     */
    public List<CheckListResponseDTO> getDateCheckList(String userId, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate lc = LocalDate.parse(date, formatter);

        List<CheckList> checkLists = checkListRepository.findByUserIdWithDate(userId, lc);
        List<CheckListResponseDTO> result = new ArrayList<>();

        for (CheckList checkList : checkLists) {
            CheckListResponseDTO checkListResponseDTO = new CheckListResponseDTO(
                    checkList.getListId(),
                    checkList.getUser().getId(),
                    checkList.getDate().toString(),
                    checkList.isChecked(),
                    checkList.getContent()
            );
            result.add(checkListResponseDTO);
        }

        if(result.size() == 0) {
            return null;
        }
        return result;

    }
}
