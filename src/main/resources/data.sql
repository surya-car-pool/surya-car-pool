INSERT INTO users (name, email, phone, password, role, enabled)
SELECT
  'System Admin',
  'admin@suryacarpool.com',
  '9949425597',
  '$2a$10$OkrZE0XtIpd4VQ3oGoHFR.QLTl8vmN6CrOxMuitX9MfPtOjBmGm1e',
  'ADMIN',
  1
WHERE NOT EXISTS (
  SELECT 1 FROM users WHERE email='admin@suryacarpool.com'
);
