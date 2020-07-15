/**
 * 奖项黑名单-详情
 */
angular.module('inspinia').controller('busPrizeBlacklistQueryDetailCtrl',function($scope,$http,i18nService,SweetAlert,$document,$state,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.businessNo=$stateParams.businessNo;
    $scope.actCode=$stateParams.actCode;
    $scope.awardCode=$stateParams.awardCode;
    //清空
    $scope.clear=function(){
        $scope.info={awardCode:$scope.awardCode};
    };
    $scope.clear();

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'id',displayName:'ID',width:80},
            { field: 'leaguerNo',displayName:'会员编号',width:180 },
            { field: 'leaguerName',displayName:'会员名称',width:180 },
            { field: 'originUserNo',displayName:'用户编号',width:180 },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'operator',displayName:'创建人',width:180 }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };
    $scope.title="";
    $scope.getPrize=function(){
        $http.post("activityChoice/getLuckPrize","awardCode="+$scope.awardCode+"&businessNo="+$scope.businessNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.prize=data.info;
                    $scope.title=data.info.awardDesc;
                }else{
                    $scope.notice(data.msg);
                }
            })
    };
    $scope.getPrize();

    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("activityChoice/getAwardBlackList","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize+"&businessNo="+$scope.businessNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.userGrid.totalItems = data.page.totalCount;
                }else{
                    $scope.notice(data.msg);
                }
                $scope.loadImg = false;
            })
            .error(function(data){
                $scope.notice(data.msg);
                $scope.loadImg = false;
            });
    };
    $scope.query();

    //返回
    $scope.goBackFun=function(){
        window.open('welcome.do#/management/busLuckDrawpConfigDetail/'+$scope.actCode+'/'+$scope.businessNo+'/'+$stateParams.source, '_self');
    };
    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});