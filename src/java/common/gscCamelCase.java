package common;

import java.util.HashMap;

public class gscCamelCase {

    public static HashMap<String, String> gscToPascalCase(String InputText) {

        HashMap<String, String> gscToPascalSpace = new HashMap<>();

        gscToPascalSpace.put("ToPascalText",InputText.substring(0,1).toUpperCase()+InputText.substring(1));
        gscToPascalSpace.put("InputText",InputText);

        return gscToPascalSpace;
    }

    public static HashMap<String, String> gscToPascalSpaceCase(String InputText) {

        HashMap<String, String> gscToPascalSpace = new HashMap<>();

        String OuputText;

        // 첫 문자 제외한 문자열 확인
        String InputTextExcludeFirst = InputText.substring(1);

        // 첫 문자 제외한 문자열 중 대문자 경우 수 확인
        int CountUpperCase = gscCommon.gscCheckUpperCase(InputTextExcludeFirst);

        // 대문자가 하나 이상일 경우, 실행
        if(CountUpperCase == 0){
            OuputText =  InputText.substring(0,1).toUpperCase()+InputText.substring(1);
        }else if(CountUpperCase > 0) {
            // 제외하였던 첫 문자열 + FromCamelCase 문자열
            OuputText = InputText.substring(0,1).toUpperCase() + gscCommon.gscAddSpaceUpperCase(CountUpperCase,InputTextExcludeFirst);
        }else {
            OuputText = InputText;
        }

        gscToPascalSpace.put("ToPascalText",OuputText);
        gscToPascalSpace.put("InputText",InputText);

        return gscToPascalSpace;
    }

    public static HashMap<String, String> gscToPascalSpaceCaseExcludeFirst(String InputText) {

        HashMap<String, String> gscToPascalSpace = new HashMap<>();

        String OuputText;

        // 첫 문자 제외한 문자열 확인
        String InputTextExcludeFirst = InputText.substring(1);

        // 첫 문자 제외한 문자열 중 대문자 경우 수 확인
        int CountUpperCase = gscCommon.gscCheckUpperCase(InputTextExcludeFirst);

        // 대문자가 하나 이상일 경우, 실행
        if(CountUpperCase > 0) {
            // 제외하였던 첫 문자열 + FromCamelCase 문자열
            OuputText = InputText.substring(0,1) + gscCommon.gscAddSpaceUpperCase(CountUpperCase,InputTextExcludeFirst);
        }else {
            OuputText = InputText;
        }

        gscToPascalSpace.put("ToPascalText",OuputText);
        gscToPascalSpace.put("InputText",InputText);

        return gscToPascalSpace;
    }

}
