/**
 * 会员列表
 */
angular.module('inspinia').controller('goodsManagementQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //物品类型
    $scope.goodsTypeSelect = angular.copy($scope.goodsTypeList);
    $scope.goodsTypeSelect.unshift({text:"全部",value:""});
    $scope.goodsTypeStr=angular.toJson($scope.goodsTypeSelect);

    $scope.goodsTypeSelectAdd = angular.copy($scope.goodsTypeList);
    $scope.goodsTypeSelectAdd.unshift({text:"请选择",value:""});

    //清空
    $scope.clear=function(){
        $scope.info={goodsType:"",businessNo:""};
    };
    $scope.clear();

    $scope.businessNoList=[];
    $scope.businessNoListAdd=[];
    $http.post("businessInfoAction/getBusinessInfoListByUser",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.businessNoList.push({value:"",text:"全部"});
                $scope.businessNoListAdd.push({value:"",text:"请选择"});
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.businessNoList.push({value:list[i].businessNo, text:list[i].businessName});
                        $scope.businessNoListAdd.push({value:list[i].businessNo, text:list[i].businessName});
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
            { field: 'id',displayName:'ID',width:80},
            { field: 'goodsCode',displayName:'物品编码',width:180 },
            { field: 'goodsName',displayName:'物品名称',width:180 },
            { field: 'goodsType',displayName:'物品类型',width:150,cellFilter:"formatDropping:" +  $scope.goodsTypeStr},
            { field: 'businessName',displayName:'业务组',width:180 },
            { field: 'originId',displayName:'来源ID',width:180 },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'operator',displayName:'操作人',width:180 },
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                    '<a ng-show="grid.appScope.hasPermit(\'goodsManagement.updateGood\')" ng-click="grid.appScope.addModal(1,row.entity)">修改 </a> ' +
                    '<a ng-show="grid.appScope.hasPermit(\'goodsManagement.deleteGood\')" ng-click="grid.appScope.deleteInfo(row.entity.id,row.entity.businessNo)" > | 删除</a> ' +
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
        $http.post("goodsManagement/getSelectPageList","info="+angular.toJson($scope.info)+"&pageNo="+
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
                    $scope.exportInfoClick("leaguerAction/importDetail",{"info":angular.toJson($scope.info)});
                }
            });
    };

    //新增
    $scope.addModal = function(sta,entity){
        $scope.editSta=sta;
        if($scope.editSta==1){
            $scope.addInfo=angular.copy(entity);
            $scope.addTitle="修改物品";
        }else{
            $scope.addInfo={goodsName:"",goodsType:"",businessNo:"",originId:""};
            $scope.addTitle="新增物品";
        }
        $('#addInfoModal').modal('show');
    };
    $scope.hideModal= function(){
        $('#addInfoModal').modal('hide');
    };
    //新增提交
    $scope.submitting = false;
    $scope.addInfoSubmit=function(){
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;
        var addUrl="";
        if($scope.editSta==1){//编辑
            addUrl="goodsManagement/updateGood";
        }else{//新增
            addUrl="goodsManagement/addGood";
        }
        $http.post(addUrl,"info="+angular.toJson($scope.addInfo)+"&businessNo="+$scope.addInfo.businessNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.hideModal();
                    $scope.query();
                }
                $scope.notice(data.msg);
                $scope.submitting = false;
            })
            .error(function(data){
                $scope.notice(data.msg);
                $scope.submitting = false;
            });
    };

    //删除
    $scope.deleteInfo=function(id,businessNo){
        SweetAlert.swal({
                title: "确认删除？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("goodsManagement/deleteGood","id="+id+"&businessNo="+businessNo,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.query();
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
                $scope.query();
            }
        })
    });
});