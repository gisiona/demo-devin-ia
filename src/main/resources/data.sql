INSERT INTO users (name, email, department, created_at) VALUES 
('João Silva', 'joao@example.com', 'Engineering', '2024-01-01 10:00:00'),
('Maria Santos', 'maria@example.com', 'Marketing', '2024-01-02 11:00:00'),
('Pedro Costa', 'pedro@example.com', 'Engineering', '2024-01-03 12:00:00'),
('Ana Oliveira', 'ana@example.com', 'Sales', '2024-01-04 13:00:00'),
('Carlos Ferreira', 'carlos@example.com', 'Engineering', '2024-01-05 14:00:00'),
('Lucia Rodrigues', 'lucia@example.com', 'Marketing', '2024-01-06 15:00:00'),
('Rafael Lima', 'rafael@example.com', 'Sales', '2024-01-07 16:00:00'),
('Fernanda Alves', 'fernanda@example.com', 'Engineering', '2024-01-08 17:00:00');

INSERT INTO user_activities (action, description, user_id, timestamp) VALUES
('LOGIN', 'User logged into system', 1, '2024-01-01 10:30:00'),
('CREATE_PROJECT', 'Created new project', 1, '2024-01-01 11:00:00'),
('LOGIN', 'User logged into system', 2, '2024-01-02 11:30:00'),
('SEND_EMAIL', 'Sent marketing email', 2, '2024-01-02 12:00:00'),
('LOGIN', 'User logged into system', 3, '2024-01-03 12:30:00'),
('CODE_REVIEW', 'Reviewed pull request', 3, '2024-01-03 13:00:00'),
('LOGIN', 'User logged into system', 4, '2024-01-04 13:30:00'),
('CALL_CLIENT', 'Called potential client', 4, '2024-01-04 14:00:00'),
('LOGIN', 'User logged into system', 5, '2024-01-05 14:30:00'),
('DEPLOY_APP', 'Deployed application', 5, '2024-01-05 15:00:00');
