
import com.matrixone.apps.cache.CacheManager;
import com.matrixone.apps.common.Workspace;
import com.matrixone.apps.common.WorkspaceVault;
import com.matrixone.apps.common.util.SubscriptionUtil;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.EnoviaResourceBundle;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.FrameworkProperties;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.PropertyUtil;
import com.matrixone.apps.domain.util.XSSUtil;
import com.matrixone.apps.domain.util.i18nNow;
import com.matrixone.apps.framework.ui.UIComponent;
import com.matrixone.apps.framework.ui.UINavigatorUtil;
import com.matrixone.apps.framework.ui.UIUtil;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import matrix.db.Access;
import matrix.db.AttributeType;
import matrix.db.Command;
import matrix.db.Context;
import matrix.db.JPO;
import matrix.db.State;
import matrix.db.StateList;
import matrix.dbutil.SelectSetting;
import matrix.util.MatrixException;
import matrix.util.Pattern;
import matrix.util.StringList;

public class emxGenericColumnsBase_mxJPO {

	public static String sColorLink = emxUtil_mxJPO.sColorLink;
	private static String COLUMN_MAP = "columnMap";
	private static String OBJECT_LIST = "objectList";
	private static String PARAM_LIST = "paramList";
	private static String LANGUAGE_STR = "languageStr";
	private static String SETTINGS = "settings";
	private static String ICON = "Icon";
	private static String TRUE_STRING = "true";
	private static String FALSE_STRING = "false";
	private static String ID_LEVEL = "id[level]";
	private static String TABLE = "table";
	private static String FORM = "form";
	private static String TYPE_KIND_OF = "type.kindof";
	
	
	
	public emxGenericColumnsBase_mxJPO(Context context, String[] args) throws Exception {
	}

	// Picture Column
	public Vector columnPicture(Context context, String[] args) throws Exception {

		HashMap programMap = (HashMap) JPO.unpackArgs(args);
		HashMap columnMap = (HashMap) programMap.get(COLUMN_MAP);
		MapList mlOIDs = (MapList) programMap.get(OBJECT_LIST);
		HashMap paramList = (HashMap) programMap.get(PARAM_LIST);
		String sLang = (String) paramList.get(LANGUAGE_STR);
		HashMap settings = (HashMap) columnMap.get(SETTINGS);
		String sValues = (String) settings.get("Values");
		String sPictures = (String) settings.get("Pictures");
		String sDefault = (String) settings.get("Default");
		String sShowValue = (String) settings.get("Show Value");
		String sStyle = (String) settings.get("Style");
		String sRelSelect = (String) settings.get("RelationshipSelect");
		String sExpression = (String) columnMap.get("expression_businessobject");
		String sAttribute = "";
		Boolean bIsBO = true;
		Vector vResult = new Vector();

		if (null != sRelSelect) {
			if (!"".equals(sRelSelect)) {
				sExpression = sRelSelect;
				bIsBO = false;
			}
		} else if (UIUtil.isNullOrEmpty(sExpression)) {
			sExpression = (String) columnMap.get("expression_relationship");
			bIsBO = false;
		}
		if (UIUtil.isNullOrEmpty(sExpression)){
			sExpression = (String) settings.get("Expression");
			bIsBO = true;
		}

		if (sDefault == null) {
			sDefault = "";
		}
		if (sShowValue == null) {
			sShowValue = FALSE_STRING;
		}
		if (sStyle == null) {
			sStyle = "";
		}

		StringBuilder sbStyle = new StringBuilder();
		sbStyle.append(" style='vertical-align:middle;").append(sStyle).append("'");

		String[] aValues = sValues.split(";");
		String[] aPictures = sPictures.split(";");

		if (aValues.length != aPictures.length) {
			System.out.println(
					"GNVColumns.columnPicture : Error in parameter settings. Number of values does not match number of pictures");
			vResult = new Vector(mlOIDs.size());
		} else {

			if (sExpression.contains("[")) {
				int iStart = sExpression.lastIndexOf('[');
				int iEnd = sExpression.lastIndexOf(']');
				sAttribute = sExpression.substring(iStart + 1, iEnd);
			}

			for (int i = 0; i < mlOIDs.size(); i++) {

				StringBuilder sbResult = new StringBuilder();
				Map mObject = (Map) mlOIDs.get(i);
				String sOID = (String) mObject.get(DomainConstants.SELECT_ID);
				DomainObject dObject = new DomainObject(sOID);
				String sValue = "";
				String sPicture = "";

				if (bIsBO) {
					sValue = dObject.getInfo(context, sExpression);
				} else {
					String sRID = (String) mObject.get(DomainConstants.SELECT_RELATIONSHIP_ID);
					if (null != sRID) {
						if (!"".equals(sRID)) {
							DomainRelationship dRel = new DomainRelationship(sRID);
							StringList slBusSelects = new StringList();
							slBusSelects.add(sExpression);
							Map mResults = dRel.getRelationshipData(context, slBusSelects);
							if (mResults.get(sExpression) instanceof StringList) {
								StringList slTemp = (StringList) mResults.get(sExpression);
								sValue = (String) slTemp.get(0);
							} else {
								sValue = (String) mResults.get(sExpression);
							}
						}
					}
				}

				if (!"".equals(sDefault)) {
					sPicture = sDefault;
				}

				for (int j = 0; j < aValues.length; j++) {
					if (aValues[j].equals(sValue)) {
						sPicture = aPictures[j];
						break;
					}
				}

				if (!"".equals(sAttribute)) {
					sValue = i18nNow.getRangeI18NString(sAttribute, sValue, sLang);
				}

				if (!"".equals(sPicture)) {
					sbResult.append("<img src='images/").append(sPicture).append("'").append(sbStyle.toString()).append(" />");
					if (TRUE_STRING.equalsIgnoreCase(sShowValue)) {
						sbResult.append(' ').append("<span style='vertical-align:middle;'>").append(sValue).append("</span>");
					}
				} else {
					sbResult.append("<span style='vertical-align:middle;'>").append(sValue).append("</span>");
				}
				vResult.add(sbResult.toString());
			}
		}

		return vResult;

	}

	public Vector columnSubscriptionLink(Context context, String[] args) throws Exception {

		HashMap programMap = (HashMap) JPO.unpackArgs(args);
		MapList mlOIDs = (MapList) programMap.get(OBJECT_LIST);
		Vector vResult = new Vector(mlOIDs.size());
		String sOID = "";
		String sRowID = "";
		String sOIDSubscription = "";
		String objType = "";
		String sResult = "";
		Map mObjects = null;
		String FROM_PUBLISH_SUBSCRIBE_TO_ID = "from[Publish Subscribe].to.id";
		if (mlOIDs.size() == 1) {
			mObjects = (Map) mlOIDs.get(0);
			sOID = (String) mObjects.get(DomainConstants.SELECT_ID);
			sRowID = (String) mObjects.get(ID_LEVEL);
			DomainObject dObject = new DomainObject(sOID);
			StringList busSelects = new StringList();
			busSelects.add(DomainConstants.SELECT_TYPE);
			busSelects.add(FROM_PUBLISH_SUBSCRIBE_TO_ID);
			Map objInfo = dObject.getInfo(context, busSelects);
			sOIDSubscription = (String) objInfo.get(FROM_PUBLISH_SUBSCRIBE_TO_ID);
			objType = (String) objInfo.get(DomainConstants.SELECT_TYPE);
			sResult = getSubscriptionLink(context, args, sOID, sOIDSubscription, sRowID, objType);
			vResult.addElement(sResult);
		} else {
			for (int i = 0; i < mlOIDs.size(); i++) {

				mObjects = (Map) mlOIDs.get(i);
				sOID = (String) mObjects.get(DomainConstants.SELECT_ID);
				sRowID = (String) mObjects.get(ID_LEVEL);
				sOIDSubscription = (String) mObjects.get(FROM_PUBLISH_SUBSCRIBE_TO_ID);
				objType = (String) mObjects.get(DomainConstants.SELECT_TYPE);
				if (UIUtil.isNullOrEmpty(objType)) {
					DomainObject dObject = new DomainObject(sOID);
					StringList busSelects = new StringList();
					busSelects.add(DomainConstants.SELECT_TYPE);
					busSelects.add(FROM_PUBLISH_SUBSCRIBE_TO_ID);

					Map objInfo = dObject.getInfo(context, busSelects);
					sOIDSubscription = (String) objInfo.get(FROM_PUBLISH_SUBSCRIBE_TO_ID);
					objType = (String) objInfo.get(DomainConstants.SELECT_TYPE);
				}
				sResult = getSubscriptionLink(context, args, sOID, sOIDSubscription, sRowID, objType);
				vResult.addElement(sResult);
			}
		}
		return vResult;
	}

	private String getSubscriptionLink(Context context, String[] args, String sOID, String sOIDSubscription,
			String sRowID, String objType) throws Exception {

		Map paramMap = (Map) JPO.unpackArgs(args);
		Map paramList = (Map) paramMap.get(PARAM_LIST);
		String sLanguage = (String) paramList.get(LANGUAGE_STR);
		String sImage = "iconActionQuickSubscribe";
		String COMPONENTS = "Components";
		String sTitle = EnoviaResourceBundle.getProperty(context, COMPONENTS, "emxComponents.Button.Subscribe",
				sLanguage);
		StringBuilder sbURL = new StringBuilder();

		if (UIUtil.isNotNullAndNotEmpty(sOIDSubscription)) {

			StringList busSelectsSubscription = new StringList();
			busSelectsSubscription.add("from[" + DomainConstants.RELATIONSHIP_SUBSCRIBED_PERSON + "].to.name");
			busSelectsSubscription.add("from[" + DomainConstants.RELATIONSHIP_SUBSCRIBED_PERSON + "].id");

			DomainObject doSubscription = new DomainObject(sOIDSubscription);
			MapList mlSubscriptions = doSubscription.getRelatedObjects(context, DomainConstants.RELATIONSHIP_PUBLISH,
					DomainConstants.TYPE_EVENT, busSelectsSubscription, null, false, true, (short) 1,
					"from[" + DomainConstants.RELATIONSHIP_SUBSCRIBED_PERSON + "].to.name == '" + context.getUser()
							+ "'",
					"", 0);

			if (mlSubscriptions.size() > 0) {
				sImage = "iconActionQuickUnSubscribe";
				sTitle = EnoviaResourceBundle.getProperty(context,COMPONENTS, "emxComponents.command.Unsubscribe",
						sLanguage);
				sbURL.append("../common/emxColumnSubscriptionProcess.jsp?objectId=" + sOID + "&amp;rowId=" + sRowID + "&amp;subscriptionId=" + sOIDSubscription);
			}

		}
		if (sbURL.length() == 0) {
			sbURL.append("../common/emxColumnSubscriptionProcess.jsp?objectId=").append(sOID).append("&amp;rowId=").append(sRowID);

			String sTypeTopic = DomainConstants.TYPE_PROJECT_VAULT;
			;
			String sTypeProject = DomainConstants.TYPE_PROJECT;

			// In case of workspace and folder event names are hard-code as
			// there is no event menu
			// for this, for other use case else block will executes and pick
			// the events from event menu.
			if (sTypeProject.equals(objType)) {
				sbURL.append("&amp;chkSubscribeEvent=").append(Workspace.EVENT_ROUTE_STARTED).append("&amp;chkSubscribeEvent=").append(Workspace.EVENT_ROUTE_COMPLETED);
				sbURL.append("&amp;chkSubscribeEvent=").append(Workspace.EVENT_FOLDER_CREATED).append("&amp;chkSubscribeEvent=").append(Workspace.EVENT_FOLDER_DELETED);
				sbURL.append("&amp;chkSubscribeEvent=").append(Workspace.EVENT_MEMBER_ADDED).append("&amp;chkSubscribeEvent=").append(Workspace.EVENT_MEMBER_REMOVED);
				sbURL.append("&amp;chkSubscribeEvent=").append(Workspace.EVENT_FOLDER_CONTENT_MODIFIED).append("&amp;chkSubscribeEvent=").append(Workspace.EVENT_NEW_DISCUSSION);
			} else if (sTypeTopic.equals(objType)) {
				sbURL.append("&amp;chkSubscribeEvent=").append(WorkspaceVault.EVENT_CONTENT_ADDED).append("&amp;chkSubscribeEvent=").append(WorkspaceVault.EVENT_CONTENT_REMOVED);
				sbURL.append("&amp;chkSubscribeEvent=").append(WorkspaceVault.EVENT_FOLDER_CREATED).append("&amp;chkSubscribeEvent=").append(WorkspaceVault.EVENT_FOLDER_DELETED);
				sbURL.append("&amp;chkSubscribeEvent=").append(WorkspaceVault.EVENT_ROUTE_STARTED).append("&amp;chkSubscribeEvent=").append(WorkspaceVault.EVENT_ROUTE_COMPLETED);
				sbURL.append("&amp;chkSubscribeEvent=").append(WorkspaceVault.EVENT_NEW_DISCUSSION);
			} else {
				HashMap requestMap = (HashMap) paramList.get("RequestValuesMap");
				MapList eventList = SubscriptionUtil.getObjectSubscribableEventsList(context, sOID, objType,
						requestMap);
				Iterator itr = eventList.iterator();

				while (itr.hasNext()) {
					HashMap tempMap = (HashMap) itr.next();
					String eventName = UIComponent.getSetting(tempMap, "Event Type");
					sbURL.append("&amp;chkSubscribeEvent=").append(eventName);
				}
			}
		}

		String sResult = "<a href='" + sbURL.toString() + "' target='listHidden'><img src='../common/images/" + sImage
				+ ".gif' border='0' TITLE=\"" + sTitle + "\" /></a>";
		return sResult;
	}

