/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.commons;

import java.util.Properties;
import javax.activation.MailcapCommandMap;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Logger;

/**
 *
 * @author sadimelbouci
 */
public class EmailSender {

    public static Logger log = Logger.getLogger(EmailSender.class);
    // private String smtpHost="gmail-smtp-in.l.google.com";
    private String smtpHost;
    private String from;
    private String username;
    private String password;
    private boolean tls;
    private String sslProtocol;
    private String encoding = "text/html";
    private int tries;
    private String licensePath;

    public EmailSender() {
    }

    public EmailSender(String smtpHost, String from) {
        this.smtpHost = smtpHost;
        this.from = from;

    }

    public void sendCalendarMail(String body, String subject, String emailto, String calendarContent) {
        boolean alt = false;
        //setEmailProperties(alt);

        //register the text/calendar mime type
        MimetypesFileTypeMap mimetypes = (MimetypesFileTypeMap) MimetypesFileTypeMap.getDefaultFileTypeMap();
        mimetypes.addMimeTypes("text/calendar ics ICS");

        //register the handling of text/calendar mime type
        MailcapCommandMap mailcap = (MailcapCommandMap) MailcapCommandMap.getDefaultCommandMap();
        mailcap.addMailcap("text/calendar;; x-java-content-handler=com.sun.mail.handlers.text_plain");

        do {
            try {
                Properties props = getProperties(alt);

                log.debug("Email From: " + from);
                log.debug("Email To: " + emailto);
                log.debug("SMTP HOST: " + smtpHost);

                Authenticator auth = new Authenticator() {
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                };

                Session session = Session.getInstance(props, auth);

                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from.trim()));// emailid
                // of the
                // sender

                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(emailto.trim()));
                // message.addRecipients(Message.RecipientType.CC, ccAddress);
                message.setSubject(subject);
                MimeMultipart multipart = new MimeMultipart("alternative");

                //part 1, html text
                BodyPart messageBodyPart = new MimeBodyPart();

                //Note: even if the content is spcified as being text/html, outlook won't read correctly tables at all
                // and only some properties from div:s. Thus, try to avoid too fancy content
                messageBodyPart.setContent(body, "text/html; charset=utf-8");

                //part 2, the calendar
                BodyPart calendarPart = buildCalendarPart(calendarContent);

                // body
                // mailServer,fromAddress,toAddress,subject,and body are all
                // String objects
                //multipart.addBodyPart(messageBodyPart);
                if (calendarPart != null) {
                    multipart.addBodyPart(calendarPart);
                }
                // second part (the image)
                message.setContent(multipart);

