CREATE TABLE APPROVAL_IF (
                             APP_ID              VARCHAR2(200),
                             TITLE           VARCHAR2(1000),
                             CONTENTS           VARCHAR2(4000),
                             REQUESTER_ID            VARCHAR2(100),
                             CREATE_DATE     DATE DEFAULT SYSDATE,
                             UPDATE_DATE     DATE DEFAULT SYSDATE
);
DROP TABLE APPROVAL_LINE_IF;
CREATE TABLE APPROVAL_LINE_IF (
                                  APP_ID              VARCHAR2(200),
                                  APP_SEQ             INTEGER,
                                  APP_TYPE              VARCHAR2(50),
                                  APP_TITLE              VARCHAR2(200),
                                  USER_ID           VARCHAR2(50),
                                  START_DATE           DATE,
                                  END_DATE           DATE,
                                  APPROVE_YN       VARCHAR2(50),
                                  IF_DATE         DATE,
                                  IF_RESULT       VARCHAR2(100),
                                  IF_MSG          VARCHAR2(1000),
                                  CREATE_DATE     DATE DEFAULT SYSDATE,
                                  UPDATE_DATE     DATE DEFAULT SYSDATE
);