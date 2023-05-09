package matrix.admin;

import com.matrixone.apps.domain.util.MqlUtil;
import matrix.db.Context;
import matrix.db.MQLCommand;

import java.util.Iterator;

public class Attribute extends AdminObject {
    private String hidden;
    private String field;
    private Context context;

    public Attribute() {
        super();
    }

    public Attribute(Context context, String name) {
        try {
            this.context = context;
            super.setType("attribute");
            super.setName(name);
            super.setDescription(context);
            super.setProperty(context);
            super.setSetting(context);
            setHidden(context);
            setField(context);
        } catch (Exception e) {
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

    public void setField(Context context)
            throws Exception
    {
        String command = "print attribute '"+getName() + "'  select type dump |";
        String res = MqlUtil.mqlCommand(context, command);
        field = res;
    }
    public String getField() {
        return field;
    }

    public String toMqlString()
    {
        StringBuffer buf = new StringBuffer();
        try
        {
            buf.append("del attribute ").append(quote(getName())).append(";\n");
            buf.append("add attribute ").append(quote(getName())).append("\n");

            // type
            buf.append("\t").append("type\t").append(quote(getField()) + "\n");

            // Description
            buf.append("\t").append("description\t").append(quote(getDescription()) + "\n");

            //property
            Iterator propertyItr = getProperty().keySet().iterator();
            while(propertyItr.hasNext())
            {
                Object key = propertyItr.next();
                Object value = getProperty().get(key);
                buf.append("\t ").append("property\t").append(quote(key.toString())).append("\tvalue\t").append(quote(value.toString())).append("\n");
            }

            buf.append(";\n");
            buf.append("add property attribute_").append(getName().replaceAll(" ","")).append(" ").append("on program eServiceSchemaVariableMapping.tcl to attribute ").append(quote(getName())).append(";\n");

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return buf.toString();
    }

}
