package matrix.admin;

import java.util.HashMap;
import java.util.Iterator;

import matrix.db.Context;
import matrix.db.MQLCommand;
import matrix.util.StringList;

import com.matrixone.apps.domain.util.FrameworkUtil;

public class TableColumn{
	private Table   table;
	private String	type	;
	private String	name	;
	private String	description	;
	private HashMap	property	;
	private HashMap	setting	;
	private String	hidden	;
	private String	id	;
	private String	label	;
	private String	number	;
	private String	value	;
	private String	selectedvalue	;
	private String	expression	;
	private String	expressiontype	;
	private String 	editable;
	private String  isderived;
	private String  multiline;
	private String  autowidth;
	private String  autoheight;
	private String	href;
	private String	alt	;
	private String	range;
	private String  update;
	private StringList user = new StringList();
	private Context context;
	public TableColumn()
	{

	}

	public TableColumn(Context context,Table table,String name)
	{
		try
		{
			this.context =  context;
			this.table = table;
			setType("column");
			setName(name);
			setDescription();
			setProperty();
			setSetting();
			setHidden();
			setLabel();
			setHref();
			setAlt();
			setUser();
			setNumber();
			setValue();
			setSelectedvalue();
			setExpression();
			setExpressiontype();
			setEditable();
			setIsderived();
			setMultiline();
			setAutowidth();
			setAutoheight();
			setRange();
			setUpdate();
			setUser();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void setHidden()
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
	public void setId()
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
	public void setLabel()
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
	public void setHref() 
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
	public void setAlt() 
	throws Exception
	{
		this.alt = getValue(context,"alt");
	}
	public String getAlt() {
		return alt;
	}
	public void setUser(StringList user) {
		this.user = user;
	}
	public void setUser() 
	throws Exception
	{
		MQLCommand mqlCommand = new MQLCommand();
		String command = "print table "+table.getName() + " select column['"+getName() + "'].user dump |";
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
	public String toMqlString()
	{
		StringBuffer buf = new StringBuffer();
		try
		{
			buf.append("column ").append("#").append(getNumber()).append("\n");
			buf.append("\t").append("name\t").append(quote(getName())).append("\n");
			if(!"".equals(getExpression()))
				buf.append("\t").append(getExpressiontype()+"\t").append(quote(getExpression())).append("\n");
			if(!"".equals(getLabel()))
				buf.append("\t").append("label\t").append(quote(getLabel())).append("\n");
			buf.append("\t").append("autoheight\t").append(quote(getAutoheight())).append("\n");
			buf.append("\t").append("autowidth\t").append(quote(getAutowidth())).append("\n");
			buf.append("\t").append("edit\t").append(quote(getEditable())).append("\n");
			buf.append("\t").append("href\t").append(quote(getHref())).append("\n");
			buf.append("\t").append("range\t").append(quote(getRange())).append("\n");
//			buf.append("\t").append("hidden\t").append(quote(getHidden())).append("\n");
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
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return buf.toString();
	}

	public void setNumber() 
	throws Exception
	{
		this.number = getValue(context,"number");
	}

	public String getNumber() {
		return number;
	}

	public void setValue() 
	throws Exception
	{
		this.value = getValue(context,"value");
	}

	public String getValue() {
		return value;
	}

	private void setSelectedvalue()
	throws Exception	
	{
		this.selectedvalue = getValue(context,"selectedvalue");
	}

	private String getSelectedvalue() {
		return selectedvalue;
	}

	public void setExpression() 
	throws Exception
	{
		this.expression = getValue(context,"expression");
	}

	public String getExpression() {
		return expression;
	}

	public void setExpressiontype() 
	throws Exception
	{
		this.expressiontype = getValue(context,"expressiontype");
	}

	public String getExpressiontype() {
		return expressiontype;
	}

	public void setEditable() 
	throws Exception
	{
		this.editable = getValue(context,"editable");
	}

	public String getEditable() {
		return editable;
	}

	public void setIsderived() 
	throws Exception
	{
		this.isderived = getValue(context,"isderived");
	}

	public String getIsderived() {
		return isderived;
	}

	private void setMultiline() 
	throws Exception
	{
		this.multiline = getValue(context,"multiline");
	}

	private String getMultiline() {
		return multiline;
	}

	public void setAutowidth() 
	throws Exception
	{
		this.autowidth = getValue(context,"autowidth");
	}

	public String getAutowidth() {
		return autowidth;
	}

	public void setAutoheight() 
	throws Exception
	{
		this.autoheight = getValue(context,"autoheight");
	}

	public String getAutoheight() {
		return autoheight;
	}

	public void setRange() 
	throws Exception
	{
		this.range = getValue(context,"range");
	}

	public String getRange() {
		return range;
	}

	public void setUpdate() 
	throws Exception
	{
		this.update = getValue(context,"update");
	}

	public String getUpdate() {
		return update;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDescription() 
	throws Exception
	{
		this.description = getValue(context,"description");
	}

	public String getDescription() {
		return description;
	}

	public void setProperty()
	throws Exception
	{
		MQLCommand mqlCommand = new MQLCommand();
		String system = "";
		if("table".equals(type))
			system = "system";
		String command = "print table " + table.getName() + " system select column[" + getName() + "].property dump |";
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
	public HashMap getProperty() {
		return property;
	}


	public HashMap getSetting() {
		return setting;
	}
	public void setSetting()
	throws Exception
	{
		System.out.println("setSetting start");
		MQLCommand mqlCommand = new MQLCommand();
		String system = "";
		if("table".equals(type))
			system = "system";
		String command = "print table " + table.getName() + " system select column[" + getName() + "].setting dump |";
		System.out.println("command=" + command);
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
		System.out.println("result=" + result);
		String command2 = "print table " + table.getName() + " system select column[" + getName() + "].setting.*";
		System.out.println("command2=" + command2);
		boolean executeOk2 = mqlCommand.executeCommand(context, command2);
		String result2;

		if (executeOk2 == true)
			result2 = mqlCommand.getResult();
		else
		{
			result2 = mqlCommand.getError();
			return;
		}
		System.out.println("result2=" + result2);
		HashMap settingMap = new HashMap();
		StringList settingList = FrameworkUtil.split(result2,"\n");
		for(int i=0; i < settingList.size(); i++)
		{
			String strSetting = (String)settingList.get(i);
			if(strSetting.indexOf("=") > 0)
			{
				if(strSetting.indexOf("value") > 0)
				{
					System.out.println("strSetting=" + strSetting);
					int ind = strSetting.indexOf("=");
					settingMap.put(strSetting.substring(0, ind).trim(), strSetting.substring(ind+1).trim());
				}
			}
		}
		System.out.println("settingMap=" + settingMap);
		HashMap _setting = new HashMap();
		settingList = FrameworkUtil.split(result,"|");
		for(int i=0 ; i < settingList.size(); i++)
		{
			String strSetting = (String)settingList.get(i);
			String settingValue = "";//settingValue = getValue(context,settingName);
			if(settingValue == null || "".equals(settingValue))
				settingValue = (String)settingMap.get("column[" + getName() + "].setting["+strSetting+"].value");
			_setting.put(strSetting.trim(), settingValue.trim());
		}
		System.out.println("_setting=" + _setting);
		setting = _setting;
	}
	public String getValue(Context context,String attr)
	throws Exception
	{
		MQLCommand mqlCommand = new MQLCommand();
		String command = "print table " + table.getName() + " system select column[" + getName() + "]."+ attr +" dump";
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
	public String quote(String str)
	{
		return "'"+str+"'";
	}

}
