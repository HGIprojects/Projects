PGDMP  	                	    |            bbdd    16.4    16.4     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    27005    bbdd    DATABASE     w   CREATE DATABASE bbdd WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Japanese_Japan.932';
    DROP DATABASE bbdd;
                postgres    false                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
                pg_database_owner    false            �           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                   pg_database_owner    false    4            �            1259    27039    calendar_db    TABLE     2  CREATE TABLE public.calendar_db (
    id integer NOT NULL,
    "selectedYear" integer,
    "selectedMonth" character varying,
    "selectedDay" integer,
    "calendarHour" integer,
    "calendarMinute" integer,
    "endHour" integer,
    "endMinute" integer,
    "calendarAppointment" character varying
);
    DROP TABLE public.calendar_db;
       public         heap    postgres    false    4            �            1259    27046    calendar_db_id_seq    SEQUENCE     �   ALTER TABLE public.calendar_db ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.calendar_db_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    215            �          0    27039    calendar_db 
   TABLE DATA           �   COPY public.calendar_db (id, "selectedYear", "selectedMonth", "selectedDay", "calendarHour", "calendarMinute", "endHour", "endMinute", "calendarAppointment") FROM stdin;
    public          postgres    false    215   8       �           0    0    calendar_db_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.calendar_db_id_seq', 25, true);
          public          postgres    false    216            Q           2606    27045    calendar_db calendar_db_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.calendar_db
    ADD CONSTRAINT calendar_db_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.calendar_db DROP CONSTRAINT calendar_db_pkey;
       public            postgres    false    215            �      x������ � �     