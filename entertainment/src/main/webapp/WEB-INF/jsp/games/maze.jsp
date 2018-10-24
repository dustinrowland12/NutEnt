<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<body>
	<h2>Maze</h2>
	<div id="vue-maze">
		<div class="loader" v-if="showAll != true">
			<i class="fas fa-spinner fa-spin fa-3x fa-fw"></i>
			<span>Loading...</span>
		</div>
		<transition name="fade">
		<template v-if="showAll">
		<div class="box">
			<transition name="slide">
			<template v-if="showMazeAreaInput">
			<div class="form-area">
				<h2>Instructions</h2>
				<p>
					Enter the maze dimensions you desire below. Dimensions must be between {{minDimension}} and {{maxDimension}}. Click Create New Maze and a maze will be created. The ball will start flashing to indicate
					you can move the ball with the arrow keys on your keyboard. If you click away from the ball, it will stop flashing, and you will not be able to continue until you click
					on the ball to return focus to it. You can generate a new maze any time you wish with the Create New Maze button. 
				</p>
				<h2>Maze Dimensions</h2>
				<div class="form-input-area">
					<div class="form-label required">
						<label for="width">Width</label>
					</div>
					<div class="form-input">
						<input name="width" for="inputWidth" v-model="inputWidth"
							@keydown.enter="validateCoordinates">
					</div>
				</div>
				<div class="form-input-area">
					<div class="form-label required">
						<label for="length">Length</label>
					</div>
					<div class="form-input">
						<input name="length" v-model="inputLength" @keydown.enter="validateCoordinates">
					</div>
				</div>
				<div class="button-section spacing">
					<button class="form-button" @click="validateCoordinates">Create New Maze</button>
				</div>
				<template v-if="showMaze">
				<div class="divider">
					<hr/>
				</div>
				</template>
			</div>
			</template>
			</transition>
			<template v-if="showMaze">
			<div class="maze-section">
				<div class="button-section split spacing">
					<button class="form-button" @click="toggleInputArea">{{showHideButtonText}}</button>
					<template v-if="!complete">
					<button class="form-button" @click="transposeMaze">Transpose Maze</button>
					</template>
					<div class="button-section">
						<div class="button-label">Maze Size:</div>
						<div>
							<button class="form-button" @click="adjustCellSize('subtract')">&nbsp;<span class="fas fa-minus"></span>&nbsp;</button>
							<button class="form-button" @click="adjustCellSize('add')">&nbsp;<span class="fas fa-plus"></span>&nbsp;</button>
						</div>
					</div>
				</div>
				<div class="container center spacing" ref="mazeContainer">
					<div class="maze" id="maze" ref="maze" :style="{ 'grid-template-columns': gridTemplateColumns, 'grid-template-rows': gridTemplateRows, 'border-width': mazeBorderWidth }">
						<template v-for="cell in cells">
						<grid-cell :cell="cell" :cursor="cursor"></grid-cell>
						</template>
						<button class="cursor" :class="{active: cursor.active, complete: complete}" @focus="activateCursor(true)" @focusout="activateCursor(false)" ref="cursor"
							:style="{ 'grid-column': cursor.x, 'grid-row': cursor.y }"
							@keydown.up.prevent.stop="moveCursor('top')" @keydown.left.prevent.stop="moveCursor('left')" @keydown.right.prevent.stop="moveCursor('right')" @keydown.down.prevent.stop="moveCursor('bottom')" >
							<transition name="fade">
							<template v-if="complete">
							<span class="fas fa-smile-o smile" :style="{ 'font-size': smileySizeWithUnit }" aria-hidden="true"></span>
							</template>
							</transition>
						</button>
					</div>
				</div>
				<transition name="fade">
				<template v-if="complete">
				<div class="container center">
					<h3>Congratulations! You win!</h3>
				</div>
				</template>
				</transition>
			</div>
			</template>
		</div>
		</template>
		</transition>
		<!-- Modal -->
		<div class="modal fade" id="modalCompleteGame" tabindex="-1" role="dialog"
			aria-labelledby="modalCompleteGameLabel" aria-hidden="true">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="modalCompleteGameLabel">You Win!</h5>
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">Congratulations! Would you like to play again?</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary" data-dismiss="modal">No Thanks</button>
						<button type="button" class="btn btn-primary" @click="newGameFromModal">New Maze</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- common functions -->
	<script type="text/javascript" src="<c:url value="/resources/js/common/utils.js"/>"></script>
	<!-- enum dependencies -->
	<script type="text/javascript" src="<c:url value="/resources/js/enums/maze-wall-type.js"/>"></script>
	<!-- model dependencies -->
	<script type="text/javascript" src="<c:url value="/resources/js/models/maze-cell.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/models/maze-cursor.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/models/maze-wall.js"/>"></script>
	<!-- Vue components -->
	<script type="text/javascript" src="<c:url value="/resources/js/components/maze.js"/>"></script>
	
</body>