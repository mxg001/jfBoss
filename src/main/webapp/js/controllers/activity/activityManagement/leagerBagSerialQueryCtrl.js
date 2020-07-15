/**
 * 全民活动出入账记录查询
 */
angular.module('inspinia').controller('leagerBagSerialQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //下发状态
    $scope.accTypeSelect = [{text:"全部",value:"0"},{text:"返现",value:"1"},{text:"提现",value:"2"}];
    $scope.accTypeStr=angular.toJson($scope.accTypeSelect);

    //清空
    $scope.clear=function(){
        $scope.info={accType:"0",leaguerNo:"",originUserNo:"",mobilePhone:"",
            createTimeStart:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    };
    $scope.clear();

    $scope.info={accType:"0"}

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'id',displayName:'序号',width:100},
            { field: 'accType',displayName:'出入类型',width:100,cellFilter:"formatDropping:" +  $scope.accTypeStr},
            { field: 'actName',displayName:'活动名称',width:230 },
            { field: 'accAmount',displayName:'金额',width:120 },
            { field: 'leaguerNo',displayName:'会员编号',width:120 },
            { field: 'originUserNo',displayName:'用户编号',width:180 },
            { field: 'leaguerName',displayName:'用户名称',width:180 },
            { field: 'mobilePhone',displayName:'手机号',width:180 },
            { field: 'createTime',displayName:'处理时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180}
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
    $scope.loadImg = false;

    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("activityManagement/getLeagerBagSerialList","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
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
                    $scope.exportInfoClick("activityManagement/importLeagerBagSerial",{"info":angular.toJson($scope.info)});
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