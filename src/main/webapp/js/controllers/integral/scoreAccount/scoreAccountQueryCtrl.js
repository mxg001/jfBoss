/**
 * 积分查询
 */
angular.module('inspinia').controller('scoreAccountQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //入账类型
    $scope.accTypeSelect = [{text:"全部",value:""},{text:"入账",value:"1"},{text:"出账",value:"2"}];
    $scope.accTypeStr=angular.toJson($scope.accTypeSelect);

    //入账状态
    $scope.accStatusSelect = [{text:"初始化",value:"1"},{text:"成功",value:"2"},{text:"失败",value:"3"}];
    $scope.accStatusStr=angular.toJson($scope.accStatusSelect);

    //权益来源类型
    $scope.fromTypeSelect = angular.copy($scope.fromTypeList);
    $scope.fromTypeSelect.unshift({text:"全部",value:""});
    $scope.fromTypeStr=angular.toJson($scope.fromTypeSelect);

    //来源系统
    $scope.fromSystemSelect = angular.copy($scope.fromSystemList);
    $scope.fromSystemSelect.unshift({text:"全部",value:""});
    $scope.fromSystemStr=angular.toJson($scope.fromSystemSelect);
    //清空
    $scope.clear=function(){
        $scope.info={accType:"",fromType:"",businessNo:"",fromSystem:"",
            createTimeBegin:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    };
    $scope.clear();

    $scope.totalInfo={total:0,addScoreNum:0,usedScoreNum:0,expScoreNum:0,cancelScoreNum:0};

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
            { field: 'accountSerialNo',displayName:'出入账流水号',width:230},
            { field: 'leaguerNo',displayName:'会员编号',width:230 },
            { field: 'leaguerName',displayName:'会员名称',width:180 },
            { field: 'businessName',displayName:'业务组',width:180 },
            { field: 'originUserNo',displayName:'用户编号',width:180 },
            { field: 'mobilePhone',displayName:'用户手机号',width:180 },
            { field: 'scoreBatchDate',displayName:'流水批次',cellFilter: 'date:"yyyyMM"',width:180},
            { field: 'accType',displayName:'积分出入账类型',width:150,cellFilter:"formatDropping:" +  $scope.accTypeStr},
            { field: 'accScore',displayName:'记账积分',width:180 },
            { field: 'fromType',displayName:'来源类型',width:150,cellFilter:"formatDropping:" +  $scope.fromTypeStr},
            { field: 'fromDesc',displayName:'来源描述',width:260 },
            { field: 'fromSystem',displayName:'来源系统',width:150,cellFilter:"formatDropping:" +  $scope.fromSystemStr},
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'serviceOrderNo',displayName:'来源业务订单',width:180 },
            { field: 'operater',displayName:'操作人',width:180 }

            // { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
            //     '<div class="lh30">'+
            //     '<a ng-show="grid.appScope.hasPermit(\'scoreAccountAction.cancelScore\')" ng-click="grid.appScope.cancelScore(row.entity)">作废</a> ' +
            //     '</div>'
            // }
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
        $http.post("scoreAccountAction/getSelectPageList","info="+angular.toJson($scope.info)+"&pageNo="+
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
            $http.post("scoreAccountAction/getSelectPageListCount","info="+angular.toJson($scope.info),
                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .success(function(data){
                    if(data.status){
                        $scope.totalInfo=data.totalInfo;
                        if($scope.totalInfo!=null){
                            $scope.userGrid.totalItems = $scope.totalInfo.total;
                        }
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
                    $scope.exportInfoClick("scoreAccountAction/importDetail",{"info":angular.toJson($scope.info)});
                }
            });
    };


    $scope.addModal = function(){
        $scope.addInfo={};
        $('#addInfoModal').modal('show');
    };
    $scope.hideModal= function(){
        $('#addInfoModal').modal('hide');
    };

    $scope.leaguerNoChange=function(){
        if($scope.addInfo.leaguerNo!=null&&$scope.addInfo.leaguerNo!=""){
            $scope.addInfo.leaguerName=null;
            $http.post("leaguerAction/checkLeaguerInfo","leaguerNo="+$scope.addInfo.leaguerNo,
                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .success(function(data){
                    if(data.status){
                       if(data.info!=null){
                           $scope.addInfo.leaguerName=data.info.leaguerName;
                       }
                    }
                });
        }
    };

    $scope.submitting = false;
    $scope.addInfoSubmit=function(){
        if(Number($scope.addInfo.accScore)>0){
            var reg=/^[1-9]\d*$/;
            if(!reg.test(Number($scope.addInfo.accScore))){
                $scope.notice("数量格式不对!");
                return;
            }
        }else{
            $scope.notice("数量格式不对!");
            return;
        }
        if($scope.addInfo.rewardEffeDay==null||$scope.addInfo.rewardEffeDay==""){
            $scope.notice("有效期不能为空!");
            return;
        }else{
            if(Number($scope.addInfo.rewardEffeDay)>0){
                var reg=/^[1-9]\d*$/;
                if(!reg.test(Number($scope.addInfo.rewardEffeDay))){
                    $scope.notice("有效期格式不对!");
                    return;
                }
            }else{
                $scope.notice("有效期格式不对!");
                return;
            }
        }
        if ($scope.submitting) {
            return;
        }
        $scope.submitting = true;
        $http.post("scoreAccountAction/addScore","info="+angular.toJson($scope.addInfo)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.query(1);
                    $scope.hideModal();
                }
                $scope.notice(data.msg);
                $scope.submitting = false;
            })
            .error(function(data){
                $scope.notice(data.msg);
                $scope.submitting = false;
            });
    };


    //作废
    $scope.cancelScore=function(entity){
        SweetAlert.swal({
                title: "确认作废？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("scoreAccountAction/cancelScore","id="+entity.id+"&businessNo="+entity.businessNo,
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