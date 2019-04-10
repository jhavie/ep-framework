ALTER TABLE GCC.GCC_GDWS_CONFIG
 DROP PRIMARY KEY CASCADE;

DROP TABLE GCC.GCC_GDWS_CONFIG CASCADE CONSTRAINTS;

CREATE TABLE GCC.GCC_GDWS_CONFIG
(
  PARA_NAME   VARCHAR2(20 BYTE)                 NOT NULL,
  PARA_DESC   VARCHAR2(100 BYTE),
  PARA_VALUE  VARCHAR2(255 BYTE)                NOT NULL
);

COMMENT ON TABLE GCC.GCC_GDWS_CONFIG IS '系统参数表';

COMMENT ON COLUMN GCC.GCC_GDWS_CONFIG.PARA_NAME IS '参数名';

COMMENT ON COLUMN GCC.GCC_GDWS_CONFIG.PARA_DESC IS '参数描述';

COMMENT ON COLUMN GCC.GCC_GDWS_CONFIG.PARA_VALUE IS '参数值';



CREATE UNIQUE INDEX GCC.GCC_GDWS_CONFIG_PK ON GCC.GCC_GDWS_CONFIG
(PARA_NAME);


ALTER TABLE GCC.GCC_GDWS_CONFIG ADD (
  CONSTRAINT GCC_GDWS_CONFIG_PK
  PRIMARY KEY
  (PARA_NAME)
  USING INDEX GCC.GCC_GDWS_CONFIG_PK
  ENABLE VALIDATE);