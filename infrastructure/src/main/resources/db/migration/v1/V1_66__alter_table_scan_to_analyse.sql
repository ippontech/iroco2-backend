-- Renommer la table "scan" en "analyse"
ALTER TABLE scan RENAME TO analyse_cur;

-- Renommer la clé étrangère dans "estimated_payload"
ALTER TABLE estimated_payload RENAME COLUMN scan_id TO analyse_id;

-- Mettre à jour la contrainte de clé étrangère
ALTER TABLE estimated_payload
    DROP CONSTRAINT estimated_payload_scan_id_fkey,
    ADD CONSTRAINT estimated_payload_analyse_id_fkey FOREIGN KEY (analyse_id) REFERENCES analyse_cur (id);