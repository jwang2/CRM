package com.autopay.crm.user;

import com.autopay.commons.bean.Capitals;
import com.autopay.crm.bean.util.JsfUtil;
import com.autopay.crm.model.dealer.User;
import com.autopay.crm.session.CompanyFacade;
import com.autopay.crm.session.UserFacade;
import java.io.Serializable;
import java.security.Principal;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    private static final Logger log = Logger.getLogger(LoginBean.class);
    private final String PENDING_ACCOUNT = "You Account is in the process of being approved";
    private final String BAD_LOGIN = "Username or Password is invalid";
    private final String DEALER = "DEALER";
    private final String BROKER = "BROKER";
    private boolean loggedIn = false;
    @EJB
    UserFacade ejbFacade;
    @EJB
    CompanyFacade ejbCompany;
//    @ManagedProperty(value = "#{inplaceSelectBean}")
//    private InplaceSelectBean inPlaceSelectBean;
    //
    private User user = new User();
    private User retrivedUser;
    private String requestedURI;
    private String timeZone;
    
    private String userName;
    private String password;

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * jaas login
     */
    public String login() {
        if (loggedIn) {
            return requestedURI;
        }
        FacesContext ctx = FacesContext.getCurrentInstance();
//        ExternalContext cntxt = ctx.getExternalContext();
//        HttpServletRequest req = (HttpServletRequest) cntxt.getRequest();
        HttpServletRequest req = getRequest();

        try {
            //req.login(this.user.getUsername(), this.user.getPasswordHash());
            req.login(userName, password);
            //log.info(">>> user logged in: " + this.user.getUsername());
           // init();
            if (this.user.getStatus() != null && !this.user.getStatus().equalsIgnoreCase("ACTIVE")) {
                // FacesMessage facesMessages = new FacesMessage(PENDING_ACCOUNT);
                // ctx.addMessage(null, facesMessages);
                JsfUtil.addErrorMessage(PENDING_ACCOUNT);
                log.debug("Status: " + this.user.getStatus());
                logout();
                return "/public/welcome.xhtml";
            }

            loggedIn = true;

            String next = getPage();


            return next;
        } catch (Exception e) {
            log.error("Error:", e);
            log.error(String.format("login failed. user: %s, due to: %s ",
                    this.user.getUsername(), e.getMessage()));
            //FacesMessage facesMessages = new FacesMessage(BAD_LOGIN);
            JsfUtil.addErrorMessage(BAD_LOGIN);
            //ctx.addMessage(null, facesMessages);
        }

        return "/public/welcome.xhtml";
    }
    
