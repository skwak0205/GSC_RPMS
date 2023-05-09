package matrix.admin;

import java.util.HashMap;
import java.util.Iterator;

import matrix.db.Context;
import matrix.db.MQLCommand;
import matrix.util.StringList;

import com.matrixone.apps.domain.util.FrameworkUtil;

public class Command extends AdminObject {
	private String	hidden	;
	private String	id	;
	private String	label	;
	private String	href	;
	private String	alt	;
	private String	global	;
	private String	code	;
	private StringList	user	;
	private StringList	menu	;
	private StringList	parent	;
	private Context context;
	public Command()
	{
		super();
	}
	
	public Command(Context context,String name)
	{
		try
		{
			this.context = context;
			super.setType("command");
			super.setName(name);
			super.setDescription(context);
			super.setProperty(context);
			super.setSetting(context);
			setHidden(context);
			setLabel(context);
			setHref(context);
			setAlt(context);
			setGlobal(context);
			setUser(context);
			setCode(context);
			setMenu(context);
			setParent(context);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void setHidden(String hidden) {
		this.hidden = hidden;
	}
	public void setHidden(Context context)
	throws Exception
	{
		this.hidden = getValue(context,"hidden");
	}
	public String getHidden() {
		return hidden;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setId(Context context)
	throws Exception
	{
		this.id = getValue(context,"id");
	}
	public String getId() {
		return id;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public void setLabel(Context context)
	throws Exception
	{
		this.label = getValue(context,"label");
	}
	public String getLabel() {
		return label;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public void setHref(Context context) 
	throws Exception
	{
		this.href = getValue(context,"href");
	}
	public String getHref() {
		return href;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public void setAlt(Context context) 
	throws Exception
	{
		this.alt = getValue(context,"alt");
	}
	public String getAlt() {
		return alt;
	}
	public void setGlobal(String global) {
		this.global = global;
	}
	public void setGlobal(Context context) 
	throws Exception
	{
		this.global = getValue(context,"global");
	}
	public String getGlobal() {
		return global;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setCode(Context context) 
	throws Exception
	{
		this.code = getValue(context,"code");
	}
	public String getCode() {
		return code;
	}
	public void setUser(StringList user) {
		this.user = user;
	}
	public void setUser(Context context) 
	throws Exception
	{
        MQLCommand mqlCommand = new MQLCommand();
        String command = "print command "+getName() + " select user dump |";
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
        	result = result.trim();
        StringList userList = FrameworkUtil.split(result,"|");
        this.user = userList;
	}
	public StringList getUser() {
		return user;
	}
	public void setMenu(StringList menu) {
		this.menu = menu;
	}
	public void setMenu(Context context) 
	throws Exception
	{
		String menu = getValue(context,"menu");
		this.menu = FrameworkUtil.split(menu, "|");
	}
	public StringList getMenu() {
		return menu;
	}
	public void setParent(StringList parent) {
		this.parent = parent;
	}
	public void setParent(Context context) 
	throws Exception
	{
		String parent = getValue(context,"parent");
		this.parent = FrameworkUtil.split(parent, "|");
	}
	public StringList getParent() {
		return parent;
	}
	public String menuString(Context context)
	throws Exception
	{
		StringBuffer buf = new StringBuffer();
		if(menu != null)
		{
			for(int i=0; i < menu.size(); i++)
			{
				String menuName = (String)menu.get(i);
				int order = 0;
				String command2 = "print menu " + menuName + " select command dump |";
				System.out.println("command2=" + command2);
				MQLCommand mqlCommand = new MQLCommand();
				boolean executeOk2 = mqlCommand.executeCommand(context, command2);
				String result2;

				if (executeOk2 == true)
					result2 = mqlCommand.getResult();
				else
				{
					result2 = mqlCommand.getError();
					return "";
				}
				System.out.println("result2=" + result2);
				HashMap settingMap = new HashMap();
				StringList commandList = FrameworkUtil.split(result2,"|");
				for(int j=0; j < commandList.size(); j++)
				{
					String strSetting = (String)commandList.get(j);
					if(strSetting.equals(getName()))
					{
						order = j + 1;
					}
				}

				buf.append("modify menu "+ menuName + " add command "+getName()+" order command "+getName() + " "+order+";\n");
			}
		}
		return buf.toString();
	}
	public String toMqlModifyString()
	{
		StringBuffer buf = new StringBuffer();
		try
		{
			buf.append("mod command ").append(quote(getName())).append("\n");
			buf.append("\t").append("description\t").append(quote(getDescription())).append("\n");
			buf.append("\t").append("label\t").append(quote(getLabel())).append("\n");
			buf.append("\t").append("href\t").append(quote(getHref())).append("\n");
			buf.append("\t").append("alt\t").append(quote(getAlt())).append("\n");
			//setting
			Iterator settingItr = getSetting().keySet().iterator();
			while(settingItr.hasNext())
			{
				Object key = settingItr.next();
				Object value = getSetting().get(key);
				buf.append("\t").append("add setting\t").append(quote(key.toString())).append("\t\t").append(quote(value.toString())).append("\n");
			}
			//user
			Iterator userItr = user.iterator();
			while(userItr.hasNext())
			{
				buf.append("\t").append("user\t").append(quote((String)userItr.next())).append("\n");
			}
			//property
			Iterator propertyItr = getProperty().keySet().iterator();
			while(propertyItr.hasNext())
			{
				Object key = propertyItr.next();
				Object value = getProperty().get(key);
				buf.append("\t").append("add property\t").append(quote(key.toString())).append("\tvalue\t").append(quote(value.toString())).append("\n");
			}
			buf.append(";\n");
			//add property command_mbsCustomerCarCreate on program eServiceSchemaVariableMapping.tcl to command mbsCustomerCarCreate;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return buf.toString();
	}
	public String toMqlAddString()
	{
		StringBuffer buf = new StringBuffer();
		try
		{
			buf.append("del command ").append(quote(getName())).append(";\n");
			buf.append("add command ").append(quote(getName())).append("\n");
			buf.append("\t").append("description\t").append(quote(getDescription())).append("\n");
			buf.append("\t").append("label\t").append(quote(getLabel())).append("\n");
			buf.append("\t").append("href\t").append(quote(getHref())).append("\n");
			buf.append("\t").append("alt\t").append(quote(getAlt())).append("\n");
			//setting
			Iterator settingItr = getSetting().keySet().iterator();
			while(settingItr.hasNext())
			{
				Object key = settingItr.next();
				Object value = getSetting().get(key);
				buf.append("\t").append("setting\t").append(quote(key.toString())).append("\t\t").append(quote(value.toString())).append("\n");
			}
			//user
			Iterator userItr = user.iterator();
			while(userItr.hasNext())
			{
				buf.append("\t").append("user\t").append(quote((String)userItr.next())).append("\n");
			}
			//property
			Iterator propertyItr = getProperty().keySet().iterator();
			while(propertyItr.hasNext())
			{
				Object key = propertyItr.next();
				Object value = getProperty().get(key);
				buf.append("\t").append("property\t").append(quote(key.toString())).append("\tvalue\t").append(quote(value.toString())).append("\n");
			}
			buf.append(";\n");
			buf.append(menuString(this.context));
			buf.append("#").append("channel ==>");
			for(int k = 0; k < parent.size(); k++)
			{
				buf.append((String)parent.get(k));
			}
			buf.append("\n");
			//add property command_mbsCustomerCarCreate on program eServiceSchemaVariableMapping.tcl to command mbsCustomerCarCreate;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return buf.toString();
	}
	public String toMqlString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append(toMqlAddString().toString());
		//buf.append(toMqlModifyString().toString());
		buf.append("add property command_").append(getName().replaceAll(" ","")).append(" ").append("on program eServiceSchemaVariableMapping.tcl to command ").append(quote(getName())).append(";\n");
		
		return buf.toString();
	}
}
