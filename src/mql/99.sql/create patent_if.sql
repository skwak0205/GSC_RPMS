CREATE TABLE patent_if
(
    mst_no             VARCHAR2(30),
    ko_app_title       VARCHAR2(1000),
    country_name_title VARCHAR2(100),
    ko_name            VARCHAR2(100),
    wf_status_name     VARCHAR2(600),
    priority_mst_no    VARCHAR2(30),
    priority_date      VARCHAR2(8),
    reg_no             VARCHAR2(30),
    reg_date           VARCHAR2(8),
    upd_datetime       DATE,
    if_date            DATE    DEFAULT sysdate,
    if_yn              CHAR(1) DEFAULT 'N',
    if_result          VARCHAR2(1000),
    if_msg             VARCHAR2(2000)
);