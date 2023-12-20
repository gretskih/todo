ALTER TABLE tasks
ADD user_id int not null references todo_user(id) DEFAULT 1;