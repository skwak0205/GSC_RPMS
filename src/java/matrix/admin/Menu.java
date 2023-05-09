package matrix.admin;

import java.util.Iterator;
import java.util.Vector;

import matrix.db.Context;
import matrix.db.MQLCommand;
import matrix.util.StringList;

import com.matrixone.apps.domain.util.FrameworkUtil;

public class Menu extends AdminObject {
	private String	hidden	;
	private String	id	;
	private String	label	;
	private String	href	;
	private String	alt	;
	private String	parent	;
	private Vector child;
	public class Child {
		private String name;
		private String type;
		public Child(String _type,String _name)
		{
			type = _type;
			name = _name;
		}
	}

	public Menu()
	{
		super();
	}

	public Menu(Context context,String name)
	{
		try
		{
			super.setType("menu");
			super.setName(name);
			super.setDescription(context);
			super.setProperty(context);
			super.setSetting(context);
			setChild(context);
			setHidden(context);
			setLabel(context);
			setHref(context);
			setAlt(context);
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
	public Vector getChild() {
		return child;
	}
	public void setChild(Context context)
	throws Exception
	{
		MQLCommand mqlCommand = new MQLCommand();
		String command = "print menu "+quote(getName()) + " select child.name dump |";
		//System.out.println("command=" + command);
		boolean executeOk = mqlCommand.executeCommand(context, command);
		String result;

		if (executeOk == true)
			result = mqlCommand.getResult();
		else
		{
			result = mqlCommand.getError();
			return;
		}
		result = result.replaceAll("\\n","");
		StringList nameList = FrameworkUtil.split(result,"|");
		command = "print menu "+quote(getName()) + " select child.type dump |";
		//System.out.println("command=" + command);
		boolean executeOk2 = mqlCommand.executeCommand(context, command);
		String result2;

		if (executeOk2 == true)
			result2 = mqlCommand.getResult();
		else
		{
			result2 = mqlCommand.getError();
			return;
		}
		result2 = result2.replaceAll("\\n","");
		StringList typeList = FrameworkUtil.split(result2,"|");
		Vector _child = new Vector();
		for(int i=0 ; i < typeList.size(); i++)
		{
			String name = (String)nameList.get(i);
			//System.out.println("name=" + name);
			String type = (String)typeList.get(i);
			_child.add(new Child(type,name));
		}
		this.child = _child;

	}
	public void setChild(Vector _child) {
		child = _child;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public void setParent(Context context) 
	throws Exception
	{
		this.parent = getValue(context,"parent");
	}
	public String getParent() {
		return parent;
	}
	public String toMqlModifyString()
	{
		StringBuffer buf = new StringBuffer();
		try
		{
			buf.append("mod " +getType() + " ").append(quote(getName())).append("\n");
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
				buf.append("\t").append("add setting\t").append(quote(key.toString())).append("\t").append(quote(value.toString())).append("\n");
			}
			//child
			//System.out.println("child=" + child);
			Iterator childItr = child.iterator();
			int i = 0;
			while(childItr.hasNext())
			{
				Child child = (Child)childItr.next(); 
				buf.append("\t").append("add ").append(child.type).append("\t").append(quote(child.name)).append("\t").append(++i).append("\n");
			}
			//property
			Iterator propertyItr = getProperty().keySet().iterator();
			while(propertyItr.hasNext())
			{
				Object key = propertyItr.next();
				Object value = getProperty().get(key);
				buf.append("\t").append("add property ").append(quote(key.toString())).append("\tvalue\t").append(quote(value.toString())).append("\n");
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
			buf.append("del ").append(getType()).append(" ").append(quote(getName())).append(";\n");
			buf.append("add ").append(getType()).append(" ").append(quote(getName())).append("\n");
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
			//child
			//System.out.println("child=" + child);
			Iterator childItr = child.iterator();
			while(childItr.hasNext())
			{
				Child child = (Child)childItr.next(); 
				buf.append("\t").append(child.type).append("\t").append(quote(child.name)).append("\n");
			}
			//property
			Iterator propertyItr = getProperty().keySet().iterator();
			while(propertyItr.hasNext())
			{
				Object key = propertyItr.next();
				Object value = getProperty().get(key);
				buf.append("\t").append("property ").append(quote(key.toString())).append("\tvalue\t").append(quote(value.toString())).append("\n");
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
	public String toMqlString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append(toMqlAddString().toString());
		//buf.append(toMqlModifyString().toString());
		buf.append("add property menu_").append(getName().replaceAll(" ","")).append(" ").append("on program eServiceSchemaVariableMapping.tcl to menu ").append(quote(getName())).append(";\n");

		return buf.toString();
	}
}
