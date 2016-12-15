CREATE TABLE DOCU_SIGN_AUTHENTICATING_USER (
TENANT_KEY VARCHAR(20) CHARACTER SET latin1 COLLATE latin1_general_cs NOT NULL, 
USERNAME VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_general_cs, 
PASSWORD VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_general_cs, 
INTEGRATOR_KEY VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_general_cs, 
ENABLED CHAR(1) CHARACTER SET latin1 COLLATE latin1_general_cs, 
ACCOUNT_ID VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_general_cs, 
LAST_UPDATED_STAMP DATETIME, 
LAST_UPDATED_TX_STAMP DATETIME, 
CREATED_STAMP DATETIME, 
CREATED_TX_STAMP DATETIME, 
CONSTRAINT PK_AUTHENTICATING_USER PRIMARY KEY (TENANT_KEY)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

CREATE TABLE DOCU_SIGN_ENVELOPE (
ENVELOPE_ID VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_general_cs NOT NULL, 
SENDER_DOCU_SIGN_USER_ID VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_general_cs, 
SENDER_DOCU_SIGN_USER_EMAIL VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_general_cs, 
EMAIL_SUBJECT VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_general_cs,
STATUS VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_general_cs,
DOCUMENTS_URI VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_general_cs,
RECIPIENTS_URI VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_general_cs,
ENVELOPE_URI VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_general_cs,
SENDER_USER_LOGIN_ID VARCHAR(20) CHARACTER SET latin1 COLLATE latin1_general_cs,
CUSTOM_FIELDS_URI VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_general_cs,
NOTIFICATION_URI VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_general_cs,
STATUS_CHANGED_DATE_TIME VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_general_cs,
DOCUMENTS_COMBINED_URI VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_general_cs,
CERTIFICATE_URI VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_general_cs,
TEMPLATES_URI VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_general_cs,
LAST_UPDATED_STAMP DATETIME,
LAST_UPDATED_TX_STAMP DATETIME,
CREATED_STAMP DATETIME,
CREATED_TX_STAMP DATETIME,
CONSTRAINT PK_DOCU_SIGN_ENVELOPE PRIMARY KEY (ENVELOPE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;


CREATE TABLE DOCU_SIGN_USER (
USER_LOGIN_ID VARCHAR(20) CHARACTER SET latin1 COLLATE latin1_general_cs NOT NULL,
DOCU_SIGN_USER_ID VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_general_cs,
EMAIL VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_general_cs,
ENABLED CHAR(1) CHARACTER SET latin1 COLLATE latin1_general_cs,
LAST_UPDATED_STAMP DATETIME,
LAST_UPDATED_TX_STAMP DATETIME,
CREATED_STAMP DATETIME,
CREATED_TX_STAMP DATETIME,
CONSTRAINT PK_DOCU_SIGN_USER PRIMARY KEY (USER_LOGIN_ID)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
