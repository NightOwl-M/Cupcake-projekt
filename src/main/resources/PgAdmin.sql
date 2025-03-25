--
-- PostgreSQL database dump
--

-- Dumped from database version 16.2 (Debian 16.2-1.pgdg120+2)
-- Dumped by pg_dump version 16.1

-- Started on 2024-10-23 08:29:13 UTC

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA IF NOT exists public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- TOC entry 3404 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 224 (class 1259 OID 24645)
-- Name: bottom; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bottom (
                               bottom_id integer NOT NULL,
                               bottom_name character varying(20) NOT NULL,
                               bottom_price numeric NOT NULL
);


ALTER TABLE public.bottom OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 24644)
-- Name: bottom_bottom_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.bottom_bottom_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.bottom_bottom_id_seq OWNER TO postgres;

--
-- TOC entry 3405 (class 0 OID 0)
-- Dependencies: 223
-- Name: bottom_bottom_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.bottom_bottom_id_seq OWNED BY public.bottom.bottom_id;


--
-- TOC entry 218 (class 1259 OID 24616)
-- Name: orders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orders (
                               order_id integer NOT NULL,
                               order_price numeric NOT NULL,
                               paid_status boolean DEFAULT false NOT NULL,
                               user_id integer
);


ALTER TABLE public.orders OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 24615)
-- Name: order_order_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.order_order_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.order_order_id_seq OWNER TO postgres;

--
-- TOC entry 3406 (class 0 OID 0)
-- Dependencies: 217
-- Name: order_order_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.order_order_id_seq OWNED BY public.orders.order_id;


--
-- TOC entry 220 (class 1259 OID 24626)
-- Name: productline; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.productline (
                                    productline_id integer NOT NULL,
                                    quantity integer DEFAULT 0 NOT NULL,
                                    total_price numeric NOT NULL,
                                    topping_id integer NOT NULL,
                                    bottom_id integer NOT NULL,
                                    order_id integer NOT NULL
);


ALTER TABLE public.productline OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 24625)
-- Name: product_line_productline_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.product_line_productline_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.product_line_productline_id_seq OWNER TO postgres;

--
-- TOC entry 3407 (class 0 OID 0)
-- Dependencies: 219
-- Name: product_line_productline_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.product_line_productline_id_seq OWNED BY public.productline.productline_id;


--
-- TOC entry 222 (class 1259 OID 24636)
-- Name: topping; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.topping (
                                topping_id integer NOT NULL,
                                topping_name character varying(20) NOT NULL,
                                topping_price numeric NOT NULL
);


ALTER TABLE public.topping OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 24635)
-- Name: topping_topping_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.topping_topping_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.topping_topping_id_seq OWNER TO postgres;

--
-- TOC entry 3408 (class 0 OID 0)
-- Dependencies: 221
-- Name: topping_topping_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.topping_topping_id_seq OWNED BY public.topping.topping_id;


--
-- TOC entry 216 (class 1259 OID 24605)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
                              user_id integer NOT NULL,
                              user_name character varying NOT NULL,
                              user_password character varying NOT NULL,
                              balance numeric DEFAULT 0 NOT NULL,
                              is_admin boolean DEFAULT false NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 24604)
-- Name: user_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_user_id_seq OWNER TO postgres;

--
-- TOC entry 3409 (class 0 OID 0)
-- Dependencies: 215
-- Name: user_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_user_id_seq OWNED BY public.users.user_id;


--
-- TOC entry 3231 (class 2604 OID 24648)
-- Name: bottom bottom_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bottom ALTER COLUMN bottom_id SET DEFAULT nextval('public.bottom_bottom_id_seq'::regclass);


--
-- TOC entry 3226 (class 2604 OID 24619)
-- Name: orders order_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders ALTER COLUMN order_id SET DEFAULT nextval('public.order_order_id_seq'::regclass);


--
-- TOC entry 3228 (class 2604 OID 24629)
-- Name: productline productline_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.productline ALTER COLUMN productline_id SET DEFAULT nextval('public.product_line_productline_id_seq'::regclass);


--
-- TOC entry 3230 (class 2604 OID 24639)
-- Name: topping topping_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.topping ALTER COLUMN topping_id SET DEFAULT nextval('public.topping_topping_id_seq'::regclass);


--
-- TOC entry 3223 (class 2604 OID 24608)
-- Name: users user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.user_user_id_seq'::regclass);


