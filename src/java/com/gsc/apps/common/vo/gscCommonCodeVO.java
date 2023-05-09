/**
 * <pre>
 * 공통 코드 Value Object Class
 * </pre>
 * 
 * @ClassName   : emdCommonCodeVO.java
 * @Description : 공통 코드 Value Object Class
 * @author      : GeonHwan,Bae
 * @since       : 2020-06-10
 * @version     : 1.0
 * @see
 * @Modification Information
 * <pre>
 * since          author         description
 * ============   ============   ===================================
 * 2020-06-10     GeonHwan,Bae   최초 생성
 * 2020-09-11     BongJun,Park   Description 추가
 * </pre>
 */

package com.gsc.apps.common.vo;

import com.gsc.apps.common.util.gscStringUtil;

public class gscCommonCodeVO {
    private String _masterCode;
    private String _code;
    private String _title;
    private String _titleKo;
    private String _description;

    /**
     * <pre>
     * 마스터 코드 세팅 Setter 
     * </pre>
     * 
    */
    public void setMasterCode(String pMasterCode) {
        this._masterCode = pMasterCode;
    }

    /**
     * <pre>
     * 마스터 값을 반환하는 Getter 
     * </pre>
     * 
     */
    public String getMasterCode() {
        return this._masterCode;
    }
    
    /**
     * <pre>
     * 코드 세팅 Setter 
     * </pre>
     * 
     * @param pCode
     */
    public void setCode(String pCode) {
        this._code = pCode;
        this._title = pCode;
    }

    /**
     * <pre>
     * 코드 값을 반환하는 Getter 
     * </pre>
     * 
     * @return
     */
    public String getCode() {
        return this._code;
    }
    
    /**
     * <pre>
     * 코드의 영문 Display Setter 
     * </pre>
     * 
     * @param pTitle
     */
    public void setTitle(String pTitle) {
        this._title = gscStringUtil.NVL(pTitle);
    }
    
    /**
     * <pre>
     * 코드의 영문 Display 명을 반환하는 Getter 
     * </pre>
     * 
     * @return
     */
    public String getTitle() {
        return this._title;
    }
    
    /**
     * <pre>
     * 코드의 한글 Display Setter 
     * </pre>
     * 
     * @param pTitleKo
     */
    public void setTitleKo(String pTitleKo) {
        this._titleKo = gscStringUtil.NVL(pTitleKo);
    }
    
    /**
     * <pre>
     * 코드의 한글 Display 명을 반환하는 Getter 
     * </pre>
     * 
     * @return
     */
    public String getTitleKo() {
        return this._titleKo;
    }
    
        
    /**
     * <pre>
     * 언어에 따라 코드의 Display 명을 반환하는 Getter 
     * </pre>
     * 
     * @return
     */
    public String getTitle(String strLanguage) {
        
        String strReturnTitle = "";
        
        if (strLanguage != null && strLanguage.startsWith("ko"))
            strReturnTitle = this._titleKo;
        
        if ("".equals(strReturnTitle))
            strReturnTitle =  this._title;
        
        return strReturnTitle;
    }

    /**
     * <pre>
     * 코드의 Description Setter
     * </pre>
     *
     * @param pDescription
     */
    public void setDescription(String pDescription) {
        this._description = gscStringUtil.NVL(pDescription);
    }
    /**
     * <pre>
     * 코드의 Description Getter
     * </pre>
     *
     * @return
     */
    public String getDescription() {
        return this._description;
    }
}