	public static StringList getPopupHeightWidth(Context context, String popupSize) throws FrameworkException {

		StringList popupHeightWidth = new StringList();
		if (UIUtil.isNullOrEmpty(popupSize)) {
			popupSize = "Medium";
		}
		try {
			popupHeightWidth = FrameworkProperties.getTokenizedProperty(context, "emxFramework.PopupSize." + popupSize,
					"x");
		} catch (Exception e) {
			popupSize = EnoviaResourceBundle.getProperty(context, "emxFramework.PopupSize.Default");
			popupHeightWidth = FrameworkProperties.getTokenizedProperty(context, "emxFramework.PopupSize." + popupSize,
					"x");
		}
		return popupHeightWidth;
	}

	// Status Column
	public Vector columnStatus(Context context, String[] args) throws Exception {

		HashMap programMap = (HashMap) JPO.unpackArgs(args);
		HashMap paramList = (HashMap) programMap.get(PARAM_LIST);
		String sLang = (String) paramList.get(LANGUAGE_STR);
		HashMap columnMap = (HashMap) programMap.get(COLUMN_MAP);
		HashMap settings = (HashMap) columnMap.get(SETTINGS);
		String sSuite = (String) settings.get("Registered Suite");
		String sKeyBold = (String) settings.get("Bold");
		String sKeyDemote = (String) settings.get("Show Demote");
		String sKeyNext = (String) settings.get("Show Next");
		String sKeyPrev = (String) settings.get("Show Previous");
		String sKeyLink = (String) settings.get("Show Link");
		String sKeyPromoteLabel = (String) settings.get("Promote Tooltip");
		String sKeyDemoteLabel = (String) settings.get("Demote Tooltip");
		String sKeyCompleteIcon = (String) settings.get("Complete Icon");
		String sKeyCompleteLabel = (String) settings.get("Complete Tooltip");
		String sKeyCompleteState = (String) settings.get("Complete State");
		String sKeyInactivateComp = (String) settings.get("Inactivate Complete");
		String sKeyInactivateIcon = (String) settings.get("Inactivate Icon");
		String sKeyInactivateLabel = (String) settings.get("Inactivate Tooltip");
		String sKeyInactivatePolicy = (String) settings.get("Inactivate Policy");
		String sKeyInactivateState = (String) settings.get("Inactivate State");
		String sKeyStates = (String) settings.get("States");
		String sKeyColors = (String) settings.get("Colors");
		String reportFormat = (String) paramList.get("reportFormat");
		boolean isExport = false;
		String popupSize = (String) settings.get("Popup Size");
		StringList popupHeightWidth = getPopupHeightWidth(context, popupSize);
		String sWindowWidth = (String) popupHeightWidth.get(0);
		String sWindowHeight = (String) popupHeightWidth.get(1);

		if ("CSV".equals(reportFormat) || "HTML".equals(reportFormat) || "Text".equals(reportFormat)) {
			isExport = true;
		}

		String sSpacer = "<img src='../common/images/utilSpacer.gif' height='16'/>";
		MapList mlOIDs = (MapList) programMap.get(OBJECT_LIST);
		Vector vResult = new Vector(mlOIDs.size());
		String sOID = "";
		String sMode = "basic";

		if (null == sSuite) {
			sSuite = "Components";
		} else {
			sSuite = sSuite.replace(" ", "");
		}
		if (null == sKeyBold) {
			sKeyBold = FALSE_STRING;
		}
		if (null == sKeyDemote) {
			sKeyDemote = TRUE_STRING;
		}
		if (null == sKeyNext) {
			sKeyNext = "";
		}
		if (null == sKeyPrev) {
			sKeyPrev = "";
		}
		if (null == sKeyLink) {
			sKeyLink = TRUE_STRING;
		}

		if (null == sKeyPromoteLabel) {
			sKeyPromoteLabel = "emxComponents.Button.Promote";
		}
		if (null == sKeyDemoteLabel) {
			sKeyDemoteLabel = "emxComponents.Button.Demote";
		}
		if (null == sKeyCompleteIcon) {
			sKeyCompleteIcon = "iconStatusFinished.gif";
		}

		if (null == sKeyInactivateComp) {
			sKeyInactivateComp = FALSE_STRING;
		}
		if (null == sKeyInactivateIcon) {
			sKeyInactivateIcon = "iconActionStop.gif";
		}
		if (null == sKeyInactivateLabel) {
			sKeyInactivateLabel = "emxComponents.Button.Inactivate";
		}
		if (null == sKeyInactivatePolicy) {
			sKeyInactivatePolicy = "";
		}
		if (null == sKeyInactivateState) {
			sKeyInactivateState = "";
		}
		if (null == sKeyStates) {
			sKeyStates = "";
		}
		if (null == sKeyColors) {
			sKeyColors = "";
		}

		String sLabelPromote = EnoviaResourceBundle.getProperty(context, sSuite, sKeyPromoteLabel, sLang);
		String sLabelDemote = EnoviaResourceBundle.getProperty(context, sSuite, sKeyDemoteLabel, sLang);
		String sLabelInactivate = EnoviaResourceBundle.getProperty(context, sSuite, sKeyInactivateLabel, sLang);
		String sLabelComplete = "";

		if (UIUtil.isNotNullAndNotEmpty(sKeyCompleteState)) {
			if (null == sKeyCompleteLabel) {
				sLabelComplete = EnoviaResourceBundle.getProperty(context, "Framework",
						"emxFramework.String.SetToFinalState", sLang);
			} else {
				sLabelComplete = EnoviaResourceBundle.getProperty(context, sSuite, sKeyCompleteLabel, sLang);
			}
		}

		String[] aStates = sKeyStates.split(",");
		String[] aColors = sKeyColors.split(",");

		StringBuilder sbStyleCommon = new StringBuilder();
		if (!FALSE_STRING.equalsIgnoreCase(sKeyBold)) {
			sbStyleCommon.append("font-weight:bold;");
		}
		if (aStates.length > aColors.length) {
			System.out.println(
					"! Error in GNVColumns.columnStatus : Number of specified states does not match number of specified colors");
			aStates = new String[0];
			aColors = new String[0];
		}

		StateList sList = null;
		Map<String, StateList> stateMap = new HashMap<String, StateList>();
    	boolean isSizeOne = mlOIDs.size() == 1;
		for (int i = 0; i < mlOIDs.size(); i++) {

			StringBuilder sbResult = new StringBuilder();
			Map mObjects = (Map) mlOIDs.get(i);
			sOID = (String) mObjects.get(DomainConstants.SELECT_ID);
			String sRowID = (String) mObjects.get(ID_LEVEL);
			String sCurrent = (String) mObjects.get(DomainConstants.SELECT_CURRENT);
			String sPolicy = (String) mObjects.get(DomainConstants.SELECT_POLICY);

    		// If the program which fetches data for structure browser don't include current & policy selectables, 
    		// it wont be available in mObjects. So, we need to fetch it from database
    		if(UIUtil.isNullOrEmpty(sCurrent) || UIUtil.isNullOrEmpty(sPolicy) || isSizeOne){ 
				StringList busSelects = new StringList();
				busSelects.add(DomainConstants.SELECT_CURRENT);
				busSelects.add(DomainConstants.SELECT_POLICY);
				DomainObject dObject = DomainObject.newInstance(context, sOID);
				Map mData = dObject.getInfo(context, busSelects);
					sCurrent = (String) mData.get(DomainConstants.SELECT_CURRENT);
					sPolicy = (String) mData.get(DomainConstants.SELECT_POLICY);
				}

			String sCompleteState = PropertyUtil.getSchemaProperty(context, "policy", sPolicy, sKeyCompleteState);

			// in case of export it should return name of current State.
			if (isExport) {
				sCurrent = EnoviaResourceBundle.getStateI18NString(context, sPolicy, sCurrent, sLang);
				vResult.addElement(sCurrent);
			} else {

				sList = stateMap.get(sPolicy);

				// Build a temporary cache if state list is empty
				if (sList == null) {
					DomainObject dObject = DomainObject.newInstance(context, sOID);
					sList = dObject.getStates(context);
					stateMap.put(sPolicy, sList);
				}

				State stateFirst = (State) sList.get(0);
				State stateLast = (State) sList.get(sList.size() - 1);
				int iCurrent = 0;
				StringBuilder sbStyle = new StringBuilder();
				sbStyle.append(sbStyleCommon);

				String sStatusPrev = "";
				String sStatusNext = "";

				for (int j = 0; j < sList.size(); j++) {
					State state = (State) sList.get(j);
					String sStatus = state.getName();
					if (sStatus.equals(sCurrent)) {
						iCurrent = j;
						break;
					}
				}

				if ("".equals(sKeyStates)) {
					sbStyle.append("color:").append(sColorLink).append(';');
				} else {
					for (int j = 0; j < aStates.length; j++) {
						String stateName = PropertyUtil.getSchemaProperty(context, "policy", sPolicy, aStates[j]);
						if (stateName.equals(sCurrent)) {
							sbStyle.append("color:#").append(aColors[j]).append(';');
						}
					}
				}
				List<String> hiddenStates = CacheManager.getInstance().getValue(context,
						CacheManager._entityNames.HIDDEN_STATES, sPolicy);

				StringBuilder sbPromote = new StringBuilder();
				if ((!sCurrent.equals(stateLast.getName()))
						&& ("".equals(sCompleteState) || !sCompleteState.equals(sCurrent))) {
					sbPromote.append("<a href='../common/emxColumnStatusAction.jsp?action=promote&amp;objectId=").append(sOID).append("&amp;rowId=").append(sRowID).append("' target='listHidden'>");
					sbPromote.append("<img style='vertical-align:middle;' src='../common/images/iconActionPromote.gif' TITLE='").append(sLabelPromote).append("'/>").append("</a>");
				} else {
					sbPromote.append(sSpacer);
				}
				if (TRUE_STRING.equalsIgnoreCase(sKeyNext)) {
					if (!sCurrent.equals(stateLast.getName())) {
						State stateNext = (State) sList.get(iCurrent + 1);
						sStatusNext = stateNext.getName();
						int iCount = 2;
						while (hiddenStates.contains(sStatusNext)) {
							if (sList.size() > iCurrent + 2) {
								stateNext = (State) sList.get(iCurrent + iCount);
								sStatusNext = stateNext.getName();
								iCount++;
							}
						}
						sStatusNext = i18nNow.getStateI18NString(sPolicy, sStatusNext, sLang);
						sStatusNext = "<span style='color:#aab8be;font-size:80%;font-style:italic;'>" + sStatusNext
								+ "</span>";
					} else {
						sStatusNext = "<img src='../common/images/utilSpacer.gif' height='12'/>";
					}
					sStatusNext = sStatusNext + "<br/>";
				}

				StringBuilder sbDemote = new StringBuilder();
				if (!sCurrent.equals(stateFirst.getName())) {
					sbDemote.append("<a href='../common/emxColumnStatusAction.jsp?action=demote&amp;objectId=").append(sOID).append("&amp;rowId=").append(sRowID).append("' target='listHidden'>");
					sbDemote.append("<img style='vertical-align:middle;' src='../common/images/iconActionDemote.gif' TITLE='").append(sLabelDemote).append("'/>").append("</a>");
					if (TRUE_STRING.equalsIgnoreCase(sKeyPrev)) {
						State statePrev = (State) sList.get(iCurrent - 1);
						sStatusPrev = statePrev.getName();
						int iCount = 2;
						while (hiddenStates.contains(sStatusPrev)) {
							if (0 <= iCurrent - iCount) {
								statePrev = (State) sList.get(iCurrent - iCount);
								sStatusPrev = statePrev.getName();
								iCount++;
							}
						}
						sStatusPrev = i18nNow.getStateI18NString(sPolicy, sStatusPrev, sLang);
						sStatusPrev = "<span style='color:#aab8be;font-size:80%;font-style:italic;'>" + sStatusPrev
								+ "</span>";
					}
				} else {
					sbDemote.append(sSpacer);
				}
				if (TRUE_STRING.equalsIgnoreCase(sKeyPrev)) {
					if ("".equalsIgnoreCase(sStatusPrev)) {
						sStatusPrev = "<img src='../common/images/utilSpacer.gif' height='12'/>";
					}
					sStatusPrev = "<br/>" + sStatusPrev;
				}

				StringBuilder sbComplete = new StringBuilder();
				if (!"".equals(sCompleteState)) {
					if (!sCurrent.equals(sCompleteState) && !sCurrent.equals(stateLast.getName())) {
						sbComplete.append("<a href='../common/emxColumnStatusAction.jsp?action=setState&amp;objectId=").append(sOID).append("&amp;rowId=").append(sRowID).append("&amp;state=").append(sCompleteState).append("&amp;label=").append(XSSUtil.encodeForURL(context, sLabelComplete)).append("' target='listHidden'>");
						sbComplete.append("<img style='vertical-align:middle;width:16px;' src='../common/images/").append(sKeyCompleteIcon).append("' TITLE='").append(XSSUtil.encodeForXML(context, sLabelComplete)).append("'/>").append("</a>");
					} else {
						sbComplete.append(sSpacer);
					}
				}

				StringBuilder sbInactivate = new StringBuilder();
				if (!"".equals(sKeyInactivateState)) {
					if ((!sCurrent.equals(sKeyInactivateState) && !sCurrent.equals(stateLast.getName()))
							&& ("".equals(sCompleteState) || !sCompleteState.equals(sCurrent)
									|| !FALSE_STRING.equalsIgnoreCase(sKeyInactivateComp))) {
						// if(true) {
						sbInactivate.append("<a href='../common/emxColumnStatusAction.jsp?action=setState&amp;objectId=").append(sOID).append("&amp;rowId=").append(sRowID).append("&amp;state=").append(sKeyInactivateState).append("&amp;label=").append(sLabelInactivate).append("' target='listHidden'>");
						sbInactivate.append("<img style='vertical-align:middle;width:16px;' src='../common/images/").append(sKeyInactivateIcon).append("' TITLE='").append(sLabelInactivate).append("'/>");
						sbInactivate.append("</a>");
					} else {
						sbInactivate.append(sSpacer);
					}
				} else if (!"".equals(sKeyInactivatePolicy)) {
					if ((!sPolicy.equals(sKeyInactivatePolicy)) && ("".equals(sCompleteState)
							|| !sCompleteState.equals(sCurrent) || !FALSE_STRING.equalsIgnoreCase(sKeyInactivateComp))) {
						sbInactivate.append("<a href='../common/emxColumnStatusAction.jsp?action=setPolicy&amp;objectId=").append(sOID).append("&amp;rowId=").append(sRowID).append("&amp;policy=").append(sKeyInactivatePolicy).append("&amp;label=").append(sLabelInactivate).append("' target='listHidden'>");
						sbInactivate.append("<img style='vertical-align:middle;width:16px;' src='../common/images/").append(sKeyInactivateIcon).append("' TITLE='").append(sLabelInactivate).append("'/>").append("</a>");
					} else {
						sbInactivate.append(sSpacer);
					}
				}

				sCurrent = EnoviaResourceBundle.getStateI18NString(context, sPolicy, sCurrent, sLang);

				sbResult.append("<table style='height:100%;width:100%' border='0' onmouseover='showLifeCycleIcons(this,true);' onmouseout='showLifeCycleIcons(this,false);'><tr>");
				sbResult.append("<td style='padding-left:3px;vertical-align:middle;width:100%;'>").append(sStatusNext);
				if (!FALSE_STRING.equalsIgnoreCase(sKeyLink)) {
					sbResult.append("<a href='#' onClick=\"var posLeft=(screen.width/2)-(").append(sWindowWidth).append("/2);var posTop = (screen.height/2)-(").append(sWindowHeight).append("/2);");
					sbResult.append("emxTableColumnLinkClick('../common/emxLifecycle.jsp?suiteKey=Framework&amp;toolbar=AEFLifecycleMenuToolBar&amp;header=emxFramework.Lifecycle.LifeCyclePageHeading&amp;export=false&amp;mode=").append(sMode).append("&amp;objectId=").append(sOID).append("', '', 'height=").append(sWindowHeight).append(",width=").append(sWindowWidth).append(",top=' + posTop + ',left=' + posLeft + ',toolbar=no,directories=no,status=no,menubar=no;return false;');return false\">");
				}
				sbResult.append("<span style='").append(sbStyle.toString()).append("'>").append(sCurrent).append("</span>");
				if (!FALSE_STRING.equalsIgnoreCase(sKeyLink)) {
					sbResult.append("</a>");
				}
				sbResult.append(sStatusPrev).append("</td>").append("<td id='lifeCycleSectionId' style='visibility:hidden;vertical-align:middle;'><table><tr><td id='columnPromoteAction' style='vertical-align:middle;width:20px'>");
				if (!hiddenStates.contains(sCurrent)) {
					sbResult.append(sbPromote.toString());
				}
				sbResult.append("</td>");

				if (!FALSE_STRING.equalsIgnoreCase(sKeyDemote) && !hiddenStates.contains(sCurrent)) {
					sbResult.append("<td style='vertical-align:middle;width:20px'>").append(sbDemote.toString()).append("</td>");
				}
				if (!"".equalsIgnoreCase(sCompleteState)) {
					sbResult.append("<td style='vertical-align:middle;width:20px'>").append(sbComplete.toString()).append("</td>");
				}
				if (!"".equalsIgnoreCase(sKeyInactivateState)) {
					sbResult.append("<td style='vertical-align:middle;width:20px'>").append(sbInactivate.toString()).append("</td>");
				} else if (!"".equalsIgnoreCase(sKeyInactivatePolicy)) {
					sbResult.append("<td style='vertical-align:middle;width:20px'>").append(sbInactivate.toString()).append("</td>");
				}
				sbResult.append("</tr></table></td></tr></table>");
				vResult.addElement(sbResult.toString());
			}
		}
		return vResult;
	}

