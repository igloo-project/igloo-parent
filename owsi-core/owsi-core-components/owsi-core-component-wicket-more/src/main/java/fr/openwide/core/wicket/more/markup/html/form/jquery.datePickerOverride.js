(function(){
	var original_gotoToday = $.datepicker._gotoToday;
	
	$.datepicker._gotoToday = function(id) {
		var target = $(id),
		inst = this._getInst(target[0]);
		
		original_gotoToday.call(this, id);
		this._selectDate(id);
		
		this._curInst.input.blur();
	}
})();