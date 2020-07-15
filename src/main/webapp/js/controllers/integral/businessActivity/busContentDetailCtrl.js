/**
 * 活动内容新详情
 */
angular.module('inspinia').controller('busContentDetailCtrl',function($scope,$http,i18nService,$state,$stateParams,SweetAlert){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.actDetCode=$stateParams.actDetCode;
    $scope.businessNo=$stateParams.businessNo;

    //内容类型
    $scope.activeTypeSelect = [{text:"请选择",value:""},{text:"奖励赠送",value:"1"},{text:"达标奖励",value:"2"},{text:"交易返现",value:"3"}];
    $scope.activeTypeStr=angular.toJson($scope.activeTypeSelect);
    //奖励方式
    $scope.awardMethodSelect = [{text:"请选择",value:""},{text:"多选一",value:"1"},{text:"多选多",value:"2"}];
    $scope.awardMethodStr=angular.toJson($scope.awardMethodSelect);
    //值类型
    $scope.calcTypeSelect = [{text:"数值",value:"1"},{text:"比例",value:"2"}];
    $scope.calcTypeStr=angular.toJson($scope.calcTypeSelect);

    //类型
    $scope.tranTypeSelect = [{text:"按交易金额返现",value:"3"},{text:"按手续费返现",value:"4"}];
    $scope.tranTypeStr=angular.toJson($scope.tranTypeSelect);

    //奖品类型
    $scope.rewardTypeSelect = angular.copy($scope.rewardTypeList);
    $scope.rewardTypeSelect.unshift({text:"请选择",value:""});
    $scope.rewardTypeStr=angular.toJson($scope.rewardTypeSelect);


    $scope.addInfo={activeType:"",awardMethod:""};
    $scope.addInfoItemList=[];

    $scope.userGrid={                           //配置表格
        data: 'addInfoItemList',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };
    $scope.cleanStatus=function(){
        $scope.tableSta=false;
        $scope.methodSta=false;
    };

    $scope.activeTypeChange=function(){
        if($scope.addInfo.activeType==null||$scope.addInfo.activeType==""){
            $scope.cleanStatus();
            return;
        }
        $scope.cleanStatus();
        $scope.tableSta=true;
        if($scope.addInfo.activeType=="1"){//奖励赠送
            $scope.userGrid.columnDefs=[
                { field: 'rewardType',displayName:'奖励类型',cellFilter:"formatDropping:" +  $scope.rewardTypeStr,width:180},
                { field: 'calcType',displayName:'值类型',width:180,cellTemplate:
                        '<div class="lh30">' +
                        '<span ng-if="row.entity.rewardType==\'4\'||row.entity.rewardType==\'5\'||row.entity.rewardType==\'6\'">{{row.entity.goodName}}</span>' +
                        '<span ng-if="row.entity.rewardType!=\'4\'&&row.entity.rewardType!=\'5\'&&row.entity.rewardType!=\'6\'">{{row.entity.calcType | calcTypeFilter}}</span>'+
                        '</div>'
                },
                { field: 'awardNum',displayName:'数量',width:180},
                { field: 'expireDay',displayName:'有效期',width:180}
            ];
            $scope.addTitle="奖励赠送";
        }else if($scope.addInfo.activeType=="2"){//达标奖励
            $scope.userGrid.columnDefs=[
                { field: 'standardAmount',displayName:'边界值',width:180},
                { field: 'rewardType',displayName:'奖励类型',cellFilter:"formatDropping:" +  $scope.rewardTypeStr,width:180},
                { field: 'calcType',displayName:'值类型',width:180,cellTemplate:
                        '<div class="lh30">' +
                        '<span ng-if="row.entity.rewardType==\'4\'||row.entity.rewardType==\'5\'||row.entity.rewardType==\'6\'">{{row.entity.goodName}}</span>' +
                        '<span ng-if="row.entity.rewardType!=\'4\'&&row.entity.rewardType!=\'5\'&&row.entity.rewardType!=\'6\'">{{row.entity.calcType | calcTypeFilter}}</span>'+
                        '</div>'
                },
                { field: 'awardNum',displayName:'数值',width:180},
                { field: 'expireDay',displayName:'有效期',width:180}
            ];
            $scope.methodSta=true;
            $scope.addTitle="达标奖励";
        }else if($scope.addInfo.activeType=="3"){//交易返现
            $scope.userGrid.columnDefs=[
                { field: 'tranType',displayName:'类型',cellFilter:"formatDropping:" +  $scope.tranTypeStr,width:180},
                { field: 'calcType',displayName:'值类型',cellFilter:"formatDropping:" +  $scope.calcTypeStr,width:180},
                { field: 'awardNum',displayName:'奖励比例(%)',width:180},
                { field: 'awardTemplateFormula',displayName:'计算公式',width:180}
            ];
            $scope.addTitle="交易返现";
        }
    };
    $scope.getActiDetail=function(){
        $http.post("activityChoice/getActiDetail",
            "actDetCode="+$scope.actDetCode+"&businessNo="+$scope.businessNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.addInfo=data.info;
                    $scope.addInfoItemList=data.info.actReward;
                    if($scope.addInfo!=null&&$scope.addInfo.activeType=="3"&&$scope.addInfoItemList!=null&&$scope.addInfoItemList.length>0){
                        for(var i=0;i<$scope.addInfoItemList.length;i++){
                            $scope.addInfoItemList[i].tranType="3";
                        }
                    }
                    $scope.activeTypeChange();
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.notice(data.msg);
            });
    };
    $scope.getActiDetail();

    //返回
    $scope.goBackFun=function(){
        if($stateParams.source==3){
            window.open('welcome.do#/management/busActivityConfig/'+$scope.addInfo.actCode+'/'+$scope.businessNo, '_self');
        }else{
            window.open('welcome.do#/management/busActivityConfigDetail/'+$scope.addInfo.actCode+'/'+$scope.businessNo+'/'+$stateParams.source, '_self');
        }
    };
}).filter('calcTypeFilter', function () {
    return function (value) {
        switch(value) {
            case "1" :
                return "数值";
                break;
            case "2" :
                return "比例";
                break;
        }
    }
});