CREATE TABLE issue (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    description TEXT
);

INSERT INTO issue (id, name, description) VALUES('550e8400-e29b-11d4-a716-446655440000', 'Test', 'This is a test');