//    public boolean isBulkOnly() {
//        if (!isDealer()) {
//            return false;
//        }
//       return user.getCompanyId().getBulk() && !user.getCompanyId().getLoan();
//    }
    private String getPage() {
        String p = "/pages/dashboard/DashboardPage.xhtml";
//        if (user.getCompanyId() == null) {
//            return p;
//        }
//
//        if (user.getCompanyId().getLoan() != null
//                && user.getCompanyId().getLoan()) {
//            //p = "/secure/index.xhtml";
//            return p;
//        } else if (user.getCompanyId().getBulk() != null
//                || user.getCompanyId().getBulk()) {
//            //p = "/secure/portfolio/uploadFile.xhtml";
//            return p;
//        }

        return p;

    }

    /**
     * jaas logout
     */
    public String logout() {

//        ExternalContext cntxt = FacesContext.getCurrentInstance().getExternalContext();
//        HttpServletRequest req = (HttpServletRequest) cntxt.getRequest();
        HttpServletRequest req = getRequest();
        Principal pp = req.getUserPrincipal();
        String aname = pp.getName();

        try {
            req.logout();
            cleanup();
            loggedIn = false;
            log.info(">>> user logged out: " + aname);
        } catch (Exception e) {
            log.error(String.format("Error logout user %s, due to: %s",
                    aname, e.getMessage()));
        }

        return "/public/welcome.xhtml";
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void cleanup() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
//        session.removeAttribute("loginBean");
//        session.removeAttribute("loanApplicationController");
//        session.removeAttribute("loanApplicationComakerController");
//        session.removeAttribute("vehicleApplicationController");
        session.invalidate();
    }

    public boolean isSuperUser() {
        boolean result;
        if (!loggedIn) {
            return false;
        }
        if (getRequest().isUserInRole("ADMIN") || getRequest().isUserInRole("AP_ADMIN") || getRequest().isUserInRole("AP_BIZ_DEV") || getRequest().isUserInRole("AP_BIZ_DEV_MGR")) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }
    
    public boolean isDealer() {
        boolean dealer = true;
        if (!loggedIn) {
            return false;
        }
        if (getRequest().isUserInRole("DEALER_LOAN")
                || getRequest().isUserInRole("DEALER_BULK")
                || getRequest().isUserInRole("BROKER_R")
                || getRequest().isUserInRole("BROKER_RW")) {
            dealer = true;
        } else {
            dealer = false;
        }

        return dealer;
    }

    public boolean isWrite() {
        boolean write = false;

        if (getRequest().isUserInRole("ADMIN")
                || getRequest().isUserInRole("AP_ADMIN")
                || getRequest().isUserInRole("AP_ANALYTICS")
                || getRequest().isUserInRole("AP_AQUISITION")
                || getRequest().isUserInRole("AP_BIZ_DEV")) {
            write = true;
        } else {
            write = false;
        }

        return write;

    }

    public boolean isPOSParamAdmin() {
        boolean pos = false;

        if (getRequest().isUserInRole("ADMIN")
                || getRequest().isUserInRole("AP_ANALYTICS")) {
            pos = true;
        } else {
            pos = false;
        }

        return pos;
    }

    public boolean isAdmin() {
        return getRequest().isUserInRole("ADMIN");
    }

    public boolean isInRole(String r) {
        return getRequest().isUserInRole(r);
    }

    private HttpServletRequest getRequest() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext cntxt = ctx.getExternalContext();
        HttpServletRequest req = (HttpServletRequest) cntxt.getRequest();
        return req;
    }

    private void init() {
        retrivedUser = ejbFacade.findUser(userName); //this.user.getUsername());
        System.out.println("===== retrivedUser: " + retrivedUser);
        if (retrivedUser != null) {
            user.setCompanyId(retrivedUser.getCompanyId());
            user.setFirstName(retrivedUser.getFirstName());
            user.setLastName(retrivedUser.getLastName());
            user.setStatus(retrivedUser.getStatus());
            user.setRoleCollection(retrivedUser.getRoleCollection());
//            Capitals capital = null;
//            try {
//                String state = user.getCompanyId().getPostalAddressId().getState();
//                capital = inPlaceSelectBean.getCapital(state);
//
//            } catch (Exception e) {
//                timeZone = "GMT-6";
//            }
//            setupTimeZone(capital);

        } else {
            throw new NullPointerException("USER IS NULL");
        }
    }

    private void setupTimeZone(Capitals capital) {
        if (capital != null) {
            timeZone = capital.getTimeZone();
        } else {
            timeZone = "MST";
        }

        try {
            TimeZone tz = TimeZone.getTimeZone(timeZone);
            DateTimeZone zone = DateTimeZone.forTimeZone(tz);


            //Date dt = new Date();
            DateTime dt = DateTime.now();
            zone = dt.getZone();
            long l = dt.getMillis();
            //
            int offset = zone.getOffset(l) / 3600000;
            log.debug("TimeZone Offset: " + offset);
            // 
            timeZone = "GMT" + offset;
        } catch (Exception e) {
            log.error(e);
        }
    }

    public User getRetrivedUser() {
        return retrivedUser;
    }

    public void setRetrivedUser(User retrivedUser) {
        this.retrivedUser = retrivedUser;
    }

    @PostConstruct
    public void initPage() {
        requestedURI = getRequest().getRequestURI();
        if (requestedURI == null || requestedURI.equals("/") || requestedURI.contains("sign")) {
            requestedURI = "/pages/loanApplication/List.xhtml";
        }
    }

//    public InplaceSelectBean getInPlaceSelectBean() {
//        return inPlaceSelectBean;
//    }
//
//    public void setInPlaceSelectBean(InplaceSelectBean inPlaceSelectBean) {
//        this.inPlaceSelectBean = inPlaceSelectBean;
//    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}

