angular.module('inspinia',['uiSwitch']).controller('busCashMeetConfigDetailCtrl',function($scope,$http,i18nService,SweetAlert,$document,$stateParams,$state){

    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //会员活动有效期类型
    $scope.expriryDateTypeListSelect = angular.copy($scope.expriryDateTypeList);
    $scope.expriryDateTypeStr=angular.toJson($scope.expriryDateTypeListSelect);

    $scope.addInfo={};
    $scope.getLuckDrawpConfig=function(){
        $http.post("activityChoice/getActivity",
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
                }else{
                    $scope.notice(data.msg);
                }
            });
    };
    $scope.getLuckDrawpConfig();

    $scope.getCashMeetAfterConfig=function(){
        $http.post("activityManagement/getAfterCondition",
            "actCode="+$stateParams.actCode,
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
            "actCode="+$stateParams.actCode,
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
            { field: 'id',displayName:'操作',width:150,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                    '<a target="_blank" ui-sref="activities.cashMeetAfterConditionDetail({actCode:row.entity.actCode,actDetCode:row.entity.actDetCode,source:1})" >详情</a> ' +
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
            { field: 'activeName',displayName:'活动内容',width:180},
            { field: 'expiryDateType',displayName:'有效期类型',width:180,cellFilter:"formatDropping:" +  $scope.expriryDateTypeStr},
            { field: 'expiryNum',displayName:'有效期',width:180},
            { field: 'expendNumber',displayName:'单日奖励次数上限',width:120},
            { field: 'allNum',displayName:'奖励总次数',width:120},
            { field: 'isLinkBehind',displayName:'关联后置条件',width:120},
            { field: 'id',displayName:'操作',width:150,pinnedRight:true, cellTemplate:
                    '<div class="lh30">'+
                    '<a target="_blank" ui-sref="activities.activityContentDetail({actCode:row.entity.actCode,actDetCode:row.entity.actDetCode})" >详情</a> ' +
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

    $scope.addCashMeetActivityContent=function(){
        window.open('welcome.do#/activities/activityContentAdd/'+$scope.addInfo.actCode, 'view_window');
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
                    $state.transitionTo('activities.activityManagement',null,{reload:true});
                }else{
                    $scope.notice(data.msg);
                }
            });
    };

})
.filter('trustHtml', function ($sce) {
    return function (input) {
        return $sce.trustAsHtml(input);
    }
});