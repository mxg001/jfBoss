/**
 * 任务详情
 */
angular.module('inspinia',['angularFileUpload']).controller('taskRewardDetailCtrl',function($scope,$http,i18nService,$state,$stateParams,SweetAlert,FileUploader){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //值类型
    $scope.calcTypeSelect = [{text:"数值",value:"1"},{text:"公式",value:"2"}];
    $scope.calcTypeStr=angular.toJson($scope.calcTypeSelect);

    //权益类型
    $scope.rewardTypeSelect = angular.copy($scope.rewardTypeList);
    $scope.rewardTypeStr=angular.toJson($scope.rewardTypeSelect);

    $scope.addInfo={taskType:""};
    $scope.taskRewardConfigList=[];//权益列表

    //频率类型
    $scope.frequencyTypeSelect = angular.copy($scope.frequencyTypeList);
    $scope.frequencyTypeSelect.unshift({value:"",text:"请选择"});

    //流水下发方式
    $scope.grantTypeSelect = [{value:null,text:"请选择"}];

    for(var i=0; i<$scope.grantTypeList.length; i++){
        $scope.grantTypeSelect.push({value:$scope.grantTypeList[i].value, text:$scope.grantTypeList[i].text});
    }

    //触发类型
    $scope.triggerTypeSelect = [{value:"",text:"请选择"}];
    for(var i=0; i<$scope.triggerTypeList.length; i++){
        $scope.triggerTypeSelect.push({value:$scope.triggerTypeList[i].value, text:$scope.triggerTypeList[i].text});
    }

    //获取任务类型
    $scope.taskTypeList=[];
    $http.post("taskRewardAction/getTaskTypeList")
        .success(function(data){
            if(data.status){
                $scope.taskTypeList.push({value:"",text:"请选择"});
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.taskTypeList.push({value:list[i].taskTypeNo, text:list[i].taskTypeName});
                    }
                }
            }
        });

    //获取数据
    $http.post("taskRewardAction/getTaskRewardInfo","id="+$stateParams.id,
        {headers: {'Content-Type':'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.addInfo=data.info;
                $scope.addInfo.frequencyType="3";
                $scope.taskRewardConfigList=data.info.taskRewardConfigList;
                //数据回显转换
                if($scope.taskRewardConfigList!=null&&$scope.taskRewardConfigList.length>0){
                    angular.forEach($scope.taskRewardConfigList,function(item){
                        if(item.calcType == "1"){
                            item.rewardTemplateStr=item.rewardTemplate;
                        }else{
                            var strs=item.rewardTemplate.split(",");
                            if(strs.length==2){
                                item.x=strs[0];
                                item.y=strs[1];
                                item.rewardTemplateStr="[触发类型值mod("+item.x+")]*"+item.y;
                            }
                        }
                    })
                }
            }
        }).error(function(data){
            $scope.notice(data.msg);
         });

    $scope.userGrid={                           //配置表格
        data: 'taskRewardConfigList',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'rewardName',displayName:'权益名称',width:150},
            { field: 'rewardType',displayName:'赠送类型',width:150,cellFilter:"formatDropping:" +  $scope.rewardTypeStr},
            { field: 'calcType',displayName:'值类型',width:150,cellFilter:"formatDropping:" +  $scope.calcTypeStr},
            { field: 'rewardTemplateStr',displayName:'数量',width:250},
            { field: 'rewardEffeDay',displayName:'有效期',width:100}
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };
}).filter('trustHtml', function ($sce) {
    return function (input) {
        return $sce.trustAsHtml(input);
    }
});