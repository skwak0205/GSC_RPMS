package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.dpm.ProjectService;
import com.dassault_systemes.enovia.dpm.ServiceUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.*;
import com.dassault_systemes.enovia.e6wv2.foundation.db.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.ArgMap;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Locale;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.time.LocalDate;
import javax.servlet.http.HttpServletRequest;

import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMapAdapter;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.RelateddataMapAdapter;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.Select;
import com.dassault_systemes.enovia.tskv2.ProjectSequence;
import com.matrixone.apps.common.Organization;
import com.matrixone.apps.common.util.ComponentsUtil;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MqlUtil;
import com.matrixone.apps.domain.util.PersonUtil;
import com.matrixone.apps.domain.util.eMatrixDateFormat;
import com.matrixone.apps.framework.ui.UIUtil;
import com.matrixone.apps.program.ProgramCentralConstants;
import com.matrixone.apps.program.ProgramCentralUtil;
import com.matrixone.apps.program.ProjectSpace;
import matrix.db.Context;
import matrix.db.JPO;
import matrix.util.StringList;

public class gscBusinessUnitService implements ServiceConstants {
    public static Datacollection getBusinessUnits(Context context, String[] args) throws FoundationException {
        ServiceParameters serviceParameters = (ServiceParameters) JPOUtil.unpackArgs(args);
        HttpServletRequest httpServletRequest = serviceParameters.getHttpRequest();
        String getServletPath = httpServletRequest.getServletPath();
        long var4 = System.currentTimeMillis();
//        com.dassault_systemes.enovia.dpm.ServiceUtil.checkLicenseDPM(context);
        Datacollection datacollection = serviceParameters.getDatacollection();
        List selects = serviceParameters.getSelects();
        ArgMap argMap = serviceParameters.getServiceArgs();
        String state = (String) argMap.get("state");

        Datacollection datacollection1;
        if(getServletPath.equals("/resources/v1/modeler/gscbusinessunits/mybusinessunit")){
            datacollection1 = gscBusinessUnit.getBusinessUnits(context,(Datacollection) null, selects, state, true);
        }else{
            if (datacollection != null && !datacollection.getDataobjects().isEmpty()) {
                datacollection1 = gscBusinessUnit.getBusinessUnits(context,datacollection, selects, state, false);
            }else{
                datacollection1 = gscBusinessUnit.getBusinessUnits(context,(Datacollection) null, selects, state, false);
            }
        }

        String var14 = String.format("%s%s (%d) ...\t", ">>> PROGRAM (SVC):  ", "getBusinessUnitss", datacollection1.getDataobjects().size());
        FoundationUtil.debug(var14, var4);
        return datacollection1;
    }

