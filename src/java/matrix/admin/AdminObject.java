package matrix.admin;

import java.util.HashMap;

import matrix.db.Context;
import matrix.db.MQLCommand;
import matrix.util.StringList;

import com.matrixone.apps.domain.util.FrameworkUtil;

public class AdminObject {
	private String	name	;
	private String	description	;
	private HashMap	property	;
	private String	modified	;
	private String	originated	;
	private HashMap	setting	;
	private String type;
	private Context context;
	public AdminObject()
	{
		
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setDescription(Context context)
	throws Exception
	{
		this.description = getValue(context,"description");
	}
	public HashMap getSetting() {
		return setting;
	}
	
	public void setSetting(HashMap setting) {
		this.setting = setting;
	}
//	public void setSetting(Context context)
//	throws Exception
//	{
//        MQLCommand mqlCommand = new MQLCommand();
//        String command = "print " + type + " " + quote(name) + " select setting dump |";
//        System.out.println("command=" + command);
//        boolean executeOk = mqlCommand.executeCommand(context, command);
//        String result;
//
//        if (executeOk == true)
//            result = mqlCommand.getResult();
//        else
//        {
//            result = mqlCommand.getError();
//            return;
//        }
//        result = result.replaceAll("\\n","");
//        //System.out.println("result=" + result+";");
//        StringList propertyList = FrameworkUtil.split(result,"|");
//        HashMap _setting = new HashMap();
//        for(int i=0 ; i < propertyList.size(); i++)
//        {
//        	String settingName = (String)propertyList.get(i);
//        	String settingValue = getValue(context,"setting["+settingName+"].value");
//        	_setting.put(settingName, settingValue);
//        }
//        this.setting = _setting;
//	}
	
	public String getDescription() {
		return description;
	}
	public void setProperty(HashMap property) {
		this.property = property;
	}
//	public void setProperty(Context context)
//	throws Exception
//	{
//        MQLCommand mqlCommand = new MQLCommand();
//        String command = "print " + getType()+ " "+quote(getName()) + " select property dump |";
//        System.out.println("command=" + command);
//        boolean executeOk = mqlCommand.executeCommand(context, command);
//        String result;
//
//        if (executeOk == true)
//            result = mqlCommand.getResult();
//        else
//        {
//            result = mqlCommand.getError();
//            return;
//        }
//        result = result.replaceAll("\\n","");
//        StringList propertyList = FrameworkUtil.split(result,"|");
//        HashMap _property = new HashMap();
//        for(int i=0 ; i < propertyList.size(); i++)
//        {
//        	String property = (String)propertyList.get(i);
//        	_property.put(property.substring(0,property.indexOf(" value ")),property.substring(property.indexOf(" value ")+7));
//        }
//		this.property = _property;
//	}
	public HashMap getProperty() {
		return property;
	}

	public void setProperty(Context context)
	throws Exception
	{
        MQLCommand mqlCommand = new MQLCommand();
        String system = "";
        if("table".equals(type))
        	system = "system";
        String command = "print " + getType() +" " + quote(name) + " " + system +" select property dump |";
        boolean executeOk = mqlCommand.executeCommand(context, command);
        String result;

        if (executeOk == true)
            result = mqlCommand.getResult();
        else
        {
            result = mqlCommand.getError();
            return;
        }
        result = result.replaceAll("\\n", "");
        StringList propertyList = FrameworkUtil.split(result,"|");
        HashMap _property = new HashMap();
        for(int i=0 ; i < propertyList.size(); i++)
        {
        	String property = (String)propertyList.get(i);
        	_property.put(property.substring(0,property.indexOf(" value ")),property.substring(property.indexOf(" value ")+7));
        }
		property = _property;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
	public void setModified(Context context)
	throws Exception
	{
		this.modified = getValue(context,"modified");
	}
	public String getModified() {
		return modified;
	}
	public void setOriginated(String originated) {
		this.originated = originated;
	}
	public void setOriginated(Context context)
	throws Exception
	{
		this.originated = getValue(context,"originated");
	}
	public String getOriginated() {
		return originated;
	}
	
	public void setSetting(Context context)
	throws Exception
	{
        MQLCommand mqlCommand = new MQLCommand();
        String system = "";
        if("table".equals(type))
        	system = "system";
       String command = "print " + getType() + " " + quote(name) + " " + system + " select setting dump |";
        boolean executeOk = mqlCommand.executeCommand(context, command);
        String result;

        if (executeOk == true)
            result = mqlCommand.getResult();
        else
        {
            result = mqlCommand.getError();
            return;
        }
        if(result != null)
        	result = result.trim().replaceAll("\\n","");
        
        StringList propertyList = FrameworkUtil.split(result,"|");
        HashMap _setting = new HashMap();
        for(int i=0 ; i < propertyList.size(); i++)
        {
        	String settingName = (String)propertyList.get(i);
        	String settingValue = getValue(context,"setting["+settingName+"].value");
        	_setting.put(settingName.trim(), settingValue.trim());
        }
        setting = _setting;
	}
	public String getValue(Context context,String attr)
	throws Exception
	{
        MQLCommand mqlCommand = new MQLCommand();
        String command = "print " + getType() + " " + quote(name)  + " select "+attr+" dump |";
        boolean executeOk = mqlCommand.executeCommand(context, command);
        String result;

        if (executeOk == true)
            result = mqlCommand.getResult();
        else
            result = mqlCommand.getError();
        if(result != null)
        	result = result.trim();
		return result;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public String quote(String str)
	{
		return "'"+str+"'";
	}
}
