function onSortableUpdate(sortableList){
	var itemsIds = sortableList.sortable('toArray');
	var itemsCount = itemsIds.length;
	for (var i=0; i<itemsCount; i++) {
		var itemId = itemsIds[i];
		$("#" + itemId + " input.position").val(i + 1);
	}
}