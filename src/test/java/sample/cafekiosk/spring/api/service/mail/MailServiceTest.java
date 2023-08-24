package sample.cafekiosk.spring.api.service.mail;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.client.mail.MailSendHistoryRepository;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

// 이 예제는 @SpringBootTest를 사용하지 않고, Mockito를 통해 서비스를 테스트 해보는 예제이다.
// MockitoExtension을 사용하면 @Mock, @Spy, @InjectMocks 등의 어노테이션을 사용할 수 있다.
@ExtendWith(MockitoExtension.class)
class MailServiceTest {

//    @Mock mock과 spy의 차이가 뭘까
//    Mock은 객체의 모든 메서드를 stubbing 해주는 것이고
//    Spy는 객체의 일부 메서드만 stubbing 해주는 것이다.
//    그래서 Spy는 stubbing한 메서드를 제외한 나머지 메서드들의 동작을 확인할 수 있다.
//    @Spy
//    private MailSendClient mailSendClient;

    @Mock
    private MailSendClient mailSendClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks
    private MailService mailService; // 위에 두 Mock을 이 서비스에 Inject 해준다.

    @DisplayName("메일 전송 테스트")
    @Test
    void sendMail() {
        // given
        // @Mock을 쓸때, BDDMockito.given을 사용하면 given절에 맞게 사용할 수 있어서 좋다.
//        Mockito.when(mailSendClient.sendMail(anyString(), anyString(), anyString(), anyString()))
//                .thenReturn(true);
        BDDMockito.given(mailSendClient.sendMail(anyString(), anyString(), anyString(), anyString()))
                .willReturn(true);

        // @Spy를 쓸때
//        doReturn(true)
//                .when(mailSendClient).sendMail(anyString(), anyString(), anyString(), anyString());

        // when
        boolean result = mailService.sendMail("from", "to", "subject", "content");

        // then
        assertThat(result).isTrue();
        // Mockito.verify를 통해 mailSendHistoryRepository.save 메서드를 얼마나 호출했는지까지 검증 가능하다.
        verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
    }

}