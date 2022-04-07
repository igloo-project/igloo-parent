select
	s.id as "storageUnitId",
	s.type as "storageUnitType",
	f.type as "fichierType",
	f.status as "fichierStatus",
	fa.type as "failureType",
	fa.status as "failureStatus",
	count(distinct f.id) as "count",
	sum(f.size) as "size"
from
	StorageUnit s
join Fichier f on
	f.storageUnit_id = s.id
join StorageFailure fa on
	fa.fichier_id = f.id
where
	fa.fichier_id is not null
group by
	s.id,
	s.type,
	f.type,
	f.status,
	fa.type,
	fa.status
order by
	s.id asc,
	s.type asc,
	f.type asc,
	f.status asc,
	fa.type asc,
	fa.status asc,
	"count" asc,
	size asc