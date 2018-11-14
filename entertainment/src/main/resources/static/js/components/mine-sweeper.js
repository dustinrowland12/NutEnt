Vue.component('mine-cell', {
	props: ['cell', 'cellSize', 'cellSizeUnit'],
	template: `
		<div class="mine-cell-container" :class="{ \'revealed\': cell.isRevealed(), \'exploded\': cell.causedExplosion }" 
					:style="{ \'grid-column\': cell.x, \'grid-row\': cell.y, \'box-shadow\': cellShadowStyle }"
					@click="$parent.revealCell(cell)" 
					@click.shift.exact="$parent.revealNeighborCells(cell, true)"
					@mouseup.right="$parent.flagCell(cell)"
					@contextmenu.prevent>
			<template v-if="cell.status == 'revealed'">
				<template v-if="cell.isMine == true">
					<span class="fa fa-bomb" :style="{ 'font-size': fontSize }"></span>
				</template>
				<template v-else-if="cell.mineCount > 0">
					<span :style="{ 'font-size': fontSize, 'color': mineCountColor }">{{cell.mineCount}}</span>
				</template>
			</template>
			<template v-else-if="cell.status == 'flagged'">
				<span class="fa fa-flag" :style="{ 'font-size': fontSize, 'color': 'darkred' }"></span>
			</template>
			<template v-else-if="cell.status == 'questioned'">
				<span class="fa fa-question" :style="{ 'font-size': fontSize, 'color': 'midnightblue' }"></span>
			</template>	
		</div>
	`,
	computed: {
		fontSize: function () {
			return (this.cellSize * .6) + this.cellSizeUnit;
		},
		cellShadow: function () {
			var style = (this.cellSize * .025) + this.cellSizeUnit + " " + (this.cellSize * .025) + this.cellSizeUnit + " " + (this.cellSize * 0) + this.cellSizeUnit + " #fff inset, "
				+ (this.cellSize * -.025) + this.cellSizeUnit + " " + (this.cellSize * -.025) + this.cellSizeUnit + " " + (this.cellSize * 0) + this.cellSizeUnit + " #222333 inset";
			return style;
		},
		cellShadowStyle: function () {
			return this.cell.isRevealed() ? "none" : this.cellShadow;
		},
		mineCountColor: function () {
			var color = "black";
			switch (this.cell.mineCount) {
				case 1:
					color = "blue";
					break;
				case 2:
					color = "green";
					break;
				case 3:
					color = "red";
					break;
				case 4:
					color = "darkblue";
					break;
				case 5:
					color = "darkred";
					break;
				case 6:
					color = "#018184";
					break;
				case 7:
					color = "black";
					break;
				case 8:
					color = "grey";
					break;					
			}
			return color;
		}
	}
});

