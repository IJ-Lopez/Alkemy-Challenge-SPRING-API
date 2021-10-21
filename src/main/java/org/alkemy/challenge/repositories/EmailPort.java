package org.alkemy.challenge.repositories;

import org.alkemy.challenge.entities.EmailBody;

public interface EmailPort {
	public boolean sendEmail(EmailBody emailBody);
}
