package com.example.walkingmate_back.login.service;

import com.example.walkingmate_back.user.entity.UserEntity;
import com.example.walkingmate_back.user.repository.UserRepository;
import com.example.walkingmate_back.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

/**
 *    사용자 이메일 인증번호 전송
 *    - 서비스 로직
 *
 *   @version          1.00 / 2023.09.11
 *   @author           전우진
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;  // 의존성 주입을 통해 필요한 객체를 가져옴
    private static final String senderEmail= "202145022@itc.ac.kr";
    private static int number;  // 랜덤 인증 코드

    private final UserRepository userRepository;

    // 랜덤 인증 코드 생성
    public static void createNumber() {
        number = (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    // 메일 양식 작성
    public MimeMessage createMail(String mail){
        createNumber();  // 인증 코드 생성
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);   // 보내는 이메일
            message.setRecipients(MimeMessage.RecipientType.TO, mail); // 보낼 이메일 설정
            message.setSubject("[WalkingMate] 회원가입을 위한 이메일 인증");  // 제목 설정
            String body = "";
            body += "<h1>" + "안녕하세요." + "</h1>";
            body += "<h1>" + "Walking Mate 입니다." + "</h1>";
            body += "<h3>" + "회원가입을 위한 요청하신 인증 번호입니다." + "</h3><br>";
            body += "<h2>" + "아래 코드를 회원가입 창으로 돌아가 입력해주세요." + "</h2>";

            body += "<div align='center' style='border:1px solid black; font-family:verdana;'>";
            body += "<h2>" + "회원가입 인증 코드입니다." + "</h2>";
            body += "<h1 style='color:blue'>" + number + "</h1>";
            body += "</div><br>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    // 실제 메일 전송
    public int sendEmail(String userId) {
        log.info("sendEmail 함수 들어옴.");
        log.info(userId);
        // 메일 전송에 필요한 정보 설정
        MimeMessage message = createMail(userId);
        // 실제 메일 전송
        javaMailSender.send(message);

        // 인증 코드 반환
        return number;
    }

    // 비밀번호 재설정 시 사용자 확인 후 이메일 인증번호 전송
    public int sendPwEmail(String userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);

        if(user != null) {
            // 메일 전송에 필요한 정보 설정
            MimeMessage message = createMail(userId);
            // 실제 메일 전송
            javaMailSender.send(message);

            // 인증 코드 반환
            return number;
        } else return -1;
    }
}
