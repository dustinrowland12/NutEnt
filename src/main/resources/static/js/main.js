//global counter for anything that needs it; added for vue list functionality
var Counter = function() {
	var self = this;
	self.count = 0;
	//call this to get a number
	self.getNext = function() {
		return ++self.count;
	}
}

function modalAddError(id, error) {
	var alerts = $("#" + id + " .modal-alerts");
	var newAlert = $("<div class='alert alert-danger' role='alert'></div>").text(error);
	alerts.append(newAlert);
}
function modalClearErrors(id) {
	$("#" + id + " .modal-alerts").empty();
}