                Transport.send(message);
                //
                // Message was sent succesfully. Set tries to 3 to exit the
                // loop.
                //
                tries = 3;
            } catch (AddressException e) {
                log.error("Address Exception", e);
            } catch (MessagingException e) {
                log.error("Messaging Exception", e);
            }
            tries++;
            alt = true;
        } while (tries <= 2);
    }
    
    private BodyPart buildCalendarPart(final String calendarContent) {
        try {
            BodyPart calendarPart = new MimeBodyPart();

            calendarPart.addHeader("Content-Class", "urn:content-classes:calendarmessage");
            calendarPart.setContent(calendarContent, "text/calendar;method=REQUEST");

            return calendarPart;
        } catch (Exception e) {
            return null;
        }
    }

    public void sendMail(String body, String subject, String emailto) {
        boolean alt = false;
        //setEmailProperties(alt);
        do {
            try {
                Properties props = getProperties(alt);

                log.debug("Email From: " + from);
                log.debug("Email To: " + emailto);
                log.debug("SMTP HOST: " + smtpHost);

                Authenticator auth = new Authenticator() {
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                };

                Session session = Session.getInstance(props, auth);

                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from.trim()));// emailid
                // of the
                // sender

                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(emailto.trim()));
                // message.addRecipients(Message.RecipientType.CC, ccAddress);
                message.setSubject(subject);
                MimeMultipart multipart = new MimeMultipart("related");

                BodyPart messageBodyPart1 = new MimeBodyPart();
                // Fill the message
                messageBodyPart1.setHeader("Content-Type", encoding);
                messageBodyPart1.setHeader("Content-Transfer-Encoding",
                        "quoted-printable");
                messageBodyPart1.setContent(body, encoding);// body is email

                // body
                // mailServer,fromAddress,toAddress,subject,and body are all
                // String objects
                multipart.addBodyPart(messageBodyPart1);
                
                message.setContent(multipart);

                Transport.send(message);
                //
                // Message was sent succesfully. Set tries to 3 to exit the
                // loop.
                //
                tries = 3;
            } catch (AddressException e) {
                log.error("Address Exception", e);
            } catch (MessagingException e) {
                log.error("Messaging Exception", e);
            }
            tries++;
            alt = true;
        } while (tries <= 2);
    }

    public void sendMail(String body, String subject, String emailto,
            String emailFrom, String ccEmail) {
        boolean alt = false;
        do {
            try {
                Properties props = getProperties(alt);

                log.debug("Email From: " + emailFrom);
                log.debug("Email To: " + emailto);
                log.debug("SMTP HOST: " + smtpHost);

                Authenticator auth = new Authenticator() {
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                };

                Session session = Session.getInstance(props, auth);

                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(emailFrom.trim()));// emailid
                // of the
                // sender
                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(emailto.trim()));
                if (ccEmail != null) {
                    message.addRecipient(Message.RecipientType.CC,
                            new InternetAddress(ccEmail.trim()));
                }

                message.setSubject(subject);

                MimeMultipart multipart = new MimeMultipart("related");

                BodyPart messageBodyPart = new MimeBodyPart();
                // Fill the message
                messageBodyPart.setHeader("Content-Type", encoding);
                messageBodyPart.setHeader("Content-Transfer-Encoding",
                        "quoted-printable");
                messageBodyPart.setContent(body, encoding);// body is email
                // body
                // mailServer,fromAddress,toAddress,subject,and body are all
                // String objects
                multipart.addBodyPart(messageBodyPart);

                message.setContent(multipart);

                Transport.send(message);

                tries = 3;
            } catch (AddressException e) {
                // TODO Auto-generated catch block
                log.warn("Error when sending email: ", e);
            } catch (MessagingException e) {
                // TODO Auto-generated catch block
                log.warn("Error when sending email: ", e);
            } catch (Exception e) {
                log.error("Error when sending email: ", e);
            }
            tries++;
            alt = true;
        } while (tries <= 2);
    }

    private Properties getProperties(boolean alt) {

        Properties props = new Properties();
        setEmailProperties(alt);
        try {
            props = UtilsProps.load("email.properties");
        } catch (Exception ex) {
            log.error("Problem reading the property File 'email.properties'.");
        }


        props.put("mail.smtp.host", smtpHost);

        if (username != null) {
            props.put("mail.protocol.user", username);
        }
        if (password != null) {
            props.put("mail.password", password);
        }
        if (tls) {
            props.put("mail.smtp.starttls.enable", "true");
        }
        if (sslProtocol != null) {
            props.put("mail.smtp.ssl.protocols", sslProtocol);
        }

        props.setProperty("mail.transport.protocol", "smtp");

        return props;
    }

    private void setEmailProperties(boolean alt) {
        Properties props = new Properties();

        try {
            props = UtilsProps.load("email.properties");
        } catch (Exception ex) {
            log.error("Problem reading the property File 'email.properties'.");
        }
        from = (String) props.getProperty("from.email");
        if (!alt) {
            smtpHost = (String) props.getProperty("smtp.mail.server");

            // Optional Parameters
            try {
                username = props.getProperty("username");
                password = props.getProperty("password");
            } catch (Exception e) {
            }
            try {
                String tlstmp = props.getProperty("use.tls");
                if (tlstmp != null && tlstmp.equalsIgnoreCase("true")) {
                    tls = true;
                }
                sslProtocol = props.getProperty("ssl.protocol");
            } catch (Exception e) {
            }
        } else {
            smtpHost = (String) props.getProperty("alt.smtp.mail.server");
            //
            // Optional Parameters
            try {
                username = props.getProperty("alt.username");
                password = props.getProperty("alt.password");
            } catch (Exception e) {
            }
            try {
                String tlstmp = props.getProperty("alt.use.tls");
                if (tlstmp != null && tlstmp.equalsIgnoreCase("true")) {
                    tls = true;
                }
                sslProtocol = props.getProperty("alt.ssl.protocol");
            } catch (Exception e) {
            }
        }

        if (smtpHost == null) {
            smtpHost = "aspmx.l.google.com";
        }
        if (from == null) {
            from = "admin@autopay.com";
        }
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getLicensePath() {
        return licensePath;
    }

    public void setLicensePath(String licensePath) {
        this.licensePath = licensePath;
    }
}