	// Related Items Column
	public Vector columnRelatedItems(Context context, String[] args) throws Exception {
		HashMap programMap = (HashMap) JPO.unpackArgs(args);
		HashMap paramList = (HashMap) programMap.get(PARAM_LIST);
		String sLang = (String) paramList.get(LANGUAGE_STR);
		HashMap columnMap = (HashMap) programMap.get(COLUMN_MAP);
		HashMap settings = (HashMap) columnMap.get(SETTINGS);
		MapList mlObjects = (MapList) programMap.get(OBJECT_LIST);
		return controlRelatedItems(context, args, (String) paramList.get("StringResourceFileId"), settings, mlObjects,
				TABLE, "listHidden", sLang);
	}

	public static Vector controlRelatedItems(Context context, String[] args, String sResourceFile, HashMap settings,
			MapList mlObjects, String sMode, String sTargetFrame, String sLang) throws Exception {

		Vector vResult = new Vector(mlObjects.size());
		String sMCSURL = emxUtil_mxJPO.getMCSURL(context, args);
		String[] aTooltipIcon = {};
		String[] aLatestLabel = null;
		String[] aEditStates = {};
		String[] aZeroStates = {};
		String[] aNonzeroStates = {};
		String[] aThumbDetails = {};
		String[] aThumbStyles = {};
		String[] aThumbTooltip = {};
		String[] aColumns = {};
		String[] aStyles = {};
		int iMaxItems = 10;
		int iTableRows = 1;
		Boolean bFrom = false;
		Boolean bTo = true;
		Calendar cNow = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aaa");
		String sSuite = (String) settings.get("Registered Suite");
		String sSuiteDir = "";
		String sLinkSuite = "";
		Map paramMap = (Map) JPO.unpackArgs(args);
		Map paramList = (Map) paramMap.get(PARAM_LIST);
		String sLanguage = (String) paramList.get(LANGUAGE_STR);
		Locale locale = new Locale(sLanguage);
		if (sSuite == null) {
			sSuite = "";
		} else {
			sSuiteDir = FrameworkProperties.getProperty(context, "eServiceSuite" + sSuite + ".Directory");
			sResourceFile = FrameworkProperties.getProperty(context,
					"eServiceSuite" + sSuite + ".StringResourceFileId");
			sLinkSuite = "&amp;suiteKey=" + sSuite + "&amp;StringResourceFileId=" + sResourceFile
					+ "&amp;SuiteDirectory=" + sSuiteDir;
		}

		// Data Retrieval Settings
		String sRelationships = (String) settings.get("Relationships");
		String sTypes = (String) settings.get("Types");
		String sFrom = (String) settings.get("From");
		String sExpandLevel = (String) settings.get("Expand Level");
		String sWhereBus = (String) settings.get("Filter BUS");
		String sWhereRel = (String) settings.get("Filter REL");
		String sPatternTypes = (String) settings.get("Pattern Types");

		// Actions Settings
		String sShowDropZone = (String) settings.get("Show Drop Zone");
		String sDropZoneHeight = (String) settings.get("Drop Zone Height");
		String sDropZoneType = (String) settings.get("Drop Zone Type");
		String sDropZoneRelationship = (String) settings.get("Drop Zone Relationship");
		String sDropZoneTooltip = (String) settings.get("Drop Zone Tooltip");
		String sAddLink = (String) settings.get("Add Link");
		String sAddIcon = (String) settings.get("Add Icon");
		String sAddText = (String) settings.get("Add Text");
		String sAddTooltip = (String) settings.get("Add Tooltip");
		String sCreateLink = (String) settings.get("Create Link");
		String sCreateIcon = (String) settings.get("Create Icon");
		String sCreateText = (String) settings.get("Create Text");
		String sCreateTooltip = (String) settings.get("Create Tooltip");
		String sEditCount = (String) settings.get("Edit Count");
		String sEditStates = (String) settings.get("Edit States");

		// Display Settings
		String sShowCounter = (String) settings.get("Show Counter");
		String sCounterStyle = (String) settings.get("Counter Style");
		String sCounterLink = (String) settings.get("Counter Link");
		String sCounterTooltip = (String) settings.get("Counter Tooltip");
		String sHighlightZeroIcon = (String) settings.get("Highlight Zero Icon");
		String sHighlightZeroLink = (String) settings.get("Highlight Zero Link");
		String sHighlightZeroStyle = (String) settings.get("Highlight Zero Style");
		String sHighlightZeroTooltip = (String) settings.get("Highlight Zero Tooltip");
		String sHighlightZeroStates = (String) settings.get("Highlight Zero States");
		String sHighlightNonzeroIcon = (String) settings.get("Highlight Nonzero Icon");
		String sHighlightNonzeroLink = (String) settings.get("Highlight Nonzero Link");
		String sHighlightNonzeroStyle = (String) settings.get("Highlight Nonzero Style");
		String sHighlightNonzeroTooltip = (String) settings.get("Highlight Nonzero Tooltip");
		String sHighlightNonzeroStates = (String) settings.get("Highlight Nonzero States");
		String sShowIcons = (String) settings.get("Show Icons");
		String sIconLink = (String) settings.get("Icon Link");
		String sIconTooltip = (String) settings.get("Icon Tooltip");
		String sSortKey = (String) settings.get("Sort Key");
		String sSortDirection = (String) settings.get("Sort Direction");
		String sSortType = (String) settings.get("Sort Type");
		String sMaxItems = (String) settings.get("Max Items");
		String sLinkMore = (String) settings.get("Link More");
		String sShowLatest = (String) settings.get("Show Latest");
		String sLatestLabel = (String) settings.get("Latest Label");
		String sLatestLink = (String) settings.get("Latest Link");
		String sLatestThreshold = (String) settings.get("Latest Threshold");
		String sLatestWidth = (String) settings.get("Latest Width");
		String sShowThumbnails = (String) settings.get("Show Thumbnails");
		String sThumbnailActions = (String) settings.get("Thumbnail Actions");
		String sThumbnailDetails = (String) settings.get("Thumbnail Details");
		String sThumbnailLink = (String) settings.get("Thumbnail Link");
		String sThumbnailSize = (String) settings.get("Thumbnail Size");
		String sThumbnailStyles = (String) settings.get("Thumbnail Styles");
		String sThumbnailTooltip = (String) settings.get("Thumbnail Tooltip");
		String sShowTable = (String) settings.get("Show Table");
		String sTableActions = (String) settings.get("Table Actions");
		String sTableCellRows = (String) settings.get("Table Cell Rows");
		String sTableColumns = (String) settings.get("Table Columns");
		String sTableLink = (String) settings.get("Table Link");
		String sTableStyles = (String) settings.get("Table Styles");
		String sTooltipSeparator = (String) settings.get("Tooltip Separator");
		String sShowMore = (String) settings.get("Show More");

		String popupSize = (String) settings.get("Popup Size");
		StringList popupHeightWidth = getPopupHeightWidth(context, popupSize);
		String sWindowWidth = (String) popupHeightWidth.get(0);
		String sWindowHeight = (String) popupHeightWidth.get(1);

		// Data Retrieval Defaults
		if (sRelationships == null) {
			sRelationships = "*";
		}
		if (sTypes == null) {
			sTypes = "*";
		}
		if (null != sFrom) {
			if (FALSE_STRING.equalsIgnoreCase(sFrom)) {
				bFrom = true;
				bTo = false;
			}
		} else {
			sFrom = TRUE_STRING;
		}
		if (sExpandLevel == null) {
			sExpandLevel = "1";
		}
		if (sWhereBus == null) {
			sWhereBus = "";
		}
		if (sWhereRel == null) {
			sWhereRel = "";
		}
		if (sPatternTypes == null) {
			sPatternTypes = "";
		}

		// Action Defaults
		if (sShowDropZone == null) {
			sShowDropZone = FALSE_STRING;
		}
		if (sDropZoneHeight == null) {
			sDropZoneHeight = "20";
		}
		if (sDropZoneType == null) {
			sDropZoneType = "Document";
		}
		if (sDropZoneRelationship == null) {
			sDropZoneRelationship = "Reference%20Document";
		} else {
			sDropZoneRelationship = sDropZoneRelationship.replace(" ", "%20");
		}
		if (sDropZoneTooltip == null) {
			sDropZoneTooltip = "emxFramework.String.TooltipDropZone";
		}
		if (sAddLink == null) {
			sAddLink = "";
		}
		if (sAddText == null) {
			sAddText = "";
		}
		if (sAddIcon == null) {
			sAddIcon = "../../common/images/iconActionAdd.png";
		}
		if (sAddTooltip == null) {
			sAddTooltip = "emxComponents.Command.AddExisting";
		}
		if (sCreateLink == null) {
			sCreateLink = "";
		}
		// if(sCreateLink == null) { sCreateLink = ""; } else { sCreateLink =
		// sCreateLink.replace("&", "&amp;"); if(!sCreateLink.contains("?")) {
		// sCreateLink += "?"; } }
		if (sCreateText == null) {
			sCreateText = "";
		}
		if (sCreateIcon == null) {
			sCreateIcon = "iconActionCreate.gif";
		}
		if (sCreateTooltip == null) {
			sCreateTooltip = EnoviaResourceBundle.getFrameworkStringResourceProperty(context,
					"emxFramework.HomePage.CreateTooltip", context.getLocale());
		}
		if (sEditStates != null) {
			sCreateTooltip = EnoviaResourceBundle.getFrameworkStringResourceProperty(context,
					"emxFramework.HomePage.CreateTooltip", context.getLocale());
		}
		if (sEditCount == null) {
			sEditCount = "";
		}
		if (sEditStates == null) {
			sEditStates = "";
		} else {
			aEditStates = sEditStates.split(",");
		}

		// Display Defaults
		if (sShowCounter == null) {
			sShowCounter = TRUE_STRING;
		}
		if (sCounterStyle == null) {
			sCounterStyle = "";
		} else {
			if (!sCounterStyle.endsWith(";")) {
				sCounterStyle += ";";
			}
		}
		if (sCounterLink == null) {
			sCounterLink = "";
		}
		if (sCounterTooltip == null) {
			sCounterTooltip = "";
		} else {
			sCounterTooltip = "title='" + sCounterTooltip + "'";
		}
		if (sHighlightZeroIcon == null) {
			sHighlightZeroIcon = "";
		}
		if (sHighlightZeroLink == null) {
			sHighlightZeroLink = "";
		}
		if (sHighlightZeroStyle == null) {
			sHighlightZeroStyle = "";
		}
		if (sHighlightZeroTooltip == null) {
			sHighlightZeroTooltip = EnoviaResourceBundle.getFrameworkStringResourceProperty(context,
					"emxFramework.HomePage.ZeroTooltip", context.getLocale());
		} else {
			sHighlightZeroTooltip = i18nNow.getI18nString(sHighlightZeroTooltip, sResourceFile, sLang);
		}
		if (sHighlightZeroStates == null) {
			sHighlightZeroStates = "";
		} else {
			aZeroStates = sHighlightZeroStates.split(",");
		}
		if (sHighlightNonzeroIcon == null) {
			sHighlightNonzeroIcon = "";
		}
		if (sHighlightNonzeroLink == null) {
			sHighlightNonzeroLink = "";
		}
		if (sHighlightNonzeroStyle == null) {
			sHighlightNonzeroStyle = "";
		}
		if (sHighlightNonzeroTooltip == null) {
			sHighlightNonzeroTooltip = EnoviaResourceBundle.getFrameworkStringResourceProperty(context,
					"emxFramework.HomePage.NonZeroTooltip", context.getLocale());
		} else {
			sHighlightNonzeroTooltip = i18nNow.getI18nString(sHighlightNonzeroTooltip, sResourceFile, sLang);
		}
		if (sHighlightNonzeroStates == null) {
			sHighlightNonzeroStates = "";
		} else {
			aNonzeroStates = sHighlightNonzeroStates.split(",");
		}
		if (sShowIcons == null) {
			sShowIcons = FALSE_STRING;
		}
		if (sIconLink == null) {
			sIconLink = "";
		}
		if (sIconTooltip == null) {
			sIconTooltip = "type,name,revision";
		}
		if (sSortKey == null) {
			sSortKey = "";
		}
		if (sSortDirection == null) {
			sSortDirection = "ascending";
		}
		if (sSortType == null) {
			sSortType = "";
		}
		if (sMaxItems != null) {
			if (!"".equals(sMaxItems)) {
				iMaxItems = Integer.parseInt(sMaxItems);
			}
		}
		if (sLinkMore == null) {
			if (!"".equals(sCounterLink)) {
				sLinkMore = sCounterLink;
			} else {
				sLinkMore = "emxTree.jsp?";
			}
		}
		if (sShowLatest == null) {
			sShowLatest = FALSE_STRING;
		}
		if (sLatestLabel == null) {
			sLatestLabel = "type,name";
		}
		if (sLatestLink == null) {
			sLatestLink = "";
		}
		if (sLatestThreshold == null) {
			cNow.add(java.util.GregorianCalendar.DAY_OF_YEAR, -10);
		} else {
			cNow.add(java.util.GregorianCalendar.DAY_OF_YEAR, -Integer.parseInt(sLatestThreshold));
		}
		if (sLatestWidth == null) {
			sLatestWidth = "250";
		}
		if (sShowThumbnails == null) {
			sShowThumbnails = FALSE_STRING;
		}
		if (sThumbnailActions == null) {
			sThumbnailActions = "";
		}
		if (sThumbnailDetails != null) {
			aThumbDetails = sThumbnailDetails.split(",");
		}
		if (sThumbnailLink == null) {
			sThumbnailLink = "emxTree.jsp?";
		}
		if (sThumbnailSize == null) {
			sThumbnailSize = "mxThumbnail Image";
		}
		if (sThumbnailStyles == null) {
			sThumbnailStyles = "";
		}
		if (sThumbnailTooltip == null) {
			sThumbnailTooltip = "type,name,revision";
		}
		if (sShowTable == null) {
			sShowTable = FALSE_STRING;
		}
		if (sTableActions == null) {
			sTableActions = "";
		}
		if (sTableCellRows != null) {
			iTableRows = Integer.parseInt(sTableCellRows);
		}
		if (sTableColumns == null) {
			sTableColumns = "Icon,type,name";
		}
		if (sTableLink == null) {
			sTableLink = "";
		}
		if (sTableStyles == null) {
			if ("Icon,type,name".equals(sTableColumns)) {
				sTableStyles = ",,font-weight:bold;";
			} else {
				sTableStyles = "";
			}
		}
		if (sTooltipSeparator == null) {
			sTooltipSeparator = "-";
		}
		if (sShowMore == null) {
			sShowMore = TRUE_STRING;
		}

		Boolean bAddLinkTree = false;
		Boolean bCreateLinkTree = false;
		Boolean bCounterLinkTree = true;
		Boolean bHighlightZeroLinkTree = true;
		Boolean bHighlightNonzeroLinkTree = true;
		Boolean bIconLinkTree = false;
		Boolean bMoreLinkTree = true;
		Boolean bLatestLinkTree = false;
		Boolean bThumbnailLinkTree = false;
		Boolean bTableLinkTree = false;

		sAddLink = getLinkURL(context, sLinkSuite, sSuiteDir, sAddLink, bAddLinkTree);
		sCreateLink = getLinkURL(context, sLinkSuite, sSuiteDir, sCreateLink, bCreateLinkTree);
		sCounterLink = getLinkURL(context, sLinkSuite, sSuiteDir, sCounterLink, bCounterLinkTree);
		sHighlightZeroLink = getLinkURL(context, sLinkSuite, sSuiteDir, sHighlightZeroLink, bHighlightZeroLinkTree);
		sHighlightNonzeroLink = getLinkURL(context, sLinkSuite, sSuiteDir, sHighlightNonzeroLink,
				bHighlightNonzeroLinkTree);
		sIconLink = getLinkURL(context, sLinkSuite, sSuiteDir, sIconLink, bIconLinkTree);
		sLinkMore = getLinkURL(context, sLinkSuite, sSuiteDir, sLinkMore, bMoreLinkTree);
		sLatestLink = getLinkURL(context, sLinkSuite, sSuiteDir, sLatestLink, bLatestLinkTree);
		sThumbnailLink = getLinkURL(context, sLinkSuite, sSuiteDir, sThumbnailLink, bThumbnailLinkTree);
		sTableLink = getLinkURL(context, sLinkSuite, sSuiteDir, sTableLink, bTableLinkTree);

		String sLinkPrefix = "onClick=\"emxFormLinkClick('../common/";
		String sLinkSuffix = "', 'popup', '', '" + sWindowWidth + "', '" + sWindowHeight + "', '')\"";
		if (TABLE.equals(sMode)) {
			sLinkPrefix = "onClick=\"emxTableColumnLinkClick('../common/";
			sLinkSuffix = "','" + sWindowWidth + "', '" + sWindowHeight + "','true', 'popup', '','')\"";
		}

		String sStyleLink = "font-size:7pt;font-style:italic;font-weight:normal;";
		String sRowHover = " onmouseover='$(this).find(\"*\").css(\"color\",\"" + sColorLink
				+ "\");' onmouseout='$(this).find(\"*\").css(\"color\",\"#333333\");' ";
		String sSizeIcon = "16";
		String sTooltipDropFiles = i18nNow.getI18nString(sDropZoneTooltip, "emxFrameworkStringResource", sLang);

		// ------------------------------------------------------------------
		// Ensure Proper Selects
		// ------------------------------------------------------------------
		StringList busSelects = new StringList();
		StringList relSelects = new StringList();
		busSelects.add(DomainConstants.SELECT_ID);
		busSelects.add(
				"type.kindof[" + PropertyUtil.getSchemaProperty(context, DomainObject.SYMBOLIC_type_DOCUMENTS) + "]");
		relSelects.add(DomainConstants.SELECT_RELATIONSHIP_ID);
		if (!"".equals(sPatternTypes)) {
			busSelects.add(DomainConstants.SELECT_TYPE);
		}
		if (TRUE_STRING.equalsIgnoreCase(sShowIcons) || TRUE_STRING.equalsIgnoreCase(sShowLatest)) {
			if (!busSelects.contains(DomainConstants.SELECT_TYPE)) {
				busSelects.add(DomainConstants.SELECT_TYPE);
			}
		}
		if (!"".equals(sSortKey)) {
			if (!busSelects.contains(sSortKey)) {
				busSelects.add(sSortKey);
			}
			if ("".equals(sSortType)) {
				if (sSortKey.contains("[")) {
					String sAttribute = sSortKey.substring(sSortKey.indexOf('[') + 1);
					sAttribute = sAttribute.substring(0, sAttribute.indexOf(']'));
					AttributeType aType = new AttributeType(sAttribute);
					sSortType = aType.getDataType(context);
				} else if ("modified".equals(sSortKey)) {
					sSortType = "date";
				} else if ("originated".equals(sSortKey)) {
					sSortType = "date";
				} else {
					sSortType = "string";
				}
			}
		}
		if (!"".equals(sIconTooltip)) {
			aTooltipIcon = sIconTooltip.split(",");
			for (int i = 0; i < aTooltipIcon.length; i++) {
				String sSelect = aTooltipIcon[i].trim();
				if (!busSelects.contains(sSelect)) {
					busSelects.add(sSelect);
				}
			}
		}
		if (TRUE_STRING.equalsIgnoreCase(sShowLatest)) {
			if (!busSelects.contains("modified")) {
				busSelects.add("modified");
			}
			aLatestLabel = sLatestLabel.split(",");
			for (int i = 0; i < aLatestLabel.length; i++) {
				String sSelect = aLatestLabel[i].trim();
				if (!busSelects.contains(sSelect)) {
					busSelects.add(sSelect);
				}
			}
		}
		if (aThumbDetails.length > 0) {
			for (int i = 0; i < aThumbDetails.length; i++) {
				String sSelect = aThumbDetails[i].trim();
				if (!busSelects.contains(sSelect)) {
					busSelects.add(sSelect);
				}
			}
			aThumbStyles = new String[aThumbDetails.length];

			String sSettings = sThumbnailStyles;
			int i = 0;
			while (sSettings.indexOf(',') != -1) {
				if (i < aThumbDetails.length - 1) {
					aThumbStyles[i] = sSettings.substring(0, sSettings.indexOf(','));
				}
				i++;
				sSettings = sSettings.substring(sSettings.indexOf(',') + 1);
			}
			if (i < aThumbDetails.length - 1) {
				aThumbStyles[i] = sSettings;
			}
		}
		if (!"".equals(sThumbnailTooltip)) {
			aThumbTooltip = sThumbnailTooltip.split(",");
			for (int i = 0; i < aThumbTooltip.length; i++) {
				String sSelect = aThumbTooltip[i].trim();
				if (!busSelects.contains(sSelect)) {
					busSelects.add(sSelect);
				}
			}
		}
		if (!"".equals(sShowTable)) {
			aColumns = sTableColumns.split(",");
			for (int i = 0; i < aColumns.length; i++) {
				String sSelect = aColumns[i].trim();
				if (!ICON.equals(sSelect)) {
					if (!"Image".equals(sSelect)) {
						if (!busSelects.contains(sSelect)) {
							busSelects.add(sSelect);
						}
					}
				}
			}
			aStyles = new String[aColumns.length];
			String sSettings = sTableStyles;
			int i = 0;
			while (sSettings.indexOf(',') != -1) {
				if (i < aColumns.length - 1) {
					aStyles[i] = sSettings.substring(0, sSettings.indexOf(','));
				}
				i++;
				sSettings = sSettings.substring(sSettings.indexOf(',') + 1);
			}
			if (i < aColumns.length) {
				aStyles[i] = sSettings;
			}
		}

		// ------------------------------------------------------------------
		// Predefine Column Widths
		// ------------------------------------------------------------------

		int iWidthHighlightIcon = 0;
		int iWidthDropZone = 30;
		int iWidthCounter = 0;
		int iWidthActions = 0;
		int iWidthTableActions = 0;

		if (TABLE.equals(sMode)) {
			sSizeIcon = "12";

			if (!"".equals(sHighlightZeroIcon) || !"".equals(sHighlightNonzeroIcon)) {
				iWidthHighlightIcon = 25;
			}
			// iWidthCounter = 35;
			if (sAddText.length() > sCreateText.length()) {
				iWidthActions = iWidthActions + (sAddText.length() * 6);
			} else {
				iWidthActions = iWidthActions + (sCreateText.length() * 6);
			}
			if (!"".equals(sCreateIcon) || !"".equals(sAddIcon)) {
				if (iWidthActions == 0) {
					iWidthActions = 24;
				} else {
					iWidthActions += 20;
				}
			}
			if (!"".equals(sTableActions)) {
				String[] aTableActions = sTableActions.split(",");
				iWidthTableActions = aTableActions.length * 20;
			}
		}

		for (int i = 0; i < mlObjects.size(); i++) {

			StringBuilder sbResult = new StringBuilder();
			Map mObject = (Map) mlObjects.get(i);
			String sOID = (String) mObject.get(DomainConstants.SELECT_ID);
			String sRowID = (String) mObject.get(ID_LEVEL);
			DomainObject dObject = new DomainObject(sOID);
			MapList mlRelatedObjects = new MapList();
			Boolean bEdit = true;
			Boolean bGetTypesDetails = false;

			if (aEditStates.length > 0) {
				bEdit = false;
				String sCurrent = (String) mObject.get(DomainConstants.SELECT_CURRENT);
				for (int j = 0; j < aEditStates.length; j++) {
					if (aEditStates[j].equalsIgnoreCase(sCurrent)) {
						bEdit = true;
						break;
					}
				}
			}

			// Check for Conditions where ADK method call is needed
			if ((bEdit && !"".equals(sEditCount)) || !"".equals(sSortKey)
					|| (!"".equals(sHighlightZeroIcon) || !"".equals(sHighlightNonzeroIcon)
							|| !"".equals(sHighlightZeroStyle) || !"".equals(sHighlightNonzeroStyle))
					|| TRUE_STRING.equalsIgnoreCase(sShowCounter) || TRUE_STRING.equalsIgnoreCase(sShowIcons)
					|| TRUE_STRING.equalsIgnoreCase(sShowThumbnails) || TRUE_STRING.equalsIgnoreCase(sShowTable)) {
				bGetTypesDetails = true;
			}

			if (bGetTypesDetails) {
				if ("".equals(sPatternTypes)) {
					mlRelatedObjects = dObject.getRelatedObjects(context, sRelationships, sTypes, busSelects,
							relSelects, bFrom, bTo, Short.parseShort(sExpandLevel), sWhereBus, sWhereRel, 0);
				} else {
					String[] aTypes = sPatternTypes.split(",");
					if (null != aTypes) {
						Pattern pTypes = new Pattern(aTypes[0]);
						for (int j = 1; j < aTypes.length; j++) {
							pTypes.addPattern(aTypes[j]);
						}
						mlRelatedObjects = dObject.getRelatedObjects(context, sRelationships, sTypes, busSelects,
								relSelects, bFrom, bTo, Short.parseShort(sExpandLevel), sWhereBus, sWhereRel, 0, pTypes,
								null, null);
					}
				}
			}
			if (bEdit) {
				if (!"".equals(sEditCount)) {
					Integer iEditCount = Integer.parseInt(sEditCount);
					if (iEditCount != mlRelatedObjects.size()) {
						bEdit = false;
					}
				}
			}

			String sRowHoverTable = sRowHover;
			if (!"".equals(sTableActions)) {
				if (bEdit) {
					sRowHoverTable = sRowHoverTable.replace("onmouseover='",
							"onmouseover='this.lastChild.style.visibility=\"visible\";");
					sRowHoverTable = sRowHoverTable.replace("onmouseout='",
							"onmouseout='this.lastChild.style.visibility=\"hidden\";");
				}
			}

			// Apply limits and sort order
			if (!"".equals(sSortKey)) {
				mlRelatedObjects.sort(sSortKey, sSortDirection, sSortType);
			}

			sbResult.append("<table");
			if (TABLE.equals(sMode)) {
				if (!"".equals(sShowThumbnails)) {
					sbResult.append(" style='margin-top:3px;margin-bottom:3px;' ");
				}
			}
			sbResult.append("><tr>");
			// ------------------------------------------------------------------
			// Highlight Icons
			// ------------------------------------------------------------------
			Boolean bHighlightZero = false;
			Boolean bHighlightNonzero = false;
			if (!"".equals(sHighlightZeroIcon) || !"".equals(sHighlightNonzeroIcon) || !"".equals(sHighlightZeroStyle)
					|| !"".equals(sHighlightNonzeroStyle)) {
				sbResult.append("<td style='text-align:center;vertical-align:middle;width:").append(iWidthHighlightIcon)
						.append("px'>").append("<div  style='float:left;'>");
				if (mlRelatedObjects.size() == 0) {
					if (!"".equals(sHighlightZeroIcon) || !"".equals(sHighlightZeroStyle)) {
						if (aZeroStates.length == 0) {
							bHighlightZero = true;
						} else {
							String sCurrent = (String) mObject.get(DomainConstants.SELECT_CURRENT);
							for (int j = 0; j < aZeroStates.length; j++) {
								if (aZeroStates[j].equalsIgnoreCase(sCurrent)) {
									bHighlightZero = true;
									break;
								}
							}
						}

						if (bHighlightZero) {
							if (!"".equals(sHighlightZeroIcon)) {
								sbResult.append("<img style='margin-right:8px;");
								if (!"".equals(sHighlightZeroLink)) {
									sbResult.append("cursor:pointer;' ").append(sLinkPrefix).append(sHighlightZeroLink)
											.append("&amp;objectId=").append(sOID).append(sLinkSuffix);
								} else {
									sbResult.append("'");
								}
								sbResult.append(" src='../common/images/").append(sHighlightZeroIcon).append("' ")
										.append(" title='").append(sHighlightZeroTooltip).append("'/>");
							}
						}
					}
				} else {
					if (!"".equals(sHighlightNonzeroIcon) || !"".equals(sHighlightNonzeroStyle)) {
						if (aNonzeroStates.length == 0) {
							bHighlightNonzero = true;
						} else {
							String sCurrent = (String) mObject.get(DomainConstants.SELECT_CURRENT);
							for (int j = 0; j < aNonzeroStates.length; j++) {
								if (aNonzeroStates[j].equalsIgnoreCase(sCurrent)) {
									bHighlightNonzero = true;
									break;
								}
							}
						}
						if (bHighlightNonzero) {
							if (!"".equals(sHighlightNonzeroIcon)) {
								sbResult.append("<img style='margin-right:8px;");
								if (!"".equals(sHighlightNonzeroLink)) {
									sbResult.append("cursor:pointer;' ").append(sLinkPrefix)
											.append(sHighlightNonzeroLink).append("&amp;objectId=").append(sOID)
											.append(sLinkSuffix);
								} else {
									sbResult.append("'");
								}
								sbResult.append(" src='../common/images/").append(sHighlightNonzeroIcon).append("' ")
										.append(" title='").append(sHighlightNonzeroTooltip).append("'/>");
							}
						}
					}
				}
				sbResult.append("</div>").append("</td>");
			}

			// ------------------------------------------------------------------
			// Drop Zone
			// ------------------------------------------------------------------
			if (TRUE_STRING.equalsIgnoreCase(sShowDropZone) && !UINavigatorUtil.isMobile(context)) {
				sbResult.append("<td style='vertical-align:middle;width:").append(iWidthDropZone).append("px'>");
				if (bEdit) {
					Access access = dObject.getAccessMask(context);
					Boolean bAccess = access.hasFromConnectAccess();
					if (bAccess) {
						String sFormId = "formDrag" + sRowID;
						String sDivId = "divDrag" + sRowID;
						String sLevel = (String) mObject.get(ID_LEVEL);
						String sDirection = "from";
						if (FALSE_STRING.equalsIgnoreCase(sFrom)) {
							sDirection = "to";
						}
						sbResult.append("<form></form>").append("<form style='height:").append(sDropZoneHeight)
								.append("px;margin-left: 10px; margin-right:5px;width:30px;' id='").append(sFormId)
								.append("' action='../common/emxFileUpload.jsp?type=").append(sDropZoneType)
								.append("&amp;relationship=").append(sDropZoneRelationship).append("&amp;objectId=")
								.append(sOID).append("'  method='post'  enctype='multipart/form-data'>");
						sbResult.append("   <div style='height:").append(sDropZoneHeight)
								.append("px;float:right;display:inline-block;' id='").append(sDivId)
								.append("' class='dropAreaColumn' ");
						sbResult.append(" title='").append(sTooltipDropFiles).append("' ")
								.append("      ondrop=\"FileSelectHandlerColumn(event,  '").append(sOID).append("', '")
								.append(sFormId).append("', '").append(sDivId).append("', 'id[level]=").append(sLevel)
								.append(".refreshRow', '").append(sLevel).append("', '', '', '', '")
								.append(sDropZoneRelationship).append("', '', '").append(sDirection)
								.append("', '');\"");
						sbResult.append("  ondragover=\"FileDragHoverColumn(event, '").append(sDivId).append("')\"")
								.append(" ondragleave=\"FileDragHoverColumn(event, '").append(sDivId).append("')\">")
								.append("   </div>").append("</form>");
					} else {
						sbResult.append("<div style='margin-left:10px;margin-right:5px;width:30px;'></div>");
					}
				} else {
					sbResult.append("<div style='margin-left:10px;margin-right:5px;width:30px;'></div>");
				}
				sbResult.append("</td>");
			}

			// ------------------------------------------------------------------
			// Add and Create Links
			// ------------------------------------------------------------------
			if (!"".equals(sAddLink) || !"".equals(sCreateLink)) {

				String sAlignment = "";
				if (FORM.equals(sMode)) {
					sAlignment = "vertical-align:middle;";
				}

				String sImagePrefix = "<img style='height:" + sSizeIcon + "px;margin-left:3px;margin-right:3px;"
						+ sAlignment + "' src='../common/images/";
				sbResult.append("<td style='cursor:pointer;text-align:left;vertical-align:middle;width:")
						.append(iWidthActions).append("px;'>");

				if (bEdit) {

					if (!"".equals(sAddLink)) {
						sAddLink = sAddLink.replace("?", "?objectId=" + sOID + "&amp;parentOID=" + sOID + "&amp;");
						if ("".equals(sAddText)) {
							if (!"".equals(sAddIcon)) {
								sbResult.append(sImagePrefix).append(sAddIcon).append("' ").append(" title='")
										.append(i18nNow.getI18nString(sAddTooltip, sResourceFile, sLang)).append("' ")
										.append(sLinkPrefix).append(sAddLink).append(sLinkSuffix).append(" />");
							}
						} else {
							sbResult.append(
									"<div style='color:transparent;' onmouseover='this.style.color=\"#508cb4\";' onmouseout='this.style.color=\"transparent\";' title='")
									.append(sAddTooltip).append("' ").append(sLinkPrefix).append(sAddLink)
									.append(sLinkSuffix).append(" >");
							if (!"".equals(sAddIcon)) {
								sbResult.append(sImagePrefix).append(sAddIcon).append("' />");
							}
							if (!"".equals(sAddText)) {
								sbResult.append("<span style='").append(sStyleLink).append("'>").append(sAddText)
										.append("</span>");
							}
							sbResult.append("</div>");
						}

						if (FORM.equals(sMode)) {
							if (!"".equals(sCreateLink)) {
								sbResult.append(
										"</td><td style='cursor:pointer;text-align:left;vertical-align:middle;width:")
										.append(iWidthActions).append("px;'>");
							}
						}

					}
					if (!"".equals(sCreateLink)) {
						if ("".equals(sCreateText)) {
							if (!"".equals(sCreateIcon)) {
								sbResult.append(sImagePrefix).append(sCreateIcon).append("' ").append(" title='")
										.append(sCreateTooltip).append("' ").append(sLinkPrefix).append(sCreateLink)
										.append("&amp;objectId=").append(sOID).append("&amp;parentOID=").append(sOID)
										.append(sLinkSuffix).append(" />");
							}
						} else {
							sbResult.append(
									"<div style='color:transparent;' onmouseover='this.style.color=\"#508cb4\";' onmouseout='this.style.color=\"transparent\";' title='")
									.append(sCreateTooltip).append("' ");
							sbResult.append(sLinkPrefix).append(sCreateLink).append("&amp;objectId=").append(sOID)
									.append(sLinkSuffix).append(" >");
							if (!"".equals(sCreateIcon)) {
								sbResult.append(sImagePrefix).append(sCreateIcon).append("' />");
							}
							if (!"".equals(sCreateText)) {
								sbResult.append("<span style='").append(sStyleLink).append("'>").append(sCreateText)
										.append("</span>");
							}
							sbResult.append("</div>");
						}
					}
				} else {

					if (!"".equals(sAddText) || !"".equals(sCreateText)) {
						sbResult.append("<div style='height:28px;'></div>");
					}
				}

				sbResult.append("</td>");

			}

			// ------------------------------------------------------------------
			// Counter
			// ------------------------------------------------------------------
			if (TRUE_STRING.equalsIgnoreCase(sShowCounter)) {
				sbResult.append("<td style='vertical-align:middle;padding-right:5px;width:").append(iWidthCounter)
						.append("px'>");
				sbResult.append("<div ").append(sCounterTooltip);
				sbResult.append(" style='text-align:right;");
				if (mlRelatedObjects.size() == 0) {
					if (bHighlightZero) {
						sbResult.append(sHighlightZeroStyle);
					} else {
						if (!"".equals(sCounterStyle)) {
							sbResult.append(sCounterStyle);
						} else {
							sbResult.append("font-weight:bold;");
						}
					}
				} else {
					if (bHighlightNonzero) {
						sbResult.append(sHighlightNonzeroStyle);
					} else {
						if (!"".equals(sCounterStyle)) {
							sbResult.append(sCounterStyle);
						} else {
							sbResult.append("font-weight:bold;");
						}
					}
				}

				if (!"".equals(sCounterLink)) {
					sbResult.append("cursor: pointer;' ");
					if (!"".equals(sCounterStyle) || !"".equals(sHighlightZeroStyle)
							|| !"".equals(sHighlightNonzeroStyle)) {
						sbResult.append(
								" onmouseover='$(this).css(\"text-decoration\",\"underline\");' onmouseout='$(this).css(\"text-decoration\",\"none\");' ");
					} else {
						sbResult.append(" onmouseover='$(this).css(\"color\",\"").append(sColorLink).append(
								"\");$(this).css(\"text-decoration\",\"underline\");' onmouseout='$(this).css(\"color\",\"#333333\");$(this).css(\"text-decoration\",\"none\");' ");
					}
					sbResult.append(sLinkPrefix).append(sCounterLink).append("&amp;objectId=").append(sOID)
							.append(sLinkSuffix).append('>');
				} else {
					sbResult.append("'>");
				}
				sbResult.append(mlRelatedObjects.size()).append("</div>").append("</td>");
			}

			// ------------------------------------------------------------------
			// Type Icons
			// ------------------------------------------------------------------
			if (TRUE_STRING.equalsIgnoreCase(sShowIcons)) {
				int iLimit = mlRelatedObjects.size();
				if (iMaxItems < mlRelatedObjects.size()) {
					iLimit = iMaxItems;
				}
				for (int j = 0; j < iLimit; j++) {

					Map mRelatedObject = (Map) mlRelatedObjects.get(j);
					String sType = (String) mRelatedObject.get(DomainConstants.SELECT_TYPE);
					String sIcon = UINavigatorUtil.getTypeIconProperty(context, sType);
					StringBuilder sbLinkIcon = getIconLink(sIconLink, mRelatedObject, sLinkPrefix, sLinkSuffix);

					sbResult.append("<td style='vertical-align:middle;padding-left:1px;cursor:pointer;' ")
							.append(sbLinkIcon.toString())
							.append("<img style='vertical-align:middle;' src='../common/images/").append(sIcon)
							.append("'");

					if (aTooltipIcon.length > 0) {
						sbResult.append(" title='");
						for (int k = 0; k < aTooltipIcon.length; k++) {
							String sText = (String) mRelatedObject.get(aTooltipIcon[k]);
							sbResult.append(sText);
							if (k < aTooltipIcon.length - 1) {
								sbResult.append(' ').append(sTooltipSeparator).append(' ');
							}
						}
						sbResult.append("'");
					}

					sbResult.append(" />").append("</td>");

				}
			}

			// ------------------------------------------------------------------
			// Thumbnails
			// ------------------------------------------------------------------
			if (TRUE_STRING.equalsIgnoreCase(sShowThumbnails)) {
				int iLimit = mlRelatedObjects.size();
				if (iMaxItems < mlRelatedObjects.size()) {
					iLimit = iMaxItems;
				}
				if (FORM.equals(sMode)) {
					sbResult.append("<td style='vertical-align:middle;height:42px;padding:0px 3px 0px 3px;'>");
				}

				for (int j = 0; j < iLimit; j++) {

					if (TABLE.equals(sMode)) {
						sbResult.append("<td style='vertical-align:middle;height:42px;padding:0px 3px 0px 3px;'>");
					}

					Map mRelatedObject = (Map) mlRelatedObjects.get(j);
					String sOIDRelated = (String) mRelatedObject.get(DomainConstants.SELECT_ID);
					String sURLImage = emxUtil_mxJPO.getPrimaryImageURL(context, args, sOIDRelated, sThumbnailSize,
							sMCSURL, "");
					StringBuilder sbLink = getIconLink(sThumbnailLink, mRelatedObject, sLinkPrefix, sLinkSuffix);

					sbResult.append("<table style='float:left'>").append("<tr ");

					if (!"".equals(sThumbnailActions) && bEdit) {
						String sRowHoverThumb = sRowHover.replace("onmouseover='",
								"onmouseover='this.lastChild.style.visibility=\"visible\";");
						sRowHoverThumb = sRowHoverThumb.replace("onmouseout='",
								"onmouseout='this.lastChild.style.visibility=\"hidden\";");
						sbResult.append(sRowHoverThumb);
					} else if (aThumbDetails.length > 0) {
						sbResult.append(sRowHover);
					}

					sbResult.append("><td style='vertical-align:middle;cursor:pointer;' ").append(sbLink.toString())
							.append("<img src='").append(sURLImage)
							.append("' style='border:1px solid #bababa;margin:2px;cursor:pointer;cursor:hand;")
							.append("box-shadow:1px 1px 2px #ccc;").append("' ");
					if (aThumbTooltip.length > 0) {
						sbResult.append(" title='");
						for (int k = 0; k < aThumbTooltip.length; k++) {
							sbResult.append((String) mRelatedObject.get(aThumbTooltip[k]));
							if (k < aThumbTooltip.length - 1) {
								sbResult.append(' ').append(sTooltipSeparator).append(' ');
							}
						}
						sbResult.append("' ");
					}
					sbResult.append("/></td>");

					if (aThumbDetails.length > 0) {
						sbResult.append(
								"<td style='cursor:pointer;white-space:nowrap;vertical-align:middle;padding-right:8px;padding-left:3px;' ")
								.append(sbLink.toString());
						for (int k = 0; k < aThumbDetails.length; k++) {
							sbResult.append("<span style='float:none;").append(aThumbStyles[k]).append("'>")
									.append((String) mRelatedObject.get(aThumbDetails[k])).append("</span>");
							if (k < aThumbDetails.length - 1) {
								sbResult.append("<br/>");
							}
						}
						sbResult.append("</td>");
					}

					if (!"".equals(sThumbnailActions)) {
						if (bEdit) {
							sbResult.append(
									"<td style='visibility:hidden;vertical-align:middle;padding-right:0px;padding-left:3px;width:39px;'>");
							String sRID = (String) mRelatedObject.get(DomainConstants.SELECT_RELATIONSHIP_ID);
							StringBuilder sbURL = new StringBuilder();
							StringBuilder sbIMG = new StringBuilder();
							sbURL.append("<a target='").append(sTargetFrame).append("' href='../common/emxRelatedItemAction.jsp?objectId=").append(sOIDRelated).append("&amp;relId=").append(sRID).append("&amp;rowId=").append(sRowID).append("&amp;action=");
							sbIMG.append("<img style='margin-right:3px;height:16px;' src='../common/images/");
							if (sThumbnailActions.contains("promote")) {
								sbResult.append(sbURL.toString()).append("promote'>").append(sbIMG.toString()).append("iconActionPromote.gif' title='Promote this item'/>").append("</a>");
							}
							if (sThumbnailActions.contains("demote")) {
								sbResult.append(sbURL.toString()).append("demote'>").append(sbIMG.toString()).append("iconActionDemote.gif' title='Demote this item'/>").append("</a>");
							}
							if (sThumbnailActions.contains("remove")) {
								sbResult.append(sbURL.toString()).append("remove'>").append(sbIMG.toString()).append("iconActionRemove.gif' title='Disconnect this item'/>").append("</a>");
							}
							if (sThumbnailActions.contains("delete")) {
								sbResult.append(sbURL.toString()).append("delete'>").append(sbIMG.toString()).append("iconActionDelete.gif' title='Delete this item'/>").append("</a>");
							}
							sbResult.append("</td>");
						}
					}
					sbResult.append("</tr>").append("</table>");
					if (TABLE.equals(sMode)) {
						sbResult.append("</td>");
					}
				}

				if (FORM.equals(sMode)) {
					sbResult.append("</td>");
				}
			}

			// ------------------------------------------------------------------
			// Table
			// ------------------------------------------------------------------
			if (TRUE_STRING.equalsIgnoreCase(sShowTable)) {
				int iLimit = mlRelatedObjects.size();
				if (iMaxItems < mlRelatedObjects.size()) {
					iLimit = iMaxItems;
				}
				sbResult.append("<td style='vertical-align:middle;padding:0px 3px 0px 0px;'>").append("<table>");
				String sHTMLCellMaster = "<td style='white-space:nowrap;vertical-align:middle;padding:1px 5px 1px 2px;' ";

				for (int j = 0; j < iLimit; j++) {

					Map mRelatedObject = (Map) mlRelatedObjects.get(j);
					StringBuilder sbLink = getIconLink(sTableLink, mRelatedObject, sLinkPrefix, sLinkSuffix);
					String sHTMLCell = sHTMLCellMaster + sbLink.toString();
					int iRow = 0;
					String sHeightIcon = "height:" + sSizeIcon + "px;";

					if (iTableRows > 1) {
						if (j < iLimit - 1) {
							sbResult.append("<tr style='cursor:pointer;border-bottom:1px solid #bababa;' ")
									.append(sRowHoverTable).append('>');
						} else {
							sbResult.append("<tr style='cursor:pointer;' ").append(sRowHoverTable).append('>');
						}
						sHeightIcon = "";
					} else {
						sbResult.append("<tr style='cursor:pointer;' ").append(sRowHoverTable).append('>');
					}

					sbResult.append(sHTMLCell);

					for (int k = 0; k < aColumns.length; k++) {

						String sSelect = aColumns[k];

						if (ICON.equals(sSelect)) {
							String sType = (String) mRelatedObject.get(DomainConstants.SELECT_TYPE);
							String sIcon = UINavigatorUtil.getTypeIconProperty(context, sType);
							if (k > 0) {
								sbResult.append("</td>").append(sHTMLCell);
							}
							sbResult.append("<img style='").append(aStyles[k]).append(sHeightIcon)
									.append("cursor:pointer;vertical-align:middle;' src='../common/images/")
									.append(sIcon).append("'/>");
							iRow = iTableRows;
						} else if ("Image".equals(sSelect)) {
							String sOIDRelated = (String) mRelatedObject.get(DomainConstants.SELECT_ID);
							String sURLImage = emxUtil_mxJPO.getPrimaryImageURL(context, args, sOIDRelated,
									"mxThumbnail Image", sMCSURL, "");
							if (k > 0) {
								sbResult.append("</td>").append(sHTMLCell);
							}
							sbResult.append("<img src='").append(sURLImage)
									.append("' style='height:40px;border:1pt solid #bababa;margin-left:3px;margin-right:3px;margin-top:1px;margin-bottom:1px;cursor:pointer;cursor:hand;")
									.append(aStyles[k]).append("' />");
							iRow = iTableRows;
						} else {
							if (iRow == iTableRows) {
								sbResult.append("</td>").append(sHTMLCell);
								iRow = 0;
							}
							String sType = (String) mRelatedObject.get(sSelect);
							if (DomainConstants.SELECT_TYPE.equalsIgnoreCase(sSelect)) {
								sType = i18nNow.getTypeI18NString(sType, context.getLocale().getCountry());
							}
							sbResult.append("<span style='").append(aStyles[k]).append("'>")
									.append(XSSUtil.encodeForXML(context, sType)).append("</span>");
							if (iRow < iTableRows - 1) {
								sbResult.append("<br/>");
							}
							iRow++;
						}

					}

					sbResult.append("</td>");

					if (!"".equals(sTableActions)) {
						sbResult.append("<td style='width:").append(iWidthTableActions)
								.append("px;vertical-align:middle;visibility:hidden;'>");
						if (bEdit) {
							String sOIDRelated = (String) mRelatedObject.get(DomainConstants.SELECT_ID);
							String sRID = (String) mRelatedObject.get("id[connection]");
							StringBuilder sbURL = new StringBuilder();
							StringBuilder sbIMG = new StringBuilder();
							sbURL.append("<a href='../common/emxRelatedItemAction.jsp?objectId=").append(sOIDRelated)
									.append("&amp;relId=").append(sRID).append("&amp;rowId=").append(sRowID)
									.append("&amp;action=");
							sbIMG.append("<img style='height:14px;margin-right:3px;' src='../common/images/");
							if (sTableActions.contains("promote")) {
								sbResult.append(sbURL.toString()).append("promote' target='listHidden'>")
										.append(sbIMG.toString())
										.append("iconActionPromote.gif' title='Promote this item'/>").append("</a>");
							}
							if (sTableActions.contains("demote")) {
								sbResult.append(sbURL.toString()).append("demote' target='listHidden'>")
										.append(sbIMG.toString())
										.append("iconActionDemote.gif' title='Demote this item'/>").append("</a>");
							}
							if (sTableActions.contains("remove")) {
								sbResult.append(sbURL.toString()).append("remove' target='listHidden'>")
										.append(sbIMG.toString())
										.append("iconActionRemove.gif' title='Disconnect this item'/>").append("</a>");
							}
							if (sTableActions.contains("delete")) {
								sbResult.append(sbURL.toString()).append("delete' target='listHidden'>")
										.append(sbIMG.toString())
										.append("iconActionDelete.gif' title='Delete this item'/>").append("</a>");
							}
						}
						sbResult.append("</td>");
					}
					sbResult.append("</tr>");
				}
				sbResult.append("</table>").append("</td>");
			}
			String sHoverMessageMore = EnoviaResourceBundle.getProperty(context, "Components",
					"emxComponents.String.ImageSummaryHoverMore", sLanguage);
			String[] messageValues = new String[1];
			messageValues[0] = String.valueOf(mlRelatedObjects.size());
			String invalidPolicyMsg = null;
			try {
				invalidPolicyMsg = com.matrixone.apps.domain.util.MessageUtil.getMessage(context, null,
						"emxComponents.String.ImageSummaryHover", messageValues, null, locale,
						"emxComponentsStringResource");
			} catch (Exception e) {

			}
			if (TRUE_STRING.equalsIgnoreCase(sShowMore)) {
				if (mlRelatedObjects.size() > iMaxItems) {
					sbResult.append("<td  title='" + invalidPolicyMsg + "' style='color:").append(sColorLink)
							.append(";white-space:nowrap;vertical-align:middle;padding-left:3px;");
					if (!"".equals(sLinkMore)) {
						sbResult.append("cursor: pointer;' ").append(sLinkPrefix).append(sLinkMore)
								.append("&amp;objectId=").append(sOID).append(sLinkSuffix).append('>');
					} else {
						sbResult.append("'>");
					}
					sbResult.append(" (").append(mlRelatedObjects.size() - iMaxItems)
							.append(" " + sHoverMessageMore + ") </td>");
				}
			}
			if (TRUE_STRING.equalsIgnoreCase(sShowLatest)) {
				sbResult.append("<td style='font-size:9px;vertical-align:middle;padding:0px 3px 0px 3px;width:")
						.append(sLatestWidth).append("px'>");
				if (mlRelatedObjects.size() > 0) {
					mlRelatedObjects.sort("modified", "descending", "date");
					Map mLatest = (Map) mlRelatedObjects.get(0);
					String sModified = (String) mLatest.get("modified");
					Calendar cModified = Calendar.getInstance();
					cModified.setTime(sdf.parse(sModified));

					String sStyleModified = "";
					if (cModified.after(cNow)) {
						sStyleModified = " style='font-weight:bold;color:#cc0000;'";
					}

					StringBuilder sbLinkIcon = getIconLink(sLatestLink, mLatest, sLinkPrefix, sLinkSuffix);
					sbResult.append("<table><tr><td style='cursor:pointer;vertical-align:middle;padding:0px 5px 0px 5px;' rowspan='2' ").append(sbLinkIcon.toString()).append("<b>Latest :</b> ");
					String sType = (String) mLatest.get(DomainConstants.SELECT_TYPE);
					String sIcon = UINavigatorUtil.getTypeIconProperty(context, sType);
					sbResult.append("<img src='../common/images/").append(sIcon).append("' /></td><td>");
					for (int j = 0; j < aLatestLabel.length; j++) {
						sbResult.append((String) mLatest.get(aLatestLabel[j]));
						if (j < aLatestLabel.length - 1) {
							sbResult.append(' ').append(sTooltipSeparator).append(' ');
						}
					}
					sbResult.append("</td></tr><tr><td").append(sStyleModified).append('>').append('(')
							.append(sModified).append(')').append("</td></tr></table>");
				}
				sbResult.append("</td>");
			}
			sbResult.append("</tr></table>");
			vResult.add(sbResult.toString());
		}
		return vResult;
	}

