/**
 * 会员列表
 */
angular.module('inspinia',['angularFileUpload']).controller('leaguerQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document,FileUploader){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.levelSelect = [{text:"全部",value:""},
        {text:"Lv.1",value:1},{text:"Lv.2",value:2},{text:"Lv.3",value:3},{text:"Lv.4",value:4},{text:"Lv.5",value:5},
        {text:"Lv.6",value:6},{text:"Lv.7",value:7},{text:"Lv.8",value:8},{text:"Lv.9",value:9},{text:"Lv.10",value:10},
        {text:"Lv.11",value:11},{text:"Lv.12",value:12},{text:"Lv.13",value:13},{text:"Lv.14",value:14},{text:"Lv.15",value:15},
        {text:"Lv.16",value:16},{text:"Lv.17",value:17},{text:"Lv.18",value:18},{text:"Lv.19",value:19},{text:"Lv.20",value:20}
    ];
    $scope.levelStr=angular.toJson($scope.levelSelect);

    //清空
    $scope.clear=function(){
        $scope.info={mLevel:"",growUpLevel:"",businessNo:"",
            createTimeBegin:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    };
    $scope.clear();
    $scope.businessNoList=[];
    $http.post("businessInfoAction/getBusinessInfoListByUser",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.businessNoList.push({value:"",text:"全部"});
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.businessNoList.push({value:list[i].businessNo, text:list[i].businessName});
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
            { field: 'leaguerNo',displayName:'会员编号',width:180},
            { field: 'leaguerName',displayName:'会员名称',width:180 },
            { field: 'businessName',displayName:'业务组',width:180 },
            { field: 'originUserNo',displayName:'用户编号',width:180 },
            { field: 'mobilePhone',displayName:'用户手机号',width:180 },
            { field: 'mLevelName',displayName:'M等级',width:180 },
            { field: 'vipLevelName',displayName:'会员等级',width:180 },
            { field: 'growUpBalance',displayName:'会员成长值',width:180 },
            { field: 'scoreBalance',displayName:'会员积分',width:180 },
            { field: 'growDate',displayName:'会员时间',cellFilter: 'date:"yyyy-MM-dd"',width:180},
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',width:300,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a target="_blank" ng-show="grid.appScope.hasPermit(\'leaguer.leaguerDetail\')"  ui-sref="leaguer.leaguerDetail({id:row.entity.id,businessNo:row.entity.businessNo})">详情</a> ' +
                '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query(2);
            });
        }
    };
    $scope.loadImg = false;

    $scope.query=function(sta){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("leaguerAction/getSelectPageList","info="+angular.toJson($scope.info)+"&pageNo="+
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

        if(sta==1){
            $http.post("leaguerAction/getSelectPageListCount","info="+angular.toJson($scope.info),
                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .success(function(data){
                    if(data.status){
                        $scope.userGrid.totalItems=data.total;
                    }else{
                        $scope.notice(data.msg);
                    }
                })
        }
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


    $scope.importDiscountShow = function(){
        $scope.addInfo={businessNo:""};
        $('#importDiscount').modal('show');
    };
    $scope.importDiscountHide = function(){
        $('#importDiscount').modal('hide');
    };
    var opts = {businessNo: ""};
    //上传图片,定义控制器路径
    var uploader = $scope.uploader = new FileUploader({
        url: 'leaguerAction/importDiscount',
        formData: [opts],
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
    $scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
        uploader.clearQueue();
    };
    $scope.importSta = false;
    //导入
    $scope.importDiscount=function(){
        $scope.importSta = true;
        uploader.queue[0].formData[0].businessNo = $scope.addInfo.businessNo;
        uploader.uploadAll();//上传
        uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
            if(response.status){
                $scope.notice(response.msg);
                $scope.importDiscountHide();
            }else{
                $scope.notice(response.msg);
            }
            $scope.importSta = false;
        };
    };
    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query(1);
            }
        })
    });
});