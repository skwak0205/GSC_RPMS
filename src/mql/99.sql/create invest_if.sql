CREATE TABLE invest_if
(
    LINE_NO     VARCHAR2(30),
    BZ_NM       VARCHAR2(500),
    WBS_CD      VARCHAR2(20),
    PLN_AMT     NUMERIC,
    TRNS_AMT    NUMERIC,
    MGM_BGT_AMT NUMERIC,
    RSLT_AMT_01 NUMERIC,
    RSLT_AMT_02 NUMERIC,
    REM_AMT     NUMERIC,
    DEPT_NM     VARCHAR2(100),
    CREATE_DATE DATE DEFAULT SYSDATE,
    UPDATE_DATE DATE DEFAULT SYSDATE,
    IF_MSG      VARCHAR2(1024 BYTE) DEFAULT '',
    IF_YN       VARCHAR2(1024 BYTE) DEFAULT 'N',
    IF_RESULT   VARCHAR2(1024 BYTE) DEFAULT 'Fail'
);