	public static String getLinkURL(Context context, String sLinkSuite, String sSuiteDir, String sLink,
			Boolean bTreeLink) throws MatrixException {

		String sResult = sLink;
		String sLinkSuiteTemp = sLinkSuite;
		String sSuiteDirTemp = sSuiteDir;

		if (bTreeLink) {
			if (!sResult.startsWith("emxTree.jsp?DefaultCategory=")) {
				sResult = "emxTree.jsp?DefaultCategory=" + sLink;
			}
		} else {

			if (!"".equals(sLink)) {
				if (!sLink.contains("?")) {
					if (!sLink.contains(".jsp")) {
						if (!sLink.contains(".html")) {
							Command commandCreate = new Command(context, sLink);
							if (null != commandCreate) {
								sResult = commandCreate.getHref();
								SelectSetting setting = commandCreate.getSettings();
								String sSuiteCommand = setting.getValue("Registered Suite");
								if (!"".equals(sSuiteCommand)) {
									sSuiteDirTemp = FrameworkProperties.getProperty(context,
											"eServiceSuite" + sSuiteCommand + ".Directory");
									if (!sResult.contains("&suiteKey")) {
										sResult += "&suiteKey=" + sSuiteCommand;
									}
									if (!sResult.contains("&StringResourceFileId")) {
										sResult += "&StringResourceFileId=" + FrameworkProperties.getProperty(context,
												"eServiceSuite" + sSuiteCommand + ".StringResourceFileId");
									}
									if (!sResult.contains("&SuiteDirectory")) {
										sResult += "&SuiteDirectory=" + sSuiteDirTemp;
									}
								}
							}
						}
					}
				}

				if (!sResult.contains("?")) {
					sResult += "?";
				}
				if (!sResult.contains("&suiteKey")) {
					sResult += sLinkSuiteTemp;
				}
				if (sResult.contains("${COMMON_DIR}/")) {
					sResult = sResult.replace("${COMMON_DIR}/", "");
				}
				if (sResult.contains("${ROOT_DIR}/")) {
					sResult = sResult.replace("${ROOT_DIR}/", "../");
				}
				if (sResult.contains("${SUITE_DIR}/")) {
					sResult = sResult.replace("${SUITE_DIR}/", "../" + sSuiteDirTemp + "/");
				}
				if (sResult.contains("${COMPONENT_DIR}/")) {
					sResult = sResult.replace("${COMPONENT_DIR}/", "../components/");
				}

				sResult = sResult.replace("&amp;", "___amp;");
				sResult = sResult.replace("&", "&amp;");
				sResult = sResult.replace("___amp;", "&amp;");

			}
		}

		return sResult;

	}

