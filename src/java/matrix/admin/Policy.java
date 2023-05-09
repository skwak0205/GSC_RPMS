package matrix.admin;

import java.util.Iterator;

import matrix.db.Context;
import matrix.db.MQLCommand;
import matrix.util.StringList;
import java.util.Vector;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MapList;

public class Policy extends AdminObject {
	private String	hidden	;
	private String	id	;
	private Vector state = new Vector();
	private String format;
	private String defaultformat;
	private String allstate;
	private String revision;
	private String store;
	private String sequence;
	private String islockingenforced;
	private String governedType;
	public Policy()
	{
		super();
	}
	
	public Policy(Context context,String name)
	{
		try
		{
			super.setType("policy");
			super.setName(name);
			super.setDescription(context);
			super.setProperty(context);
			super.setSetting(context);
			setState(context);
			setHidden(context);
			setFormat(context);
			setDefaultformat(context);
			setAllstate(context);
			setRevision(context);
			setStore(context);
			setSequence(context);
			setIslockingenforced(context);
			setGovernedType(context);
			
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
	public void setState(Vector _state) {
		this.state = _state;
	}
	public void setState(Context context) 
	throws Exception
	{
		System.out.println("setState start");
        MQLCommand mqlCommand = new MQLCommand();
        String command = "print policy "+quote(getName()) + "  select state dump |";
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
        	String stateName = (String)colList.get(i);
        	System.out.println("stateName=" + stateName);
        	State st = new State(context,this,stateName);
        	state.add(st);
        }
	}
	public Vector getState() {
		return state;
	}
	public String toMqlString()
	{
		StringBuffer buf = new StringBuffer();
		try
		{
//			buf.append("del policy ").append(quote(getName())).append(";\n");
			buf.append("mod policy ").append(quote(getName())).append("\n");
			System.out.println("state.size()=" + state.size());
			buf.append("\t").append("description").append(" ").append(quote(getDescription())).append("\n");
			buf.append("\t").append("add type").append(" ").append(quote(getGovernedType())).append("\n");
			buf.append("\t").append("store").append(" ").append(getStore()).append("\n");
			buf.append("\t").append("add format").append(" ").append(getFormat()).append("\n");
			buf.append("\t").append("sequence").append(" ").append(getSequence()).append("\n");
			for(int i = 0; i < state.size(); i++)
			{
				State fil = (State)state.get(i);
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
			buf.append("add property policy_").append(getName().replaceAll(" ","")).append(" ").append("on program eServiceSchemaVariableMapping.tcl to policy ").append(quote(getName())).append(";\n");

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return buf.toString();
	}

	public void setFormat(Context context) 
	throws Exception
	{
		this.format = getValue(context,"format");
	}

	public String getFormat() {
		return format;
	}

	public void setDefaultformat(Context context) 
	throws Exception
	{
		this.defaultformat = getValue(context,"defaultformat");
	}

	public String getDefaultformat() {
		return defaultformat;
	}

	public void setAllstate(Context context) 
	throws Exception
	{
		this.allstate = getValue(context,"allstate");
	}

	public String getAllstate() {
		return allstate;
	}

	public void setRevision(Context context) 
	throws Exception
	{
		this.revision = getValue(context,"revision");
	}

	public String getRevision() {
		return revision;
	}

	public void setStore(Context context) 
	throws Exception
	{
		this.store = getValue(context,"store");
	}

	public String getStore() {
		return store;
	}
	public void setSequence(Context context) 
	throws Exception
	{
		this.sequence = getValue(context,"revision");
	}
	public String getSequence()
	{
		return this.sequence;
	}

	public void setIslockingenforced(Context context) 
	throws Exception
	{
		this.islockingenforced = getValue(context,"islockingenforced");
	}

	public String getIslockingenforced() {
		return islockingenforced;
	}

	public String getGovernedType() {
		return governedType;
	}

	public void setGovernedType(Context context) throws Exception{
		this.governedType = getValue(context,"type");
	}

}
