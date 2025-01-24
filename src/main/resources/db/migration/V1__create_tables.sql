CREATE TABLE status (
    status_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    status_name VARCHAR(30) NOT NULL
);

INSERT INTO status (status_id, status_name) VALUES
(1, 'NEW'),
(2, 'IN_PROGRESS'),
(3, 'COMPLETED'),
(4, 'BLOCKED'),
(5, 'DEFERRED');

CREATE TABLE priority (
    priority_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    priority_name VARCHAR(30) NOT NULL
);

INSERT INTO priority (priority_id, priority_name) VALUES
(1, 'CRITICAL'),
(2, 'HIGH'),
(3, 'MEDIUM'),
(4, 'LOW'),
(5, 'UNSPECIFIED');

CREATE TABLE tasks (
    task_id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    task_summary VARCHAR(100) NOT NULL,
    task_status_id INT NOT NULL,
    task_priority_id INT NOT NULL,
    task_notes TEXT,
    due_date DATE,
    FOREIGN KEY (task_status_id) REFERENCES status(status_id),
    FOREIGN KEY(task_priority_id) REFERENCES priority(priority_id)

);