	public static StringBuilder getIconLink(String sLinkIcon, Map mRelatedObject, String sLinkPrefix,
			String sLinkSuffix) {

		StringBuilder sbResult = new StringBuilder();
		String sOIDObject = (String) mRelatedObject.get(DomainConstants.SELECT_ID);

		if ("".equals(sLinkIcon)) {
			String sIsDocument = (String) mRelatedObject.get("type.kindof[DOCUMENTS]");
			if (FALSE_STRING.equalsIgnoreCase(sIsDocument)) {
				sbResult.append(sLinkPrefix).append("emxTree.jsp?").append("&amp;objectId=").append(sOIDObject).append(sLinkSuffix).append(">");
			} else {
				sbResult.append("onClick=\"javascript:callCheckout('").append(sOIDObject).append("',").append("'download', '', '', 'null', 'null', 'structureBrowser', 'APPDocumentSummary', 'null')\">");
			}
		} else if ("download".equals(sLinkIcon)) {
			sbResult.append("onClick=\"javascript:callCheckout('").append(sOIDObject).append("',").append("'download', '', '', 'null', 'null', 'structureBrowser', 'APPDocumentSummary', 'null')\">");
		} else {
			sbResult.append(sLinkPrefix).append(sLinkIcon).append("&amp;objectId=").append(sOIDObject).append(sLinkSuffix).append('>');
		}
		return sbResult;
	}

