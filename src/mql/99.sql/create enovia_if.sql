Create
tablespace enovia_IF_TS datafile 'D:/oracle/oradata/ORCL/ENOVIA_IF.DBF' size 5G autoextend on next 500M;

CREATE
USER enovia_if
    IDENTIFIED BY enovia2023  DEFAULT TABLESPACE enovia_IF_TS
    QUOTA UNLIMITED ON enovia_IF_TS
    ACCOUNT UNLOCK;
GRANT "CONNECT" TO enovia_if;
GRANT "RESOURCE" TO enovia_if;

ALTER
USER enovia_if
    QUOTA UNLIMITED
    ON enovia_IF_TS;
