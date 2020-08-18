CREATE SEQUENCE public.orderid
    INCREMENT 1
    START 117
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.orderid
    OWNER TO postgres;

CREATE SEQUENCE public.products_productid_seq
    INCREMENT 1
    START 17
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public.products_productid_seq
    OWNER TO postgres;

CREATE SEQUENCE public.users_serialnumber_seq
    INCREMENT 1
    START 4
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public.users_serialnumber_seq
    OWNER TO postgres;

CREATE TABLE public.users
(
    serialnumber integer NOT NULL DEFAULT nextval('users_serialnumber_seq'::regclass) ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    loginid character varying(10) COLLATE pg_catalog."default" NOT NULL,
    pwd character varying(20) COLLATE pg_catalog."default" NOT NULL,
    typeofuser character varying(10) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (loginid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.users
    OWNER to postgres;

CREATE TABLE public.products
(
    productid integer NOT NULL DEFAULT nextval('products_productid_seq'::regclass) ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    productname character varying(20) COLLATE pg_catalog."default" NOT NULL,
    productcost real NOT NULL,
    productavalibility integer NOT NULL,
    productpreparationtime real,
    CONSTRAINT products_pkey PRIMARY KEY (productid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.products
    OWNER to postgres;

CREATE TABLE public.orders
(
    orderid integer NOT NULL,
    orderinfo json NOT NULL,
    ordorcost real,
    orderstatus character varying(20) COLLATE pg_catalog."default",
    ordercreatedat timestamp without time zone DEFAULT now(),
    CONSTRAINT orders_pkey PRIMARY KEY (orderid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.orders
    OWNER to postgres;

CREATE TYPE public.orderitems AS
(
	itemid integer,
	quantity integer
);

ALTER TYPE public.orderitems
    OWNER TO postgres;

CREATE OR REPLACE FUNCTION public.placeorder(
	jobject json)
    RETURNS integer
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$
declare
	orderid integer;
	itemid integer;
	quantity integer;
	quantity_check integer;
	cost_ real;
	total_cost real;
	order_status text;
	r orderitems;
BEGIN
	SELECT nextval('orderId') into orderid;
	total_cost=0;
	FOR r IN select * from json_populate_recordset(null::orderitems, jobject) LOOP
		itemid=r.itemid;
		quantity=r.quantity;
		select productavalibility-quantity  into quantity_check from products where productid=itemid;
		select productcost*quantity into cost_ from products where productid=itemid;
		total_cost = total_cost+cost_;
		if quantity_check<0 then 
			order_status='failed';
		else
			order_status='checking';
			update products set productavalibility=quantity_check where productid=itemid;
		end if;
	END LOOP;
	if order_status != 'failed' then
		INSERT INTO orders (orderId,orderInfo,ordorCost,orderStatus) VALUES(orderid, jobject, total_cost, 'order placed');
		return orderid;
	else 
		INSERT INTO orders (orderId,orderInfo,ordorCost,orderStatus) VALUES(orderid, jobject, total_cost, order_status);
		return 0;
	end if;
END;
$BODY$;

ALTER FUNCTION public.placeorder(json)
    OWNER TO postgres;


CREATE OR REPLACE FUNCTION public.fetchpendingorders(
	)
    RETURNS text
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$declare
	pending_orders text;
	pending_order_dtl text;
	orderid integer; 
	quantity integer;
	product_name text;
	order_status text;
	r orders%rowtype;
	r1 orderitems;
BEGIN
	pending_orders = '';
	FOR r IN select * from orders where lower(orderstatus) not in ('failed','collected','cancled') LOOP
		orderid = r.orderid;
		order_status = r.orderstatus;
		if pending_orders = '' then
			pending_orders = pending_orders || 	'{"orderid":"' || orderid || '","orderstatus":"' || order_status ||'","items":[';
		else
			pending_orders = pending_orders || 	',{"orderid":"' || orderid || '","orderstatus":"' || order_status ||'","items":[';
		end if;
		pending_order_dtl='';
		FOR r1 IN select * from json_populate_recordset(null::orderitems, r.orderinfo) LOOP
			quantity=r1.quantity;
			select productname into product_name from products where productid=r1.itemid;
			if pending_order_dtl != '' then 
				pending_order_dtl = pending_order_dtl || ',';
			end if;
			pending_order_dtl = pending_order_dtl || '{"productname":"' || product_name || '","quantity":"' || quantity ||'"}';
		END LOOP;
		pending_orders = pending_orders || pending_order_dtl || ']}';
	END LOOP;
	pending_orders='[' || pending_orders || ']';
	return pending_orders;
END;
$BODY$;

ALTER FUNCTION public.fetchpendingorders()
    OWNER TO postgres;
