package com.autopay.crm.session;

import com.autopay.crm.model.Schedules;
import com.autopay.crm.model.Task;
import com.autopay.crm.model.dealer.User;
import com.autopay.crm.util.CrmUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author Judy
 */
@Stateless
public class TaskReminder {

    @EJB
    NotificationFacade ejbNotification;
    @EJB
    SchedulesFacade ejbSchedule;
    @EJB
    UserFacade ejbUser;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Schedule(hour="8", minute="1")
    private void executeTask(Timer timer) {
        Calendar c = Calendar.getInstance(); // starts with today's date and time
        c.add(Calendar.DAY_OF_YEAR, 2);  // advances day by 2
        Date date = c.getTime(); // gets modified time
        
        List<Schedules> tasks = ejbSchedule.getScheduledTasksByDate(date);
        if (tasks != null) {
            for (Schedules task : tasks) {
                sendTaskReminderEmail(task);
            }
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private void sendTaskReminderEmail(final Schedules schedule) {
        User assignToUser = ejbUser.findUser(schedule.getAssignedUser());
        if (assignToUser != null && assignToUser.getEmail() != null) {
            String taskName = "";
            for (Task task : schedule.getTaskCollection()) {
                if (taskName.length() == 0) {
                    taskName = task.getName();
                } else {
                    taskName = taskName + ", " + task.getName();
                }
            }
            String body = "Dear " + assignToUser.getFirstName() + " " + assignToUser.getLastName() + ",<br/><br/>"
                    + "Just a reminder: you have task(s) '" + taskName + "' need to be done on " + CrmUtils.getDateString(schedule.getScheduledDatetime(), "yyyy-MM-dd")+ ". Ignore this reminder if you have already done your task(s)."
                    + "<br/><br/>"
                    + "Thank you<br/>"
                    + "CMR";
            ejbNotification.sendMail(assignToUser.getEmail(), "Task Reminder", body);
        }
    }
}