//main component
var vm = new Vue({
	el: '#minesweeper',
	data: {
		cells: [],
		inputLength: 5,
		inputWidth: 5,
		inputMines: 5,
		length: 0,
		width: 0,
		mines: 0,
		minDimension: 5,
		maxDimension: 30,
		cellSize: 2.5,
		cellSizeUnit: "em",
		minCellSize: 1,
		maxCellSize: 5,
		gameContainerWidth: 0,
		showInput: true,
		showGame: false,
		pageReady: false,
		complete: false,
		showWin: false,
		showLose: false,
		time: 0,
		setIntervalFunction: null
	},
	mounted: function () {
		var self = this;
		self.pageReady = true;
		self.$nextTick(function () {
			window.addEventListener('resize', self.updateGameContainerWidth);
		});
	},
	watch: {
		cellsRemaining: function (val) {
			if (val == false && this.cells.length > 0 && !this.complete) {
				this.win();
			}
		},
		mineRevealed: function (val) {
			if (val == true && this.cells.length > 0 && !this.complete) {
				this.lose();
			}
		},
		gameContainerWidth: function () {
			this.autoAdjustCellSize();
		},
		time: function (val) {
			if (val == this.maxTime) {
				this.stopTimer();
			}
		}
	},
	methods: {
		openInstructions: function () {
			$("#modalInstructions").modal();
		},
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
			this.showInput = !this.showInput;
		},
		newGame: function () {
			this.validateInputs();
		},
		newGameFromModal: function () {
			$("#modalCompleteGame").modal('hide');
			this.newGame();
		},
		validateInputs: function () {
			var self = this;
			/*
			self.$validator.validateAll().then(function(result) {
				if (result) {
					self.initialize();
				}
				else {
					self.$nextTick(focusFirstMessage);
				}
			});*/
			self.initialize();
		},
		initialize: function () {
			var self = this;
			self.resetGame();
			self.$nextTick(function () {
				self.createGame();
				self.showInput = false;
				self.showGame = true;
				self.updateGameContainerWidth();
			});
		},
		startTimer: function() {
			var self = this;
			
			self.setIntervalFunction = setInterval(function () {
				self.time++;
			}, 1000);
		},
		stopTimer: function () {
			var self = this;
			clearInterval(self.setIntervalFunction);
		},
		resetGame: function () {
			this.showGame = false;
			this.cells = [];
			this.complete = false;
			this.showWin = false;
			this.showLose = false;
			this.time = 0;
			this.stopTimer();
			this.setIntervalFunction = null;
		},
		createGame: function () {
			this.width = this.inputWidth;
			this.length = this.inputLength;
			this.mines = this.inputMines;
			
			var totalCells = 0;
			
			this.createCells();
			this.setMines();
			this.setMineCounts();
		},
		createCells: function () {
			while (this.totalNumberCells < this.gameArea) {
				var x = (this.totalNumberCells % this.width) + 1;
				var y = 1 + Math.floor(this.totalNumberCells / this.width);
				var cell = new MineCell(x,y);
				//this.setCellEdges(cell);
				this.cells.push(cell);
			}
		},
		setMines: function () {
			var self = this;
			
			for (i = 0; i < self.mines; i++) {
				var cell = this.pickRandomSafeCell();
				cell.isMine = true;
			}
		},
		pickRandomSafeCell: function () {
			var index = getRndInteger(1, this.totalNumberSafeCells) - 1;
			var cell = this.safeCells[index];
			return cell;
		},
		flagCell: function (cell) {
			switch (cell.status) {
				case CellStatus.none:
					cell.status = CellStatus.flagged;
					break;
				case CellStatus.flagged:
					cell.status = CellStatus.questioned;
					break;
				case CellStatus.questioned:
					cell.status = CellStatus.none;
					break;
			}
			return false;
		},
		revealCell: function (cell) {
			if (cell.status == CellStatus.revealed) {
				return;
			}
			
			//reveal the cell
			cell.status = CellStatus.revealed;
			
			//check if user hit a mine
			if (cell.isMine == true) {
				cell.causedExplosion = true;
				return;
			}
			
			if (this.setIntervalFunction == null) {
				this.startTimer();
			}
			
			this.revealNeighborCells(cell);
		},
		revealNeighborCells: function (cell, isShiftClicked) {
			var self = this;
			
			var neighborCells = self.getNeighborCells(cell);
			
			if (cell.mineCount == 0) {
				_.each(neighborCells, function(neighborCell) {
					self.revealCell(neighborCell);
				});
			}
			else if (isShiftClicked) {
				var flaggedNeighborCells = _.filter(neighborCells, function (neighborCell) {
					return neighborCell.status == CellStatus.flagged;
				});
				
				if (flaggedNeighborCells.length < cell.mineCount) {
					//user hasn't flagged all mines around cell, don't reveal neighbor cells
					return;
				}
				
				_.each(neighborCells, function(neighborCell) {
					if (neighborCell.status != CellStatus.flagged) {
						self.revealCell(neighborCell);
					}
				});
			}			
			
		},
		revealAllCells: function () {
			_.each(this.cells, function(cell) {
				cell.status = CellStatus.revealed;
			});
		},
		getNeighborCells: function (cell) {
			var self = this;
			
			var x = cell.x;
			var y = cell.y;
						
			var neighborCells = [];
			
			for (var property in Direction) {
				if (Direction.hasOwnProperty(property)) {
					neighborCells.push(self.getNeighborCell(cell, property));
				}
			}
			
			neighborCells = _.filter(neighborCells, function(neighborCell) {
				return neighborCell != null;
			});
		
			return neighborCells;
		},
		getNeighborCell: function(cell, direction) {
			var self = this;
			
			var x = cell.x;
			var y = cell.y;
			
			neighbor = null;
			
			switch(direction) {
				case Direction.up:
					neighbor = _.find(self.cells, function (neighborCell) { return neighborCell.x == (x) && neighborCell.y == (y - 1)});
					break;
				case Direction.down:
					neighbor = _.find(self.cells, function (neighborCell) { return neighborCell.x == (x) && neighborCell.y == (y + 1)});
					break;
				case Direction.left:
					neighbor = _.find(self.cells, function (neighborCell) { return neighborCell.x == (x - 1) && neighborCell.y == (y)});
					break;
				case Direction.right:
					neighbor = _.find(self.cells, function (neighborCell) { return neighborCell.x == (x + 1) && neighborCell.y == (y)});
					break;
				case Direction.upLeft:
					neighbor = _.find(self.cells, function (neighborCell) { return neighborCell.x == (x - 1) && neighborCell.y == (y - 1)});
					break;
				case Direction.upRight:
					neighbor = _.find(self.cells, function (neighborCell) { return neighborCell.x == (x + 1) && neighborCell.y == (y - 1)});
					break;
				case Direction.downLeft:
					neighbor = _.find(self.cells, function (neighborCell) { return neighborCell.x == (x - 1) && neighborCell.y == (y + 1)});
					break;
				case Direction.downRight:
					neighbor = _.find(self.cells, function (neighborCell) { return neighborCell.x == (x + 1) && neighborCell.y == (y + 1)});
					break;
			}
			
			return neighbor;
		},
		setMineCounts: function() {
			var self = this;
			
			_.each(self.cells, function (cell) {
				var mineCount = 0;
				var neighbors = self.getNeighborCells(cell);
				
				_.each(neighbors, function (neighbor) {
					if (neighbor.isMine == true) {
						mineCount ++;
					}
				});
				
				cell.mineCount = mineCount;
			});
		},
		win: function () {
			var self = this;
			
			self.stopTimer();
			
			self.complete = true;
			
			self.revealAllCells();
			
			self.showWin = true;

			self.$nextTick(function () {
				$("#modalCompleteGame").modal();
			});
		},
		lose: function () {
			var self = this;
			
			self.stopTimer();
			
			self.complete = true;
			
			self.revealAllCells();
			
			self.showLose = true;
			
			self.$nextTick(function () {
				$("#modalCompleteGame").modal();
			});
		}
	},
	computed: {
		gameArea: function () {
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
		safeCells: function () {
			return _.filter(this.cells, function (cell) {
				return cell.isMine == false;
			});
		},
		mineCells: function () {
			return _.filter(this.cells, function (cell) {
				return cell.isMine == true;
			});
		},
		totalNumberSafeCells: function () {
			return this.safeCells.length;
		},
		cellsRemaining: function () {
			return _.some(this.cells, function (cell) {
				//{ 'isMine': false, 'isRevealed': false });
				return cell.isMine == false && cell.isRevealed() == false;
			});
		},
		mineRevealed: function () {
			var mineRevealed = _.find(this.cells, function (cell) {
				return cell.isMine == true && cell.isRevealed() == true;
			});

			return mineRevealed != null ? true : false;
		},
		showHideButtonText: function () {
			return this.showInput ? "Only Show Game" : "Show All";
		},
		cellSizeWithUnit: function () {
			return this.cellSize + this.cellSizeUnit;
		},
		gamePaddingWithUnit: function () {
			return (this.cellSize / 2) + this.cellSizeUnit;
		},
		gameTrackerSizeWithUnit: function () {
			return (this.cellSize / 2) + this.cellSizeUnit;
		},
		gameTrackerPaddingWithUnit: function () {
			return (this.cellSize / 4) + this.cellSizeUnit;
		},
		gameTrackerBoxShadow: function () {
			var style = (this.cellSize * .025) + this.cellSizeUnit + " " + (this.cellSize * .025) + this.cellSizeUnit + " " + (this.cellSize * 0) + this.cellSizeUnit + " #fff inset, "
				+ (this.cellSize * -.05) + this.cellSizeUnit + " " + (this.cellSize * -.05) + this.cellSizeUnit + " " + (this.cellSize * 0) + this.cellSizeUnit + " #222333 inset";
			return style;
		},
		maxMines: function () {
			var inputArea = isInt(this.inputLength) && isInt(this.inputWidth) ? this.inputLength * this.inputWidth : this.maxDimension * this.maxDimension;
			
			return inputArea - 1;
		},
		minMines: function () {
			return this.minDimension;
		},
		minesRemaining: function () {
			var minesMarked = _.filter(this.cells, function (cell) {
				return cell.status == CellStatus.flagged;
			});
			
			return minesMarked != null ? this.mines - minesMarked.length : this.mines;
		},
		formattedTime: function () {
			return moment().hour(0).minute(0).second(this.time).format('H:mm:ss');
		},
		maxTime: function () {
			var maxTime = moment().hour(9).minute(59).second(59);
			return maxTime.hour() * 60 * 60 + maxTime.minute() * 60 + maxTime.second();
		}
	}
});