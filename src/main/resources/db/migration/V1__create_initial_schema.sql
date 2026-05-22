

-- =====================================================
-- 1. ELIMINAR VISTA SI EXISTE
-- =====================================================

DROP VIEW IF EXISTS budget_cycle_balance;


-- =====================================================
-- 2. ELIMINAR TABLAS SI EXISTEN
-- =====================================================

DROP TABLE IF EXISTS savings_contributions;
DROP TABLE IF EXISTS savings_goals;
DROP TABLE IF EXISTS expenses;
DROP TABLE IF EXISTS expense_categories;
DROP TABLE IF EXISTS incomes;
DROP TABLE IF EXISTS income_categories;
DROP TABLE IF EXISTS budget_cycles;
DROP TABLE IF EXISTS refresh_tokens;
DROP TABLE IF EXISTS users;


-- =====================================================
-- 3. TABLA: users
-- Guarda los usuarios registrados en la aplicación.
-- =====================================================

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    subscription_type VARCHAR(30) NOT NULL DEFAULT 'FREE',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT chk_users_subscription_type
        CHECK (subscription_type IN ('FREE', 'PREMIUM'))
);


-- =====================================================
-- 4. TABLA: refresh_tokens
-- Guarda tokens para renovar sesión JWT.
-- =====================================================

CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(500) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_refresh_token_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);


-- =====================================================
-- 5. TABLA: budget_cycles
-- Guarda ciclos presupuestales:
-- quincenal, mensual o bimestral.
-- =====================================================

CREATE TABLE budget_cycles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(30) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    cloned_from_cycle_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_budget_cycle_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_budget_cycle_cloned_from
        FOREIGN KEY (cloned_from_cycle_id)
        REFERENCES budget_cycles(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_budget_cycle_type
        CHECK (type IN ('BIWEEKLY', 'MONTHLY', 'BIMONTHLY')),

    CONSTRAINT chk_budget_cycle_dates
        CHECK (end_date >= start_date)
);


-- =====================================================
-- 6. TABLA: income_categories
-- Catálogo de categorías de ingresos.
-- =====================================================

CREATE TABLE income_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(30) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT chk_income_category_type
        CHECK (type IN ('FIXED', 'VARIABLE')),

    CONSTRAINT uk_income_category_name_type
        UNIQUE (name, type)
);


-- =====================================================
-- 7. TABLA: incomes
-- Guarda ingresos del usuario por ciclo presupuestal.
-- =====================================================

