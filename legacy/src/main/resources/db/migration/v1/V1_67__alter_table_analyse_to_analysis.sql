-- Renommer la table "analyse" en "analysis"
ALTER TABLE analyse_cur RENAME TO analysis;

-- Renommer la clé étrangère dans "estimated_payload"
ALTER TABLE estimated_payload RENAME COLUMN analyse_id TO analysis_id;

-- Mettre à jour la contrainte de clé étrangère
ALTER TABLE estimated_payload
    DROP CONSTRAINT estimated_payload_analyse_id_fkey,
    ADD CONSTRAINT estimated_payload_analysis_id_fkey FOREIGN KEY (analysis_id) REFERENCES analysis (id);