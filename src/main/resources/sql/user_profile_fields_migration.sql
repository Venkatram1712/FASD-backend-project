-- Run this once for existing databases where users table already exists.
ALTER TABLE users ADD COLUMN IF NOT EXISTS phone VARCHAR(50) NULL;
ALTER TABLE users ADD COLUMN IF NOT EXISTS bio TEXT NULL;
ALTER TABLE users ADD COLUMN IF NOT EXISTS institution VARCHAR(255) NULL;
ALTER TABLE users ADD COLUMN IF NOT EXISTS specialization VARCHAR(255) NULL;
ALTER TABLE users ADD COLUMN IF NOT EXISTS experience_years INT NULL;
ALTER TABLE users ADD COLUMN IF NOT EXISTS questionnaire_completed BIT(1) NOT NULL DEFAULT b'0';
