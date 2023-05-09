package matrix.admin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import matrix.db.Context;
import matrix.db.MQLCommand;
import matrix.util.StringList;

import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MqlUtil;

public class State{
	private Policy  policy;
	private String	name	;
	private String	id	;
	private String	publicaccess	;
	private String 	owneraccess;
	private String  notify;
	private String  route;
	private String  action;
	private String  check;
	private Vector 	access = new Vector();
	private Vector 	signature = new Vector();
	private Vector user = new Vector();
	private Vector 	trigger = new Vector();
	private String	revisionable;
	private String	versionable	;
	private String	autopromote;
	private String  checkouthistory;
	private HashMap userAccess = new HashMap();
	private HashMap userAccessFilter = new HashMap();
	private HashMap triggerMap = new HashMap();
	private Context context;
	public State()
	{

	}

	public State(Context context,Policy policy,String name)
	{
		try
		{
			this.context =  context;
			this.policy = policy;
			setName(name);
			setRoute();
			setAction();
			setCheck();
			setOwneraccess();
			setPublicaccess();
			setUserAccess();
			//setSignature();
			setTrigger();
			setRevisionable();
			setVersionable();
			setAutopromote();
			setCheckouthistory();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public class Access
	{
		String user;
		String access;
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
	public String toMqlString()
	{
		StringBuffer buf = new StringBuffer();
		try
		{
			buf.append("state ").append("\t").append(quote(getName())).append("\n");
			//user
			buf.append("\t").append("revision").append(" ").append(getRevisionable()).append("\n");
			buf.append("\t").append("version").append(" ").append(getVersionable()).append("\n");
			buf.append("\t").append("promote").append(" ").append(getAutopromote()).append("\n");
			buf.append("\t").append("checkouthistory").append(" ").append(getCheckouthistory()).append("\n");
			buf.append("\t").append("public").append(" \t").append(getPublicaccess()).append("\n");
			buf.append("\t").append("owner").append(" \t").append(getOwneraccess()).append("\n");
			Iterator userItr = getUser().iterator();
			while(userItr.hasNext())
			{
				String userName = (String)userItr.next();
				buf.append("\t").append("user ").append(quote(userName)).append(" ").append((String)userAccess.get(userName)).append("\n");
				String filter = (String)userAccessFilter.get(userName);
				if(filter != null && !"".equals(filter))
					buf.append("\t    ").append("filter").append(" ").append(doubleQuote((String)userAccessFilter.get(userName))).append("\n");
			}
			buf.append(getTriggerString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return buf.toString();
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue(Context context,String attr)
			throws Exception
			{
		MQLCommand mqlCommand = new MQLCommand();
		String command = "print policy " + quote(policy.getName()) + " select state[" + getName() + "]."+ attr +" dump";
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
	public String doubleQuote(String str)
	{
		return "\""+str+"\"";
	}

	public void setPublicaccess() 
			throws Exception
			{
		this.publicaccess = getValue(context,"publicaccess");
			}

	public String getPublicaccess() {
		return publicaccess;
	}

	public void setOwneraccess() 
			throws Exception
			{
		this.owneraccess = getValue(context,"owneraccess");
			}

	public String getOwneraccess() {
		return owneraccess;
	}

	public void setNotify() 
			throws Exception
			{
		this.notify = getValue(context,"notify");
			}

	public String getNotify() {
		return notify;
	}

	public void setRoute() 
			throws Exception
			{
		this.route = getValue(context,"route");
			}

	public String getRoute() {
		return route;
	}

	public void setAction() 
			throws Exception
			{
		this.action = getValue(context,"action");
			}

	public String getAction() {
		return action;
	}

	public void setCheck() 
			throws Exception
			{
		this.check = getValue(context,"check");
			}

	public String getCheck() {
		return check;
	}



	public Vector getAccess() {
		return access;
	}

	public void setSignature(Vector signature) 
			throws Exception
			{
		this.signature = signature;
			}

	public Vector getSignature() {
		return signature;
	}

	public void setTrigger() 		throws Exception
	{
//	    #trigger PromoteCheck:emxTriggerManager(PolicyECPartStatePreliminaryPromoteCheck)
//	    trigger Promote Check emxTriggerManager input PolicyECPartStatePreliminaryPromoteCheck
		String result = getValue(context,"trigger");
		StringList list = FrameworkUtil.splitString(result, ",");
		for(int i = 0; i< list.size(); i++)
		{
			String triggerStr = (String)list.get(i);
			System.out.println("triggerStr=" + triggerStr);
			String triggerType = triggerStr.substring(0,triggerStr.indexOf(":"));
			System.out.println("triggerType=" + triggerType);
			String triggerInput = triggerStr.substring(triggerStr.indexOf("(")+1,triggerStr.indexOf(")"));
			System.out.println("triggerInput=" + triggerInput);
			triggerMap.put(triggerType, triggerInput);
		}
	}
	public String getTriggerString()
	{
		StringBuffer buf = new StringBuffer();
		Iterator itr = triggerMap.keySet().iterator();
		while(itr.hasNext())
		{
			String triggerType = (String)itr.next();
			if("PromoteCheck".equals(triggerType))
			{
				buf.append("\t").append("add trigger Promote Check emxTriggerManager input ").append((String)triggerMap.get(triggerType)).append("\n");
			}
			else if("PromoteAction".equals(triggerType))
			{
				buf.append("\t").append("add trigger Promote Action emxTriggerManager input ").append((String)triggerMap.get(triggerType)).append("\n");
			}
			else if("DemoteCheck".equals(triggerType))
			{
				buf.append("\t").append("add trigger Demote Check emxTriggerManager input ").append((String)triggerMap.get(triggerType)).append("\n");
			}
			else if("DemoteAction".equals(triggerType))
			{
				buf.append("\t").append("add trigger Demote Action emxTriggerManager input ").append((String)triggerMap.get(triggerType)).append("\n");
			}
		}
		return buf.toString();
	}
	public Vector getTrigger() {
		return trigger;
	}

	public void setRevisionable() 
			throws Exception
			{
		this.revisionable = getValue(context,"revisionable");
			}

	public String getRevisionable() {
		return revisionable;
	}

	public void setVersionable() 
			throws Exception
			{
		this.versionable = getValue(context,"versionable");
			}

	public String getVersionable() {
		return versionable;
	}

	public void setAutopromote() 
			throws Exception
			{
		this.autopromote = getValue(context,"autopromote");
			}

	public String getAutopromote() {
		return autopromote;
	}

	public void setCheckouthistory() 
			throws Exception
			{
		this.checkouthistory = getValue(context,"checkouthistory");
			}

	public String getCheckouthistory() {
		return checkouthistory;
	}

	public Vector getUser() {
		return user;
	}

	public void setUserAccess() throws Exception
	{
		MQLCommand mqlCommand = new MQLCommand();
		String command = "print policy " + quote(policy.getName()) + " select state[" + getName() + "].access";
		System.out.println("command=" + command);
		boolean executeOk = mqlCommand.executeCommand(context, command);
		String result;

		if (executeOk == true)
			result = mqlCommand.getResult();
		else
			result = mqlCommand.getError();
		if(result != null)
		{
			StringList accessList = FrameworkUtil.splitString(result,"\n");
			for(int i = 1; i<accessList.size(); i++)
			{
				//state[Release].access[ECR Coordinator] = revise
				String accessStr = (String)accessList.get(i);
				accessStr = accessStr.substring(accessStr.indexOf(".")+1);
				System.out.println("accessStr=" + accessStr);
				if(!"".equals(accessStr))
				{
					StringList accList = FrameworkUtil.splitString(accessStr, "=");
					String userStr = (String)accList.get(0);
					System.out.println("userStr=" + userStr);
					String access = (String)accList.get(1);
					System.out.println("access=" + access);
					String userName = userStr.substring(userStr.indexOf("[")+1,userStr.indexOf("]"));
					System.out.println("userName=" + userName);
					user.add(userName);
					userAccess.put(userName,access);
				}
			}
		}
		mqlCommand = new MQLCommand();
		command = "print policy " + quote(policy.getName()) + " select state[" + getName() + "].filter";
		System.out.println("command=" + command);
		executeOk = mqlCommand.executeCommand(context, command);

		if (executeOk == true)
			result = mqlCommand.getResult();
		else
			result = mqlCommand.getError();
		if(result != null)
		{
			StringList accessList = FrameworkUtil.splitString(result,"\n");
			for(int i = 1; i<accessList.size(); i++)
			{
				//access[Senior Manufacturing Engineer] = read,modify,create,fromconnect,toconnect,fromdisconnect,todisconnect,show
				String accessStr = (String)accessList.get(i);
				accessStr = accessStr.substring(accessStr.indexOf(".")+1);
				System.out.println("accessStr=" + accessStr);
				if(!"".equals(accessStr))
				{
					StringList accList = FrameworkUtil.splitString(accessStr, "=");
					String userStr = (String)accList.get(0);
					System.out.println("userStr=" + userStr);
					String filter = accessStr.substring(accessStr.indexOf("=")+1).trim();
					System.out.println("filter=" + filter);
					
					String userName = userStr.substring(userStr.indexOf("[")+1,userStr.indexOf("]"));
					//				user.add(userName);
					userAccessFilter.put(userName,filter);
				}
			}
		}
	}

	public HashMap getUserAccessFilter() {
		return userAccessFilter;
	}

	public void setUserAccessFilter(HashMap userAccessFilter) {
		this.userAccessFilter = userAccessFilter;
	}

	public HashMap getTriggerMap() {
		return triggerMap;
	}

	public void setTriggerMap(HashMap triggerMap) {
		this.triggerMap = triggerMap;
	}
}
