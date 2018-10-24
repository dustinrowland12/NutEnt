var Cell = function (x, y) {
	var self = this;

	self.visited = false;
	self.isStart = false;
	self.isEnd = false;
	self.x = x;
	self.y = y;
	self.top = new Wall(WallType.top);
	self.bottom = new Wall(WallType.bottom);
	self.left = new Wall(WallType.left);
	self.right = new Wall(WallType.right);
	
	self.getCoordinates = function () {
		return self.x + "," + self.y;
	}
};