CREATE TABLE incomes (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    budget_cycle_id BIGINT NOT NULL,
    income_category_id BIGINT,
    name VARCHAR(120) NOT NULL,
    type VARCHAR(30) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    income_date DATE NOT NULL,
    recurring BOOLEAN NOT NULL DEFAULT FALSE,
    observations VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_income_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_income_budget_cycle
        FOREIGN KEY (budget_cycle_id)
        REFERENCES budget_cycles(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_income_category
        FOREIGN KEY (income_category_id)
        REFERENCES income_categories(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_income_type
        CHECK (type IN ('FIXED', 'VARIABLE')),

    CONSTRAINT chk_income_amount
        CHECK (amount >= 0)
);


-- =====================================================
-- 8. TABLA: expense_categories
-- Catálogo de categorías de gastos.
-- =====================================================

CREATE TABLE expense_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(30) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT chk_expense_category_type
        CHECK (type IN ('FIXED', 'VARIABLE')),

    CONSTRAINT uk_expense_category_name_type
        UNIQUE (name, type)
);


-- =====================================================
-- 9. TABLA: expenses
-- Guarda gastos del usuario por ciclo presupuestal.
-- =====================================================

CREATE TABLE expenses (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    budget_cycle_id BIGINT NOT NULL,
    expense_category_id BIGINT,
    name VARCHAR(120) NOT NULL,
    type VARCHAR(30) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    due_date DATE,
    payment_date DATE,
    recurring BOOLEAN NOT NULL DEFAULT FALSE,
    reminder_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    paid BOOLEAN NOT NULL DEFAULT FALSE,
    observations VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_expense_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_expense_budget_cycle
        FOREIGN KEY (budget_cycle_id)
        REFERENCES budget_cycles(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_expense_category
        FOREIGN KEY (expense_category_id)
        REFERENCES expense_categories(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_expense_type
        CHECK (type IN ('FIXED', 'VARIABLE')),

    CONSTRAINT chk_expense_amount
        CHECK (amount >= 0)
);


-- =====================================================
-- 10. TABLA: savings_goals
-- Guarda metas de ahorro del usuario.
-- =====================================================

CREATE TABLE savings_goals (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(120) NOT NULL,
    target_amount NUMERIC(12,2) NOT NULL,
    current_amount NUMERIC(12,2) NOT NULL DEFAULT 0,
    target_date DATE NOT NULL,
    automatic_contribution BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_savings_goal_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_savings_target_amount
        CHECK (target_amount > 0),

    CONSTRAINT chk_savings_current_amount
        CHECK (current_amount >= 0)
);


-- =====================================================
-- 11. TABLA: savings_contributions
-- Guarda aportaciones a metas de ahorro.
-- =====================================================

CREATE TABLE savings_contributions (
    id BIGSERIAL PRIMARY KEY,
    savings_goal_id BIGINT NOT NULL,
    budget_cycle_id BIGINT,
    amount NUMERIC(12,2) NOT NULL,
    contribution_date DATE NOT NULL,
    contribution_type VARCHAR(30) NOT NULL DEFAULT 'MANUAL',
    observations VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_savings_contribution_goal
        FOREIGN KEY (savings_goal_id)
        REFERENCES savings_goals(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_savings_contribution_cycle
        FOREIGN KEY (budget_cycle_id)
        REFERENCES budget_cycles(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_savings_contribution_amount
        CHECK (amount > 0),

    CONSTRAINT chk_savings_contribution_type
        CHECK (contribution_type IN ('MANUAL', 'AUTOMATIC'))
);


-- =====================================================
-- 12. INSERTAR CATÁLOGO DE INGRESOS
-- =====================================================

INSERT INTO income_categories (name, type) VALUES
('Salario', 'FIXED'),
('Pensión', 'FIXED'),
('Renta', 'FIXED'),
('Bonos', 'VARIABLE'),
('Aguinaldo', 'VARIABLE'),
('Venta de artículos', 'VARIABLE'),
('Freelance', 'VARIABLE');


-- =====================================================
-- 13. INSERTAR CATÁLOGO DE GASTOS
-- =====================================================

INSERT INTO expense_categories (name, type) VALUES
('Agua', 'FIXED'),
('Luz', 'FIXED'),
('Internet', 'FIXED'),
('Gasolina', 'FIXED'),
('Renta', 'FIXED'),
('Seguro', 'FIXED'),
('Colegiaturas', 'FIXED'),
('Tarjeta de crédito', 'VARIABLE'),
('Reparaciones', 'VARIABLE'),
('Consultas médicas', 'VARIABLE'),
('Compras extraordinarias', 'VARIABLE');


-- =====================================================
-- 14. INSERTAR USUARIOS DE PRUEBA
-- =====================================================

INSERT INTO users (
    name,
    email,
    password,
    subscription_type,
    active
) VALUES
(
    'Juan Carlos Hernández',
    'juan@appfinanzas.com',
    '$2a$10$abcdefghijklmnopqrstuv1234567890abcdefghijklmnopqrstu',
    'FREE',
    TRUE
),
(
    'María López',
    'maria@appfinanzas.com',
    '$2a$10$abcdefghijklmnopqrstuv1234567890abcdefghijklmnopqrstu',
    'PREMIUM',
    TRUE
);


-- =====================================================
-- 15. INSERTAR REFRESH TOKENS DE PRUEBA
-- =====================================================

INSERT INTO refresh_tokens (
    user_id,
    token,
    expiry_date,
    revoked
) VALUES
(
    1,
    'refresh-token-demo-juan-001',
    CURRENT_TIMESTAMP + INTERVAL '30 days',
    FALSE
),
(
    2,
    'refresh-token-demo-maria-001',
    CURRENT_TIMESTAMP + INTERVAL '30 days',
    FALSE
);


-- =====================================================
-- 16. INSERTAR CICLOS PRESUPUESTALES
-- =====================================================

INSERT INTO budget_cycles (
    user_id,
    type,
    start_date,
    end_date,
    active,
    cloned_from_cycle_id
) VALUES
(
    1,
    'MONTHLY',
    '2026-05-01',
    '2026-05-31',
    TRUE,
    NULL
),
(
    1,
    'BIWEEKLY',
    '2026-06-01',
    '2026-06-15',
    TRUE,
    NULL
),
(
    2,
    'MONTHLY',
    '2026-05-01',
    '2026-05-31',
    TRUE,
    NULL
);


-- =====================================================
-- 17. INSERTAR INGRESOS DE PRUEBA
-- Usuario 1: Juan
-- Ciclo 1: Mayo 2026
-- =====================================================

INSERT INTO incomes (
    user_id,
    budget_cycle_id,
    income_category_id,
    name,
    type,
    amount,
    income_date,
    recurring,
    observations,
    active
) VALUES
(
    1,
    1,
    1,
    'Salario mensual',
    'FIXED',
    18000.00,
    '2026-05-15',
    TRUE,
    'Pago de nómina mensual',
    TRUE
),
(
    1,
    1,
    7,
    'Proyecto freelance',
    'VARIABLE',
    2500.00,
    '2026-05-20',
    FALSE,
    'Pago por proyecto adicional',
    TRUE
),
(
    1,
    2,
    1,
    'Salario primera quincena',
    'FIXED',
    9000.00,
    '2026-06-15',
    TRUE,
    'Pago quincenal',
    TRUE
),
(
    2,
    3,
    1,
    'Salario mensual',
    'FIXED',
    22000.00,
    '2026-05-15',
    TRUE,
    'Pago de nómina',
    TRUE
);


-- =====================================================
-- 18. INSERTAR GASTOS DE PRUEBA
-- Usuario 1: Juan
-- Ciclo 1: Mayo 2026
-- =====================================================

INSERT INTO expenses (
    user_id,
    budget_cycle_id,
    expense_category_id,
    name,
    type,
    amount,
    due_date,
    payment_date,
    recurring,
    reminder_enabled,
    paid,
    observations,
    active
) VALUES
(
    1,
    1,
    5,
    'Renta departamento',
    'FIXED',
    8000.00,
    '2026-05-05',
    '2026-05-04',
    TRUE,
    TRUE,
    TRUE,
    'Pago mensual de renta',
    TRUE
),
(
    1,
    1,
    2,
    'Recibo de luz',
    'FIXED',
    650.00,
    '2026-05-10',
    '2026-05-09',
    TRUE,
    TRUE,
    TRUE,
    'Pago de electricidad',
    TRUE
),
(
    1,
    1,
    3,
    'Internet hogar',
    'FIXED',
    500.00,
    '2026-05-12',
    '2026-05-12',
    TRUE,
    TRUE,
    TRUE,
    'Pago mensual de internet',
    TRUE
),
(
    1,
    1,
    8,
    'Pago tarjeta de crédito',
    'VARIABLE',
    3200.00,
    '2026-05-18',
    NULL,
    FALSE,
    TRUE,
    FALSE,
    'Pago pendiente de tarjeta',
    TRUE
),
(
    1,
    2,
    4,
    'Gasolina primera quincena',
    'FIXED',
    1200.00,
    '2026-06-07',
    NULL,
    TRUE,
    TRUE,
    FALSE,
    'Gasto estimado de gasolina',
    TRUE
),
(
    2,
    3,
    5,
    'Renta casa',
    'FIXED',
    9500.00,
    '2026-05-05',
    '2026-05-05',
    TRUE,
    TRUE,
    TRUE,
    'Pago mensual',
    TRUE
),
(
    2,
    3,
    3,
    'Internet',
    'FIXED',
    600.00,
    '2026-05-12',
    '2026-05-11',
    TRUE,
    TRUE,
    TRUE,
    'Internet fibra óptica',
    TRUE
);


-- =====================================================
-- 19. INSERTAR METAS DE AHORRO
-- =====================================================

INSERT INTO savings_goals (
    user_id,
    name,
    target_amount,
    current_amount,
    target_date,
    automatic_contribution,
    active
) VALUES
(
    1,
    'Fondo de emergencia',
    30000.00,
    5000.00,
    '2026-12-31',
    FALSE,
    TRUE
),
(
    1,
    'Viaje de vacaciones',
    20000.00,
    2500.00,
    '2026-08-31',
    FALSE,
    TRUE
),
(
    2,
    'Enganche automóvil',
    80000.00,
    15000.00,
    '2027-03-31',
    TRUE,
    TRUE
);


-- =====================================================
-- 20. INSERTAR APORTACIONES DE AHORRO
-- =====================================================

INSERT INTO savings_contributions (
    savings_goal_id,
    budget_cycle_id,
    amount,
    contribution_date,
    contribution_type,
    observations
) VALUES
(
    1,
    1,
    1000.00,
    '2026-05-15',
    'MANUAL',
    'Aportación después de recibir salario'
),
(
    1,
    1,
    500.00,
    '2026-05-25',
    'MANUAL',
    'Aportación extra'
),
(
    2,
    1,
    800.00,
    '2026-05-20',
    'MANUAL',
    'Ahorro para viaje'
),
(
    3,
    3,
    3000.00,
    '2026-05-15',
    'AUTOMATIC',
    'Aportación automática programada'
);


-- =====================================================
-- 21. CREAR VISTA DE BALANCE FINANCIERO
-- Calcula:
-- total_income - total_expense = balance
-- =====================================================

CREATE OR REPLACE VIEW budget_cycle_balance AS
SELECT
    bc.id AS budget_cycle_id,
    bc.user_id,
    u.name AS user_name,
    bc.type AS cycle_type,
    bc.start_date,
    bc.end_date,

    COALESCE(inc.total_income, 0) AS total_income,
    COALESCE(exp.total_expense, 0) AS total_expense,

    COALESCE(inc.total_income, 0) - COALESCE(exp.total_expense, 0) AS balance

FROM budget_cycles bc

INNER JOIN users u
    ON u.id = bc.user_id

LEFT JOIN (
    SELECT 
        budget_cycle_id,
        SUM(amount) AS total_income
    FROM incomes
    WHERE active = TRUE
    GROUP BY budget_cycle_id
) inc 
    ON inc.budget_cycle_id = bc.id

LEFT JOIN (
    SELECT 
        budget_cycle_id,
        SUM(amount) AS total_expense
    FROM expenses
    WHERE active = TRUE
    GROUP BY budget_cycle_id
) exp 
    ON exp.budget_cycle_id = bc.id;


-- =====================================================
-- 22. CREAR ÍNDICES 
-- Mejoran consultas por usuario, ciclo y fechas.
-- =====================================================

CREATE INDEX idx_refresh_tokens_user_id
ON refresh_tokens(user_id);

CREATE INDEX idx_budget_cycles_user_id
ON budget_cycles(user_id);

CREATE INDEX idx_budget_cycles_user_active
ON budget_cycles(user_id, active);

CREATE INDEX idx_budget_cycles_dates
ON budget_cycles(start_date, end_date);

CREATE INDEX idx_incomes_user_id
ON incomes(user_id);

CREATE INDEX idx_incomes_budget_cycle_id
ON incomes(budget_cycle_id);

CREATE INDEX idx_incomes_income_date
ON incomes(income_date);

CREATE INDEX idx_expenses_user_id
ON expenses(user_id);

CREATE INDEX idx_expenses_budget_cycle_id
ON expenses(budget_cycle_id);

CREATE INDEX idx_expenses_due_date
ON expenses(due_date);

CREATE INDEX idx_savings_goals_user_id
ON savings_goals(user_id);

CREATE INDEX idx_savings_contributions_goal_id
ON savings_contributions(savings_goal_id);

CREATE INDEX idx_savings_contributions_cycle_id
ON savings_contributions(budget_cycle_id);


-- =====================================================
-- 23. CONSULTAS DE VALIDACIÓN
-- =====================================================

SELECT * FROM users;

SELECT * FROM income_categories;

SELECT * FROM expense_categories;

SELECT * FROM budget_cycles;

SELECT * FROM incomes;

SELECT * FROM expenses;

SELECT * FROM savings_goals;

SELECT * FROM savings_contributions;

SELECT * FROM budget_cycle_balance;