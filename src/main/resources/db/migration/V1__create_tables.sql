-- public.tb_bank definição

-- Drop table

-- DROP TABLE public.tb_bank;

CREATE TABLE public.tb_bank (
    id uuid NOT NULL,
    code serial4 NOT NULL,
    "name" varchar(255) NOT NULL,
    exclusion_date_time timestamp(6) NULL,
    CONSTRAINT tb_bank_code_key UNIQUE (code),
    CONSTRAINT tb_bank_pkey PRIMARY KEY (id)
);

-- public.tb_wallet definição

-- Drop table

-- DROP TABLE public.tb_wallet;

CREATE TABLE public.tb_wallet (
    id uuid NOT NULL,
    code serial4 NOT NULL,
    description varchar(255) NULL,
    balance numeric(38, 2) NOT NULL,
    bank_id uuid NULL,
    "type" varchar(255) NOT NULL,
    exclusion_date_time timestamp(6) NULL,
    CONSTRAINT tb_wallet_code_key UNIQUE (code),
    CONSTRAINT tb_wallet_pkey PRIMARY KEY (id)
);


-- public.tb_wallet chaves estrangeiras

ALTER TABLE public.tb_wallet ADD CONSTRAINT fklwr5dnn4ft57u8fim3hmjmix6 FOREIGN KEY (bank_id) REFERENCES public.tb_bank(id);

-- public.tb_movement definição

-- Drop table

-- DROP TABLE public.tb_movement;

CREATE TABLE public.tb_movement (
    id uuid NOT NULL,
    code serial4 NOT NULL,
    description varchar(255) NOT NULL,
    movement_date date NOT NULL,
    value numeric(38, 2) NOT NULL,
    wallet_id uuid NOT NULL,
    status varchar(255) NOT NULL,
    exclusion_date_time timestamp(6) NULL,
    CONSTRAINT tb_movement_code_key UNIQUE (code),
    CONSTRAINT tb_movement_pkey PRIMARY KEY (id)
);


-- public.tb_movement chaves estrangeiras

ALTER TABLE public.tb_movement ADD CONSTRAINT fkqf7n9j80m3akcdo7g5ekg6poa FOREIGN KEY (wallet_id) REFERENCES public.tb_wallet(id);