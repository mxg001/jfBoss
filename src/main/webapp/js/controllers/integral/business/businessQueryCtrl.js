/**
 * 业务组表
 */
angular.module('inspinia').controller('businessQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

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
            { field: 'businessNo',displayName:'业务组编号',width:180},
            { field: 'businessName',displayName:'业务组名称',width:180 },
            { field: 'link',displayName:'链接',width:180 },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',width:300,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a target="_blank" ui-sref="management.businessDetail({businessNo:row.entity.businessNo})">详情</a> ' +
                '<a target="_blank" ng-show="grid.appScope.hasPermit(\'businessInfoAction.businessTaskPreview\')" ui-sref="management.businessTaskPreview({id:row.entity.id,businessNo:row.entity.businessNo})"> | 预览</a> ' +
                '<a target="_blank" ng-show="grid.appScope.hasPermit(\'businessInfoAction.updateBusinessBasic\')" ui-sref="management.businessBasic({businessNo:row.entity.businessNo})"> | 基本资料</a> ' +
                '<a target="_blank" ng-show="grid.appScope.hasPermit(\'businessInfoAction.addBusinessTask\')" ui-sref="management.businessTask({businessNo:row.entity.businessNo})"> | 任务选择</a> ' +
                '<a target="_blank" ng-show="grid.appScope.hasPermit(\'activityChoice.addActivityList\')" ui-sref="management.activityChoice({businessNo:row.entity.businessNo})"> | 活动选择</a> ' +
                 '</div>'
            }
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
        $http.post("businessInfoAction/getSelectPageList","info="+angular.toJson($scope.info)+"&pageNo="+
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



    $scope.addModal = function(){
        $scope.addInfo={businessNo:""};
        $('#addInfoModal').modal('show');
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
        if($scope.addInfo.businessNo==null||$scope.addInfo.businessNo==""){
            $scope.notice("业务组不能为空!");
            return;
        }else{
            angular.forEach($scope.businessList,function(item){
                if($scope.addInfo.businessNo == item.value){
                    $scope.addInfo.businessName=item.text;
                }
            })
        }
        $scope.submitting = true;
        $http.post("businessInfoAction/addBusinessInfo","info="+angular.toJson($scope.addInfo),
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

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});