package com.gsc.dbConfig;

import com.matrixone.apps.domain.util.MapList;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class dbSessionUtil {
    public enum IsCommit {Y, N}
    private String _configPath = "";
    private String _namespace = "";
    private IsCommit is_Commit = IsCommit.Y;
    protected SqlSession sqlSession = null;

    /**
     * <pre>
     *     Constructor
     * </pre>
     * @param _namespace sql mapper namespace
     */
    public dbSessionUtil(String _namespace) {
        this._namespace = _namespace;
    }

    /**
     * <pre>
     *     open sql session
     * </pre>
     * @return SqlSession
     */
    public SqlSession openSession() {
        if (this.sqlSession == null) {
            this.sqlSession = dbConnectionUtil.getSqlSession();
        }

        return this.sqlSession;
    }

    /**
     * <pre>
     *     sql commit
     * </pre>
     */
    public void commit() {
        if (this.sqlSession != null)
            this.sqlSession.commit();
    }

    /**
     * <pre>
     *     sql rollback
     * </pre>
     */
    public void rollback() {
        if (this.sqlSession != null)
            this.sqlSession.rollback();
    }

    /**
     * <pre>
     *     sql close
     * </pre>
     */
    public void close() {
        if (this.sqlSession != null) {
            this.sqlSession.close();
            this.sqlSession = null;
        }
    }

    public void updateIF(String queryId, String if_yn, String if_result, String if_msg, String id) {
        Map<String, Object> param = new HashMap<>();
        param.put("if_yn", if_yn);
        param.put("if_result", if_result);
        param.put("if_msg", if_msg == null ? "Success" : if_msg);
        param.put("value", id);
        this.executeSql("update", queryId == null ? "updateIFColumn" : queryId, param);
    }

    /**
     * <pre>
     *     execute sql query
     * </pre>
     *
     * @param queryId sql mapper query id
     * @param paramMap paramMap
     * @throws Exception
     */
    public Object executeSql(String type, String queryId, Map<String, Object> paramMap) {
        switch (type) {
            case "list" :
                return selectList(_namespace, queryId, paramMap);

            case "checkExist":
                return checkExist(_namespace, queryId, paramMap);

            case "insert":
                return insert(_namespace, queryId, paramMap);

            case "update":
                return update(_namespace, queryId, paramMap);
        }
        return null;
    }

    /**
     * <pre>
     *     select sql query
     * </pre>
     *
     * @param sNameSpace sql mapper namespace
     * @param queryId sql mapper query id
     * @param paramMap paramMap
     * @throws Exception
     */
    private MapList selectList(String sNameSpace, String queryId, Map<String, Object> paramMap) {
        MapList res = new MapList();

        try {
            String sMapper = String.format("%s.%s", sNameSpace, queryId);
            List<Map<String, Object>> list = openSession().selectList(sMapper, paramMap);
            res.addAll(list);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close();
        }

        return res;
    }

    private int checkExist(String sNameSpace, String queryId, Map<String, Object> paramMap) {
        int res = 0;

        try {
            String sMapper = String.format("%s.%s", sNameSpace, queryId);
            res = openSession().selectOne(sMapper, paramMap);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close();
        }

        return res;
    }

    private int insert(String sNameSpace, String queryId, Map<String, Object> paramMap) {
        int res = 0;

        try {
            String sMapper = String.format("%s.%s", sNameSpace, queryId);
            res = openSession().insert(sMapper, paramMap);
            commit();
        } catch (Exception e) {
            rollback();
            e.printStackTrace();
        } finally {
            close();
        }


        return res;
    }
    private int update(String sNameSpace, String queryId, Map<String, Object> paramMap) {
        int res = 0;

        try {
            String sMapper = String.format("%s.%s", sNameSpace, queryId);
            res = openSession().update(sMapper, paramMap);
           commit();
        } catch (Exception e) {
            rollback();
            e.printStackTrace();
        } finally {
            close();
        }
        return res;
    }

    public String getDate() throws IOException {
        Properties prop = dbConnectionUtil.getProperty();
        String dateFormat = prop.getProperty("DATE_FORMAT");
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        String dateStr = format.format(calendar.getTime());

        return dateStr;
    }
}
