var Wall = function (type) {
	var self = this;
	
	self.type = type;
	self.isEdge = false;
	self.isVisible = true;
	
	self.canDestroy = function () {
		return self.isEdge == false && self.isVisible == true;
	}
	
	self.destroy = function () {
		self.isVisible = false;
	}
}