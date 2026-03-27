CREATE TYPE "role_enum" AS ENUM (
  'ADMIN',
  'CLIENT',
  'CONSEILLER',
  'AUDITEUR'
);

CREATE TYPE "statut_utilisateur_enum" AS ENUM (
  'ACTIF',
  'BLOQUE',
  'SUSPENDU'
);

CREATE TYPE "type_compte_enum" AS ENUM (
  'COURANT',
  'EPARGNE'
);

CREATE TYPE "statut_compte_enum" AS ENUM (
  'ACTIF',
  'BLOQUE',
  'CLOTURE'
);

CREATE TYPE "type_transaction_enum" AS ENUM (
  'VIREMENT',
  'DEPOT',
  'RETRAIT'
);

CREATE TYPE "statut_transaction_enum" AS ENUM (
  'PENDING',
  'COMPLETED',
  'FAILED',
  'CANCELLED'
);

CREATE TYPE "type_piece_enum" AS ENUM (
  'CNI',
  'PASSEPORT',
  'TITRE_SEJOUR'
);

CREATE TYPE "entite_cible_enum" AS ENUM (
  'COMPTE',
  'TRANSACTION',
  'UTILISATEUR',
  'PIECE_IDENTITE'
);

CREATE TABLE "utilisateur" (
                               "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
                               "nom" varchar(100) NOT NULL,
                               "prenom" varchar(100) NOT NULL,
                               "email" varchar(255) UNIQUE NOT NULL,
                               "telephone" varchar(20),
                               "date_naissance" date NOT NULL,
                               "adresse_rue" varchar(255) NOT NULL,
                               "adresse_ville" varchar(100) NOT NULL,
                               "code_postal" varchar(10) NOT NULL,
                               "pays" varchar(2) NOT NULL DEFAULT 'FR',
                               "mot_de_passe_hash" varchar(255) NOT NULL,
                               "role" role_enum NOT NULL,
                               "statut" statut_utilisateur_enum NOT NULL DEFAULT 'ACTIF',
                               "nb_echecs_auth" integer NOT NULL DEFAULT 0,
                               "date_creation" timestamp NOT NULL DEFAULT (now()),
                               "date_modification" timestamp NOT NULL DEFAULT (now()),
                               "version" bigint NOT NULL DEFAULT 0
);

CREATE TABLE "piece_identite" (
                                  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
                                  "utilisateur_id" uuid NOT NULL,
                                  "type_piece" type_piece_enum NOT NULL,
                                  "numero" varchar(50) UNIQUE NOT NULL,
                                  "date_expiration" date NOT NULL,
                                  "date_creation" timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE "compte_bancaire" (
                                   "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
                                   "titulaire_id" uuid NOT NULL,
                                   "conseiller_id" uuid,
                                   "iban" varchar(34) UNIQUE NOT NULL,
                                   "bic" varchar(11) NOT NULL,
                                   "type_compte" type_compte_enum NOT NULL,
                                   "solde" numeric(15,2) NOT NULL DEFAULT 0,
                                   "devise" varchar(3) NOT NULL DEFAULT 'EUR',
                                   "decouvert_autorise" numeric(15,2) NOT NULL DEFAULT 0,
                                   "taux_interet" numeric(5,4) DEFAULT 0,
                                   "statut" statut_compte_enum NOT NULL DEFAULT 'ACTIF',
                                   "date_ouverture" date NOT NULL DEFAULT (current_date),
                                   "date_cloture" date,
                                   "date_modification" timestamp NOT NULL DEFAULT (now()),
                                   "version" bigint NOT NULL DEFAULT 0
);

CREATE TABLE "transaction" (
                               "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
                               "compte_debiteur_id" uuid,
                               "compte_crediteur_id" uuid,
                               "initiateur_id" uuid NOT NULL,
                               "reference" varchar(35) UNIQUE NOT NULL,
                               "type_transaction" type_transaction_enum NOT NULL,
                               "montant" numeric(15,2) NOT NULL,
                               "devise" varchar(3) NOT NULL DEFAULT 'EUR',
                               "libelle" varchar(140),
                               "statut" statut_transaction_enum NOT NULL DEFAULT 'PENDING',
                               "iban_externe" varchar(34),
                               "date_execution" timestamp NOT NULL DEFAULT (now()),
                               "date_valeur" date,
                               "message_erreur" varchar(255),
                               "version" bigint NOT NULL DEFAULT 0
);

CREATE TABLE "action" (
                          "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
                          "code" varchar(100) UNIQUE NOT NULL,
                          "libelle" varchar(255) NOT NULL
);

CREATE TABLE "audit_trail" (
                               "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
                               "utilisateur_id" uuid NOT NULL,
                               "action_id" uuid NOT NULL,
                               "entite_cible" entite_cible_enum NOT NULL,
                               "entite_id" uuid,
                               "ancien_etat" text,
                               "nouvel_etat" text,
                               "endpoint" varchar(200),
                               "adresse_ip" varchar(45),
                               "user_agent" varchar(500),
                               "timestamp" timestamp NOT NULL DEFAULT (now())
);

COMMENT ON COLUMN "utilisateur"."mot_de_passe_hash" IS 'BCrypt';

ALTER TABLE "piece_identite" ADD FOREIGN KEY ("utilisateur_id") REFERENCES "utilisateur" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "compte_bancaire" ADD FOREIGN KEY ("titulaire_id") REFERENCES "utilisateur" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "compte_bancaire" ADD FOREIGN KEY ("conseiller_id") REFERENCES "utilisateur" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "transaction" ADD FOREIGN KEY ("compte_debiteur_id") REFERENCES "compte_bancaire" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "transaction" ADD FOREIGN KEY ("compte_crediteur_id") REFERENCES "compte_bancaire" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "transaction" ADD FOREIGN KEY ("initiateur_id") REFERENCES "utilisateur" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "audit_trail" ADD FOREIGN KEY ("utilisateur_id") REFERENCES "utilisateur" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "audit_trail" ADD FOREIGN KEY ("action_id") REFERENCES "action" ("id") DEFERRABLE INITIALLY IMMEDIATE;
