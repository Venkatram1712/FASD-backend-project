ALTER TABLE sessions
    ADD COLUMN start_time TIME NULL,
    ADD COLUMN end_time TIME NULL;

UPDATE sessions
SET start_time = IFNULL(start_time, TIME(date)),
    end_time = IFNULL(end_time, ADDTIME(TIME(date), '01:00:00'))
WHERE start_time IS NULL OR end_time IS NULL;

ALTER TABLE sessions
    MODIFY COLUMN start_time TIME NOT NULL,
    MODIFY COLUMN end_time TIME NOT NULL;
