package matrix.admin;

import java.util.Iterator;

import matrix.db.Context;
import matrix.db.MQLCommand;
import matrix.util.StringList;
import java.util.Vector;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MapList;

public class Table extends AdminObject {
	private String	hidden	;
	private String	id	;
	private Vector column = new Vector();
	public Table()
	{
		super();
	}
	
	public Table(Context context,String name)
	{
		try
		{
			super.setType("table");
			super.setName(name);
			super.setDescription(context);
			super.setProperty(context);
			super.setSetting(context);
			setColumn(context);
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
	public void setColumn(Vector _column) {
		this.column = _column;
	}
	public void setColumn(Context context) 
	throws Exception
	{
		System.out.println("setcolumn start");
        MQLCommand mqlCommand = new MQLCommand();
        String command = "print table "+getName() + " system select column dump |";
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
        	String colName = (String)colList.get(i);
        	System.out.println("colName=" + colName);
        	TableColumn col = new TableColumn(context,this,colName);
        	column.add(col);
        }
	}
	public Vector getColumn() {
		return column;
	}
	public String toMqlString()
	{
		StringBuffer buf = new StringBuffer();
		try
		{
			buf.append("del table ").append(quote(getName())).append(" system;\n");
			buf.append("add table ").append(quote(getName())).append(" system\n");
			System.out.println("column.size()=" + column.size());
			for(int i = 0; i < column.size(); i++)
			{
				TableColumn col = (TableColumn)column.get(i);
				buf.append(col.toMqlString());
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
			buf.append("add property table_").append(getName().replaceAll(" ","")).append(" ").append("on program eServiceSchemaVariableMapping.tcl to table ").append(quote(getName())).append(" system;\n");

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return buf.toString();
	}

}
