var app = angular.module('myApp', []);
app.controller('selectPage', function($scope, $http){
    $scope.response = result;
    $scope.content = result.content;
    $scope.commodityCondition={name:"",type:""};
    $scope.pages = $scope.response.totalPages;
    function getPageSequence(pageNum){
        var pageSequence = [];
        for (var x=1;x<=pageNum;x++){
            pageSequence.push(x);
        }
        return pageSequence;
    }
    $scope.pageSequence=getPageSequence($scope.pages);
    $scope.findPage=function(curPage){
        var name=$scope.commodityCondition.name;
        var type=$scope.commodityCondition.type;
        $http({
            method:'GET',
            url:'/commodity/index',
            params:{
                'currentPage':curPage,
                'name':name,
                'type':type
            }
        }).success(function(response){
            console.log(response);
           $scope.response=response;
           $scope.content=response.content;
           $scope.pageSequence=getPageSequence(response.totalPages);

        });
    };
    $scope.updateCommodity=function(id){
        window.location="/commodity/"+id;
    };
    $scope.deleteCommodity=function(id){
        $http({
                    method:'DELETE',
                    url:'/commodity/'+id
                }).success(function(response){
                    console.log(response);
                    alert("has been deleted!");
                    $scope.response=response;
                    $scope.content=response.content;
                    $scope.pageSequence=getPageSequence(response.totalPages);
                });
    };
});
app.controller('updatePage',function($scope, $http){
    $scope.commodity = commodity;

});
app.controller('savePage', function($scope, $http){
    $scope.commodities=[{name:"",type:"",price:"",quantity:""}];
    validate=function(item){
        if(item.name==""||item.type==""){
                    alert("name or type should not be empty!");
                    return false;
                }
        if(!(angular.isNumber(item.price)&&angular.isNumber(item.quantity))){
                    alert("price or quantity should be number!");
                    return false;
        }
        return true;
    }
    $scope.addOneItem=function($index,item){
        if(validate(item))
        {
        $scope.commodities.splice($index+1,0,{name:'',type:'',price:0,quantity:0});
        }
    };
    $scope.deleteOneItem=function($index){
    	$scope.commodities.splice($index,1);
    };
    $scope.saveCommodities=function(){
        var validated=false;
        for (var item in $scope.commodities){
            if(!validate(item)){
                alert("save fail, check the form!");
                return;
            }
        }
        $http({
            method:'POST',
            url:'/commodity',
            data:$scope.commodities
        }).success(function(response){
            alert("save!");
        });
    }
})

