/**
 * 业务组-活动选择
 */
angular.module('inspinia',['uiSwitch']).controller('activityChoiceCtrl',function($scope,$http,i18nService,SweetAlert,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.businessNo=$stateParams.businessNo;

    //会员活动类型
    $scope.actTypeSelect = angular.copy($scope.actTypeList);
    $scope.actTypeStr=angular.toJson($scope.actTypeSelect);
    //适用范围
    $scope.actRangeTypeSelect = [{text:"全部",value:"0"},{text:"部分",value:"1"}];
    $scope.actRangeTypeStr=angular.toJson($scope.actRangeTypeSelect);

    //清空
    $scope.clear=function(){
        $scope.info={businessNo:$scope.businessNo};
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
            { field: 'actCode',displayName:'活动编号',width:180},
            { field: 'actType',displayName:'活动类型',cellFilter:"formatDropping:" +  $scope.actTypeStr,width:180},
            { field: 'actName',displayName:'活动名称',width:180 },
            { field: 'actDesc',displayName:'活动说明',width:180 },
            {field: 'openStatusInt',displayName: '是否打开',width: 180,
                cellTemplate:
                '<span ng-show="grid.appScope.hasPermit(\'activityChoice.activitySwitch\')"><switch class="switch switch-s" ng-model="row.entity.openStatusInt" ng-true-value="1" ng-false-value="0" ng-change="grid.appScope.activitySwitch(row.entity)" /></span>'
                +'<span class="lh30" ng-show="!grid.appScope.hasPermit(\'activityChoice.activitySwitch\')"> <span ng-show="row.entity.openStatusInt==1">开启</span><span ng-show="row.entity.openStatusInt==0">关闭</span></span>'
            },
            {field: 'showStatusInt',displayName: '是否首页显示',width: 180,
                cellTemplate:
                    '<span ng-show="grid.appScope.hasPermit(\'activityChoice.homePageSwitch\')"><switch class="switch switch-s" ng-model="row.entity.showStatusInt" ng-true-value="1" ng-false-value="0" ng-change="grid.appScope.homePageSwitch(row.entity)" /></span>'
                    +'<span class="lh30" ng-show="!grid.appScope.hasPermit(\'activityChoice.homePageSwitch\')"> <span ng-show="row.entity.showStatusInt==1">开启</span><span ng-show="row.entity.showStatusInt==0">关闭</span></span>'
            },
            { field: 'seqNo',displayName:'序列号',width:180 },
            { field: 'actRangeType',displayName:'适用范围',width:180,cellTemplate:
                    '<div class="lh30">'+
                        '<span ng-show="!grid.appScope.hasPermit(\'activityChoice.importMember\')" >{{row.entity.actRangeType | actRangeTypeFilter}}</span> ' +
                        '<a ng-show="grid.appScope.hasPermit(\'activityChoice.importMember\')" ng-click="grid.appScope.actRangeTypeChange(row.entity)" >{{row.entity.actRangeType | actRangeTypeFilter}}</a> ' +
                    '</div>'
            },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',width:250,pinnedRight:true, cellTemplate:
                    '<div class="lh30">'+
                        '<div ng-show="row.entity.actType==1">'+
                            '<a target="_blank" ui-sref="management.busActivityConfigDetail({actCode:row.entity.actCode,businessNo:row.entity.businessNo,source:1})">详情</a> ' +
                            '<a target="_blank" ng-show="grid.appScope.hasPermit(\'activityChoice.saveActivity\')" ui-sref="management.busActivityConfig({actCode:row.entity.actCode,businessNo:row.entity.businessNo})"> | 修改 </a> ' +
                            '<a target="_blank" ng-show="row.entity.openStatusInt==0&&grid.appScope.hasPermit(\'activityChoice.deleteActivityList\')" ng-click="grid.appScope.deleteActivity(row.entity)" > | 删除 </a> ' +
                            '<a target="_blank" ng-show="row.entity.actRangeType==1" ui-sref="management.rangeManagement({actCode:row.entity.actCode,businessNo:row.entity.businessNo})"> | 范围管理 </a> ' +
                    '</div>'+
                        '<div ng-show="row.entity.actType==2">'+
                            '<a target="_blank" ui-sref="management.busLuckDrawpConfigDetail({actCode:row.entity.actCode,businessNo:row.entity.businessNo,source:1})">详情</a> ' +
                            '<a target="_blank" ng-show="grid.appScope.hasPermit(\'activityChoice.saveLuckConfig\')" ui-sref="management.busLuckDrawpConfig({actCode:row.entity.actCode,businessNo:row.entity.businessNo})"> | 修改 </a> ' +
                            '<a target="_blank" ng-show="row.entity.openStatusInt==0&&grid.appScope.hasPermit(\'activityChoice.deleteActivityList\')" ng-click="grid.appScope.deleteActivity(row.entity)" > | 删除 </a> ' +
                            '<a target="_blank" ng-show="row.entity.actRangeType==1" ui-sref="management.rangeManagement({actCode:row.entity.actCode,businessNo:row.entity.businessNo})"> | 范围管理 </a> ' +

                    '</div>'+
                        '<div ng-show="row.entity.actType==3">'+
                        '<a target="_blank" ui-sref="activities.cashMeetConfigDetail({actCode:row.entity.actCode,businessNo:row.entity.businessNo,source:1})">详情</a> ' +
                        '<a target="_blank" ng-show="grid.appScope.hasPermit(\'activityChoice.saveCashMeetConfig\')" ui-sref="activities.cashMeetConfig({actCode:row.entity.actCode,businessNo:row.entity.businessNo,source:1})"> | 修改 </a> ' +
                        '<a target="_blank" ng-show="row.entity.openStatusInt==0&&grid.appScope.hasPermit(\'activityChoice.deleteActivityList\')" ng-click="grid.appScope.deleteActivity(row.entity)" > | 删除 </a> ' +
                        '<a target="_blank" ng-show="row.entity.actRangeType==1" ui-sref="management.rangeManagement({actCode:row.entity.actCode,businessNo:row.entity.businessNo})"> | 范围管理 </a> ' +

                        '</div>'+
                    '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };

    $scope.activityListGrid={                           //配置表格
        data: 'addActivityList',
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'actCode',displayName:'活动编号',width:180},
            { field: 'actName',displayName:'活动名称',width:180 },
            { field: 'actType',displayName:'活动类型',cellFilter:"formatDropping:" +  $scope.actTypeStr,width:180},
            { field: 'actDesc',displayName:'活动说明',width:300 }
        ],
        onRegisterApi: function(gridApi) {
            $scope.activityListGridApi = gridApi;
        }
    };

    $scope.loadImg = false;
    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("activityChoice/getActivityList",
            "info="+angular.toJson($scope.info),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
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

    $scope.activityList=[];
    $scope.addActivityList=[];
    $http.post("activityManagement/getActivityList",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.activityList=data.list;
            }else{
                $scope.notice(data.msg);
            }
        });


    $scope.addModal = function(){
        $scope.initList();
        $('#addInfoModal').modal('show');
    };
    $scope.initList=function(){
      if($scope.activityList!=null&&$scope.activityList.length>0){
          $scope.addActivityList=angular.copy($scope.activityList);
      }
    };
    $scope.hideModal= function(){
        $('#addInfoModal').modal('hide');
    };

    //新增
    $scope.submitting = false;
    $scope.addInfoSubmit=function(){
        if ($scope.submitting) {
            return;
        }
        var selectList = $scope.activityListGridApi.selection.getSelectedRows();
        if(selectList==null||selectList.length<=0){
            $scope.notice("添加活动不能为空!");
            return;
        }

        var ids="";
        angular.forEach(selectList,function(item){
            ids = ids+item.id +",";
        });
        if(ids==""){
            $scope.notice("添加活动不能为空!");
            return;
        }
        ids=ids.substring(0,ids.length-1);

        $scope.submitting = true;
        $http.post("activityChoice/addActivityList","ids="+ids+"&businessNo="+$stateParams.businessNo,
            {headers: {'Content-Type':'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.query();
                    $scope.hideModal();
                }else{
                    $scope.notice(data.msg);
                }
                $scope.submitting = false;
            })
            .error(function(data){
                $scope.notice(data.msg);
                $scope.submitting = false;
            });
    };

    //活动开关
    $scope.activitySwitch=function(entity){
        var title="";
        var sta=0;
        if(entity.openStatusInt==false){
            title="确认关闭?"
        }else{
            title="确认开启?"
            sta=1;
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
                    $http.post("activityChoice/activitySwitch","id="+entity.id+"&status="+sta+"&businessNo="+$scope.businessNo,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.query();
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
        if(entity.openStatusInt==false){
            entity.openStatusInt=true;
        }else{
            entity.openStatusInt=false;
        }
    };

    //活动开关
    $scope.homePageSwitch=function(entity){
        var title="";
        var sta=0;
        if(entity.showStatusInt==false){
            title="确认关闭?"
        }else{
            title="确认开启?"
            sta=1;
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
                    $http.post("activityChoice/homePageSwitch","id="+entity.id+"&status="+sta+"&businessNo="+$scope.businessNo,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.query();
                            }else{
                                $scope.notice(data.msg);
                                $scope.callBackShowSwitch(entity);
                            }
                        })
                        .error(function(data){
                            $scope.notice(data.msg);
                            $scope.callBackShowSwitch(entity);
                        });
                }else{
                    $scope.callBackShowSwitch(entity);
                }
            });
    };
    $scope.callBackShowSwitch=function (entity) {
        if(entity.showStatusInt==false){
            entity.showStatusInt=true;
        }else{
            entity.showStatusInt=false;
        }
    };

    $scope.deleteActivity=function (entry) {
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
                    $http.post("activityChoice/deleteActivityList","actCode="+entry.actCode+"&businessNo="+$stateParams.businessNo,
                        {headers: {'Content-Type':'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.query();
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


    //适用范围
    $scope.actRangeTypeChange=function (entity) {
        var title="";
        var actRangeType="";
        if(entity.actRangeType=="0"){
            title="确认"+entity.actName+"(" + entity.actCode+")活动的适应范围值修改为部分?";
            actRangeType="1";
        }else{
            title="确认"+entity.actName+"(" + entity.actCode+")活动的适应范围值修改为全部?";
            actRangeType="0";
        }
        SweetAlert.swal({
                title: title,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("activityChoice/actRangeTypeEdit",
                        "actCode="+entity.actCode+"&businessNo="+$scope.businessNo+"&actRangeType="+actRangeType,
                        {headers: {'Content-Type':'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.query();
                                if(actRangeType=="1"){
                                    window.open('welcome.do#/management/rangeManagement/'+$scope.businessNo+'/'+entity.actCode, 'view_window');
                                }
                            }else{
                                $scope.notice(data.msg);
                            }
                        })
                }
            });
    };

}).filter('actRangeTypeFilter', function () {
    return function (value) {
        switch(value) {
            case "0" :
                return "全部";
                break;
            case "1" :
                return "部分";
                break;
        }
    }
});