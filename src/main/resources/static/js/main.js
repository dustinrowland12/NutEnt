//global counter for anything that needs it; added for vue list functionality
var Counter = function() {
	var self = this;
	self.count = 0;
	//call this to get a number
	self.getNext = function() {
		return ++self.count;
	}
}

var AlertType = {
	INFO: "info",
	CONFIRMATION: "success",
	ALERT: "danger"
}

function addAlert(type, message) {
	var alerts = $("#systemMessages");
	var newAlert = $("<div class='alert alert-" + type + " alert-dismissible fade show' role='alert'></div>").html(message + ' <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>');
	alerts.append(newAlert);
}

function clearAlerts() {
	$("#systemMessages").empty();
}

function addAlertModal(id, type, message) {
	var alerts = $("#" + id + " .modal-alerts");
	var newAlert = $("<div class='alert alert-" + type + " alert-dismissible fade show' role='alert'></div>").html(message + ' <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>');
	alerts.append(newAlert);
}

function clearAlertsModal(id) {
	$("#" + id + " .modal-alerts").empty();
}

function handleAjaxErrorsModal(id, data) {
	if (data.responseJSON.errors != null) {
		//validation errors
		data.responseJSON.errors.forEach(function(error) {
			addAlertModal(id, AlertType.ALERT, error.defaultMessage);
		})
	}
	else {
		//custom errors
		addAlertModal(id, AlertType.ALERT, data.responseJSON.message);
	}
}