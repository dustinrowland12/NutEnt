Vue.component('grid-cell', {
	props: ['cell'],
	template: '<div class="cell-container" :class="borderClasses" :style="{ \'grid-column\': cell.x, \'grid-row\': cell.y }"></div>',
	computed: {
		borderClasses: function () {
			return {
				'cell-top': this.cell.top.isVisible && !this.cell.top.isEdge, 
				'cell-right': this.cell.right.isVisible && !this.cell.right.isEdge, 
				'cell-bottom': this.cell.bottom.isVisible && !this.cell.bottom.isEdge, 
				'cell-left': this.cell.left.isVisible && !this.cell.left.isEdge,
				'cell-top-edge': this.cell.top.isVisible && this.cell.top.isEdge, 
				'cell-right-edge': this.cell.right.isVisible && this.cell.right.isEdge, 
				'cell-bottom-edge': this.cell.bottom.isVisible && this.cell.bottom.isEdge, 
				'cell-left-edge': this.cell.left.isVisible && this.cell.left.isEdge
			}
		}
	}
});

//main component
var vm = new Vue({
	el: '#vue-maze',
	data: {
		cells: [],
		cellStack: [],
		inputLength: 5,
		inputWidth: 5,
		length: 0,
		width: 0,
		minDimension: 5,
		maxDimension: 20,
		cellSize: 3,
		cellSizeUnit: "em",
		minCellSize: 1,
		maxCellSize: 5,
		gameContainerWidth: 0,
		cursor: new Cursor(),
		cursorIcons: [
			{ className: 'fas fa-fighter-jet', rotate: true },
			{ className: 'fas fa-dragon', rotate: true },
			{ className: 'fas fa-chess-rook', rotate: false },
			{ className: 'fas fa-dog', rotate: true },
			{ className: 'fas fa-truck-monster', rotate: true },
			{ className: 'fas fa-ghost', rotate: false },
			{ className: 'fas fa-sun fa-spin', rotate: false}			
		],
		cursorIcon: "",
		cursorDirection: WallType.right,
		showMazeAreaInput: true,
		showMaze: false,
		showAll: false,
	},
	mounted: function () {
		var self = this;
		self.showAll = true;
		self.cursorIcon = self.cursorIcons[Math.floor(Math.random()*self.cursorIcons.length)];
		self.$nextTick(function () {
			window.addEventListener('resize', self.updateGameContainerWidth);
		});
	},
	computed: {
		mazeArea: function () {
			return isInt(this.length) && isInt(this.width) ? this.length * this.width : 0;
		},
		totalNumberCells: function () {
			return this.cells.length;
		},
		gridTemplateColumns: function () {
			return "repeat(" + this.width + ", " + this.cellSizeWithUnit + ")";
		},
		gridTemplateRows: function () {
			return "repeat(" + this.length +  ", " + this.cellSizeWithUnit + ")";
		},
		startCell: function () {
			return _.find(this.cells, function(cell) { 
				return cell.isStart == true 
			});
		},
		endCell: function () {
			return _.find(this.cells, function(cell) { 
				return cell.isEnd == true 
			});
		},
		cellsRemaining: function () {
			return _.some(this.cells, { 'visited': false });
		},
		complete: function () {
			if (this.cursor == null || this.endCell == null) {
				return false;
			}
			return this.cursor.x == this.endCell.x && this.cursor.y == this.endCell.y;
		},
		showHideButtonText: function () {
			return this.showMazeAreaInput ? "Only Show Maze" : "Show All";
		},
		cellSizeWithUnit: function () {
			return this.cellSize + this.cellSizeUnit;
		},
		cursorSizeWithUnit: function () {
			return (this.cellSize * .65) + this.cellSizeUnit;
		},
		mazeBorderWidth: function () {
			return (this.cellSize / 2) + this.cellSizeUnit;
		},
		cursorButtonClasses: function() {
			return {
				active: this.cursor.active, 
				complete: this.complete
			};
		},
		cursorDirectionClass: function() {
			var className;
			
			if (this.cursorIcon.rotate) {
				switch (this.cursorDirection) {
					case WallType.bottom:
						className = 'fa-rotate-90';
						break;
					case WallType.left:
						className = 'fa-flip-horizontal';
						break;
					case WallType.top:
						className = 'fa-rotate-270';
						break;
				}
			}
			
			return className;
		}
	},
	watch: {
		complete: function (val) {
			var self = this;
			if (val == true) {
				this.cursor.movable = false;
				this.cursor.active = false;
				this.$nextTick(function () {
					$("#modalCompleteGame").modal();
				});
			}
		},
		gameContainerWidth: function () {
			this.autoAdjustCellSize();
		}
	},
	methods: {
		adjustCellSize: function (value) {
			if (value == 'add' && this.cellSize < this.maxCellSize) {
				this.cellSize = this.cellSize + .5;
			}
			if (value == 'subtract' && this.cellSize > this.minCellSize) {
				this.cellSize = this.cellSize - .5;
			}
			
			this.updateGameContainerWidth();
		},
		autoAdjustCellSize: function () {
			var gameWidth = this.$refs.game != null ? this.$refs.game.offsetWidth : 0;
			
			if (this.gameContainerWidth <= gameWidth && this.cellSize > this.minCellSize) {
				this.adjustCellSize("subtract");
			}
		},
		updateGameContainerWidth: function () {
			var self = this;
			self.$nextTick(function () {
				this.gameContainerWidth = this.$refs.gameContainer != null ? $(this.$refs.gameContainer).width() : 0;
			});
		},
		toggleInputArea: function () {
			this.showMazeAreaInput = !this.showMazeAreaInput;
		},
		validateCoordinates: function () {
			var self = this;
			/*
			self.$validator.validateAll().then(function(result) {
				if (result) {
					self.initialize();
				}
				else {
					self.$nextTick(focusFirstMessage);
				}
			});
			*/
			self.initialize();
		},
		newGameFromModal: function () {
			var self = this;
			$("#modalCompleteGame").modal('hide');
			self.validateCoordinates();
		},
		initialize: function () {
			this.resetMaze();
			this.createMaze();
			this.initializeCursor();
			this.showMaze = true;
			this.showMazeAreaInput = false;
			this.updateGameContainerWidth();
			this.$nextTick(this.setCursorFocus);
		},
		setCursorFocus: function() {
			this.$refs.cursor.focus();
		},
		resetMaze: function () {
			this.cells = [];
			this.cellStack = [];
			this.cursor = new Cursor();
			this.setCursorDirection(WallType.right);
		},
		createMaze: function () {
			this.width = this.inputWidth;
			this.length = this.inputLength;
			
			var totalCells = 0;
			
			//create start and end cells
			var startCell = new Cell(1,1);
			startCell.isStart = true;
			this.setCellEdges(startCell);
			startCell.left.destroy();
			
			var endCell = new Cell(this.width, this.length);
			endCell.isEnd = true;
			this.setCellEdges(endCell);
			endCell.right.destroy();
			
			//create the remaining cells
			while (this.totalNumberCells < this.mazeArea) {
				switch (this.totalNumberCells) {
					case 0:
						this.cells.push(startCell);
						break;
					case (this.mazeArea - 1):
						this.cells.push(endCell);
						break;
					default:
						var x = (this.totalNumberCells % this.width) + 1;
						var y = 1 + Math.floor(this.totalNumberCells / this.width);
						var cell = new Cell(x,y);
						this.setCellEdges(cell);
						this.cells.push(cell);
						break;
				}
			}
			
			this.depthFirstAlgorithm();
			
		},
		depthFirstAlgorithm: function () {
			/*
				Algorithm Break Down: 
				1. Choose starting point
				2. Mark cell visited
				3. Check if any walls can be knocked down
					- wall must be visible, cannot be edge, cannot be in cell already visited
					- if no walls available:
						a. pop the last cell from the stack and store it as current cell
						b. repeat steps 1-3 with the new current cell
					- if a wall is available, continue
				4. Pick random wall to knock down
				5. knock down wall
				6. knock down adjacent cell's wall
				7. Add current cell to stack
				8. traverse through the knocked down wall into the new cell
			*/
			
			//start with startCell
			var activeCell = this.startCell;
			activeCell.visited = true;
			
			var iterations = 1;
			
			while (this.cellsRemaining) {					
				//console.log(iterations + " start: " + activeCell.getCoordinates());
				if (this.canKnockDownAnyWall(activeCell)) {
					var neighborCell = this.knockDownWall(activeCell);
					this.cellStack.push(activeCell);
					activeCell = neighborCell;
					activeCell.visited = true;
				}
				else {
					activeCell = this.cellStack.pop();
					if (activeCell != null){
						//console.log(iterations + " after pop: " + activeCell.getCoordinates());
					}
					else {
						//console.log(iterations + " Done: all cells popped");
						break;
					}
				}
				
				iterations++;
			}
			
		},
		setCellEdges: function (cell) {
			if (cell.x == 1) {
				cell.left.isEdge = true;
			}
			if (cell.y == 1) {
				cell.top.isEdge = true;
			}
			if (cell.x == this.width) {
				cell.right.isEdge = true;
			}
			if (cell.y == this.length) {
				cell.bottom.isEdge = true;
			}
		},
		initializeCursor: function () {				
			this.cursor.x = this.startCell.x;
			this.cursor.y = this.startCell.y;
		},
		canKnockDownAnyWall: function (cell) {
			return (cell.left.canDestroy() && this.canVisitNeighborCell(this.getNeighborCell(cell, cell.left))) ||
				(cell.right.canDestroy() && this.canVisitNeighborCell(this.getNeighborCell(cell, cell.right))) ||
				(cell.bottom.canDestroy() && this.canVisitNeighborCell(this.getNeighborCell(cell, cell.bottom))) ||
				(cell.top.canDestroy() && this.canVisitNeighborCell(this.getNeighborCell(cell, cell.top)));
		},
		canKnockDownWall: function (cell, wall) {
			return wall.canDestroy() && this.canVisitNeighborCell(this.getNeighborCell(cell, wall));
		},
		getCell: function (x,y) {
			return _.find(this.cells, function(cell) {
					return cell.x == x && cell.y == y;
				});
		},
		getNeighborCell: function (cell, wall) {
			var neighborX = null;
			var neighborY = null;
			
			switch(wall.type) {
				case WallType.top:
					if (cell.y <= 1) {
						return null;
					}
					neighborX = cell.x;
					neighborY = cell.y - 1;
					break;
				case WallType.right:
					if (cell.x >= this.width) {
						return false;
					}
					neighborX = cell.x + 1;
					neighborY = cell.y;
					break;
				case WallType.bottom:
					if (cell.y >= this.length) {
						return false;
					}
					neighborX = cell.x;
					neighborY = cell.y + 1;
					break;
				case WallType.left:
					if (cell.y <= 1) {
						return false;
					}
					neighborX = cell.x - 1;
					neighborY = cell.y;
					break;
			}
			
			return this.getCell(neighborX, neighborY);
		},
		canVisitNeighborCell: function (neighborCell) {
			if (neighborCell == null) {
				return false;
			}
			return neighborCell.visited == false;
		},
		knockDownWall: function (cell) {
			var wallKnockedDown = false;
			var neighborCell = null;
			
			while (wallKnockedDown == false) {
				var wall = this.pickRandomWall(cell);
				if (this.canKnockDownWall(cell,wall)) {
					wall.destroy();
					neighborCell = this.knockDownNeighborWall(cell,wall);
					wallKnockedDown = true;
				}
			}
			
			return neighborCell;
		},
		knockDownNeighborWall: function (cell, wall) {
			neighborCell = this.getNeighborCell(cell,wall);
			switch(wall.type) {
				case WallType.top:
					neighborCell.bottom.destroy();
					break;
				case WallType.bottom:
					neighborCell.top.destroy();
					break;
				case WallType.left:
					neighborCell.right.destroy();
					break;
				case WallType.right:
					neighborCell.left.destroy();
					break;
			}
			return neighborCell;
		},
		pickRandomWall: function (cell) {
			var int = getRndInteger(1, 4);
			var wall = null;
			switch(int) {
				case 1:
					wall = cell.top;
					break;
				case 2:
					wall = cell.right;
					break;
				case 3:
					wall = cell.bottom;
					break;
				case 4:
					wall = cell.left;
					break;
			}
			return wall;
		},
		activateCursor: function (active) {
			if (this.cursor.movable) {
				this.cursor.active = active;
			}
		},
		moveCursor: function (wallType) {
			if (this.cursor.movable) {
				var cell = this.getCell(this.cursor.x, this.cursor.y);
				this.setCursorDirection(wallType);
				switch(wallType) {
					case WallType.right:
						if (!cell.right.isVisible && !cell.right.isEdge) {
							this.cursor.x = this.cursor.x + 1;
						}
						break;
					case WallType.left:
						if (!cell.left.isVisible && !cell.left.isEdge) {
							this.cursor.x = this.cursor.x - 1;
						}
						break;
					case WallType.top:
						if (!cell.top.isVisible && !cell.top.isEdge) {
							this.cursor.y = this.cursor.y - 1;
						}
						break;
					case WallType.bottom:
						if (!cell.bottom.isVisible && !cell.bottom.isEdge) {
							this.cursor.y = this.cursor.y + 1;
						}
						break;
				}
			}
		},
		transposeMaze: function() {
			var self = this;
			
			var inputLength = self.inputLength;
			var inputWidth = self.inputWidth;
			var length = self.length;
			var width = self.width;
			
			//transpose items before changing width/length
			self.cells = self.getTransposedCells(self.cells);
			self.cursor = self.getTransposedCursor(self.cursor);
			
			//change maze and input width/length
			self.inputLength = inputWidth;
			self.inputWidth = inputLength;
			self.length = width;
			self.width = length;
			
			//update cursor direction
			switch(self.cursorDirection) {
				case WallType.left:
					this.setCursorDirection(WallType.top);
					break;
				case WallType.top:
					this.setCursorDirection(WallType.right);
					break;
				case WallType.right:
					this.setCursorDirection(WallType.bottom);
					break;
				case WallType.bottom:
					this.setCursorDirection(WallType.left);
					break;
			}
			
			//set focus to cursor
			self.updateGameContainerWidth();
			self.$nextTick(function(){self.$refs.cursor.focus();});
		},
		setCursorDirection: function(direction) {
			this.cursorDirection = direction;
		},
		updateCursorIcon: function(icon) {
			this.cursorIcon = icon;
			this.$nextTick(this.setCursorFocus);
		},
		getTransposedCells: function (cells) {
			var self = this;
			var newCellArray = [];
			_.each(cells, function (cell) {
				var newCell = self.getTransposedCell(cell);
				newCellArray.push(newCell);
			});
			return newCellArray;
		},
		getTransposedCell: function (cell) {
			var self = this;
			var newX = self.length - cell.y + 1;
			var newY = cell.x;
			
			var newCell = new Cell(newX, newY);
			
			newCell.top = cell.left;
			newCell.left = cell.bottom;
			newCell.right = cell.top;
			newCell. bottom = cell.right;
			
			newCell.visited = cell.visited;
			newCell.isStart = cell.isStart;
			newCell.isEnd = cell.isEnd;
			
			return newCell;
		},
		getTransposedCursor: function (cursor) {
			var self = this;
			var newCursor = new Cursor();
			
			var newX = self.length - cursor.y + 1;
			var newY = cursor.x;
			
			newCursor.x = newX;
			newCursor.y = newY;
			newCursor.active = cursor.active;
			newCursor.movable = cursor.movable;
			
			return newCursor;
		}
	}
});