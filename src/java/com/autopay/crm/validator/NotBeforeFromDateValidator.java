package com.autopay.crm.validator;

import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.richfaces.component.UICalendar;

/**
 *
 * @author Judy
 */
public class NotBeforeFromDateValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        UIInput fromDateComponent = (UIInput) component.getAttributes().get("Comparefield");
        Date fromDate = null;
        if (fromDateComponent instanceof UICalendar) {
            UICalendar uic = (UICalendar)fromDateComponent;
            fromDate = (Date) uic.getValue();
        }
        
        Date toDate = (Date) value;
        
        if (fromDate != null && toDate != null && fromDate.after(toDate)) {
            String message = component.getId() + " may not be later than " + fromDateComponent.getId();
            FacesMessage fm = new FacesMessage(message);
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(fm);
        }
    }
};