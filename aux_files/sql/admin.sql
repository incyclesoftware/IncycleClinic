USE InCycleClinique;
CREATE USER 'admin_user'@'%' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON *.* TO 'admin_user'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;