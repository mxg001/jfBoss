angular.module('inspinia',['uiSwitch']).controller('cashMeetConfigCtrl',function($scope,$http,i18nService,SweetAlert,$document,$stateParams,$state){

    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.source = $stateParams.source;
    $scope.businessNo = $stateParams.businessNo;

    //会员活动有效期类型
    $scope.expriryDateTypeListSelect = angular.copy($scope.expriryDateTypeList);
    $scope.expriryDateTypeStr=angular.toJson($scope.expriryDateTypeListSelect);
    $scope.activeTypeSelect = [];
    $scope.activeTypeSelect.push({text:"登录APP",value:"5"});
    $scope.activeTypeSelect.push({text:"交易收款",value:"6"});
    $scope.activeTypeSelect.push({text:"会员积分",value:"7"});
    $scope.activeTypeSelect.push({text:"邀请好友",value:"8"});
    $scope.activeTypeSelect.push({text:"绑定公众号",value:"9"});
    $scope.activeTypeStr=angular.toJson($scope.activeTypeSelect);

    $scope.addInfo={};
    $scope.getLuckDrawpConfig=function(){
        $http.post("activityManagement/getLuckTemplate",
            "actCode="+$stateParams.actCode+"&businessNo="+$stateParams.businessNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.addInfo=data.info;
                    if($scope.addInfo.actBeginTime!=null){
                        $scope.addInfo.actBeginTime=moment($scope.addInfo.actBeginTime).format('YYYY-MM-DD HH:mm:ss');
                    }
                    if($scope.addInfo.actEndTime!=null){
                        $scope.addInfo.actEndTime=moment($scope.addInfo.actEndTime).format('YYYY-MM-DD HH:mm:ss');
                    }
                    if($scope.addInfo.withdrawalTimeEnd!=null){
                        $scope.addInfo.withdrawalTimeEnd=moment($scope.addInfo.withdrawalTimeEnd).format('YYYY-MM-DD HH:mm:ss');
                    }
                }else{
                    $scope.notice(data.msg);
                }
            });
    };
    $scope.getLuckDrawpConfig();

    $scope.getCashMeetAfterConfig=function(){
        $http.post("activityManagement/getAfterCondition",
            "actCode="+$stateParams.actCode+"&businessNo="+$stateParams.businessNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.info;
                    // $scope.expendTypeChange(false);
                }else{
                    $scope.notice(data.msg);
                }
            });
    };
    $scope.getCashMeetAfterConfig();

    $scope.getCashMeetActivityContentConfig=function(){
        $http.post("activityManagement/getActivityContent",
            "actCode="+$stateParams.actCode+"&businessNo="+$stateParams.businessNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.contentResult=data.info;
                    // $scope.expendTypeChange(false);
                }else{
                    $scope.notice(data.msg);
                }
            });
    };
    $scope.getCashMeetActivityContentConfig();

    $scope.afterConditionGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'actDetCode',displayName:'编码',width:150},
            { field: 'activeName',displayName:'活动内容',width:180},
            { field: 'expiryDateType',displayName:'有效期类型',width:180,cellFilter:"formatDropping:" +  $scope.expriryDateTypeStr},
            { field: 'expiryNum',displayName:'有效期',width:180},
            { field: 'expendNumber',displayName:'单日奖励次数上限',width:120},
            { field: 'activeDesc',displayName:'活动说明',width:120},
            {field: 'openStatus',displayName: '是否打开',width: 180,
                cellTemplate:
                    '<span><switch class="switch switch-s" ng-model="row.entity.openStatus" ng-true-value="1" ng-false-value="0" ng-change="grid.appScope.activitySwitch(row.entity)" /></span></span>'
            },
            { field: 'id',displayName:'操作',width:150,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                    '<a target="_blank" ui-sref="activities.cashMeetAfterConditionDetail({actCode:row.entity.actCode,actDetCode:row.entity.actDetCode,business:grid.appScope.businessNo,source:2})" >详情</a> ' +
                    '<a target="_blank" ui-sref="activities.cashMeetAfterConditionUpdate({actCode:row.entity.actCode,actDetCode:row.entity.actDetCode,business:grid.appScope.businessNo,source:grid.appScope.source})" >修改</a> ' +
                '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };

    $scope.activityContentGrid={                           //配置表格
        data: 'contentResult',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'actDetCode',displayName:'编码',width:150},
            { field: 'activeType',displayName:'活动类型',width:150,cellFilter:"formatDropping:" +  $scope.activeTypeStr},
            { field: 'activeName',displayName:'活动内容',width:180},
            { field: 'expiryDateType',displayName:'有效期类型',width:180,cellFilter:"formatDropping:" +  $scope.expriryDateTypeStr},
            { field: 'expiryNum',displayName:'有效期',width:180},
            { field: 'expendNumber',displayName:'单日奖励次数上限',width:120},
            { field: 'allNum',displayName:'奖励总次数',width:120},
            { field: 'isLinkBehind',displayName:'关联后置条件',width:120},
            {field: 'openStatus',displayName: '是否打开',width: 180,
                cellTemplate:
                    '<span ><switch class="switch switch-s" ng-model="row.entity.openStatus" ng-true-value="1" ng-false-value="0" ng-change="grid.appScope.activitySwitch(row.entity)" /></span></span>'
            },
            { field: 'id',displayName:'操作',width:150,pinnedRight:true, cellTemplate:
                    '<div class="lh30">'+
                    '<a target="_blank" ui-sref="activities.activityContentDetail({actCode:row.entity.actCode,actDetCode:row.entity.actDetCode,business:grid.appScope.businessNo,source:grid.appScope.source})" >详情</a> ' +
                    '<a target="_blank" ui-sref="activities.activityContentConfig({actCode:row.entity.actCode,actDetCode:row.entity.actDetCode,business:grid.appScope.businessNo,source:grid.appScope.source})" >修改</a> ' +
                    '<a target="_blank" ng-if="grid.appScope.source == 1" ui-sref="activities.activityBlackQuery({actDetCode:row.entity.actDetCode})" >黑名单</a> ' +
                    '<a target="_blank" ng-if="grid.appScope.source == 1 && row.entity.openStatus==0" ng-click="grid.appScope.deleteChildActivity(row.entity)" >删除</a> ' +
                    '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };

    /**
     *富文本框按钮控制
     */
    $scope.summeroptions = {
        toolbar: [
            ['style', ['bold', 'italic', 'underline','clear']],
            ['fontface', ['fontname']],
            ['textsize', ['fontsize']],
            ['fontclr', ['color']],
            ['alignment', ['ul', 'ol', 'paragraph', 'lineheight']],
            ['height', ['height']],
            ['insert', ['hr']],
            // ['insert', ['link','picture','video','hr']],
            ['view', ['codeview']]
        ]
    };


    //活动开关
    $scope.activitySwitch=function(entity){
        var title="";
        var status=0;
        if(entity.openStatus==false){
            title="确认关闭?"
        }else{
            title="确认开启?"
            status=1;
        }
        SweetAlert.swal({
                title:title,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("activityManagement/activitySwitch","actDetCode="+entity.actDetCode+"&status="+status+"&actCode="+$stateParams.actCode,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.getCashMeetActivityContentConfig();
                                $scope.getCashMeetAfterConfig();
                            }else{
                                $scope.notice(data.msg);
                                $scope.callBackOpenSwitch(entity);
                            }
                        })
                        .error(function(data){
                            $scope.notice(data.msg);
                            $scope.callBackOpenSwitch(entity);
                        });
                }else{
                    $scope.callBackOpenSwitch(entity);
                }
            });
    };
    $scope.callBackOpenSwitch=function (entity) {
        if(entity.openStatus==false){
            entity.openStatus=true;
        }else{
            entity.openStatus=false;
        }
    };

    $scope.deleteChildActivity=function (entry) {
        SweetAlert.swal({
                title: "确定删除吗？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("activityManagement/deleteChildActivity","actDetCode="+entry.actDetCode,
                        {headers: {'Content-Type':'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.getCashMeetActivityContentConfig();
                            }else{
                                $scope.notice(data.msg);
                            }
                        })
                        .error(function(data){
                            $scope.notice(data.msg);
                        });
                }
            });
    };

    $scope.addCashMeetActivityContent=function(){
        window.open('welcome.do#/activities/activityContentAdd/'+$scope.addInfo.actCode, '_self');
    };

    $scope.saveCashMeetConfig=function(){
        $scope.addInfoSub=angular.copy($scope.addInfo);
        var data={
            info:angular.toJson($scope.addInfoSub)
        };
        var postCfg = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };
        $http.post("activityManagement/saveCashMeetConfig",data,postCfg)
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.goBackFun();
                    // $state.transitionTo('activities.activityManagement',null,{reload:true});
                }else{
                    $scope.notice(data.msg);
                }
            });
    };

    //返回
    $scope.goBackFun=function(){
        if($stateParams.source==0){
            window.open('welcome.do#/activities/activityManagement', '_self');
        }else if($stateParams.source==1){
            window.open('welcome.do#/management/activityChoice/'+$stateParams.businessNo, '_self');
        }
    };

})
.filter('trustHtml', function ($sce) {
    return function (input) {
        return $sce.trustAsHtml(input);
    }
});