package matrix.admin;

import java.util.HashMap;
import java.util.Iterator;

import matrix.db.Context;
import matrix.db.MQLCommand;
import matrix.util.StringList;

import com.matrixone.apps.domain.util.FrameworkUtil;

public class Inquiry extends AdminObject {
	private String	hidden	;
	private String	id	;
	private String	code	;
	private String	pattern	;
	private String	format	;
	private StringList	argument	;
	private Context context;
	public Inquiry()
	{
		super();
	}
	
	public Inquiry(Context context,String name)
	{
		try
		{
			this.context = context;
			super.setType("inquiry");
			super.setName(name);
			super.setDescription(context);
			super.setProperty(context);
			super.setSetting(context);
			setHidden(context);
			setPattern(context);
			setCode(context);
			setArgument(context);
			setFormat(context);
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
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public void setPattern(Context context)
	throws Exception
	{
		this.pattern = getValue(context,"pattern");
	}
	public String getPattern() {
		return pattern;
	}
	public String getFormat()
	{
		return this.format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public void setFormat(Context context) 
	throws Exception
	{
		this.format = getValue(context,"format");
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
	public void setArgment(StringList argument) {
		this.argument = argument;
	}
	public void setArgument(Context context) 
	throws Exception
	{
		String argument = getValue(context,"argument");
		System.out.println("argument="+argument);
		this.argument = FrameworkUtil.split(argument, "|");
	}
	public StringList getArgument() {
		return argument;
	}
	public String argumentString(Context context)
	throws Exception
	{
		StringBuffer buf = new StringBuffer();
		if(argument != null)
		{
			for(int i=0; i < argument.size(); i++)
			{
				String arguName = (String)argument.get(i);
				int order = 0;
				String command2 = "print inquiry " + quote(getName()) + " select argument["+arguName+"].value dump";
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

				buf.append("\targument "+ quote(arguName) + " " +result2.trim()).append("\n");
			}
		}
		return buf.toString();
	}

	public String toMqlAddString()
	{
		StringBuffer buf = new StringBuffer();
		try
		{
			buf.append("del inquiry ").append(quote(getName())).append(";\n");
			buf.append("add inquiry ").append(quote(getName())).append("\n");
			buf.append("\t").append("pattern\t").append(quote(getPattern())).append("\n");
			buf.append("\t").append("format\t").append(quote(getFormat())).append("\n");
			buf.append("\t").append("code\t").append(quote(getCode())).append("\n");
			buf.append(argumentString(context));
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
			System.out.println(buf.toString());
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
		buf.append("add property inquiry_").append(getName().replaceAll(" ","")).append(" ").append("on program eServiceSchemaVariableMapping.tcl to argument ").append(quote(getName())).append(";\n");
		
		return buf.toString();
	}
}
