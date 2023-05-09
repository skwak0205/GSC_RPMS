<%--  gscDocumentDownloadFile.jsp  -  This page exports the  line items.

    (c) Dassault Systemes, 1993 - 2016.  All rights reserved.

  static const char RCSID[] = "$Id: /ENOSourcingCentral/CNext/webroot/documentcentral\emxLibraryCentralJobsDownloadFile.jsp.jsp 1.3.2.2.1.1 Wed Oct 29 22:23:58 2008 GMT przemek Experimental$"
--%>

<%@ include file  = "../emxUICommonAppInclude.inc" %>
<%@  include file = "emxMultipleClassificationUtils.inc" %>
<%@ page import = "java.lang.reflect.*" %>

<script language="JavaScript">
    /*function windowClose(){
      parent.window.getWindowOpener().parent.frames[1].location.reload();
      getTopWindow().close();
    }*/
</script>

<%
    String url=null;
    String clientSideURL=null;
    try
    {
        String jobId = emxGetParameter(request,"objectId");
        String format = emxGetParameter(request,"format");
        String fileName = emxGetParameter(request,"fileName");
        Job job=new Job(jobId);
        matrix.db.FileList filesList=job.getFiles(context);
        matrix.db.File dbFile = null;

        if(fileName == null || "".equals(fileName))
        {
            dbFile = (matrix.db.File)filesList.get(0);
            fileName = dbFile.getName();
        }
        String workspaceDir = context.createWorkspace() ;
        java.io.File directory = new java.io.File(workspaceDir);
        if ( !directory.exists() && !directory.mkdir() )
        {
            workspaceDir = context.createWorkspace();
        }
        java.io.File file = new java.io.File(workspaceDir + java.io.File.separator + fileName);

        job.checkoutFile(context, false, format, fileName, workspaceDir);

        String fileEncodeType = request.getCharacterEncoding();
        if ("".equals(fileEncodeType) || fileEncodeType == null || fileEncodeType == "null") {
            fileEncodeType = UINavigatorUtil.getFileEncoding(context, request);
            System.out.println("fileEncodeType = " + fileEncodeType);
        }

        response.setContentType("application/octet-stream; charset=" + fileEncodeType);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
        java.io.InputStreamReader inputStreamReader=new java.io.InputStreamReader(fileInputStream, fileEncodeType);

        out.clear(); // removes unnecessary spaces
        int i;
        while ((i = inputStreamReader.read()) != -1)
            out.write(i);
        inputStreamReader.close();
        fileInputStream.close();
        //file.delete();

    }catch(Exception e)
    {
        e.printStackTrace();
    }

%>


