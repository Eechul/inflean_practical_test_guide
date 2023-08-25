package sample.cafekiosk.spring;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.client.mail.MailSendClient;


// 추상 클래스를 만들어 테스트 클래스가 상속 받음으로써,
// 환경을 통합 할 수 있다.
// 이렇게 함으로서 테스트를 위한 SpringBoot 서버 구동횟수를 줄일 수 있을 것이다.
@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {

    // 테스트 환경마다 다르지만
    // 이 MockBean을 쓰지 않는 서비스 테스트 클래스도 있을 것이다.
    // 그래서 일반적인 통합 테스트 환경과 Mock을 위한 통합 테스트 환경을 분리해주면 좋을듯 하다.
    @MockBean
    protected MailSendClient mailSendClient;
}