	// Drag & Drop Columns
	public Vector columnDropZone(Context context, String[] args) throws Exception {

		Vector vResult = new Vector();
		HashMap programMap = (HashMap) JPO.unpackArgs(args);
		HashMap paramList = (HashMap) programMap.get(PARAM_LIST);
		String sParentOID = (String) paramList.get("objectId");
		String strRefStr =   (String) paramList.get("refreshStructure");		
		MapList mlObjects = (MapList) programMap.get(OBJECT_LIST);
		HashMap columnMap = (HashMap) programMap.get(COLUMN_MAP);
		HashMap settings = (HashMap) columnMap.get(SETTINGS);
		String sDragTypes = (String) settings.get("Drag Types");
		String sDropTypes = (String) settings.get("Drop Types");
		String sStructureTypes = (String) settings.get("Structure Types");
		String sDirections = (String) settings.get("Directions");
		String sRelationships = (String) settings.get("Relationships");
		String sFileDropRelationship = (String) settings.get("DocumentDrop Relationship"); // if
																							// user
																							// drop
																							// a
																							// file
																							// on
																							// drozone
		String sFileDropType = (String) settings.get("File Drop Type");
		String sAttributes = (String) settings.get("Attributes");
		String sAction = (String) settings.get("Drop Action");
		String validator = (String) settings.get("Validate");

		if (UIUtil.isNullOrEmpty(validator)) {
			validator = DomainObject.EMPTY_STRING;
		}

		String[] aDragTypes = getAdminPropertyActualNames(context, sDragTypes);
		String[] aDropTypes = getAdminPropertyActualNames(context, sDropTypes);
		String[] aRelationships = getAdminPropertyActualNames(context, sRelationships);
		sFileDropRelationship = PropertyUtil.getSchemaProperty(context, sFileDropRelationship);
		sFileDropType = PropertyUtil.getSchemaProperty(context, sFileDropType);

		String[] aDirections = sDirections.split(",");
		String[] aAttributes = new String[aRelationships.length];

		if (null != sAttributes) {
			aAttributes = getAdminPropertyActualNames(context, sAttributes);
			;
		} else {
			for (int i = 0; i < aAttributes.length; i++) {
				aAttributes[i] = "";
			}
		}
		if (UIUtil.isNullOrEmpty(sStructureTypes)) {
			sStructureTypes = "Workspace Vault";
		}
		if (UIUtil.isNullOrEmpty(sFileDropRelationship)) {
			sFileDropRelationship = "Reference Document";
		}
		if (UIUtil.isNullOrEmpty(sFileDropType)) {
			sFileDropType = "Document";
		}
		if (null == sAction) {
			sAction = "refreshRow";
		} else if ("Expand".equalsIgnoreCase(sAction)) {
			sAction = "expandRow";
		}

		StringList busSelects = new StringList();
		busSelects.add(DomainConstants.SELECT_ID);
		busSelects.add(DomainConstants.SELECT_TYPE);
		busSelects.add(TYPE_KIND_OF);
		int size = mlObjects.size();
		String[] objectIds = new String[size];

		for (int i = 0; i < size; i++) {
			Map mObject = (Map) mlObjects.get(i);
			String sOID = (String) mObject.get(DomainConstants.SELECT_ID);
			objectIds[i] = sOID;
		}

		MapList objectList = DomainObject.getInfo(context, objectIds, busSelects);
		Map<String, Map> objectInfo = new HashMap<String, Map>();

		Iterator objectListIterator = objectList.iterator();
		while (objectListIterator.hasNext()) {
			Map objectInfoMap = (Map) objectListIterator.next();
			String objectId = (String) objectInfoMap.get(DomainConstants.SELECT_ID);
			objectInfo.put(objectId, objectInfoMap);
		}

		for (int i = 0; i < mlObjects.size(); i++) {

			StringBuilder sbResult = new StringBuilder();
			Map mObject = (Map) mlObjects.get(i);
			Boolean bShowDropZone = false;

			String sOID = (String) mObject.get(DomainConstants.SELECT_ID);
			String sLevel = (String) mObject.get(ID_LEVEL);
			String sFormId = "formDrag" + i + sLevel;
			String sDivId = "divDrag" + i + sLevel;

			Map mInfo = objectInfo.get(sOID);
			String sType = (String) mInfo.get(DomainConstants.SELECT_TYPE);
			String sKind = (String) mInfo.get(TYPE_KIND_OF);

			StringBuilder sbTypes = new StringBuilder();
			StringBuilder sbRelationships = new StringBuilder();
			StringBuilder sbAttributes = new StringBuilder();
			StringBuilder sbFrom = new StringBuilder();

			for (int j = 0; j < aDropTypes.length; j++) {
				if (aDropTypes[j].equals(sType) || aDropTypes[j].equals(sKind)) {
					bShowDropZone = true;
					sbTypes.append(aDragTypes[j]).append(",");
					sbRelationships.append(aRelationships[j]).append(",");
					sbAttributes.append(aAttributes[j]).append(",");
					sbFrom.append(aDirections[j]).append(",");
				}
			}

			if (sbTypes.length() > 0) {
				sbTypes.setLength(sbTypes.length() - 1);
			}
			if (sbRelationships.length() > 0) {
				sbRelationships.setLength(sbRelationships.length() - 1);
			}
			if (sbAttributes.length() > 0) {
				sbAttributes.setLength(sbAttributes.length() - 1);
			}
			if (sbFrom.length() > 0) {
				sbFrom.setLength(sbFrom.length() - 1);
			}
			if(UIUtil.isNullOrEmpty(strRefStr)) {
				strRefStr = "";
			}
			if (bShowDropZone) {
				sbResult.append("<form></form>");
				sbResult.append("<form id='").append(sFormId).append("' action=\"../common/emxFileUpload.jsp?objectId=").append(sOID).append("&amp;relationship=").append(sFileDropRelationship).append("&amp;type=").append(sFileDropType).append("\"  method='post'  enctype='multipart/form-data'>").append("   <div id='").append(sDivId).append("' class='dropAreaColumn'").append("        ondrop=\"FileSelectHandlerColumn(event, ").append("'").append(sOID).append("', ").append("'").append(sFormId).append("', ").append("'").append(sDivId).append("', ").append("'id[level]=").append(sLevel).append(".").append(sAction).append("', ").append("'").append(sLevel).append("', ").append("'").append(sParentOID).append("', ").append("'").append(sType).append("', ").append("'").append(sbTypes.toString()).append("', ").append("'").append(sbRelationships.toString()).append("', ").append("'").append(sbAttributes.toString()).append("', ").append("'").append(sbFrom.toString()).append("', ").append("'").append(sStructureTypes).append("', ").append("'").append(validator).append("', ").append("'").append(strRefStr).append("')\" ").append("    ondragover=\"FileDragHoverColumn(event, '").append(sDivId).append("')\" ").append("   ondragleave=\"FileDragHoverColumn(event, '").append(sDivId).append("')\">").append("   </div>").append("</form>");
			}
			vResult.add(sbResult.toString());
		}

		return vResult;

	}

