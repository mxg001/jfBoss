/**
 * 黑名单查询
 */
app = angular.module('inspinia',['ngSanitize', 'ui.select']);

app.filter('propsFilter', function() {
    return function(items, props) {
        var out = [];

        if (angular.isArray(items)) {
            var keys = Object.keys(props);

            items.forEach(function(item) {
                var itemMatches = false;

                for (var i = 0; i < keys.length; i++) {
                    var prop = keys[i];
                    var text = props[prop].toLowerCase();
                    if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                        itemMatches = true;
                        break;
                    }
                }

                if (itemMatches) {
                    out.push(item);
                }
            });
        } else {
            out = items;
        }

        return out;
    };
});

app.controller('activityBlackListQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document,$stateParams,$state){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.actDetCode = $stateParams.actDetCode;

    $scope.info = {serviceNo:$scope.actDetCode,leaguer:{id:null,name:null}}
    $scope.addInfo = {serviceNo:$scope.actDetCode}

    //清空
    $scope.clear=function(){
        $scope.info={leaguerNo:""};
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
            { field: 'originUserNo',displayName:'用户编号',width:230 },
            { field: 'leaguerName',displayName:'用户名称',width:120 },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'operater',displayName:'操作人',width:180 },
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a ng-click="grid.appScope.deleteBlack(row.entity)">删除</a> ' +
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
        $scope.info.serviceNo = $scope.actDetCode;
        $scope.loadImg = true;
        $http.post("activityManagement/getActivityBlackList","info="+angular.toJson($scope.info)+"&pageNo="+
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
    };
    $scope.query();

    $scope.addActivityBlackShow = function(){
        $('#addActivityBlack').modal('show');
    };
    $scope.addActivityBlackHide = function(){
        $('#addActivityBlack').modal('hide');
    };

    $scope.deleteBlack=function (entry) {
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
                    $http.post("activityManagement/deleteActivityBlack","serviceNo="+$scope.actDetCode+"&leaguerNo="+entry.leaguerNo,
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

    $scope.saveActivityBlack=function(){
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
        $http.post("activityManagement/saveActivityBlack",data,postCfg)
            .success(function(data){
                if(data.status){
                    $scope.addActivityBlackHide();
                    $scope.notice(data.msg);
                    window.open('welcome.do#/activities/activityBlackQuery/'+$scope.actDetCode, '_self');
                    $scope.query();
                }else{
                    $scope.notice(data.msg);
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


    $scope.testArr = [];

    $scope.queryChange = function () {
        no = $scope.info.leaguer.leaguerNo;
        $scope.info.leaguerNo = no;
    }

    $scope.addChange = function () {
        no = $scope.addInfo.leaguer.leaguerNo;
        $scope.addInfo.leaguerNo = no;
    }

    $scope.refreshAddresses = function (value) {
        if(value){
            var params = {key: value};
            return $http.get('leaguerAction/findLeaguerInfoByUserNo', {params: params})
                .then(function(data) {
                    if(data.status){
                        var info = data.data.info;
                        $scope.testArr = [];
                        for(i=0; i<info.length; i++){
                            var infoId = info[i].originUserNo;
                            var infoName = info[i].leaguerName;
                            var leaguerNo = info[i].leaguerNo;
                            if(infoId && infoName){
                                infoName = infoName+"("+infoId+")";
                                $scope.testArr.push({id: infoId,name: infoName,leaguerNo: leaguerNo});
                            }
                        }
                    }else{
                        $scope.notice(data.msg);
                    }
                });
        }
    }


});
