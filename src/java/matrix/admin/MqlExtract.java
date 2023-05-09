package matrix.admin;

import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MqlUtil;
import matrix.db.Context;
import matrix.util.StringList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MqlExtract {

	public static void main(String[] args)
	throws Exception
	{
		Context context = new Context("https://rpmsdev.gscaltex.co.kr/internal");
		//Modified for V6 upgrade - End
		context.setUser("admin_platform");
		context.setPassword("Qwer1234");
		context.setVault("eService Production");
		//Modified for V6 upgrade - Start
		context.connect(true);
		getList(context, "table");
	}

	public static void analyzeDB(Context context)
	{
		
	}
	public static void toMqlString(Context context,String[] args) throws IOException {
//		if(args == null || args.length < 2)
//			return;
		String adminType = args[0];
		String adminName = args[1];

		String dir = "C:\\Users\\ASPENS\\Documents\\GitHub\\gs_3dspace\\src\\mql";

		if("command".equals(adminType))
		{
			Command cmd = new Command(context,adminName);
			System.out.println(cmd.toMqlString());

			String path = dir + "\\06_command\\06_command_" + adminName + ".mql";
			createFile(path, cmd.toMqlString());
		}
		else if("menu".equals(adminType))
		{
			Menu cmd = new Menu(context,adminName);
			System.out.println(cmd.toMqlString());

			String path = dir + "\\07_menu\\07_menu_" + adminName + ".mql";
			createFile(path, cmd.toMqlString());
		}
		else if("table".equals(adminType))
		{
			Table cmd = new Table(context,adminName);
			System.out.println(cmd.toMqlString());

			String path = dir + "\\09_table\\09_table_" + adminName + ".mql";
			createFile(path, cmd.toMqlString());
		}
		else if("form".equals(adminType))
		{
			Form cmd = new Form(context,adminName);
			System.out.println(cmd.toMqlString());

			String path = dir + "\\08_form\\08_form_" + adminName + ".mql";
			createFile(path, cmd.toMqlString());
		}
		else if("policy".equals(adminType))
		{
			Policy cmd = new Policy(context,adminName);
			System.out.println(cmd.toMqlString());

			String path = dir + "\\04_policy\\04_policy_" + adminName + ".mql";
			createFile(path, cmd.toMqlString());
		}
		else if("channel".equals(adminType))
		{
			Channel cmd = new Channel(context,adminName);
			System.out.println(cmd.toMqlString());

			String path = dir + "\\10_channel&portal\\10_channel_" + adminName + ".mql";
			createFile(path, cmd.toMqlString());
		}
		else if("portal".equals(adminType))
		{
			Portal cmd = new Portal(context,adminName);
			System.out.println(cmd.toMqlString());

			String path = dir + "\\10_channel&portal\\10_portal_" + adminName + ".mql";
			createFile(path, cmd.toMqlString());
		}

		else if("attribute".equals(adminType))
		{
			Attribute cmd = new Attribute(context,adminName);
			System.out.println(cmd.toMqlString());
			String path = dir + "\\01_attribute\\01_attribute_" + adminName + ".mql";
			createFile(path, cmd.toMqlString());
		}
	}

	public static void getList(Context context, String selection) {
		try {
			String val = selection.equals("table") ? " system" : "";
			String mql = "list " + selection + val;
			String res = MqlUtil.mqlCommand(context, mql);
			StringList list = FrameworkUtil.split(res, "\n");
			String[] args = new String[2];
			args[0] = selection;

			for (int i = 0; i < list.size(); i++) {
				String row = list.get(i);
				args[1] = row;
				toMqlString(context, args);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	public static void createFile(String path, String cmd) {
		try {
			// attribute의 이름에서 '/'이 포함된 경우 '.'으로 변경
			path = path.contains("/") ? path.replace('/', '.') : path;
			File file = new File(path);

			if(!file.exists()){
				file.createNewFile();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
			writer.write(cmd);

			writer.flush();
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
}
