/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.session;

import com.autopay.commons.EmailSender;
import java.util.StringTokenizer;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import org.apache.log4j.Logger;

/**
 *
 * @author sadimelbouci
 */
@Stateless
public class NotificationFacade {
    private static Logger log = Logger.getLogger(NotificationFacade.class);

    @Asynchronous
    public void sendMail (String to, String subject, String body) {
        EmailSender sender = new EmailSender();
        
        if (to == null) {
            log.error("Destination Email is NULL");
            return;
        }
        StringTokenizer tokens = new StringTokenizer(to, ";");
        while (tokens.hasMoreTokens()) {
            String emailTo = tokens.nextToken();
            sender.sendMail(body, subject, emailTo);
        }
    }
    
    public void sendCalendarMail (String to, String subject, String body, String calendarContent) {
        EmailSender sender = new EmailSender();
        
        if (to == null) {
            log.error("Destination Email is NULL");
            return;
        }
        StringTokenizer tokens = new StringTokenizer(to, ";");
        while (tokens.hasMoreTokens()) {
            String emailTo = tokens.nextToken();
            sender.sendCalendarMail(body, subject, emailTo, calendarContent);
        }
    }

}
