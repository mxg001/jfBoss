/**
 * 业务组-任务选择
 */
angular.module('inspinia',['uiSwitch']).controller('businessTaskCtrl',function($scope,$http,i18nService,SweetAlert,$stateParams,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.businessNo=$stateParams.businessNo;
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
            { field: 'taskNo',displayName:'任务编号',width:180},
            { field: 'taskName',displayName:'任务名称',width:180 },
            { field: 'taskTypeName',displayName:'任务类型',width:180 },
            {field: 'status',displayName: '状态',width: 180,
                cellTemplate:
                '<span ng-show="grid.appScope.hasPermit(\'businessInfoAction.closeBusinessTask\')"><switch class="switch switch-s" ng-model="row.entity.status" ng-true-value="1" ng-false-value="0" ng-change="grid.appScope.closeTask(row.entity)" /></span>'
                +'<span class="lh30" ng-show="!grid.appScope.hasPermit(\'businessInfoAction.closeBusinessTask\')"> <span ng-show="row.entity.status==1">开启</span><span ng-show="row.entity.status==0">关闭</span></span>'
            },
            { field: 'taskDesc',displayName:'任务说明',width:300 },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
            '<div class="lh30">'+
            '<a target="_blank" ui-sref="management.businessTaskDetail({id:row.entity.id,businessNo:grid.appScope.businessNo})">详情</a> ' +
            '<a ng-show="grid.appScope.hasPermit(\'businessInfoAction.editBusinessTask\')" target="_blank" ui-sref="management.businessTaskEdit({id:row.entity.id,businessNo:grid.appScope.businessNo})"> | 修改</a> ' +
            '<a ng-show="grid.appScope.hasPermit(\'businessInfoAction.deleteBusinessTask\')" ng-click="grid.appScope.deleteBusinessTask(row.entity)"> | 删除</a> ' +
            '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };
    $scope.taskListGrid={                           //配置表格
        data: 'addTaskRewardList',
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'taskNo',displayName:'任务编号',width:180},
            { field: 'taskName',displayName:'任务名称',width:180 },
            { field: 'taskTypeName',displayName:'任务类型',width:180 },
            { field: 'taskDesc',displayName:'任务说明',width:300 }
        ],
        onRegisterApi: function(gridApi) {
            $scope.taskListGridApi = gridApi;
        }
    };
    $scope.loadImg = false;

    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("businessInfoAction/getBusinessTaskList","businessNo="+$stateParams.businessNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.list;
                    var taskNos="";
                    if(data.list!=null&&data.list.length>0){
                        angular.forEach(data.list,function(item){
                            taskNos = taskNos+item.taskNo +",";
                        });
                    }
                    $scope.choiceStr=taskNos;
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

    $scope.taskRewardList=[];
    $scope.addTaskRewardList=[];
    $http.post("taskRewardAction/getTaskRewardInfoList",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.taskRewardList=data.list;
            }else{
                $scope.notice(data.msg);
            }

        });


    $scope.addModal = function(){
        $scope.initList();
        $('#addInfoModal').modal('show');
    };
    $scope.initList=function(){
      if($scope.taskRewardList!=null&&$scope.taskRewardList.length>0){
          $scope.addTaskRewardList=[];
          angular.forEach($scope.taskRewardList,function(item){
              if((","+$scope.choiceStr).indexOf(","+item.taskNo+",")<=-1){
                  $scope.addTaskRewardList.push(item);
              }
          });
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
        var selectList = $scope.taskListGridApi.selection.getSelectedRows();
        if(selectList==null||selectList.length<=0){
            $scope.notice("添加任务不能为空!");
            return;
        }

        var ids="";
        angular.forEach(selectList,function(item){
            ids = ids+item.id +",";
        });
        if(ids==""){
            $scope.notice("添加任务不能为空!");
            return;
        }
        ids=ids.substring(0,ids.length-1);

        $scope.submitting = true;
        $http.post("businessInfoAction/addBusinessTask","ids="+ids+"&businessNo="+$stateParams.businessNo,
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

    $scope.closeTask=function(entity){
        var title="";
        var sta=0;
        if(entity.status==false){
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
                    $http.post("businessInfoAction/closeBusinessTask","id="+entity.id+"&businessNo="+entity.businessNo+"&sta="+sta,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.query();
                            }else{
                                $scope.notice(data.msg);
                                if(entity.status==false){
                                    entity.status=true;
                                }else{
                                    entity.status=false;
                                }
                            }
                        })
                        .error(function(data){
                            $scope.notice(data.msg);
                            if(entity.status==false){
                                entity.status=true;
                            }else{
                                entity.status=false;
                            }
                        });
                }else{
                    if(entity.status==false){
                        entity.status=true;
                    }else{
                        entity.status=false;
                    }
                }
            });
    };

    $scope.deleteBusinessTask=function (entry) {
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
                    $http.post("businessInfoAction/deleteBusinessTask","id="+entry.id+"&businessNo="+$stateParams.businessNo,
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
});