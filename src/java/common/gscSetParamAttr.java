package common;

public class gscSetParamAttr {

    public static String removeLastChar(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return str.substring(0, str.length() - 1);
    }

    public static String gscSetParamAttr(String InputText) {

        if(!gscCheckBasicAttr.gscCheckPSBasicAttr(InputText)){
            if(gscCommon.gscCheckgsc(InputText)) {
                InputText = "attribute[XP_Project_Space_Ext."+InputText+"]";
            }else {
                InputText = gscCamelCase.gscToPascalSpaceCase(InputText).get("ToPascalText");
                InputText = "attribute["+InputText+"]";
            }
        }

        return InputText;
    }

    public static String gscRemoveParamAttr(String Attrkey) {

        if(gscCommon.gscCheckParamCustomAttr(Attrkey)) {
            Attrkey = Attrkey.replace("attribute[XP_Project_Space_Ext.","");
        }else if(gscCommon.gscCheckParamAttr(Attrkey)) {
            Attrkey = Attrkey.replace("attribute[","");
        }

        if(Attrkey.substring(Attrkey.length() - 1).equals("]")){
            Attrkey = removeLastChar(Attrkey);
        }

        return Attrkey;
    }
}
