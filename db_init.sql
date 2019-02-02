CREATE TABLE public.transactions
(
  id serial NOT NULL PRIMARY KEY,
  user_id integer,
  sum real
)
WITH (
  OIDS=FALSE
);

CREATE TABLE public.users
(
  id serial NOT NULL PRIMARY KEY,
  login text,
  pass text,
  CONSTRAINT users_login_key UNIQUE (login)
)
WITH (
  OIDS=FALSE
);


