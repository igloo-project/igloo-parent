select
	s.id as "storageUnitId",
	s.type as "storageUnitType",
	fa.status as "failureStatus",
	count(distinct fa.id) as "count"
from
	StorageUnit s
join StorageConsistencyCheck c on
	c.storageUnit_id = s.id
join StorageFailure fa on
	c.id = fa.consistencyCheck_id
where
	fa.type = :missingEntityFailureType
group by
	s.id,
	s.type,
	fa.type,
	fa.status
order by
	s.id asc,
	s.type asc,
	fa.type asc,
	fa.status asc,
	"count" asc