/**
 * 用户激活
 */
angular.module('inspinia',['angularFileUpload']).controller('activityConfigCtrl',function($scope,$http,i18nService,SweetAlert,$document,$stateParams,$state,FileUploader){

    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //内容类型
    $scope.activeTypeSelect = [{text:"全部",value:""},{text:"奖励赠送",value:"1"},{text:"达标奖励",value:"2"},{text:"交易返现",value:"3"}];
    $scope.activeTypeStr=angular.toJson($scope.activeTypeSelect);

    $scope.addInfo={};
    $scope.getActivityTemp=function(){
        $http.post("activityManagement/getActivityTemp",
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
                    $scope.result=data.info.actiDetailList;
                }else{
                    $scope.notice(data.msg);
                }
            });
    };
    $scope.getActivityTemp();

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'actDetCode',displayName:'编码',width:150},
            { field: 'activeName',displayName:'活动内容',width:150},
            { field: 'activeType',displayName:'类型',cellFilter:"formatDropping:" +  $scope.activeTypeStr,width:180},
            { field: 'activeDesc',displayName:'活动说明',width:250},
            { field: 'id',displayName:'操作',width:150,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                    '<a target="_blank" ui-sref="activities.contentDetail({actDetCode:row.entity.actDetCode,source:3})" >详情</a> ' +
                    '<a target="_blank" ng-show="grid.appScope.hasPermit(\'activityManagement.editActiInfo\')" ui-sref="activities.contentEdit({actDetCode:row.entity.actDetCode})" > | 修改</a> ' +
                    '<a ng-show="grid.appScope.hasPermit(\'activityManagement.deleteActiInfo\')" ng-click="grid.appScope.deleteActiInfo(row.entity.actDetCode)" > | 删除 </a> ' +
                '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };

    //上传图片,定义控制器路径
    var uploaderImg = $scope.uploaderImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            if(item.size>500*1024){
                $scope.notice("上传图片太大了!");
                return false;
            }
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });

    uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.saveActivityTemp(response.url);
        }
    };
    $scope.submitting = false;
    //新增
    $scope.commitInfo = function(){
        if($scope.submitting){
            return;
        }
        if(uploaderImg.queue.length<1){
            if($scope.addInfo.actImg==null||$scope.addInfo.actImg==""){
                $scope.notice("图片不能为空!");
                return;
            }else{
                $scope.submitting = true;
                $scope.saveActivityTemp(null);
            }
        }else{
            $scope.submitting = true;
            uploaderImg.uploadAll();// 上传消息图片
        }
    };

    $scope.saveActivityTemp=function(url){
        $scope.addInfoSub=angular.copy($scope.addInfo);
        $scope.addInfoSub.actImg=url;
        $scope.addInfoSub.awardDesc=null;
        $scope.addInfoSub.actRule=null;
        var data={
            info:angular.toJson($scope.addInfoSub),
            awardDesc:$scope.addInfo.awardDesc,
            actRule:$scope.addInfo.actRule
        };
        var postCfg = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };
        $http.post("activityManagement/saveActivityTemp",data,postCfg)
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('activities.activityManagement',null,{reload:true});
                }else{
                    $scope.notice(data.msg);
                }
            }).error(function(data){
                 $scope.submitting = false;
                 $scope.notice(data.msg);
            });
    };

    $scope.addPrizeClick=function(){
        if($scope.addInfo.actCode!=null&&$scope.addInfo.actCode!=""){
            window.open('welcome.do#/activities/contentAdd/'+$scope.addInfo.actCode, 'view_window');
        }else{
            $scope.notice("请先保存抽奖基础配置信息!");
        }
    };

    $scope.deleteActiInfo=function(actDetCode){
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
                    $http.post("activityManagement/deleteActiInfo","actDetCode="+actDetCode,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.getActivityTemp();
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