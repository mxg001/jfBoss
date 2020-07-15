/**
 * 会员详情
 */
angular.module('inspinia').controller('leaguerDetailCtrl',function($scope,$http,i18nService,SweetAlert,$stateParams,$document){
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

    $scope.leaguerGrowList=[];
    $scope.leaguerScoreList=[];
    $scope.scoreAccountList=[];

    /**
     * 获取敏感数据
     */
    $scope.dataSta=true;
    $scope.getDataProcessing=function(init){
        var url="";
        if(init){
            url="leaguerAction/getLeaguerInfo";
        }else{
            url="leaguerAction/leaguerDetailSensitive";
            $scope.dataSta=false;
        }
        $http.post(url,
            "id="+$stateParams.id+"&businessNo="+$stateParams.businessNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.info=data.info;
                    if($scope.info!=null){
                        $scope.leaguerGrowList=data.info.leaguerGrowList;
                        $scope.leaguerScoreList=data.info.leaguerScoreList;
                    }
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.notice(data.msg);
            });
    };
    $scope.getDataProcessing(true);


    $scope.growGrid={                           //配置表格
        data: 'leaguerGrowList',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'createTime',displayName:'时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:200},
            { field: 'beforeLevelName',displayName:'原等级',width:200 },
            { field: 'afterLevelName',displayName:'更新后等级',width:200 }
        ],
        onRegisterApi: function(gridApi) {
            $scope.growGridApi = gridApi;
        }
    };

    $scope.leaguerScoreGrid={                           //配置表格
        data: 'leaguerScoreList',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'scoreBalance',displayName:'剩余积分',width:200 },
            { field: 'scoreUsedBalance',displayName:'已使用积分',width:200 },
            { field: 'scoreExpBalance',displayName:'过期积分',width:200 },
            { field: 'scoreBatchMonth',displayName:'本月到期积分',width:200 }
        ],
        onRegisterApi: function(gridApi) {
            $scope.leaguerScoreGridApi = gridApi;
        }
    };

    $scope.scoreAccountGrid={                   //配置表格
        data: 'scoreAccountList',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'accountSerialNo',displayName:'出入账流水号',width:180},
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'accType',displayName:'积分出入账类型',width:150,cellFilter:"formatDropping:" +  $scope.accTypeStr},
            { field: 'accScore',displayName:'记账积分',width:180 },
            { field: 'fromType',displayName:'来源类型',width:150,cellFilter:"formatDropping:" +  $scope.fromTypeStr},
            { field: 'fromDesc',displayName:'来源描述',width:260 },
            { field: 'beforeScore',displayName:'记账前积分',width:180 },
            { field: 'afterScore',displayName:'记账后积分',width:180 },
            { field: 'fromSystem',displayName:'来源系统',width:150,cellFilter:"formatDropping:" +  $scope.fromSystemStr},
            { field: 'operater',displayName:'操作人',width:180 }
        ],
        onRegisterApi: function(gridApi) {
            $scope.scoreAccountGridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query(2);
            });
        }
    };

    $scope.clear=function(){
        $scope.addInfo={accType:"",fromType:"",
            createTimeBegin:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    };
    $scope.clear();


    $scope.loadImg = false;
    $scope.query=function(sta){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $scope.addInfo.leaguerNo=$scope.info.leaguerNo;
        $http.post("scoreAccountAction/getUserAccountList",
            "info="+angular.toJson($scope.addInfo)+
            "&businessNo="+$stateParams.businessNo+
            "&pageNo="+$scope.paginationOptions.pageNo+
            "&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.scoreAccountList=data.page.result;
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
            $http.post("scoreAccountAction/getUserAccountListCount",
                "info="+angular.toJson($scope.addInfo)+"&businessNo="+$stateParams.businessNo,
                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .success(function(data){
                    if(data.status){
                        $scope.scoreAccountGrid.totalItems=data.total;
                    }else{
                        $scope.notice(data.msg);
                    }
                })
        }
    };

});