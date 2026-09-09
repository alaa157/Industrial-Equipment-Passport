CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(80) NOT NULL,
    email VARCHAR(180) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uk_users_username UNIQUE(username),
    CONSTRAINT uk_users_email UNIQUE(email)
);

CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role VARCHAR(30) NOT NULL,
    PRIMARY KEY(user_id,role),
    CONSTRAINT fk_user_roles_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_user_roles_role ON user_roles(role);

CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    token_hash VARCHAR(64) NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    revoked_at TIMESTAMPTZ,
    CONSTRAINT uk_refresh_token_hash UNIQUE(token_hash),
    CONSTRAINT fk_refresh_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_refresh_token_hash ON refresh_tokens(token_hash);
CREATE INDEX idx_refresh_user ON refresh_tokens(user_id);
