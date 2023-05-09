package matrix.admin;

import java.util.HashMap;
import java.util.Iterator;

import matrix.db.Context;
import matrix.db.MQLCommand;
import matrix.util.StringList;

import com.matrixone.apps.domain.util.FrameworkUtil;

public class Portal extends AdminObject {
	private String	hidden	;
	private String	id	;
	private String	href	;
	private String	label	;
	private StringList	channel = new StringList();
	private Context context;
	private String alt;
	private HashMap	setting	;
	private StringList	parent	;
	private int row = 0;
	private int column = 0;
	private StringList place = new StringList();
	private HashMap channelMap = new HashMap();
	public Portal()
	{
		super();
	}
	
	public Portal(Context context,String name)
	{
		try
		{
			this.context = context;
			super.setType("portal");
			super.setName(name);
			super.setDescription(context);
			super.setProperty(context);
			super.setSetting(context);
			setHidden(context);
			setHref(context);
			setChannel(context);
			setHref(context);
			setLabel(context);
			setPlace(context);
			setChannelMap();
//			setParent(context);
			setSetting();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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
	public String getLabel()
	{
		return this.label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public void setLabel(Context context) 
	throws Exception
	{
		this.label = getValue(context,"label");
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
	public void setChannel(StringList channel) {
		this.channel = channel;
	}
	public void setChannel(Context context) 
	throws Exception
	{
		String channel = getValue(context,"channel");
		System.out.println("channel="+channel);
		this.channel = FrameworkUtil.split(channel, "|");
	}
	public StringList getCommand() {
		return channel;
	}
	public void setSetting()
	throws Exception
	{
        MQLCommand mqlCommand = new MQLCommand();
        String system = "";
       String channel = "print portal '" + getName() + "' select setting dump |";
        boolean executeOk = mqlCommand.executeCommand(context, channel);
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
        String channel2 = "print portal " + getName() + " select setting.*";
        boolean executeOk2 = mqlCommand.executeCommand(context, channel2);
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
        StringList propertyList = FrameworkUtil.split(result,"|");
        HashMap _setting = new HashMap();
        for(int i=0 ; i < propertyList.size(); i++)
        {
        	String settingName = (String)propertyList.get(i);
        	System.out.println("settingName=" + settingName);
        	String settingValue = "";//settingValue = getValue(context,settingName);
        	if(settingValue == null || "".equals(settingValue))
        		settingValue = (String)settingMap.get("setting["+settingName+"].value");
        	_setting.put(settingName.trim(), settingValue.trim());
        }
        System.out.println("_setting=" + _setting);
        setting = _setting;
	}
	public HashMap getSetting() {
		return setting;
	}

	public String channelString(Context context)
	throws Exception
	{
		StringBuffer buf = new StringBuffer();
		if(channel != null)
		{
			for(int i=1; i <= row; i++)
			{
				buf.append("\tchannel \t");
				for(int j = 1; j <= column; j++)
				{
					String channelName = (String)channelMap.get(i+","+j);
					if(channelName != null)
						buf.append(channelName).append(",");
				}
				buf = buf.replace(buf.length()-1, buf.length(), "\n");
				System.out.println("buf=" + buf);
			}
		}
		return buf.toString();
	}

	public String toMqlAddString()
	{
		StringBuffer buf = new StringBuffer();
		try
		{
			buf.append("del portal ").append(quote(getName())).append(";\n");
			buf.append("add portal ").append(quote(getName())).append("\n");
			buf.append("\t").append("description\t").append(quote(getDescription())).append("\n");
			buf.append("\t").append("label\t").append(quote(getLabel())).append("\n");
			buf.append("\t").append("href\t").append(quote(getHref())).append("\n");
			buf.append(channelString(context));
			Iterator settingItr = getSetting().keySet().iterator();
			while(settingItr.hasNext())
			{
				Object key = settingItr.next();
				Object value = getSetting().get(key);
				buf.append("\t").append("setting\t").append(quote(key.toString())).append("\t\t").append(quote(value.toString())).append("\n");
			}
			//setting

			//property
			Iterator propertyItr = getProperty().keySet().iterator();
			while(propertyItr.hasNext())
			{
				Object key = propertyItr.next();
				Object value = getProperty().get(key);
				buf.append("\t").append("property\t").append(quote(key.toString())).append("\tvalue\t").append(quote(value.toString())).append("\n");
			}
			buf.append(";\n");
//			buf.append("#").append("portal ==>");
//			for(int k = 0; k < parent.size(); k++)
//			{
//				buf.append((String)parent.get(k));
//			}
//			buf.append("\n");
//			System.out.println(buf.toString());
			//add property channel_mbsCustomerCarCreate on program eServiceSchemaVariableMapping.tcl to channel mbsCustomerCarCreate;
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
		buf.append("add property portal_").append(getName().replaceAll(" ","")).append(" ").append("on program eServiceSchemaVariableMapping.tcl to portal ").append(quote(getName())).append(";\n");
		
		return buf.toString();
	}

	public HashMap getChannelMap() {
		return channelMap;
	}

	public void setChannelMap() throws Exception {
		for(int i = 0; i < getPlace().size(); i++)
		{
			String place = (String)getPlace().get(i);
			StringList rowColumn = FrameworkUtil.splitString(place,",");
			int r = Integer.parseInt((String)rowColumn.get(0));
			int c = Integer.parseInt((String)rowColumn.get(1));
			if(r > row)
				row = r;
			if(c > column)
				column = c;
			channelMap.put(place, (String)getChannel().get(i));
		}
		System.out.println("channelMap=" + channelMap);
	}

	public StringList getPlace() {
		return place;
	}
	public StringList getChannel()
	{
		return this.channel;
	}
	public void setPlace(Context context) throws Exception {
		String parent = getValue(context,"place");
		this.place = FrameworkUtil.split(parent, "|");
	}
}
