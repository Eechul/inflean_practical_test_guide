package sample.cafekiosk.spring.client.mail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;

@Repository
public interface MailSendHistoryRepository extends JpaRepository<MailSendHistory, Long> {

}