--
-- TOC entry 3398 (class 0 OID 24645)
-- Dependencies: 224
-- Data for Name: bottom; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.bottom VALUES (1, 'Chocolate', 5.00);
INSERT INTO public.bottom VALUES (2, 'Vanilla', 5.00);
INSERT INTO public.bottom VALUES (3, 'Nutmeg', 5.00);
INSERT INTO public.bottom VALUES (4, 'Pistacio', 6.00);
INSERT INTO public.bottom VALUES (5, 'Almond', 7.00);


--
-- TOC entry 3392 (class 0 OID 24616)
-- Dependencies: 218
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3394 (class 0 OID 24626)
-- Dependencies: 220
-- Data for Name: productline; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3396 (class 0 OID 24636)
-- Dependencies: 222
-- Data for Name: topping; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.topping VALUES (1, 'Chocolate', 5.00);
INSERT INTO public.topping VALUES (2, 'Blueberry', 5.00);
INSERT INTO public.topping VALUES (3, 'Rasberry', 5.00);
INSERT INTO public.topping VALUES (4, 'Crispy', 6.00);
INSERT INTO public.topping VALUES (5, 'Strawberry', 6.00);
INSERT INTO public.topping VALUES (6, 'Rum/Raisin', 7.00);
INSERT INTO public.topping VALUES (7, 'Orange', 8.00);
INSERT INTO public.topping VALUES (8, 'Lemon', 8.00);
INSERT INTO public.topping VALUES (9, 'Blue cheese', 9.00);


--
-- TOC entry 3390 (class 0 OID 24605)
-- Dependencies: 216
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users VALUES (1, 'admin@admin.admin', '1234', 50000, true);


--
-- TOC entry 3410 (class 0 OID 0)
-- Dependencies: 223
-- Name: bottom_bottom_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.bottom_bottom_id_seq', 5, true);


--
-- TOC entry 3411 (class 0 OID 0)
-- Dependencies: 217
-- Name: order_order_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.order_order_id_seq', 1, false);


--
-- TOC entry 3412 (class 0 OID 0)
-- Dependencies: 219
-- Name: product_line_productline_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.product_line_productline_id_seq', 1, false);


--
-- TOC entry 3413 (class 0 OID 0)
-- Dependencies: 221
-- Name: topping_topping_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.topping_topping_id_seq', 9, true);


--
-- TOC entry 3414 (class 0 OID 0)
-- Dependencies: 215
-- Name: user_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_user_id_seq', 1, true);


--
-- TOC entry 3241 (class 2606 OID 24652)
-- Name: bottom bottom_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bottom
    ADD CONSTRAINT bottom_pkey PRIMARY KEY (bottom_id);


--
-- TOC entry 3235 (class 2606 OID 24624)
-- Name: orders order_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT order_pkey PRIMARY KEY (order_id);


--
-- TOC entry 3237 (class 2606 OID 24634)
-- Name: productline product_line_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.productline
    ADD CONSTRAINT product_line_pkey PRIMARY KEY (productline_id);


--
-- TOC entry 3239 (class 2606 OID 24643)
-- Name: topping topping_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.topping
    ADD CONSTRAINT topping_pkey PRIMARY KEY (topping_id);


--
-- TOC entry 3233 (class 2606 OID 24614)
-- Name: users user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_pkey PRIMARY KEY (user_id);


--
-- TOC entry 3242 (class 2606 OID 24658)
-- Name: orders orders_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id) NOT VALID;


--
-- TOC entry 3243 (class 2606 OID 24668)
-- Name: productline productline_bottom_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.productline
    ADD CONSTRAINT productline_bottom_id_fkey FOREIGN KEY (bottom_id) REFERENCES public.bottom(bottom_id) NOT VALID;


--
-- TOC entry 3244 (class 2606 OID 24673)
-- Name: productline productline_order_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.productline
    ADD CONSTRAINT productline_order_id_fkey FOREIGN KEY (order_id) REFERENCES public.orders(order_id) NOT VALID;


--
-- TOC entry 3245 (class 2606 OID 24663)
-- Name: productline productline_topping_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.productline
    ADD CONSTRAINT productline_topping_id_fkey FOREIGN KEY (topping_id) REFERENCES public.topping(topping_id) NOT VALID;


-- Completed on 2024-10-23 08:29:14 UTC

--
-- PostgreSQL database dump complete
--

