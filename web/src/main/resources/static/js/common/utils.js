var getRndInteger = function(min, max) {
	return Math.floor(Math.random() * (max - min + 1) ) + min;
}

var focusFirstMessage = function () {
	var firstError = $(".error:visible").first();
	var name = firstError.attr("for");
	firstError.siblings(".form-input").find("[name='" + name + "']").focus();
}

var isInt = function (value) {
  return !isNaN(value) && 
		 parseInt(Number(value)) == value && 
		 !isNaN(parseInt(value, 10));
}

var baseUrl = "/entertainment/";