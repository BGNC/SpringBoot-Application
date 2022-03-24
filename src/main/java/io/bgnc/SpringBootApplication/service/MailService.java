package io.bgnc.SpringBootApplication.service;

import io.bgnc.SpringBootApplication.exceptions.SpringBootApplicationException;
import io.bgnc.SpringBootApplication.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {


    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;


    /**
     * In order to reduce the time taken while the user is registering,
     * it is aimed to run the program more effectively by running the program asynchronously.
     * In this way, a decrease in time was observed.
     * @param notificationEmail
     * @throws SpringBootApplicationException
     */

    // AJAX
    @Async
    public void sendMail(NotificationEmail notificationEmail) throws SpringBootApplicationException {
        MimeMessagePreparator messagePreparator  = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("trial@email.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };
        try {
            mailSender.send(messagePreparator);
            /**
             * SLF4J
             */
            log.info("E-Mail Sent ! ");
        }
        catch (MailException e)
        {
            throw new SpringBootApplicationException("Exception occured when the email sent"+notificationEmail.getRecipient());

        }
    }
}
