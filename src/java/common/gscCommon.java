package common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class gscCommon {

    static List<String> UpperCase = new ArrayList<>(Arrays.asList(
            "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"
    ));

    public static Boolean gscCheckgsc(String InputText) {

        if(InputText.startsWith("com/gsc")) {
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }

    public static Boolean gscCheckParamAttr(String InputText) {

        if(InputText.startsWith("attribute[")) {
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }

    public static Boolean gscCheckParamCustomAttr(String InputText) {

        if(InputText.startsWith("attribute[XP_Project_Space_Ext.")) {
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }

    public static Integer gscCheckUpperCase(String InputText) {

        int gscCountUpperCase = 0;

        String[] Array_InputText = InputText.split("");

        for(int i = 0; i < Array_InputText.length; i++){

            if(UpperCase.contains(Array_InputText[i])) {
                gscCountUpperCase += 1;
            }

        }

        return gscCountUpperCase;
    }

    public static String gscAddSpaceUpperCase(Integer gscCountUpperCase, String InputText) {

        String OutPutText = InputText;
        int StartPoint = 0;

        for(int i = 0; i < gscCountUpperCase; i++){
            String[] Array_OutPutText = OutPutText.split("");
            
            // i 추가는 이전 것 다음의 문자를 조회하기 위함
            for(int z = StartPoint+i; z < Array_OutPutText.length; z++) {
                if(UpperCase.contains(Array_OutPutText[z])) {
                    OutPutText = OutPutText.substring(0,z)+" "+OutPutText.substring(z,OutPutText.length());
                    StartPoint = z+1; // Space 추가로, +1 을 하여야 본인 위치 시작
                    break;
                }
            }
        }

        return OutPutText;
    }

    public static HashMap<String, String> gscToCamelCase(String InputText) {

        HashMap<String, String> gscToCamel = new HashMap<>();

        String[] words = InputText.split("[\\W_]+");

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                word = word.isEmpty() ? word : word.toLowerCase();
            } else {
                word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
            }
            builder.append(word);
        }

        gscToCamel.put("ToCamelText",builder.toString());
        gscToCamel.put("InputText",InputText);

        return gscToCamel;
    }

    public static HashMap<String, String> gscToCamelCaseExcludeFirst(String InputText) {

        HashMap<String, String> gscToCamel = new HashMap<>();

        // 첫 글자 제외 한, 문자열 확인
        String InputTextExcludeFirst = InputText.substring(1);

        String[] words = InputTextExcludeFirst.split("[\\W_]+");

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                word = word.isEmpty() ? word : word.toLowerCase();
            } else {
                word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
            }
            builder.append(word);
        }

        gscToCamel.put("ToCamelText",InputText.substring(0,1)+builder.toString());
        gscToCamel.put("InputText",InputText);

        return gscToCamel;
    }

    public static HashMap<String, String> gscToPascalCase(String InputText) {

        HashMap<String, String> gscToPascalSpace = new HashMap<>();

        gscToPascalSpace.put("ToPascalText",InputText.substring(0,1).toUpperCase()+InputText.substring(1));
        gscToPascalSpace.put("InputText",InputText);

        return gscToPascalSpace;
    }

    public static Boolean gscChkNullorEmpty(String InputText){
        if(InputText != null && !InputText.isEmpty()){
            return false;
        }else{
            return true;
        }
    }

}
