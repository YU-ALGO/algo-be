package com.stock.yu.downbitbe.user.application;

import com.stock.yu.downbitbe.user.domain.user.MailCode;
import com.stock.yu.downbitbe.user.domain.user.MailCodeRepository;
import com.stock.yu.downbitbe.user.utils.RandomCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class MailService {

    private final MailCodeRepository mailCodeRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(String username, String nickname) {
        int code = RandomCodeUtil.createCode();
        SimpleMailMessage msg = new SimpleMailMessage();

        StringBuilder subject = new StringBuilder();
        subject.append("[ALGo] 이메일 인증 코드");

        StringBuilder content = new StringBuilder();
        content.append("ALGo 인증요청\n");
        content.append(nickname);
        content.append("님의 인증요청 코드\n");
        content.append(code);

        msg.setTo(username);
        msg.setSubject(subject.toString());
        msg.setText(content.toString());
        javaMailSender.send(msg);

        MailCode mailCode = MailCode.builder()
                .username(username)
                //.user(user)
                .code(code)
                .build();
        mailCodeRepository.save(mailCode);
    }

    public boolean validateCode(String username, int code) {
        MailCode mailCode = mailCodeRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("인증 기록이 존재하지 않습니다."));
        if(mailCode.getModifiedAt().isAfter(LocalDateTime.now().minusMinutes(5)) && mailCode.getCode() == code) {
            mailCodeRepository.save(mailCode.updateIsValidate());
            return true;
        }
        else
            return false;
    }
}
