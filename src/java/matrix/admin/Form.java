package matrix.admin;

import java.util.Iterator;

import matrix.db.Context;
import matrix.db.MQLCommand;
import matrix.util.StringList;
import java.util.Vector;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MapList;

public class Form extends AdminObject {
	private String	hidden	;
	private String	id	;
	private Vector field = new Vector();
	private String rule;
	private String web;
	public Form()
	{
		super();
	}
	
	public Form(Context context,String name)
	{
		try
		{
			super.setType("form");
			super.setName(name);
			super.setDescription(context);
			super.setProperty(context);
			super.setSetting(context);
			setField(context);
			setHidden(context);
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
	public void setField(Vector _field) {
		this.field = _field;
	}
	public void setField(Context context) 
	throws Exception
	{
		System.out.println("setcolumn start");
        MQLCommand mqlCommand = new MQLCommand();
        String command = "print form "+getName() + "  select field dump |";
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
        StringList colList = FrameworkUtil.split(result,"|");
        for(int i=0; i < colList.size(); i++)
        {
        	String fieldName = (String)colList.get(i);
        	System.out.println("fieldName=" + fieldName);
        	FormField fil = new FormField(context,this,fieldName);
        	field.add(fil);
        }
	}
	public Vector getField() {
		return field;
	}
	public String toMqlString()
	{
		StringBuffer buf = new StringBuffer();
		try
		{
			buf.append("del form ").append(quote(getName())).append(";\n");
			buf.append("add form ").append(quote(getName())).append(" web\n");
			System.out.println("field.size()=" + field.size());
			for(int i = 0; i < field.size(); i++)
			{
				FormField fil = (FormField)field.get(i);
				buf.append(fil.toMqlString());
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
			buf.append("add property form_").append(getName().replaceAll(" ","")).append(" ").append("on program eServiceSchemaVariableMapping.tcl to form ").append(quote(getName())).append(";\n");

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return buf.toString();
	}

}
