/**
 * 中奖记录
 */
angular.module('inspinia').controller('winningRecordQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){

    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //中奖状态
    $scope.lotteryStatusSelect = [{text:"全部",value:""},{text:"已中奖",value:"1"},{text:"已发奖",value:"2"},{text:"未中奖",value:"3"}];
    $scope.lotteryStatusStr=angular.toJson($scope.lotteryStatusSelect);

    //清空
    $scope.clear=function(){
        $scope.info={lotteryStatus:"",
            createTimeBegin:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
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
            { field: 'id',displayName:'序号',width:100},
            { field: 'lotteryNo',displayName:'编号',width:210},
            { field: 'lotteryCode',displayName:'奖品码',width:210},
            { field: 'awardDesc',displayName:'奖项说明',width:180},
            { field: 'awardName',displayName:'奖品名称',width:180},
            { field: 'lotteryStatus',displayName:'状态',cellFilter:"formatDropping:" +  $scope.lotteryStatusStr,width:180},
            { field: 'actName',displayName:'活动名称',width:180},
            { field: 'leaguerNo',displayName:'会员编号',width:210},
            { field: 'originUserNo',displayName:'用户编号',width:180},
            { field: 'leaguerName',displayName:'会员名称',width:180},
            { field: 'mobilePhone',displayName:'手机号',width:180},
            { field: 'createTime',displayName:'抽奖时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180}
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query(1);
            });
        }
    };

    $scope.loadImg = false;
    $scope.query=function(countSta){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("winningRecord/getSelectPageList","info="+angular.toJson($scope.info)+"&pageNo="+
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
        if(countSta==2){
            $http.post("winningRecord/getWinningRecordCount","info="+angular.toJson($scope.info),
                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .success(function(data){
                    if(data.status){
                        $scope.userGrid.totalItems=data.total;
                    }else{
                        $scope.notice(data.msg);
                    }
                })
                .error(function(data){
                    $scope.notice(data.msg);
                });
        }
    };
    // 导出
    $scope.exportInfo = function () {
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
                    $scope.exportInfoClick("winningRecord/importDetail",{"info":angular.toJson($scope.info)});
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