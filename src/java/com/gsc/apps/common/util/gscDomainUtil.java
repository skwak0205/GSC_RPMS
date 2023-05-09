/**
 * <pre>
 * DomainObject 관련 Util 기능 정의.
 * </pre>
 * 
 * @ClassName   : gscDomainUtil.java
 * @Description : DomainObject 관련 Util 기능 정의.
 * @author      : GeonHwan,Bae
 * @since       : 2020-06-15
 * @version     : 1.0
 * @see
 * @Modification Information
 * <pre>
 * since          author         description
 * ============   ============   ===================================
 * 2020-06-15     GeonHwan,Bae   최초 생성
 * </pre>
 */

package com.gsc.apps.common.util;

import com.gsc.apps.app.util.gscMQLConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.MqlUtil;
import matrix.db.Attribute;
import matrix.db.AttributeList;
import matrix.db.AttributeType;
import matrix.db.Context;

import java.util.HashMap;

public class gscDomainUtil {
    
    public gscDomainUtil() {
    }
    
    /**
     * <pre>
     * Type,Name,Revision으로 Object Id를 반환 
     * </pre>
     * 
     * @param context
     * @param strType
     * @param strName
     * @param strRevision
     * @return
     */
    public static String getObjectId(Context context, String strType, String strName, String strRevision) {
        String strObjectId = "";
        try {
            
            if (isExists(context, strType, strName, strRevision))
                strObjectId = MqlUtil.mqlCommand(context, gscMQLConstants.PRINT_BUS_TNR1, strType, strName, strRevision, "id", "|");
            else 
                strObjectId = "";
        } catch(Exception ex) {
            strObjectId = "";
        }
        
        return strObjectId;
    }
    
    /**
     * <pre>
     * Type,Name,Revision으로 Object 존재 여부 반환  
     * </pre>
     * 
     * @param context
     * @param strType
     * @param strName
     * @param strRevision
     * @return
     */
    public static boolean isExists(Context context, String strType, String strName, String strRevision) {
        boolean bleExists = false;
        try {
            String strExists = MqlUtil.mqlCommand(context, gscMQLConstants.PRINT_BUS_TNR1, strType, strName, strRevision, "exists", "|");
            
            bleExists = "TRUE".equalsIgnoreCase(strExists);
            
        } catch(Exception ex) {
            bleExists = false;
        }
        
        return bleExists;
    }


    /**
     * <pre>
     * objectId으로 Object 존재 여부 반환
     * </pre>
     * @param context the eMatrix <code>Context</code> object
     * @param objectId object ID
     * @return object 존재 유무
     */
    public static boolean isExists(Context context, String objectId) {
        boolean bleExists ;
        try {
            String strExists = MqlUtil.mqlCommand(context, gscMQLConstants.PRINT_BUS_ID1, objectId, "exists", "|");
            bleExists = "TRUE".equalsIgnoreCase(strExists);
        } catch (Exception ex) {
            bleExists = false;
        }
        return bleExists;
    }
    
