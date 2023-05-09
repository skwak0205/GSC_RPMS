package com.gsc.dbConfig;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.SqlSessionManager;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class dbConnectionUtil {
    private static final Log logger = LogFactory.getLog(SqlSessionManager.class);
    private static final String DB_PROPERTY = "gscDBConfig/db.properties";
    private static final String CONFIG_INTERFACE = "gscDBConfig/interfaceConfig.xml";

    public static SqlSessionFactory getSqlSessionFactory() {
        Properties prop;
        SqlSessionFactory sqlSessionFactory = null;
        
        try (Reader reader = Resources.getResourceAsReader(CONFIG_INTERFACE)) {
            prop = getProperty();
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, prop);

        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getStackTrace()[0].getClassName()
                    + "." + e.getStackTrace()[0].getMethodName(), e);
        }

        return sqlSessionFactory;
    }

    public static SqlSession getSqlSession() {
        try{
            SqlSession session = getSqlSessionFactory().openSession();
            return session;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Properties getProperty() throws IOException {
        return Resources.getResourceAsProperties(DB_PROPERTY);
    }
}
