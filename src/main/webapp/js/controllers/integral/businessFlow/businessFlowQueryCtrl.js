/**
 * 业务流水查询
 */
angular.module('inspinia').controller('businessFlowQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //下发状态
    $scope.rewardStatusSelect = [{text:"全部",value:""},{text:"未发放",value:"0"},{text:"已发放",value:"1"}];
    $scope.rewardStatusStr=angular.toJson($scope.rewardStatusSelect);

    //权益来源类型
    $scope.fromTypeSelect = angular.copy($scope.fromTypeList);
    $scope.fromTypeSelect.unshift({text:"全部",value:""});
    $scope.fromTypeStr=angular.toJson($scope.fromTypeSelect);

    //来源系统
    $scope.fromSystemSelect = angular.copy($scope.fromSystemList);
    $scope.fromSystemSelect.unshift({text:"全部",value:""});
    $scope.fromSystemStr=angular.toJson($scope.fromSystemSelect);

    //权益类型
    $scope.rewardTypeSelect = angular.copy($scope.rewardTypeList);
    $scope.rewardTypeSelect.unshift({text:"全部",value:""});
    $scope.rewardTypeStr=angular.toJson($scope.rewardTypeSelect);


    //清空
    $scope.clear=function(){
        $scope.info={rewardType:"",rewardStatus:"",fromType:"",businessNo:"",fromSystem:"",
            createTimeBegin:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    };
    $scope.clear();


    $scope.businessNoList=[];
    $http.post("businessInfoAction/getBusinessInfoListByUser",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.businessNoList.push({value:"",text:"全部"});
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.businessNoList.push({value:list[i].businessNo, text:list[i].businessName});
                    }
                }
            }
        });

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'taskOrderNo',displayName:'业务单号',width:230},
            { field: 'rewardOrderNo',displayName:'流水号',width:230},
            { field: 'leaguerNo',displayName:'会员编号',width:230 },
            { field: 'leaguerName',displayName:'会员名称',width:120 },
            { field: 'businessName',displayName:'业务组',width:120 },
            { field: 'originUserNo',displayName:'用户编号',width:180 },
            { field: 'mobilePhone',displayName:'用户手机号',width:180 },
            { field: 'rewardStatus',displayName:'下发状态',width:150,cellFilter:"formatDropping:" +  $scope.rewardStatusStr},
            { field: 'rewardType',displayName:'物品类型',width:150,cellFilter:"formatDropping:" +  $scope.rewardTypeStr},
            { field: 'calcTypeName',displayName:'物品名称',width:180 },
            { field: 'fromType',displayName:'来源类型',width:150,cellFilter:"formatDropping:" +  $scope.fromTypeStr},
            { field: 'fromDesc',displayName:'来源描述',width:260 },
            { field: 'fromSystem',displayName:'来源系统',width:150,cellFilter:"formatDropping:" +  $scope.fromSystemStr},
            { field: 'rewardName',displayName:'权益名称',width:180 },
            { field: 'rewardNum',displayName:'数量',width:180 },
            { field: 'originOrderNo',displayName:'来源业务订单',width:260 },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a ng-show="row.entity.rewardStatus==0&&grid.appScope.hasPermit(\'businessFlowAction.lowerHair\')" ng-click="grid.appScope.lowerHair(row.entity)">下发</a> ' +
                '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query(2);
            });
        }
    };
    $scope.loadImg = false;

    $scope.query=function(sta){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("businessFlowAction/getSelectPageList","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                }else{
                    $scope.notice(data.msg);
                }
                $scope.loadImg = false;
            })
            .error(function(data){
                $scope.notice(data.msg);
                $scope.loadImg = false;
            });

        if(sta==1){
            $http.post("businessFlowAction/getSelectPageListCount","info="+angular.toJson($scope.info),
                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .success(function(data){
                    if(data.status){
                        $scope.userGrid.totalItems=data.total;
                    }else{
                        $scope.notice(data.msg);
                    }
                })
        }
    };

    //导出信息
    $scope.exportInfo=function(){
        SweetAlert.swal({
                title: "确认导出？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    $scope.exportInfoClick("businessFlowAction/importDetail",{"info":angular.toJson($scope.info)});
                }
            });
    };

    //下发
    $scope.lowerHair=function(entity){
        SweetAlert.swal({
                title: "确认下发？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("businessFlowAction/lowerHair","rewardOrderNo="+entity.rewardOrderNo+"&businessNo="+entity.businessNo,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.query(1);
                            }
                            $scope.notice(data.msg);
                        })
                        .error(function(data){
                            $scope.notice(data.msg);
                        });
                }
            });
    };

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query(1);
            }
        })
    });
});