/**
 * 业务组-抽奖
 */
angular.module('inspinia',['uiSwitch']).controller('busLuckDrawpConfigCtrl',function($scope,$http,i18nService,SweetAlert,$document,$stateParams,$state){

    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.actCode=$stateParams.actCode;
    $scope.businessNo=$stateParams.businessNo;

    //抽奖条件
    $scope.expendTypeSelect = [{text:"积分抽奖",value:"1"},{text:"交易抽奖",value:"2"}];
    $scope.expendTypeStr=angular.toJson($scope.expendTypeSelect);

    $scope.expendTypeSta=true;
    $scope.expendTypeChange=function(dataInit){
        if( $scope.addInfo!=null&& $scope.addInfo.luckDrawpDetail!=null
            &&$scope.addInfo.luckDrawpDetail.expendType=="2"){
            $scope.expendTypeSta=false;
        }else{
            $scope.expendTypeSta=true;

        }
        if(dataInit){
            $scope.addInfo.luckDrawpDetail.expendNum=null;
            $scope.addInfo.luckDrawpDetail.amountRangeMin=null;
        }
    }

    $scope.addInfo={};
    $scope.getLuckDrawpConfig=function(){
        $http.post("activityChoice/getLuckDrawpConfig",
            "actCode="+$scope.actCode+"&businessNo="+$scope.businessNo,
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
                    $scope.result=data.info.prizeInfoList;
                    $scope.expendTypeChange(false);
                }else{
                    $scope.notice(data.msg);
                }
            });
    };
    $scope.getLuckDrawpConfig();

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'awardCode',displayName:'奖项编号',width:150},
            { field: 'awardDesc',displayName:'奖项说明',width:180},
            { field: 'awardName',displayName:'奖品名称',width:180},
            { field: 'openStatusInt',displayName: '是否有效',width:180,cellTemplate:
                    '<span ng-show="grid.appScope.hasPermit(\'activityChoice.closeOpenPrize\')"><switch class="switch switch-s" ng-model="row.entity.openStatusInt" ng-true-value="0" ng-false-value="1" ng-change="grid.appScope.closeOpenPrize(row.entity)" /></span>'
                    +'<span ng-show="!grid.appScope.hasPermit(\'activityChoice.closeOpenPrize\')"> <span ng-show="row.entity.openStatusInt==1">开启</span><span ng-show="row.entity.openStatusInt==0">关闭</span></span>'
            },
            { field: 'seqNo',displayName:'序列',width:120},
            { field: 'awardNum',displayName:'奖品数量',width:120},
            { field: 'numSurplus',displayName:'奖品剩余',width:120},
            { field: 'awardOdds',displayName:'中奖概率%',width:120},
            { field: 'dayNums',displayName:'每天最多派发',width:120},
            { field: 'dayNumSurplus',displayName:'今日剩余',width:120},
            { field: 'dayMax',displayName:'每天最多中奖',width:120},
            { field: 'id',displayName:'操作',width:200,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                    '<a target="_blank" ui-sref="management.busPrizeDetail({businessNo:grid.appScope.businessNo,awardCode:row.entity.awardCode,source:3})" >详情</a> ' +
                    '<a target="_blank" ui-sref="management.blacklistBack({businessNo:grid.appScope.businessNo,actCode:grid.appScope.actCode,awardCode:row.entity.awardCode})" > | 黑名单</a> ' +
                    '<a target="_blank" ng-show="grid.appScope.hasPermit(\'activityChoice.editLuckPrize\')&&row.entity.openStatusInt==0" ui-sref="management.busPrizeEdit({businessNo:grid.appScope.businessNo,awardCode:row.entity.awardCode})" > | 修改</a> ' +
                    '<a ng-show="grid.appScope.hasPermit(\'activityChoice.deletePrize\')&&row.entity.openStatusInt==0" ng-click="grid.appScope.deletePrize(row.entity.id)" > | 删除 </a> ' +
                    '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };


    $scope.saveLuckDrawpConfig=function(){
        if($scope.addInfo.luckDrawpDetail.expendType=="1"){
            if($scope.addInfo.luckDrawpDetail.expendNum==null||$scope.addInfo.luckDrawpDetail.expendNum===""){
                $scope.notice("抽奖条件-每次抽奖消耗积分不能为空!");
                return;
            }else{
                var reg=/^[1-9]\d*|0$/;
                if(!reg.test($scope.addInfo.luckDrawpDetail.expendNum)){
                    $scope.notice("抽奖条件-每次抽奖消耗积分格式不对!");
                    return;
                }
            }
        }else{
            if($scope.addInfo.luckDrawpDetail.amountRangeMin==null||$scope.addInfo.luckDrawpDetail.amountRangeMin===""){
                $scope.notice("抽奖条件-金额不能为空!");
                return;
            }else{
                var reg=/^([1-9]\d{0,9}|0)(\.\d{1,2})?$/;
                if(!reg.test($scope.addInfo.luckDrawpDetail.amountRangeMin)){
                    $scope.notice("抽奖条件-金额格式不对!");
                    return;
                }
            }
        }
        $scope.addInfoSub=angular.copy($scope.addInfo);
        $scope.addInfoSub.actRule=null;
        $scope.addInfoSub.actLink=null;
        var data={
            info:angular.toJson($scope.addInfoSub),
            awardDesc:$scope.addInfo.awardDesc,
            actRule:$scope.addInfo.actRule,
            businessNo:$scope.businessNo
        };
        var postCfg = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };
        $http.post("activityChoice/saveLuckConfig",data,postCfg)
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('management.activityChoice',{businessNo:$scope.businessNo},{reload:true});
                }else{
                    $scope.notice(data.msg);
                }
            });
    };

    $scope.addPrizeClick=function(){
        if($scope.addInfo.actCode!=null&&$scope.addInfo.actCode!=""
            &&$scope.addInfo.luckDrawpDetail!=null
            &&$scope.addInfo.luckDrawpDetail.actDetCode!=null&&$scope.addInfo.luckDrawpDetail.actDetCode!=""){
            window.open('welcome.do#/management/busPrizeAdd/'+$scope.businessNo+'/'+$scope.addInfo.actCode+'/'+$scope.addInfo.luckDrawpDetail.actDetCode, 'view_window');
        }else{
            $scope.notice("请先保存抽奖基础配置信息!");
        }
    };


    $scope.closeOpenPrize=function(entity){
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
                    $http.post("activityChoice/closeOpenPrize","id="+entity.id+"&status="+sta+"&businessNo="+$scope.businessNo,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.getLuckDrawpConfig();
                            }else{
                                $scope.notice(data.msg);
                                $scope.callBackSwitch(entity);
                            }
                        })
                        .error(function(data){
                            $scope.notice(data.msg);
                            $scope.callBackSwitch(entity);
                        });
                }else{
                    $scope.callBackSwitch(entity);
                }
            });
    };
    $scope.callBackSwitch=function (entity) {
        if(entity.openStatusInt==false){
            entity.openStatusInt=true;
        }else{
            entity.openStatusInt=false;
        }
    };


    $scope.deletePrize=function(id){
        SweetAlert.swal({
                title: "确认删除?",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("activityChoice/deletePrize","id="+id+"&businessNo="+$scope.businessNo,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.getLuckDrawpConfig();
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


    $scope.winningListInfo={};
    $scope.showConfig={};
    //中奖记录配置显示
    $scope.addDataModal = function(){
        $http.post("activityChoice/getPrizeDisplay",
            "actCode="+$scope.actCode+"&businessNo="+$scope.businessNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.showConfig=data.info;
                    if($scope.showConfig!=null){
                        $scope.winningListInfo=$scope.showConfig.winningList;
                    }
                    $('#addInfoModal').modal('show');
                }
            });
    };

    $scope.hideDataModal= function(){
        $('#addInfoModal').modal('hide');
    };
    $scope.addDataSubmit=function(){
        if($scope.showConfig==null){
            $scope.showConfig={actCode:$scope.actCode,businessNo:$scope.businessNo};
        }
        $scope.showConfig.winningList = $scope.winningListInfo;
        $http.post("activityChoice/savePrizeDisplay",
            "info="+angular.toJson($scope.showConfig)+"&businessNo="+$scope.businessNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.hideDataModal();
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.notice(data.msg);
            });
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
});