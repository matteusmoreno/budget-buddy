INSERT INTO customers (
    id,
    username,
    password,
    name,
    email,
    phone,
    cpf,
    address_id,
    country,
    balance,
    role,
    created_at,
    updated_at,
    deleted_at,
    active
) VALUES (
             UNHEX(REPLACE(UUID(), '-', '')), -- Gerar um ID único no formato BINARY(16)
             'admin',
             'admin', -- Substitua com a senha real
             'Admin',
             'admin@admin.com',
             '(22)999999999', -- Substitua com o telefone real
             '123.456.789-00', -- Substitua com o CPF real
             NULL, -- Se não houver um endereço associado, mantenha NULL
             'BRAZIL',
             0.00,
             'ADMIN',
             CURRENT_TIMESTAMP,
             NULL,
             NULL, -- Assumindo que não está deletado
             1 -- Ativo
         );
