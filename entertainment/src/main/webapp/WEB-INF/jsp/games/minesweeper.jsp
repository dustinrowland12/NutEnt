<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<head>
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/games/minesweeper.css"/>">
</head>

<body>
	<h2>Minesweeper</h2>
	<div id="minesweeper">
		<div class="loader" v-if="pageReady != true">
			<i class="fa fa-spinner fa-spin fa-3x fa-fw"></i>
			<span>Loading...</span>
		</div>
		<transition name="fade">
			<div v-if="pageReady">
				<transition name="slide">
					<div class="game-header" v-if="showInput">
						<h3>How to Play</h2>
						<p>
							Enter the dimensions you desire below. Dimensions must be between {{minDimension}} and {{maxDimension}}. Click New Game and a new Minesweeper game will be created.
							Please <a href="#instructions" @click.prevent="openInstructions">click here</a> for instructions on how to play Minesweeper.
						</p>
						<h3>Dimensions</h2>
						<div class="form-group">
							<label for="width">Width</label>
							<input type="input" class="form-control" name="width" v-model="inputWidth" @keydown.enter="newGame">
						</div>
						<div class="form-group">
							<label for="length">Length</label>
							<input type="input" class="form-control" name="length" v-model="inputLength" @keydown.enter="newGame">
						</div>
						<div class="form-group">
							<label for="mines">Mines</label>
							<input type="input" class="form-control" name="mines" v-model="inputMines" @keydown.enter="newGame">
						</div>
						<button class="btn btn-primary" @click="newGame">New Game</button>
						<div class="divider" v-if="showGame">
							<hr/>
						</div>
					</div>
				</transition>
				<transition name="fade-both">
					<div class="game-section" v-if="showGame">
						<div class="btn-toolbar justify-content-between">
							<button class="btn btn-dark" @click="toggleInputArea">{{showHideButtonText}}</button>
							<div class="btn-group" role="group" aria-label="Options">
								<button class="btn btn-dark" @click="adjustCellSize('subtract')" title="Smaller">&nbsp;<span class="fa fa-minus"></span>&nbsp;</button>
								<button class="btn btn-dark" @click="adjustCellSize('add')" title="Larger">&nbsp;<span class="fa fa-plus"></span>&nbsp;</button>
							</div>
						</div>
						<div id="mine-sweeper-outer-container" ref="gameContainer">
							<div class="game-container" id="mine-sweeper-container" ref="game" :style="{ 'padding': gamePaddingWithUnit, 'box-shadow': gameContainerBoxShadow }">
								<div class="minesweeper-tracker" id="mine-sweeper-timer" :style="{ 'padding': gameTrackerPaddingWithUnit, 'box-shadow': gameTrackerBoxShadow }">
									<span :style="{ 'font-size': gameTrackerSizeWithUnit }">{{formattedTime}}</span>
								</div>
								<div class="minesweeper-tracker" id="mine-sweeper-mines-remaining" :style="{ 'padding': gameTrackerPaddingWithUnit, 'box-shadow': gameTrackerBoxShadow }">
									<span :style="{ 'font-size': gameTrackerSizeWithUnit }">{{minesRemaining}}</span>
								</div>
								<div id="mine-sweeper" :style="{ 'grid-template-columns': gridTemplateColumns, 'grid-template-rows': gridTemplateRows }">
									<template v-for="cell in cells">
									<mine-cell :cell="cell" :cell-size="cellSize" :cell-size-unit="cellSizeUnit"></mine-cell>
									</template>
								</div>
							</div>
						</div>
						<transition name="fade">
							<template v-if="showWin">
								<div class="container center">
									<h3>Congratulations! You win!</h3>
								</div>
							</template>
							<template v-if="showLose">
								<div class="container center">
									<h3>Sorry, you LOSE!</h3>
								</div>
							</template>
						</transition>
					</div>
				</transition>
			</div>
		</transition>
		<!-- Modal -->
		<div class="modal fade" id="modalCompleteGame" tabindex="-1" role="dialog"
			aria-labelledby="modalCompleteGameLabel" aria-hidden="true">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="modalCompleteGameLabel">
						<template v-if="showWin">
							You Win!
						</template>
						<template v-if="showLose">
							You LOSE!
						</template>
						</h5>
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<template v-if="showWin">
							Congratulations! Would you like to play again?
						</template>
						<template v-if="showLose">
							You LOSE! Would you like to play again?
						</template>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary" data-dismiss="modal">No Thanks</button>
						<button type="button" class="btn btn-primary" @click="newGameFromModal">New Game</button>
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade" id="modalInstructions" tabindex="-1" role="dialog"
			aria-labelledby="modalInstructionsLabel" aria-hidden="true">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="modalInstructionsLabel">
							Instructions
						</h5>
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<p>
							The purpose of the game is to open all the cells of the board 
							which do not contain a mine. You lose if you set off a mine cell. Every non-mine 
							cell you open will tell you the total number of mines in the eight neighboring cells. 
							Once you are sure that a cell contains a mine, you can right-click to put a flag it 
							on it as a reminder. Once you have flagged all the mines around an open cell, you can 
							quickly open the remaining non-mine cells by shift-clicking on the cell. 
						</p>
						<p>
							To start a new game (abandoning the current one), click the New Game button. Happy mine hunting!
						</p>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- common functions -->
	<script type="text/javascript" src="<c:url value="/resources/js/common/utils.js"/>"></script>
	<!-- enum dependencies -->
	<script type="text/javascript" src="<c:url value="/resources/js/enums/direction.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/enums/mine-cell-status.js"/>"></script>
	<!-- model dependencies -->
	<script type="text/javascript" src="<c:url value="/resources/js/models/mine-cell.js"/>"></script>
	<!-- Vue components -->
	<script type="text/javascript" src="<c:url value="/resources/js/components/mine-sweeper.js"/>"></script>
</body>
</html>