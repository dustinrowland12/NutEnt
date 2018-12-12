//main component
var vm = new Vue({
	el: '#loginForm',
	data: {
		username: "",
		password: "",
		success: false,
		failure: false
		
	},
	mounted: function () {
		var self = this;
	},
	methods: {
		submit: function () {
			var validated = this.validateInputs();
			
			if (validated) {
				this.login();
			}
		},
		validateInputs: function () {
			var self = this;
			return true;
		},
		login: function () {
			var self = this;
			$.ajax({
				url: "login",
				method: "post",
				data: {
					username: self.username,
					password: self.password
				},
				dataType: "json"
			}).done(function(data) {
				alert(data);
			});
		}
	}
});