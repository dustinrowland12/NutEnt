//helper classes
var PaginatedTableColumn = function(displayName, propertyName) {
	this.displayName = displayName != null ? displayName : "Column Name";
	this.propertyName = propertyName != null ? propertyName : "propertyName";
}

var PaginatedTableAction = function(displayName, action) {
	this.displayName = displayName != null ? displayName : "Action";
	this.action = action != null ? action : function() { return true; };
}

var PaginatedTableActionColumn = function(displayName, actions) {
	this.displayName = displayName != null ? displayName : "";
	this.actions = actions != null ? actions : []; //add PaginatedTableAction objects to this
}

//vue components
Vue.component("paginated-table-row-actions", {
	template: `
    	<div class="dropdown">
	  	    <button class="btn btn-sm dropdown-toggle no-icon" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><i class="fas fa-ellipsis-v"></i></button>
	  	    <div class="dropdown-menu dropdown-menu-right">
	  	  		<button type="button" class="dropdown-item btn" v-for="action in actions" @click="action.action(rowData)">{{action.displayName}}</button>
	  	    </div>
	  	</div> 
  	`,
  	props: ['actions', 'row'],
  	computed: {
  		rowData: function() {
  			return /*JSON.stringify(this.row)*/ this.row;
  		}
  	}
});

Vue.component("paginated-table-pagination", {
	template: `
		<nav aria-label="Page navigation example">
		    <ul class="pagination justify-content-end">
		    	<li class="page-item" :class="[ isPreviousActive ? '' : 'disabled' ]"><button class="page-link bg-dark text-white" :value="previousPageNumber">Previous</button></li>
		    	<li class="page-item" v-for="n in numberOfPages" :class="[ n == currentPageNumber ? 'active' : '' ]"><button class="page-link bg-dark text-white" :value="n">{{n}}</button></li>
			    <li class="page-item" :class="[ isNextActive ? '' : 'disabled' ]"><button class="page-link bg-dark text-white" :value="nextPageNumber">Next</button></li>
		    </ul>
		</nav>
	`,
	props: ['numberOfPages', 'currentPageNumber'],
	computed: {
		isPreviousActive: function() {
			return this.currentPageNumber == 1 ? false : true;
		},
		isNextActive: function() {
			return this.currentPageNumber == this.numberOfPages ? false : true;
		},
		previousPageNumber: function() {
			return this.currentPageNumber == 1 ? 1 : this.currentPageNumber - 1; 
		},
		nextPageNumber: function() {
			return this.currentPageNumber == this.numberOfPages ? this.numberOfPages : this.currentPageNumber + 1;
		}
	},
	methods: {
		changePage: function(e) {
			this.$emit("change-page", e.target.value);
		}
	}
});

Vue.component("paginated-table-row-td", {
	template: `
		<td>
			{{columnValue}}
		</td>
	`,
	props: ['row', 'column'],
	computed: {
		columnValue: function() {
			var propertyNameString = this.column.propertyName;
			var propertyNameParts = propertyNameString.split(".");
			
			var colVal = this.row;			
			//traverse the column property names by dot notation
			for (var i = 0; i < propertyNameParts.length; i++) {
				colVal = colVal[propertyNameParts[i]];
			}
			
			return colVal;
		}
	}
});

Vue.component("paginated-table-row", {
	template: `
		<tr>
			<paginated-table-row-td v-for="column in columns" :row="row" :column="column"></paginated-table-row-td>
			<td v-if="actionColumn != null" class="text-right">
				<paginated-table-row-actions :actions="actionColumn.actions" :row="row"></paginated-table-row-actions>
			</td>
		</tr>
	`,
	props: ['row', 'columns', 'actionColumn'],
	computed: {
		numberOfColumns: function() {
			return this.numberOfColumns.length;
		}
	}
});

