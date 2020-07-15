/**
 * 模板活动管理
 */
angular.module('inspinia').controller('activityManagementQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){

    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //会员活动类型
    $scope.actTypeSelect = angular.copy($scope.actTypeList);
    $scope.actTypeStr=angular.toJson($scope.actTypeSelect);

    //清空
    $scope.clear=function(){
        $scope.info={};
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
            { field: 'actCode',displayName:'编号'},
            { field: 'actName',displayName:'活动名称'},
            { field: 'actType',displayName:'活动类型',cellFilter:"formatDropping:" +  $scope.actTypeStr},
            { field: 'actDesc',displayName:'活动说明'},
            { field: 'id',displayName:'操作',width:150,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                    '<div ng-show="row.entity.actType==1">'+
                        '<a target="_blank" ui-sref="activities.activityConfigDetail({actCode:row.entity.actCode,businessNo:0})">详情</a> ' +
                        '<a target="_blank" ng-show="grid.appScope.hasPermit(\'activityManagement.saveActivityTemp\')" ui-sref="activities.activityConfig({actCode:row.entity.actCode,businessNo:0})"> | 修改 </a> ' +
                    '</div>'+
                    '<div ng-show="row.entity.actType==2">'+
                        '<a target="_blank" ui-sref="activities.luckDrawpConfigDetail({actCode:row.entity.actCode,businessNo:0})">详情</a> ' +
                        '<a target="_blank" ng-show="grid.appScope.hasPermit(\'activityManagement.saveLuckTemplate\')" ui-sref="activities.luckDrawpConfig({actCode:row.entity.actCode,businessNo:0})"> | 修改 </a> ' +
                    '</div>'+
                    '<div ng-show="row.entity.actType==3">'+
                    '<a target="_blank" ui-sref="activities.cashMeetConfigDetail({actCode:row.entity.actCode,businessNo:0,source:0})">详情</a> ' +
                    '<a target="_blank" ng-show="grid.appScope.hasPermit(\'activityManagement.saveCashMeetConfig\')" ui-sref="activities.cashMeetConfig({actCode:row.entity.actCode,businessNo:0,source:0})"> | 修改 </a> ' +
                    '</div>'+
                 '</div>'
            }
        ],

            onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };

    $scope.loadImg = false;
    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("activityManagement/getActivityList", {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.list;
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

});