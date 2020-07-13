var app = angular.module('myApp', []);

app.controller('orderPage', function($scope, $http){
    $scope.response = result;
    $scope.content = result.content;
    $scope.orderCondition={customerName:"",address:"",startDate:"",endDate:""};
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
        var customerName=$scope.orderCondition.customerName;
        var startDate=$scope.orderCondition.startDate;
        var endDate=$scope.orderCondition.endDate;
        $http({
            method:'GET',
            url:'/totalorder/index',
            params:{
                'currentPage':curPage,
                'customerName':customerName,
                'startDate':startDate,
                'endDate':endDate
            }
        }).success(function(response){
           console.log(response);
           $scope.response=response;
           $scope.content=response.content;
           $scope.pageSequence=getPageSequence(response.totalPages);

        });
    };
    $scope.updateOrder=function(id){
        window.location="/totalorder/"+id;
    };
    $scope.deleteOrder=function(id){
        $http({
                    method:'DELETE',
                    url:'/totalorder/'+id
                }).success(function(response){
                    console.log(response);
                    alert("has been deleted!");
                    $scope.response=response;
                    $scope.content=response.content;
                    $scope.pageSequence=getPageSequence(response.totalPages);
                });
    };
});
app.controller('updatePage',function($scope, $http, $filter){
    for(var index in order.orderDetails){
        order.orderDetails[index].orderInfoMapper = null;
    }
    $scope.order=order;
    $scope.order.orderInfo.date = $filter('date')(order.orderInfo.date,"yyyy-MM-dd HH:mm:ss");
    $scope.saveUpdate=function(){
         $http({
                method:'put',
                data:$scope.order,
               url:'/totalorder/'
                           }).success(function(response){
                               alert("has been updated!");
                               $scope.order=response.data;
                           });
    };
    $scope.computeTotal=function(){
        var sum=0;
        var orderDetails = $scope.order.orderDetails;
        for(var index in orderDetails){
            sum+=orderDetails[index].price*orderDetails[index].quantity;
        }
        $scope.order.orderInfo.totalPrice=sum;
        return sum;
    }

    validate=function(orderDetail){
        if(orderDetail.commodityMapper.id==null){
                    alert("select a commodity");
                    return false;
                }
        if(!(angular.isNumber(orderDetail.price)&&angular.isNumber(orderDetail.quantity))){
                    alert("price or quantity should be number!");
                    return false;
        }
        return true;
    }
    validateOrderInfo=function(orderInfo){
        if(orderInfo.customerName==""||orderInfo.address==""){
            alert("please enter customer name and address");
            return false;
        }
        if(orderInfo.date==""){
            alert("date");
            return false;
        }
        if(orderInfo.totalPrice!=computeTotalPrice()){
            alert("total price has been change to"+orderInfo.totalPrice);
        }
        return true;
    }
    $scope.addOneItem=function($index,item){
        if(validate(item))
        {
        $scope.order.orderDetails.splice($index+1,0,{commodityMapper:{},price:0,quantity:0});
        }
    };
    $scope.deleteOneItem=function($index){
    	$scope.order.orderDetails.splice($index,1);
    };
    $scope.commodityDetail=function(commodity){
            alert("库存："+commodity.quantity);
    }

});
app.controller('savePage', function($scope, $http, $filter){
    var curDate = new Date;
    var curDateTimeString = $filter('date')(curDate,"yyyy-MM-dd HH:mm:ss");
    $scope.commodities = commodities;
    $scope.order={
        orderInfo:{
            customerName:"",
            address:"",
            date: curDateTimeString
        },
        orderDetails:[
            {
                commodityMapper:{},
                price:0,
                quantity:0
            }
        ]
        };
    validate=function(orderDetail){
        if(orderDetail.commodityMapper.id==null){
                    alert("select a commodity");
                    return false;
                }
        if(!(angular.isNumber(orderDetail.price)&&angular.isNumber(orderDetail.quantity))){
                    alert("price or quantity should be number!");
                    return false;
        }
        return true;
    }
    validateOrderInfo=function(orderInfo){
        if(orderInfo.customerName==""||orderInfo.address==""){
            alert("please enter customer name and address");
            return false;
        }
        if(orderInfo.date==""){
            alert("date");
            return false;
        }
        if(orderInfo.totalPrice!=computeTotalPrice()){
            alert("total price has been change to"+orderInfo.totalPrice);
        }
        return true;
    }
    $scope.setDefault=function(orderDetail, commodity){
        orderDetail.price=commodity.price;
    }
    computeTotalPrice=function(){
        var sum=0;
        var orderDetails = $scope.order.orderDetails;
        for(var index in orderDetails){
            sum+=orderDetails[index].price*orderDetails[index].quantity;
        }
        return sum;
    }
    $scope.computeTotal=function(){
        $scope.order.orderInfo.totalPrice=computeTotalPrice();
    }
    $scope.addOneItem=function($index,item){
        if(validate(item))
        {
        $scope.order.orderDetails.splice($index+1,0,{commodityMapper:{},price:0,quantity:0});
        }
    };
    $scope.deleteOneItem=function($index){
    	$scope.order.orderDetails.splice($index,1);
    };
    $scope.saveOrder=function(){
        var orderDetails = $scope.order.orderDetails;
        var orderInfo = $scope.order.orderInfo;
        for (var index in orderDetails){
            if(!validate(orderDetails[index])){
                alert("save fail, check the form!");
                return;
            }
        }
        if(!validateOrderInfo(orderInfo)){
            return;
        }
        $http({
            method:'POST',
            url:'/totalorder',
            data:$scope.order
        }).success(function(response){
            alert("save!");
            window.location="/totalorder";
        });
    }
    $scope.commodityDetail=function(commodity){
        alert("库存："+commodity.quantity);
    }
})