Vue.component("paginated-table", {
	template: `
		<div class="paginated-table-wrapper">
			<div class="row">
				<div class="col-sm-12">
					<!--<paginated-table-actions></paginated-table-actions>-->
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12">
					<label class="text-nowrap">
						Show <select class="custom-select custom-select-sm form-control form-control-sm w-auto" v-model="pageSize">
							<option value="10">10</option>
							<option value="25">25</option>
							<option value="50">50</option>
							<option value="100">100</option>
						</select> entries
					</label>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12">
					<table class="paginated-table table table-striped table-hover mt-2 mb-2">
						<thead>
							<th v-for="column in columns">
								{{column.displayName}}
							</th>
							<th v-if="actionColumn != null">
								{{actionColumn.displayName}}
							</th>
						</thead>
						<tbody>
							<tr v-if="numberOfRows == 0">
								<td :colspan="numberOfColumns" class="text-center">There is no data to display.</td>
							</tr>
							<paginated-table-row v-else v-for="row in currentRows" :key="row.id" :row="row.data" :columns="columns" :action-column="actionColumn"></paginated-table-row>
						</tbody>
					</table>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12 col-md-5">
					<div class="mt-2">
						Showing {{firstIndexDisplay}} to {{lastIndexDisplay}} of {{numberOfRows}} entries
					</div>
				</div>
				<div class="col-sm-12 col-md-7">
					<paginated-table-pagination :number-of-pages="numberOfPages" :current-page-number="currentPage" @change-page="goToPage"></paginated-table-pagination>
				</div>
			</div>
		</div>
	`,
	props: {
		columns: Array,
		tableData: Array,
		actionColumn: PaginatedTableActionColumn
	},
	data: function() {
		return {
			pageSize: 10,
			currentPage: 1,
			paginatedTableUniqueIdGenerator: globalCounter
		}
	},
	computed: {
		numberOfColumns: function() {
			var numOfColumns = this.columns.length;
			if (this.actionColumn != null) {
				numOfColumns += 1;
			}
			return numOfColumns;
		},
		numberOfPages: function() {
			var numPages = 1;
			if (this.tableData != null && this.numberOfRows > 0) {
				numPages = Math.ceil(this.numberOfRows / this.pageSize);
			}
			return numPages;
		},
		numberOfRows: function() {
			return this.tableData.length;
		},
		firstIndex: function() {
			var firstIndex;
			if (this.numberOfRows == 0) {
				firstIndex = 0;
			}
			else {
				firstIndex = (this.currentPage - 1) * this.pageSize;
			}
			return firstIndex;
		},
		lastIndex: function() {
			var lastIndex;
			if (this.numberOfRows == 0) {
				lastIndex = 0;
			}
			else {
				lastIndex = (this.firstIndex + this.pageSize) - 1;
				if (lastIndex > this.numberOfRows) {
					lastIndex = this.numberOfRows - 1;
				}
			}
			return lastIndex;
		},
		firstIndexDisplay: function() {
			var display;
			if (this.numberOfRows == 0) {
				display = 0;
			}
			else {
				display = this.firstIndex + 1;
			}
			return display;
		},
		lastIndexDisplay: function() {
			var display;
			if (this.numberOfRows == 0) {
				display = 0;
			}
			else {
				display = this.lastIndex + 1;
			}
			return display;
		},
		currentRows: function() {
			var rows = [];
			
			if (this.numberOfRows == 0) {
				return rows;
			}
			
			for (var i = this.firstIndex; i <= this.lastIndex; i++) {
				var row = {};
				row.data = this.tableData[i];
				row.id = this.getUniqueKey();

				rows.push(row);
			}
			
			return rows;
		}
	},
	methods: {
		goToPage: function(pageNumber) {
			if (pageNumber > 0 && pageNumber <= this.numberOfPages) {
				this.currentPage = pageNumber;
			}
		},
		getUniqueKey: function() {
			return this.paginatedTableUniqueIdGenerator.getNext();
		}
	}
	
})