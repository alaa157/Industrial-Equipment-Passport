SELECT 'CREATE DATABASE passport_auth'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'passport_auth')\gexec
SELECT 'CREATE DATABASE passport_equipment'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'passport_equipment')\gexec
SELECT 'CREATE DATABASE passport_maintenance'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'passport_maintenance')\gexec
SELECT 'CREATE DATABASE passport_audit'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'passport_audit')\gexec
SELECT 'CREATE DATABASE passport_notification'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'passport_notification')\gexec
