package com.example.walkingmate_back.history.service;

import com.example.walkingmate_back.history.dto.CheckListRequestDTO;
import com.example.walkingmate_back.history.entity.CheckList;
import com.example.walkingmate_back.history.repository.CheckListRepository;
import com.example.walkingmate_back.user.entity.UserEntity;
import com.example.walkingmate_back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 *    체크리스트 등록, 수정, 삭제, 체크 및 해제
 *    - 서비스 로직
 *
 *   @version          1.00 / 2023.07.12
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
    public int saveCheckList(CheckListRequestDTO checkListRequestDTO) {
        Optional<UserEntity> user = userRepository.findById(checkListRequestDTO.getUserId());
        LocalDateTime now = LocalDateTime.now();

        if(user != null) {
            CheckList checkList = new CheckList(user.get(), now, checkListRequestDTO.getContent(), false);
            checkListRepository.save(checkList);

            return 1;
        } else {
            return -1;
        }

    }

    /**
     * 체크리스트 탐색 후 체크리스트 수정
     * - 전우진 2023.07.12
     */
    public int updateCheckList(Long listId, CheckListRequestDTO checkListRequestDTO) {
        Optional<CheckList> result = checkListRepository.findById(listId);
        LocalDateTime now = LocalDateTime.now();

        if(result == null) {
            return -1;
        }
        CheckList checkList = result.get();
        checkList.update(checkListRequestDTO, now);

        checkListRepository.save(checkList);

        return 1;
    }

    /**
     * 체크리스트 탐색 후 체크리스트 체크 및 해제
     * - 전우진 2023.07.12
     */
    public int updateCheckd(Long listId) {
        Optional<CheckList> result = checkListRepository.findById(listId);

        if(result == null) {
            return -1;
        }

        boolean checked = result.get().isChecked();

        if(checked == true) {
            CheckList checkList = result.get();
            checkList.updateCheckd(false);

            checkListRepository.save(checkList);

            return 0;
        } else {
            CheckList checkList = result.get();
            checkList.updateCheckd(true);

            checkListRepository.save(checkList);

            return 1;
        }
    }

    /**
     * 체크리스트 탐색 후 체크리스트 삭제
     * - 전우진 2023.07.12
     */
    public int deleteCheckList(Long listId) {

        CheckList checkList = checkListRepository.findById(listId).orElse(null);

        if (checkList == null) {
            return -1;
        }
        checkListRepository.delete(checkList);
        return 1;

    }
}
