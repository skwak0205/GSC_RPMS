/*
* @quickreview [Modifier] [Reviewer] [DD:MM:YYYY] - [IRFamily] [Comment]
* @quickreview  VAI1 KIE1 [DD:MM:YYYY] : HL FUN103144 - TRM modeler integration to subscribe/unsubscribe
* @quickreview  VAI1 ZUD  [17:11:2021] : IR-888265 - The Move function fails in Requirements Structure Editor widget depending on the user's security context
*/

    import java.util.HashMap;
    import matrix.db.Context;
    import matrix.db.JPO;
    import matrix.db.BusinessObject;
    import matrix.util.StringList;
    import java.util.*;
    import com.matrixone.apps.domain.DomainObject;
    import com.dassault_systemes.requirements.RMT3DNotificationJSON;
    import javax.json.JsonObject;
    //import com.dassault_systemes.i3dx.appsservices.notifications.NotificationUtil;
    import com.dassault_systemes.i3dx.appsservices.notifications.nomatrix.NotificationBasicUtil;
    import com.dassault_systemes.i3dx.appsservices.notifications.nomatrix.NotifConf;
    import com.matrixone.apps.common.util.SubscriptionUtil;

    public class emxRMT3DNotificationBase_mxJPO {
       	NotifConf notifConf = null;
			
    	public  emxRMT3DNotificationBase_mxJPO (Context context, String[] args)throws Exception {
		
    		//System.out.println("--------In ${CLASSNAME} constructor -------------");
			
    	}

    	public void sendNotif(Context context, String[] args) throws Exception{

			try{
						
				//System.out.println("--args[8]:"+args[8]);
			
				if((args[8].isEmpty()) || (args[8].equals("owner") && (!args[9].equals(args[10])))){
					
    				//Find the notification service
					String notifURL = System.getenv("MYAPPS_URL");
					//System.out.println("--notifURL:"+notifURL);
    				String notificationUrl = get3DNotificationsURL() /*System.getenv("NOTIFICATION_URL")*/ + "/api/notify";
    				//System.out.println("--notificationUrl:"+notificationUrl);
    				notifConf = NotifConf.getInstance();
					//System.out.println("--notifConf:"+notifConf);
    				notifConf.init(notificationUrl, 10000);
    				String NOTIFICATION_ID = "eno3dnotification.subscriptions";			
    				
					
    				if(args.length>1){									
    				
    					//On Revise, copy subscriptions to new reivison
    					if(args[1].equals("MajorRevision")){
    						//System.out.println("=============BEGIN COPY SUBSCRIPTIONS===========");
    						try{
    							String originID = args[0];
    							//System.out.println("Original BO:"+originID);
    							DomainObject doreq_obj = new DomainObject(originID);
    							doreq_obj.open(context);
    							matrix.db.BusinessObjectList boList = doreq_obj.getMajorRevisions(context);
    							BusinessObject lastSrcBo = (BusinessObject) boList.elementAt(boList.size() - 1);
    							String targetID = lastSrcBo.getObjectId(context);
    							//System.out.println("Last Revision BO:"+targetID);
    							//System.out.println("Type:"+doreq_obj.getTypeName());
    							//System.out.println("Calling propagateObjectSubscriptionsOnRevise...");
    							SubscriptionUtil.propagateObjectSubscriptionsOnRevise(context, originID, targetID, doreq_obj.getTypeName());
    							//System.out.println("Copy done.");
    						} catch (Exception e) {
    							//System.out.println("ERROR: Failed to copy subscriptions");
    							e.printStackTrace();
    						} finally {
    							//System.out.println("=============END COPY SUBSCRIPTIONS===========");
    						}
    					}
    					
    					
						RMT3DNotificationJSON notifJSONBuilder = new RMT3DNotificationJSON(context, args, NOTIFICATION_ID, "ENORERE_AP");
    					
    					JsonObject result_json = notifJSONBuilder.getJSONObject();
						//System.out.println("--result_json:" +result_json);
				
    					if(result_json!=null) {
    						
    						try{
    							//System.out.println("Sending notification.");
								//System.out.println("JSON_tostring:"+result_json.toString());
    							NotificationBasicUtil.sendNotification(notifConf, result_json.toString());
								
    						} catch (Exception e) {
    							e.printStackTrace();
    						}						
    					}
    					else {
    						//System.out.println("--getJSONObject returned NULL, maybe no subscribers");
							}
    					//System.out.println("-----------------------------------------");
    				    				
    					/*** Experimental
    					try{
    					NotificationUtil.sendNotification(context, ${CLASS:emxRMT3DNotificationJSON}.getJSONObject().toString());
    					} catch (Exception e) {
    						e.printStackTrace();
    					}

    					try{
    						NotificationUtil.sendNotification(context, ${CLASS:emxRMT3DNotificationJSON}.getJSONObject().toString(), notificationUrl, 10000);
    					} catch (Exception e) {
    						e.printStackTrace();
    					}		
    					***/
					}
				}
			}catch (Exception e){
				e.printStackTrace();
				}			
			}
    	
    	
    	
        /**
         * Get 3DNotification URL.
         * <p>
         * this URL is computed from MyApps URL.
         *
         * @param 
         * @return 3DNotification URL.
         * @throws FrameworkException if there is a framework error
         */
        private String get3DNotificationsURL() throws Exception {
            String url = System.getenv("NOTIFICATION_URL");
           
            if (url==null || url.isEmpty()) {
            	//System.out.println("ERROR: NOTIFICATION_URL is empty, trying to build from MY_APPS");
    			//in cloud, the notificaiton_url is not set. fall back to building it from another
    			String notifURL = System.getenv("MYAPPS_URL");
                //If all else fails...
                if (notifURL==null || "".equals(notifURL)) {
                	//System.out.println("ERROR: unable to locate notification service, must be in default odt environment");
                	notifURL="http://localhost";
                }
                notifURL = notifURL.replace("apps", "intf");
    			notifURL = notifURL.replace("enovia", "");
                if (notifURL.contains(":443/")) {
                    notifURL = notifURL.split(":443/")[0];
                }

                url = notifURL;
            }

            return url;
        }	
    }
