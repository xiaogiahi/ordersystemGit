var app = angular.module('myApp', []);

app.controller('myCtrl', function($scope,$timeout) {
    $scope.orders=[{name:'',price:0,quantity:0}];
    $scope.items=["A","B"];
	$scope.addOneItem=function($index){
		$scope.orders.splice($index+1,0,{name:'',price:0,quantity:0});
	};
	$scope.deleteOneItem=function($index){
		$scope.orders.splice($index,1);
	};
	$scope.computePrice=function(){
		var sum = 0;
		for (var x in $scope.orders){
			sum+=$scope.orders[x].price*$scope.orders[x].quantity;
		};
		return sum;
	};
	$scope.setItemName=function($x,$i){
		$x.name=$i;
	};
	var updateClock = function(){
	    $scope.clock = new Date();
	    $timeout(function(){
	        updateClock();
	    },1000);
	};
	updateClock();

});

app.controller('pagination',function($scope,$http){
    var reSearch = function(){
        var postData = {
            currentPage:$scope.paginationConf.currentPage,
            pageSize: $scope.paginationConf.itemPerPage
        };
        $http.get('selectPage',postData).success(function(response){
            $scope.paginationConf.totalElements = response.totalElements;
            $scope.paginationConf.totalPages = response.totalPages;
            $scope.commodityList = response.content;
        });
    };
    $scope.commodityList=[{name:"A"}];
    $scope.reSearch = reSearch;
    $scope.paginationConf = {
        currentPage:1,
        itemPerPage:5,
        perPageOptions:[5,10,20],
        totalElements:0,
        totalPages:0
    };
    $scope.$watch('paginationConf.currentPage+paginationConf.itemPerPage',reSearch);
});
