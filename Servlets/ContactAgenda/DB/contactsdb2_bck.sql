PGDMP  *                	    |            contactsdb2    16.4    16.4     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16484    contactsdb2    DATABASE     ~   CREATE DATABASE contactsdb2 WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Japanese_Japan.932';
    DROP DATABASE contactsdb2;
                postgres    false                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
                pg_database_owner    false            �           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                   pg_database_owner    false    4            �            1259    27056    contact_card    TABLE     &  CREATE TABLE public.contact_card (
    id integer NOT NULL,
    postal_code character varying(10),
    address character varying(500),
    company_name character varying(50),
    second_name character varying(50),
    first_name character varying(50),
    phone_number character varying(15)
);
     DROP TABLE public.contact_card;
       public         heap    postgres    false    4            �            1259    27055    contact_card_id_seq    SEQUENCE     �   CREATE SEQUENCE public.contact_card_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.contact_card_id_seq;
       public          postgres    false    4    216            �           0    0    contact_card_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.contact_card_id_seq OWNED BY public.contact_card.id;
          public          postgres    false    215            P           2604    27059    contact_card id    DEFAULT     r   ALTER TABLE ONLY public.contact_card ALTER COLUMN id SET DEFAULT nextval('public.contact_card_id_seq'::regclass);
 >   ALTER TABLE public.contact_card ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    216    215    216            �          0    27056    contact_card 
   TABLE DATA           u   COPY public.contact_card (id, postal_code, address, company_name, second_name, first_name, phone_number) FROM stdin;
    public          postgres    false    216   �       �           0    0    contact_card_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.contact_card_id_seq', 6, true);
          public          postgres    false    215            �   &   x�3�LNNƆ�L9�1��eƙ���s��qqq �     