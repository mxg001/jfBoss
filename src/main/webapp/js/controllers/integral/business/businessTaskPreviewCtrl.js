/**
 * 业务组表
 */
angular.module('inspinia').controller('businessTaskVipLevelPreview',function($scope,$http,i18nService,SweetAlert,$document,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.businessNo=$stateParams.businessNo;

    $scope.tableHeaderData = null;

    //创建表头
    $scope.createHeader = function(data){
        $scope.tableHeader = [];//表头
        $scope.tableHeader.push({field: 'rewardName',displayName:'权益名称',width:100,cellTemplate:'<input rewardCode="{{row.entity.rewardCode}}"  name="rewardName" ng-model="row.entity.rewardName" style="line-height: 26px; border: 1px solid #eee; width: 90%; padding: 0 1%; margin-top: 4px;"  type="text"/>'});
        $.each(data,function(index,val){
            $scope.tableHeader.push({field:val.name,displayName:val.name,width:100,cellTemplate:'<input level="'+val.level+'" name="{{row.entity.rewardCode}}" ng-checked="row.entity.level'+index+'==1" type="checkbox"/>' });
        });
        $scope.tableHeader.push({ field: 'rewardName',displayName:'操作',width:120,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a ng-click="grid.appScope.delBusinessTaskPreview(row,rowRenderIndex)">删除</a> ' +
                '</div>'
        });
    };

    //获取表头
    $scope.getTableHeader = function(){
        $.ajax({
            url:"businessInfoAction/getBusinessTaskPreviewTableHeader",
            async:false,
            data:{"businessNo":$scope.businessNo},
            success:function(res){
                if(res.status){
                    $scope.createHeader(res.data);
                    $scope.tableHeaderData = angular.copy(res.data);
                }else{
                    $scope.notice(res.msg);
                }
              },error:function(data){
                $scope.notice(data.msg);
              }
            });
    };
    $scope.getTableHeader();


    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:$scope.tableHeader,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };

    $scope.query=function(){
        $http.post("businessInfoAction/businessTaskPreview","businessNo="+$scope.businessNo,
          {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
          .success(function(data){
              if(data.status){
                  $scope.result=data.list;
              }else{
                  $scope.notice(data.msg);
              }
          })
          .error(function(data){
              $scope.notice(data.msg);
          });
    };
    $scope.query();

    var initNum=100;
    //直接保存一行 刷新页面
    $scope.addBusinessTaskPreview = function(){
        initNum=initNum+1;
        $scope.row={rewardCode:"HM"+initNum,rewardName:""};
        $.each($scope.tableHeaderData,function(index,val){
            var str=val.level;
            $scope.row.str="0";
        });
        $scope.result.push($scope.row);
    };

    $scope.save = function(){
        $scope.rewardLevel = [];
        if($scope.result!=null&&$scope.result.length>0){
            for(var i=0;i<$scope.result.length;i++){
                if($scope.result[i].rewardName==null||$scope.result[i].rewardName==""){
                    $scope.notice("权益名称不能为空!");
                    return;
                }
            }
            $.each($("input[name='rewardName']"),function (i,data) {
                var rewardCode = $(data).attr("rewardCode");
                var levels = new Array();
                var isSelect = new Array();
                $.each($("input[name="+rewardCode+"]"),function (lIndex,level) {
                    //levels["level"+lIndex] = level.checked?1:0;
                    isSelect.push(level.checked?1:0);
                    levels.push($(level).attr("level"));
                });
                $scope.rewardLevel.push({"rewardName":data.value,"isSelect":isSelect,"rewardCode":rewardCode,"levels":levels});
            });
        }

        $http.post("businessInfoAction/saveBatchBusinessTaskPreview","rewardLevel="+angular.toJson($scope.rewardLevel)+"&businessNo="+$scope.businessNo,
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

    $scope.delBusinessTaskPreview=function (row,index) {
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
                    $scope.result.splice(index, 1);
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