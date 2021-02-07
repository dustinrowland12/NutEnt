var MineCell = function (x, y) {
	var self = this;

	self.x = x;
	self.y = y;
	self.isMine = false;
	self.status = CellStatus.none;
	self.mineCount = 0;
	self.causedExplosion = false;
	
	self.getCoordinates = function () {
		return self.x + "," + self.y;
	}
	
	self.isRevealed = function () {
		return self.status == CellStatus.revealed;
	}
};