    /**
     * <pre>
     * Session 에서 프로세스 진행 정보를 가지고 오는 메뉴스  
     * </pre>
     * 
     * @param sessTimeName
     * @return
     */
    public static String getProgressValue(String sessTimeName) {
        String strPer = "";
        try {            
            String strTotalName = sessTimeName + "T";
            String strCurrentName = sessTimeName + "C"; 
            int sessTotal = (int)gscSessionUtil.get(strTotalName);
            Object objCurrent = gscSessionUtil.get(strCurrentName);
            if(objCurrent != null || objCurrent.toString().length() != 0) {
                int current = (int)objCurrent;
                int iPer = (int)(Math.round(((double)(current+1) / (double)sessTotal ) * 100));
                if(iPer > 100) iPer = 100;
                strPer = iPer + "";
                if("100".equals(strPer) && current < sessTotal - 1) {
                    strPer = "99";
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return strPer;
    }
    
    /**
     * <pre>
     * Session 에 프로세스 진행 Total 진행 시간을 setting 
     * </pre>
     * 
     * @param sessTimeName
     * @param total
     * @return
     */
    public static void setProgressTotal(String sessTimeName, int total) {
        try {
            String strTotalName = sessTimeName + "T";
            gscSessionUtil.add(strTotalName, total);
        } catch(Exception e) {
            //e.printStackTrace();
        }
    }
    
    /**
     * <pre>
     * Session 에 프로세스  진행 시간을 setting 
     * </pre>
     * 
     * @param context
     * @param sessTimeName
     * @param current
     * @param total
     * @return
     */
    public static void setProgressValue(Context context, String sessTimeName, int current, int total) {
        try {
            String strPer = Math.round(((double)(current+1) / (double)total ) * 100) + "";
            gscSessionUtil.add(sessTimeName, strPer);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * <pre>
     * Session 에 프로세스  진행 시간을 setting 
     * </pre>
     * 
     * @param sessTimeName
     * @param txt
     * @return
     */
    public static void setProgressValue(String sessTimeName, String txt) {
        try {
            String strTotalName = sessTimeName + "T";
            String strCurrentName = sessTimeName + "C"; 
            String strTxtName = sessTimeName + "TXT";
            int sessTotal = (int)gscSessionUtil.get(strTotalName);
            int current = 0;
            Object objCurrent = gscSessionUtil.get(strCurrentName);
            if(objCurrent == null || objCurrent.toString().length() == 0) {
                gscSessionUtil.add(strCurrentName, current);
            } else {
                current = (int)objCurrent;
                gscSessionUtil.add(strCurrentName, current + 1);
            }
            int iPer = (int)(Math.round(((double)(current + 1) / (double)sessTotal ) * 100));
            if(iPer > 100) iPer = 100;
            String strPer = iPer + "";
            if("100".equals(strPer) && current < sessTotal - 1) {
                strPer = "99";
            }
            gscSessionUtil.add(strTxtName, txt);
            gscSessionUtil.add(sessTimeName, strPer);
        } catch(Exception e) {
        }
    }
    /**
     * <pre>
     * Session 에 프로세스  진행 시간을 setting 
     * </pre>
     * 
     * @param sessTimeName
     * @param add
     * @return
     */
    public static void addProgressTotal(String sessTimeName, int add) {
        try {
            String strTotalName = sessTimeName + "T";
            String strTotal = (gscSessionUtil.get(strTotalName) != null ? gscSessionUtil.get(strTotalName).toString():"0");
            int total = Integer.parseInt(strTotal) + add;
            gscSessionUtil.add(strTotalName, total);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     * Set Multi Attribute Value
     * </pre>
     *
     * @since : 2020-07-09
     * @param context
     * @param domObj
     * @param attr
     * @param attrValues
     * @throws Exception
     */
     public static void setAttributeValueForMulti(Context context, DomainObject domObj, String attr, String[] attrValues) throws Exception {
        try {
            if(attrValues == null || attrValues.length == 0) return;

            AttributeType attrType = new AttributeType(attr);
            AttributeList attrList = new AttributeList();
            HashMap attrMap=new HashMap();
            for(int i=0; i<attrValues.length; i++) {
                String attrValue = attrValues[i];
                attrMap.put(i+1, attrValue);
            }

            Attribute attribute = new Attribute(attrType, attrMap);
            attrList.addElement(attribute);
            domObj.setAttributeValues(context, attrList);
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
     
    /**
     * <pre>
     * Relationship 연결 여부 확인 
     * </pre>
     * 
     * @param context
     * @param strRelType
     * @param strTargetObjectId
     * @param strFindObjectId
     * @param bleIsFrom
     * @return boolean
     * @throws FrameworkException
     */
    public static boolean isConnect(Context context, String strRelType, String strTargetObjectId, String strFindObjectId, boolean bleIsFrom) throws FrameworkException {
        boolean bleCheckDuplication = false;
        StringBuilder sbuSelectExpression = new StringBuilder();
        
        if (bleIsFrom) {
            sbuSelectExpression.append("from[");
        } else {
            sbuSelectExpression.append("to[");
        }
        
        sbuSelectExpression.append(strRelType);
        sbuSelectExpression.append("|");
        
        if (bleIsFrom) {
            sbuSelectExpression.append("to.id=='");
        } else {
            sbuSelectExpression.append("from.id=='");
        }
        
        sbuSelectExpression.append(strFindObjectId);
        sbuSelectExpression.append("'");
        sbuSelectExpression.append("]");
        
        String strResult = MqlUtil.mqlCommand(context, gscMQLConstants.PRINT_BUS_ID1, true, strTargetObjectId, sbuSelectExpression.toString(), "|");
        if ("TRUE".equalsIgnoreCase(strResult)) {
            bleCheckDuplication = true;
        }
        
        return bleCheckDuplication;
    }
}