	public Vector columnDragIcon(Context context, String[] args) throws Exception {

		Vector vResult = new Vector();
		HashMap programMap = (HashMap) JPO.unpackArgs(args);
		HashMap paramList = (HashMap) programMap.get(PARAM_LIST);
		String sParentOID = (String) paramList.get("objectId");
		MapList mlObjects = (MapList) programMap.get(OBJECT_LIST);
		HashMap columnMap = (HashMap) programMap.get(COLUMN_MAP);
		String sNameColumn = (String) columnMap.get(DomainConstants.SELECT_NAME);
		HashMap settings = (HashMap) columnMap.get(SETTINGS);
		String sIcon = (String) settings.get(ICON);
		String _PARAM = "_param=";
		if (null == sIcon) {
			sIcon = "../common/images/iconDragDrop.png";
		}

		StringList busSelects = new StringList();
		busSelects.add(DomainConstants.SELECT_ID);
		busSelects.add(DomainConstants.SELECT_TYPE);
		busSelects.add(TYPE_KIND_OF);
		int size = mlObjects.size();
		String[] objectIds = new String[size];

		for (int i = 0; i < size; i++) {
			Map mObject = (Map) mlObjects.get(i);
			String sOID = (String) mObject.get(DomainConstants.SELECT_ID);
			objectIds[i] = sOID;
		}

		MapList objectList = DomainObject.getInfo(context, objectIds, busSelects);
		Iterator objectListIterator = objectList.iterator();
		Map<String, Map> objectInfo = new HashMap<String, Map>();

		while (objectListIterator.hasNext()) {
			Map objectInfoMap = (Map) objectListIterator.next();
			String objectId = (String) objectInfoMap.get(DomainConstants.SELECT_ID);
			objectInfo.put(objectId, objectInfoMap);
		}

		for (int i = 0; i < mlObjects.size(); i++) {

			StringBuilder sbResult = new StringBuilder();
			Map mObject = (Map) mlObjects.get(i);
			String sOID = (String) mObject.get(DomainConstants.SELECT_ID);
			String sRID = (String) mObject.get(DomainConstants.SELECT_RELATIONSHIP_ID);
			String sLevel = (String) mObject.get(ID_LEVEL);
			String sID = sNameColumn + sLevel.replace(",", "_");

			Map mInfo = objectInfo.get(sOID);
			String sType = (String) mInfo.get(DomainConstants.SELECT_TYPE);
			String sKind = (String) mInfo.get(TYPE_KIND_OF);

			sbResult.append("<a style='cursor:move;text-decoration: none !important;' ");
			sbResult.append(" href='_param=").append(sID).append(_PARAM).append(sOID).append(_PARAM).append(sRID).append(_PARAM).append(sLevel).append(_PARAM).append(sParentOID).append(_PARAM).append(sType).append(_PARAM).append(sKind).append("_param=' target='listHidden'>").append("<img src='").append(sIcon).append("' />").append("</a>");

			vResult.add(sbResult.toString());

		}

		return vResult;

	}

	private String[] getAdminPropertyActualNames(Context context, String symbolicNames) {

		if (UIUtil.isNullOrEmpty(symbolicNames)) {
			return new String[] { "" };
		}

		StringList symbolicNamesList = FrameworkUtil.split(symbolicNames, ",");
		String[] arrayActualNames = new String[symbolicNamesList.size()];
		for (int i = 0; i < symbolicNamesList.size(); i++) {
			arrayActualNames[i] = PropertyUtil.getSchemaProperty(context, (String) symbolicNamesList.get(i));
		}

		return arrayActualNames;
	}

}
