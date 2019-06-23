CREATE TABLE if not exists public.car
(
    maker integer NOT NULL,
    maturity_date timestamp without time zone,
    model character varying COLLATE pg_catalog."default" NOT NULL,
    price integer,
    CONSTRAINT car_pkey PRIMARY KEY (maker, model)
);
CREATE TABLE if not exists public.app_user
(
    username character varying NOT NULL,
    email character varying,
    password_hash bytea,
    PRIMARY KEY (username)
);
