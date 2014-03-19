package com.autopay.crm.bean.util;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author judy
 */
public class SessionTimeoutFilter implements Filter {

    private final Logger log = Logger.getLogger(SessionTimeoutFilter.class);
    private String startPage = "/CRM/";
    private String timeoutPage = "public/welcome.xhtml";
    private static final String pushServletURLMapping = "__richfaces_push"; 
    private ConcurrentHashMap<String, Long> sessionLastAccessedMap = new ConcurrentHashMap<String, Long>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //logger.info("init()");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
//            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//
//            // is session expire control required for this request?
//            if (isSessionControlRequiredForThisResource(httpServletRequest)) {
//
//                // is session invalid?
//                if (isSessionInvalid(httpServletRequest)) {
//                    String timeoutUrl = httpServletRequest.getContextPath() + "/" + getTimeoutPage();
//                    logger.info("session is invalid! redirecting to timeoutpage : " + timeoutUrl);
//
//                    httpServletResponse.sendRedirect(timeoutUrl);
//                    return;
//                }
//            }
//        }
//        chain.doFilter(request, response);
        
        if (request instanceof HttpServletRequest == true)  
        {              
            HttpServletRequest httpServletRequest = (HttpServletRequest)request;  
  
            HttpSession session = httpServletRequest.getSession(false);  
            
            if (session != null) {
                String sessionId = session.getId(); 
                //logger.info("session id = " + sessionId);
                Integer sessionMaxActiveInterval = session.getMaxInactiveInterval();  // seconds  
                String requestURI = httpServletRequest.getRequestURI();  
                //logger.info("requestURI = " + requestURI);
                Long now = new Date().getTime(); // milliseconds  

                // do a manual timeout check, regardless of the request  
                if (sessionLastAccessedMap.containsKey(sessionId) == true)  
                {  
                    Long lastAccessed = sessionLastAccessedMap.get(sessionId);  

                    // if the difference between now and lastAccessed is >= the maxActiveInterval, invalidate the session  
                    long millisecondsDifference = now - lastAccessed;  

                    //long secondsDifference = millisecondsDifference/1000;  
                    long secondsDifference = TimeUnit.SECONDS.convert(millisecondsDifference, TimeUnit.MILLISECONDS);  

                    // if the difference between now and lastAccessed is >= the maxActiveInterval, invalidate the session  
                    if (secondsDifference >= sessionMaxActiveInterval.longValue() == true)  
                    {  
                        log.info("timeout reached, invalidating session: " + sessionId);  
                        session.invalidate();  
                    }                  
                }  

                if (StringUtils.contains(requestURI, pushServletURLMapping) == false)  
                {  
                    // if NOT a request for the push servlet, update the last accessed map  
                    sessionLastAccessedMap.put(sessionId, now);  
                }  
            } else {
                //session is null;
//                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//                httpServletResponse.sendRedirect("/public/sessionExpire.xhtml");
            }
  
        }  
  
        chain.doFilter(request, response);  
    }

    private boolean isSessionControlRequiredForThisResource(HttpServletRequest httpServletRequest) {
        String requestPath = httpServletRequest.getRequestURI();
        log.info("======= requestPath: " + requestPath);
        boolean controlRequired = requestPath.equals(getStartPage());
        if (controlRequired) {
            return false;
        }
        controlRequired = !StringUtils.contains(requestPath, getTimeoutPage());
        log.info("=========== controlRequired: " + controlRequired);
        return controlRequired;
    }

    private boolean isSessionInvalid(HttpServletRequest httpServletRequest) {
//        log.info("!!!!!!!!!!!!!111 : " + httpServletRequest.getRequestedSessionId());
//        log.info("!!!!!!!!!!!!!222 : " + httpServletRequest.isRequestedSessionIdValid());
//        String requestPath = httpServletRequest.getRequestURI();
//        log.info("======= requestPath: " + requestPath);
//        boolean controlRequired = StringUtils.contains(requestPath, getTimeoutPage());
//        log.info("======== controlRequired: " + controlRequired);
//        if (!controlRequired) {
//            return false;
//        }
        boolean sessionInValid = (httpServletRequest.getRequestedSessionId() != null)
                && !httpServletRequest.isRequestedSessionIdValid();
        log.info("############## sessionInvalid: " + sessionInValid);
        return sessionInValid;
    }

    @Override
    public void destroy() {
        log.info("destroy()");  
    }

    public String getTimeoutPage() {
        return timeoutPage;
    }

    public void setTimeoutPage(String timeoutPage) {
        this.timeoutPage = timeoutPage;
    }
    
    public String getStartPage() {
        return startPage;
    }
    
    public void setStartPage(String startPage) {
        this.startPage = startPage;
    }
}
