/**
 * 范围管理-部分-详情
 */
angular.module('inspinia',['angularFileUpload']).controller('rangeManagementQueryDetailCtrl',function($scope,$http,i18nService,SweetAlert,$document,$state,$stateParams,FileUploader){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.businessNo=$stateParams.businessNo;
    $scope.actCode=$stateParams.actCode;

    //操作方式
    $scope.typeSelect = [{text:"覆盖导入",value:"1"},{text:"追加导入",value:"2"}];
    $scope.typeStr=angular.toJson($scope.typeSelect);

    //清空
    $scope.clear=function(){
        $scope.info={actCode:$scope.actCode};
    };
    $scope.clear();

    $scope.title="";
    $scope.getActCodeInfo=function(){
        $http.post("activityChoice/getActCodeInfo","actCode="+$scope.actCode+"&businessNo="+$scope.businessNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.actCodeInfo=data.info;
                    $scope.title=$scope.actCodeInfo.actName+"("+$scope.actCodeInfo.actCode+")";
                }else{
                    $scope.notice(data.msg);
                }
            })
    };
    $scope.getActCodeInfo();

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'id',displayName:'ID',width:80},
            { field: 'leaguerNo',displayName:'会员编号',width:180 },
            { field: 'leaguerName',displayName:'会员名称',width:180 },
            { field: 'businessName',displayName:'业务组',width:180 },
            { field: 'originUserNo',displayName:'用户编号',width:180 },
            { field: 'mobilePhone',displayName:'用户手机号',width:180 },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'operator',displayName:'创建人',width:180 },
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

    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("activityChoice/getRangeNameList","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize+"&businessNo="+$scope.businessNo,
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

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});