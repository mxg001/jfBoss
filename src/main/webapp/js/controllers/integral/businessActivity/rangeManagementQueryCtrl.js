/**
 * 范围管理-部分
 */
angular.module('inspinia',['angularFileUpload']).controller('rangeManagementQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document,$state,$stateParams,FileUploader){
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
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a ng-show="grid.appScope.hasPermit(\'activityChoice.deleteRangeName\')" ng-click="grid.appScope.deleteRangeName(row.entity)"">删除</a> ' +
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

    $scope.addModal = function(){
        $scope.addInfo={actCode:$scope.actCode,leaguerStr:""};
        $('#addInfoModal').modal('show');
    };
    $scope.hideModal= function(){
        $('#addInfoModal').modal('hide');
    };

    //保存名单
    $scope.submitting = false;
    $scope.commitInfo = function(){
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;
        $http.post("activityChoice/addRangeName","info="+angular.toJson($scope.addInfo)+"&businessNo="+$scope.businessNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.hideModal();
                    $scope.query();
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.submitting = false;
                $scope.notice(data.msg);
            });
    };



    // 删除
    $scope.deleteRangeName = function (entity) {
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
                    $http.post("activityChoice/deleteRangeName","info="+angular.toJson(entity)+"&businessNo="+$scope.businessNo,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
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
    //获取商户信息
    $scope.merchantNoChange=function () {
      if($scope.addInfo.leaguerStr!=null&&$scope.addInfo.leaguerStr!=""){
          $http.post("leaguerAction/getLeaguerInfoByBus","leaguerNo="+$scope.addInfo.leaguerStr+"&businessNo="+$scope.businessNo,
              {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
              .success(function(data){
                  if(data.status){
                      if(data.info!=null){
                          $scope.addInfo.leaguerNo=data.info.leaguerNo;
                          $scope.addInfo.leaguerName=data.info.leaguerName;
                      }else{
                          $scope.addInfo.leaguerNo=null;
                          $scope.addInfo.leaguerName=null;
                      }
                  }else{
                      $scope.notice(data.msg);
                  }
              });
      }
    };


    //导入数据
    $scope.importDiscountShow = function(){
        $scope.importInfo={type:"1",actCode:$scope.actCode};
        $('#importDiscount').modal('show');
    };
    $scope.importDiscountHide = function(){
        $('#importDiscount').modal('hide');
    };

    //上传图片,定义控制器路径
    var uploader = $scope.uploader = new FileUploader({
        url: 'activityChoice/importMember',
        formData:[{actCode:"",businessNo:"",type:""}],
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploader.filters.push({
        name: 'isFile',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
        if(response.status){
            $scope.notice(response.msg);
            $scope.query();
            $scope.importDiscountHide();
        }else{
            $scope.notice(response.msg);
        }
        $scope.loadImg1=false;
    };
    $scope.loadImg1=false;
    //导入
    $scope.importDiscount=function(){
        if(uploader.queue.length<1){
            $scope.notice("文件不能为空!");
            return;
        }
        $scope.uploader.queue[0].formData[0].actCode=$scope.importInfo.actCode;
        $scope.uploader.queue[0].formData[0].businessNo=$scope.businessNo;
        $scope.uploader.queue[0].formData[0].type=$scope.importInfo.type;
        $scope.loadImg1=true;
        uploader.uploadAll();//上传
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