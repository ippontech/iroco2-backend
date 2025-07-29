UPDATE services SET availability = 'UNAVAILABLE';
ALTER TABLE services ALTER COLUMN availability SET NOT NULL;