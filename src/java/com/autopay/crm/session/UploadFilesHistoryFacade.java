/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.session;

import com.autopay.crm.model.UploadFilesHistory;
import com.autopay.crm.util.CrmUtils;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;

/**
 *
 * @author Judy
 */
@Stateless
public class UploadFilesHistoryFacade extends AbstractFacade<UploadFilesHistory> {
    private static Logger log = Logger.getLogger(UploadFilesHistoryFacade.class);
    @PersistenceContext(unitName = "CRMPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UploadFilesHistoryFacade() {
        super(UploadFilesHistory.class);
    }
    
    public UploadFilesHistory getUploadFilesHistoryByFileName(final String fileName) {
        final String queryStr = "select * from upload_files_history where file_name = '" + fileName + "'";
        try {
            UploadFilesHistory result = (UploadFilesHistory)em.createNativeQuery(queryStr, UploadFilesHistory.class).getSingleResult();
            return result;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
    
    public UploadFilesHistory getUploadFilesHistoryByFileDate(final Date fileDate) {
        final String queryStr = "select * from upload_files_history where file_date = '" + CrmUtils.getDateString(fileDate, "yyyy-MM-dd") + "'";
        try {
            List<UploadFilesHistory> result = em.createNativeQuery(queryStr, UploadFilesHistory.class).getResultList();
            if (result == null || result.isEmpty()) {
                return null;
            } else {
                return result.get(0);
            }
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
}
