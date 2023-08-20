package sample.cafekiosk.spring.domain.history.mail;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MailSendHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String fromEmail;
    private String toEmail;
    private String subject;
    private String content;

    @Builder
    public MailSendHistory(Long id, String fromEmail, String toEmail, String subject, String content) {
        this.id = id;
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.subject = subject;
        this.content = content;
    }
}
