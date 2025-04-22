CREATE TABLE regulation_types
(
    name VARCHAR(255)
        CONSTRAINT types_pk PRIMARY KEY
);

INSERT INTO regulation_types(name)
values ('main');
INSERT INTO regulation_types(name)
values ('sub');

CREATE SEQUENCE regulations_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE regulations
(
    id         NUMBER(19) CONSTRAINT regulations_pk PRIMARY KEY,
    r_name     VARCHAR2(255) NOT NULL CONSTRAINT regulations_name_uk UNIQUE,
    type       VARCHAR(255) NOT NULL,
    sort       NUMBER(19) DEFAULT 1,
    test       NUMBER(1) DEFAULT 1 CHECK (test IN (0, 1)),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_regulations_type FOREIGN KEY (type) REFERENCES regulation_types (name)
);

CREATE TABLE regulation_relationships
(
    parent_id NUMBER(19),
    child_id  NUMBER(19),
    sort      NUMBER(19) DEFAULT 1,
    CONSTRAINT fk_regulation_relationships_parent_id FOREIGN KEY (parent_id)
        REFERENCES regulations (id) ON DELETE CASCADE,
    CONSTRAINT fk_regulation_relationships_child_id FOREIGN KEY (child_id)
        REFERENCES regulations (id) ON DELETE CASCADE,
    CONSTRAINT regulation_relationships_pk PRIMARY KEY (parent_id, child_id)
);

CREATE TABLE regulation_languages
(
    code VARCHAR(10)
        CONSTRAINT regulation_languages_pk PRIMARY KEY,
    name VARCHAR(50) NOT NULL
        CONSTRAINT regulation_languages_name_uk UNIQUE
);

INSERT INTO regulation_languages(code, name)
values ('en', 'English');

CREATE SEQUENCE regulation_contents_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE regulation_contents
(
    id            NUMBER(19) CONSTRAINT regulation_contents_pk PRIMARY KEY,
    lang          VARCHAR(10) NOT NULL,
    title         VARCHAR2(255) NOT NULL,
    text       CLOB,
    regulation_id NUMBER(19),
    CONSTRAINT regulation_contents_uk UNIQUE (lang, regulation_id),
    CONSTRAINT fk_regulation_contents_lang FOREIGN KEY (lang) REFERENCES regulation_languages (code),
    CONSTRAINT fk_regulation_contents_regulation_id FOREIGN KEY (regulation_id) REFERENCES regulations (id)
)