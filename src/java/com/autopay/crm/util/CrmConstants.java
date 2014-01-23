/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.util;

/**
 *
 * @author Judy
 */
public class CrmConstants {

    public static int DEFAULT_ITEMS_PER_PAGE = 25;
    
    public static String[] SPECIAL_CHARACTERS = new String[] {".", ",", "!", "@", "#", "$", "%", "^", "&", "*", "'", "\\", "?", "-", "+", "_"};
    
    public enum ContactTitle {
        GM, OWNER, OFFICE, GSM, SALES
    }
    
    public enum CampaignType {
        PHONE, EMAIL, MAIL
    }
    
    public enum TaskType {
        ONE_TIME, EVERY_HOUR, EVERY_DAY, EVERY_WEEK, EVERY_MONTH, EVERY_YEAR
    }
    
    public enum Task {
        CALL_CUSTOMER("Call customer"),
        EMAIL_CUSTOMER("Email customer"),
        TASK_X("Task X"),
        TASK_Y("Task Y"),
        TASK_Z("Task Z");

        private Task(String displayString) {
            this.displayString = displayString;
        }

        public static Task findByDisplayString(final String displayString) {
            Task result = null;

            for (Task type : Task.values()) {
                if (displayString.equals(type.getDisplayString())) {
                    result = type;
                    break;
                }
            }
            return result;
        }

        public String getDisplayString() {
            return displayString;
        }
        private final String displayString;
    }
    
    public enum ScheduleStatus {
        ASSIGNED, IN_PROGRESS, DONE
    }
    
    public enum AddressType {
        REGULAR, POBOX
    }
    
    public enum CustomerType {
        DEALER, FINANCE_COMPANY, BOTH
    }
    
    public enum CustomerStatus {
        Open, Attempted, Contact_Opportunity, Contact_Qualified, Contact_Not, Contact_Interested, Contace_Unqualified, File_Pending
    }
    
    public enum CustomerAddressType {
        REGULAR, POBOX
    }
    
    public enum CustomerSortBy {
        Id, Name, Type, Status
    }
    
    public enum DealerDetailsFileColumn {

        DELEAR_NAME("Dealer Name"),
        LENDER_NAME("Lender Name"),
        DEALER_COUNTY("Dealer County"),
        DEALER_ADDRESS("Dealer Address"),
        DEALER_CITY("Dealer City"),
        DEALER_STATE("Dealer State"),
        DEALER_ZIP_CODE("Dealer Zip Code"),
        TOTAL_FINANCED("Total Financed"),
        TOTAL_FINANCED_PERCENT("Total Financed %"),
        TOTAL_LOAN("Total "),
        TOTAL_LOAN_PERCENT("Total Financed %"),
        NEW_LOAN("Total Financed"),
        NEW_LOAN_PERCENT("Total Financed %"),
        USED_LOAN("Total Financed"),
        USED_LOAN_PERCENT("Total Financed %"),
        TOTAL_LEASE("Total "),
        TOTAL_LEASE_PERCENT("Total Financed %"),
        NEW_LEASE("Total Financed"),
        NEW_LEASE_PERCENT("Total Financed %"),
        USED_LEASE("Total Financed"),
        USED_LEASE_PERCENT("Total Financed %"),
        TOTAL_NO_LENDER("Total "),
        TOTAL_NO_LENDER_PERCENT("Total Financed %"),
        NEW_NO_LENDER("Total Financed"),
        NEW_NO_LENDER_PERCENT("Total Financed %"),
        USED_NO_LENDER("Total Financed"),
        USED_NO_LENDER_PERCENT("Total Financed %");

        private DealerDetailsFileColumn(String displayString) {
            this.displayString = displayString;
        }

        public static DealerDetailsFileColumn findByDisplayString(final String displayString) {
            DealerDetailsFileColumn result = null;

            for (DealerDetailsFileColumn type : DealerDetailsFileColumn.values()) {
                if (displayString.equals(type.getDisplayString())) {
                    result = type;
                    break;
                }
            }
            return result;
        }

        public String getDisplayString() {
            return displayString;
        }
        private final String displayString;
    }
    
    public enum ActiveStatusType {
        Active(true), Inactive(false);
        
        private ActiveStatusType(boolean value) {
            this.value = value;
        }
        
        public boolean getValue() {
            return value;
        }
        
        private final boolean value;
    }
    
    public enum LeadOwnerType {
        Internal, External
    }
}

