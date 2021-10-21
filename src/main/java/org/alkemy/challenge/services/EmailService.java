package org.alkemy.challenge.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.alkemy.challenge.entities.DisneyUser;
import org.alkemy.challenge.entities.EmailBody;
import org.alkemy.challenge.repositories.EmailPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements EmailPort{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private JavaMailSender sender;

	@Override
	public boolean sendEmail(EmailBody emailBody)  {
		LOGGER.info("EmailBody: {}", emailBody.toString());
		return sendEmailTool(emailBody.getContent(),emailBody.getEmail(), emailBody.getSubject());
	}

	private boolean sendEmailTool(String textMessage, String email,String subject) {
		boolean send = false;
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);		
		try {
			helper.setTo(email);
			helper.setText(textMessage, true);
			helper.setSubject(subject);
			sender.send(message);
			send = true;
			LOGGER.info("Mail Sent!");
		} catch (MessagingException e) {
			LOGGER.error("Something went wrong ", e);
		}
		return send;
	}

        public boolean sendRegistrationEmail(DisneyUser user){
            EmailBody mail = new EmailBody();
            mail.setEmail(user.getEmail());
            mail.setSubject("Welcome to Disney API");
            mail.setContent("Your account was created Succesfully");
            return sendEmail(mail);
        }
}
