/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.session;

import com.autopay.crm.model.RegionArea;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Judy
 */
@Stateless
public class RegionAreaFacade extends AbstractFacade<RegionArea> {
    @PersistenceContext(unitName = "CRMPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RegionAreaFacade() {
        super(RegionArea.class);
    }
    
    public void deleteRegionAreas(final List<RegionArea> regionAreas) {
        String ids = "";
        for (RegionArea ra : regionAreas) {
            if (ids.length() == 0) {
                ids = ra.getId() + "";
            } else {
                ids = ids + ", " + ra.getId();
            }
        }
        String queryStr = "delete from region_area where id in (" + ids + ")";
        try{
            em.createNativeQuery(queryStr).executeUpdate();
        } catch (Exception e) {
            
        }
    }
}
