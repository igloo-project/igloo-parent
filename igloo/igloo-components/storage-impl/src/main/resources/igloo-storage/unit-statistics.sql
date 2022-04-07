select
	s.id as "storageUnitId",
	s.type as "storageUnitType",
	f.type as "fichierType",
	f.status as "fichierStatus",
	count(distinct f.id) as "count",
	sum(f.size) as "size"
from
	StorageUnit s
join Fichier f on
	f.storageUnit_id = s.id
group by
	s.id,
	s.type,
	f.type,
	f.status
order by
	s.id asc,
	s.type asc,
	f.type asc,
	f.status asc,
	"count" asc,
	size asc