//global counter for anything that needs it; added for vue list functionality
var Counter = function() {
	var self = this;
	self.count = 0;
	//call this to get a number
	self.getNext = function() {
		return ++self.count;
	}
}