    public static void getEstimatedMemberCount(Context context, String[] args) throws Exception {
        ServiceParameters serviceParameters = (ServiceParameters)JPOUtil.unpackArgs(args);
        HttpServletRequest httpRequest = serviceParameters.getHttpRequest();
//        com.gsc.enovia.dpm.ServiceUtil.checkLicenseProject(context, httpRequest);
        List<Dataobject> dataobjectList = serviceParameters.getDatacollection().getDataobjects();
        Iterator<Dataobject> iterator = dataobjectList.iterator();

        // Dataobject 마다 실행
        while(iterator.hasNext()) {
            Dataobject dataobject = iterator.next();
            String dataobjectId = dataobject.getId();

            // 해당 부서에서 Active Member 수 조회
            // eval expr 'count TRUE' on temp query bus Person * * where '(current == Active) AND (relationship[Member].from.physicalid == 29523ABE00004538636066C800005808)';
            String strActiveMemberCount =  MqlUtil.mqlCommand(context, String.format("eval expr 'count true' on temp query bus Person * * where \"relationship[Member].from.physicalid==%s && current=='Active'\"",dataobjectId)); // 'count TRUE' 및 '' 와 같은 표현 때문에 String.format 처리
            int intActiveMemberCount = Integer.parseInt(strActiveMemberCount);

            LocalDate currentDate = LocalDate.now();
            int year = currentDate.getYear();

            // 전체 인력관리 중 해당 부서, gscYear가 당일연도,+1,+2 인 것에서
            // physicalid, gscResourceTransferType(형태-경력,전출,퇴직 등), gscResourceName(인원) 조회
            // 'Resource Request'의 'Resource Pool' Rel 인원 Count 용
            int inc_YearCount = 2;
            int dec_RPCount = 0;
            int inc_RPCount = 0;

            // 전체 인력관리 중 해당 부서, gscYear가 당일연도,+1,+2 인 것에서
            for(int index_year=0; index_year<inc_YearCount+1; index_year++){
                year = index_year + year;
                // mql 조회 - physicalid, gscResourceTransferType(형태-경력,전출,퇴직 등), gscResourceName(인원) 조회
                String strMQL =  MqlUtil.mqlCommand(context,String.format("temp query bus 'Resource Request' * * where \"attribute[gscYear]==%s && relationship[Resource Pool].to.physicalid==%s\" select physicalid attribute[gscResourceTransferType] attribute[gscResourceName] dump |",year,dataobjectId));
                if(ProgramCentralUtil.isNotNullString(strMQL)){
                    String[] strlistMQL = strMQL.split("\n");
                    for(int i=0; i<strlistMQL.length; i++){
                        String[] each_strlistMQL = strlistMQL[i].split("\\|");
                        String gscResourceTransferType = each_strlistMQL[4];
                        int gscResourceName = Integer.parseInt(each_strlistMQL[5]);

                        // RTT04 - 전출, RTT05 - 퇴직
                        if(gscResourceTransferType.equals("RTT04") || gscResourceTransferType.equals("RTT05")){
                            // 감소값 별도 연산 후, 후처리
                            dec_RPCount = dec_RPCount + gscResourceName;
                        }else{
                            // 증가값 별도 연산 후, 후처리
                            inc_RPCount = inc_RPCount + gscResourceName;
                        }
                    }
                }
            }

            int intResourcePoolMemberCount = inc_RPCount - dec_RPCount;

            int intEstimatedMemberCount = intActiveMemberCount + intResourcePoolMemberCount;

            // 각각의 field 에 값 입력
            DataelementMapAdapter.setDataelementValue(dataobject, "getActiveMemberCount", Integer.toString(intActiveMemberCount));
            DataelementMapAdapter.setDataelementValue(dataobject, "getResourcePoolMemberCount", Integer.toString(intResourcePoolMemberCount));
            DataelementMapAdapter.setDataelementValue(dataobject, "getEstimatedMemberCount", Integer.toString(intEstimatedMemberCount));
        }

    }
    public static void getLicensedProducts(Context context, String[] args) throws Exception {
        ServiceParameters serviceParameters = (ServiceParameters)JPOUtil.unpackArgs(args);
        HttpServletRequest httpRequest = serviceParameters.getHttpRequest();
//        com.gsc.enovia.dpm.ServiceUtil.checkLicenseProject(context, httpRequest);
        List<Dataobject> dataobjectList = serviceParameters.getDatacollection().getDataobjects();
        Iterator<Dataobject> iterator = dataobjectList.iterator();

        // Dataobject 마다 실행
        while(iterator.hasNext()) {
            Dataobject dataobject = iterator.next();
            String dataobjectId = dataobject.getId();
            String userid = MqlUtil.mqlCommand(context, String.format("print bus %s select name dump",dataobjectId));
            // 해당 부서에서 Active Member 수 조회
            // eval expr 'count TRUE' on temp query bus Person * * where '(current == Active) AND (relationship[Member].from.physicalid == 29523ABE00004538636066C800005808)';
            String products =  MqlUtil.mqlCommand(context, String.format("print person '%s' select product dump",userid)); // 'count TRUE' 및 '' 와 같은 표현 때문에 String.format 처리
            String preference_defaultsecuritycontext =  MqlUtil.mqlCommand(context, String.format("print person '%s' select property[preference_DefaultSecurityContext].value dump",userid));

            // 각각의 field 에 값 입력
            DataelementMapAdapter.setDataelementValue(dataobject, "products", products);
            DataelementMapAdapter.setDataelementValue(dataobject, "defaultsecuritycontext", preference_defaultsecuritycontext);
        }

    }
}
