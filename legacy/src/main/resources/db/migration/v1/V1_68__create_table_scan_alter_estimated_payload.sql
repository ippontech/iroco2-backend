CREATE TABLE IF NOT EXISTS scan (
    id UUID PRIMARY KEY,
    user_id VARCHAR,
    account_id VARCHAR,
    creation_date TIMESTAMP
);

ALTER TABLE estimated_payload
    ADD COLUMN scan_id UUID,
    ADD CONSTRAINT estimated_payload_scan_id_fkey FOREIGN KEY (scan_id) REFERENCES scan (id),
    ADD CONSTRAINT check_only_one_fk CHECK (
        (scan_id IS NOT NULL AND analysis_id IS NULL) OR
        (scan_id IS NULL AND analysis_id IS NOT NULL)
    );