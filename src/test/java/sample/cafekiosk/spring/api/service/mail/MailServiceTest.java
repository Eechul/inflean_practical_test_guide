package sample.cafekiosk.spring.api.service.mail;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
        when(mailSendClient.sendMail(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(true);

        // when
        boolean result = mailService.sendMail("from", "to", "subject", "content");

        // then
        assertThat(result).isTrue();
        // Mockito.verify를 통해 mailSendHistoryRepository.save 메서드를 얼마나 호출했는지까지 검증 가능하다.
        verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
    }

}