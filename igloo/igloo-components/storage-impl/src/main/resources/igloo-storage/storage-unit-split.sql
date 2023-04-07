SELECT s.* from StorageUnit s
JOIN Fichier f ON f.size IS NOT NULL and f.storageUnit_id = s.id
WHERE s.status = 'ALIVE'
GROUP BY s.id
-- max age or total file size condition
HAVING
  -- splitDuration - nanoseconds to seconds / use localtimestamp as now is timestamp with timezone
  (s.splitDuration IS NOT NULL AND EXTRACT(epoch FROM localtimestamp) - EXTRACT(epoch FROM s.creationDate) > s.splitDuration / 1000000000)
  OR
  (s.splitSize IS NOT NULL AND sum(f.size) > s.splitSize)
ORDER BY s.id ASC