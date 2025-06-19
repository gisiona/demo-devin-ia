CREATE DATABASE IF NOT EXISTS financial_control;
USE financial_control;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    type ENUM('INCOME', 'EXPENSE') NOT NULL,
    color VARCHAR(7), -- Hex color code
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    type ENUM('CHECKING', 'SAVINGS', 'CREDIT_CARD', 'INVESTMENT') NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00,
    currency VARCHAR(3) DEFAULT 'BRL',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    category_id BIGINT,
    amount DECIMAL(15,2) NOT NULL,
    type ENUM('INCOME', 'EXPENSE', 'TRANSFER') NOT NULL,
    description VARCHAR(255),
    transaction_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    tags JSON,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (account_id) REFERENCES accounts(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE investment_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    tax_category VARCHAR(50), -- For tax declaration purposes
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE investments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    investment_type_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    symbol VARCHAR(20), -- Stock symbol, fund code, etc.
    quantity DECIMAL(15,6),
    purchase_price DECIMAL(15,2),
    current_price DECIMAL(15,2),
    purchase_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (investment_type_id) REFERENCES investment_types(id)
);

CREATE TABLE tax_declarations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    year INT NOT NULL,
    status ENUM('DRAFT', 'SUBMITTED', 'APPROVED') DEFAULT 'DRAFT',
    total_income DECIMAL(15,2),
    total_expenses DECIMAL(15,2),
    total_investments DECIMAL(15,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE KEY unique_user_year (user_id, year)
);

INSERT INTO categories (name, description, type, color) VALUES
('Salário', 'Salário mensal', 'INCOME', '#4CAF50'),
('Freelance', 'Trabalhos freelance', 'INCOME', '#8BC34A'),
('Investimentos', 'Rendimentos de investimentos', 'INCOME', '#2196F3'),
('Alimentação', 'Gastos com alimentação', 'EXPENSE', '#FF5722'),
('Transporte', 'Gastos com transporte', 'EXPENSE', '#FF9800'),
('Moradia', 'Aluguel, financiamento, condomínio', 'EXPENSE', '#795548'),
('Saúde', 'Gastos médicos e farmácia', 'EXPENSE', '#E91E63'),
('Educação', 'Cursos, livros, educação', 'EXPENSE', '#9C27B0'),
('Lazer', 'Entretenimento e lazer', 'EXPENSE', '#673AB7'),
('Outros', 'Outros gastos', 'EXPENSE', '#607D8B');

INSERT INTO investment_types (name, description, tax_category) VALUES
('Ações', 'Ações de empresas listadas na bolsa', 'VARIABLE_INCOME'),
('Fundos Imobiliários', 'Fundos de investimento imobiliário', 'VARIABLE_INCOME'),
('CDB', 'Certificado de Depósito Bancário', 'FIXED_INCOME'),
('LCI/LCA', 'Letra de Crédito Imobiliário/Agronegócio', 'FIXED_INCOME'),
('Tesouro Direto', 'Títulos públicos do governo', 'FIXED_INCOME'),
('Fundos de Investimento', 'Fundos de investimento diversos', 'VARIABLE_INCOME'),
('Previdência Privada', 'PGBL/VGBL', 'PENSION'),
('Criptomoedas', 'Moedas digitais', 'CRYPTO');
