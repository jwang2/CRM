/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.session;

import com.autopay.crm.model.Region;
import com.autopay.crm.model.RegionArea;
import com.autopay.crm.model.RegionRep;
import com.autopay.crm.model.Representative;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Judy
 */
@Stateless
public class RegionFacade extends AbstractFacade<Region> {

    @PersistenceContext(unitName = "CRMPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RegionFacade() {
        super(Region.class);
    }

    public List<Region> getAllRegions() {
        String queryStr = "select * from region";
        try {
            List<Region> result = em.createNativeQuery(queryStr, Region.class).getResultList();
            getRegionDetail(result);
            return result;
        } catch (Exception e) {
            //log.error(e);
            return null;
        }
    }

    public void getRegionDetail(final List<Region> regions) {
        getRegionRepresentatives(regions);
        getRegionAreas(regions);
    }

    private String getRegionIDsStr(final List<Region> regions) {
        String regionIDs = "";
        for (Region region : regions) {
            if (regionIDs.length() == 0) {
                regionIDs = region.getId() + "";
            } else {
                regionIDs = regionIDs + ", " + region.getId();
            }
        }
        return regionIDs;
    }

    private void getRegionRepresentatives(final List<Region> regions) {
        String regionIDs = getRegionIDsStr(regions);
        if (regionIDs.length() > 0) {
            String queryStr = "select * from region_rep where region_id in (" + regionIDs + ")";
            List<RegionRep> result = (List<RegionRep>) em.createNativeQuery(queryStr, RegionRep.class).getResultList();
            Map<Long, List<RegionRep>> region_rep_info = new HashMap<Long, List<RegionRep>>();
            for (RegionRep regionRep : result) {
                Long regionId = regionRep.getRegionId().getId();
                List<RegionRep> repList = region_rep_info.get(regionId);
                if (repList == null) {
                    repList = new ArrayList<RegionRep>();
                    region_rep_info.put(regionId, repList);
                }
                repList.add(regionRep);

            }
            for (Region region : regions) {
                if (region_rep_info.containsKey(region.getId())) {
                    region.setRegionRepCollection(region_rep_info.get(region.getId()));
                } else {
                    region.setRegionRepCollection(new ArrayList<RegionRep>());
                }
            }
        }
    }

    private void getRegionAreas(final List<Region> regions) {
        String regionIDs = getRegionIDsStr(regions);
        if (regionIDs.length() > 0) {
            String queryStr = "select * from region_area where region_id in (" + regionIDs + ")";
            List<RegionArea> result = (List<RegionArea>) em.createNativeQuery(queryStr, RegionArea.class).getResultList();
            Map<Long, List<RegionArea>> region_area_info = new HashMap<Long, List<RegionArea>>();
            for (RegionArea regionArea : result) {
                Long regionId = regionArea.getRegionId().getId();
                List<RegionArea> areaList = region_area_info.get(regionId);
                if (areaList == null) {
                    areaList = new ArrayList<RegionArea>();
                    region_area_info.put(regionId, areaList);
                }
                areaList.add(regionArea);

            }
            for (Region region : regions) {
                if (region_area_info.containsKey(region.getId())) {
                    region.setRegionAreaCollection(region_area_info.get(region.getId()));
                } else {
                    region.setRegionAreaCollection(new ArrayList<RegionArea>());
                }
            }
        }
    }

    public List<Representative> getRegionRepresentatives(final Region region) {
        try {
            String queryStr = "select rep.* from representative rep, region_rep rr where rep.id = rr.representative_id and rr.region_id = " + region.getId();
            List<Representative> result = (List<Representative>) em.createNativeQuery(queryStr, Representative.class).getResultList();
            return result;
        } catch (Exception e) {
            //log.error(e);
            return null;
        }
    }
    
    public boolean isRegionNameExisted(final String regionName) {
        try {
            String queryStr = "select * from region where name = '" + regionName + "'";
            Region region = (Region)em.createNativeQuery(queryStr, Region.class).getSingleResult();
            if (region == null) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }
    
    public List<Region> getRegionsUserRepresent(final String userName) {
        try {
            String queryStr = "select r.* from representative rep, region_rep rr, region r where rep.username = '" + userName + "' and rep.id = rr.representative_id and rr.region_id = r.id";
            List<Region> result = (List<Region>) em.createNativeQuery(queryStr, Region.class).getResultList();
            if (result != null && !result.isEmpty()) {
                getRegionAreas(result);
            }
            return result;
        } catch (Exception e) {
            //log.error(e);
            return null;
        }
    }
}
