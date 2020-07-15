/**
 * 业务组-详情
 */
angular.module('inspinia').controller('businessDetailCtrl',function($scope,$http,i18nService,$state,$stateParams,SweetAlert){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.businessNo=$stateParams.businessNo;

    $scope.levelSelect = [{text:"请选择",value:""},
        {text:"Lv.1",value:1},{text:"Lv.2",value:2},{text:"Lv.3",value:3},{text:"Lv.4",value:4},{text:"Lv.5",value:5},
        {text:"Lv.6",value:6},{text:"Lv.7",value:7},{text:"Lv.8",value:8},{text:"Lv.9",value:9},{text:"Lv.10",value:10},
        {text:"Lv.11",value:11},{text:"Lv.12",value:12},{text:"Lv.13",value:13},{text:"Lv.14",value:14},{text:"Lv.15",value:15},
        {text:"Lv.16",value:16},{text:"Lv.17",value:17},{text:"Lv.18",value:18},{text:"Lv.19",value:19},{text:"Lv.20",value:20}
    ];
    $scope.levelStr=angular.toJson($scope.levelSelect);

    $scope.addInfo={};
    $scope.growLevelInfoList=[];
    $scope.mLevelInfoList=[];
    //获取数据
    $http.post("businessInfoAction/getBusinessBasic","businessNo="+$stateParams.businessNo,
        {headers: {'Content-Type':'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.addInfo=data.info;
                if($scope.addInfo!=null){
                    $scope.scoreDesc=$scope.addInfo.scoreDesc;
                    $scope.levelDesc=$scope.addInfo.levelDesc;
                    $scope.growLevelInfoList=$scope.initList(data.info.growLevelInfoList);
                    $scope.mLevelInfoList=$scope.initList(data.info.mLevelInfoList);
                }
            }
        }).error(function(data){
        $scope.notice(data.msg);
    });

    $scope.initList=function (oldList) {
        if(oldList!=null&&oldList.length>0){
            return oldList;
        }else{
            return [];
        }

    };
    $scope.growLevelGrid={                           //配置表格
        data: 'growLevelInfoList',
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'level',displayName:'会员等级',width:100,cellFilter:"formatDropping:" +  $scope.levelStr},
            { field: 'levelName',displayName:'等级名称',width:150},
            { field: 'sectionRemark',displayName:'成长值',width:150}
        ],
        onRegisterApi: function(gridApi) {
            $scope.tradeGridApi = gridApi;
        }
    };

    $scope.mLevelGrid={                           //配置表格
        data: 'mLevelInfoList',
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'level',displayName:'M等级',width:100,cellFilter:"formatDropping:" +  $scope.levelStr},
            { field: 'levelName',displayName:'M等级名称',width:150},
            { field: 'sectionRemark',displayName:'M值',width:150}
        ],
        onRegisterApi: function(gridApi) {
            $scope.mLevelGridApi = gridApi;
        }
    };


    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'taskNo',displayName:'任务编号',width:180},
            { field: 'taskName',displayName:'任务名称',width:180 },
            { field: 'taskTypeName',displayName:'任务类型',width:180 },
            {field: 'status',displayName: '状态',width: 180,
                cellTemplate:
                '<span class="lh30"><span ng-show="row.entity.status==1">开启</span><span ng-show="row.entity.status==0">关闭</span></span>'
            },
            { field: 'taskDesc',displayName:'任务说明',width:300 },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
            '<div class="lh30">'+
            '<a target="_blank" ui-sref="management.businessTaskDetailTwo({id:row.entity.id,businessNo:grid.appScope.businessNo})">详情</a> ' +
            '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };
    $scope.query=function(){
        $http.post("businessInfoAction/getBusinessTaskList","businessNo="+$stateParams.businessNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.list;
                    var taskNos="";
                    if(data.list!=null&&data.list.length>0){
                        angular.forEach(data.list,function(item){
                            taskNos = taskNos+item.taskNo +",";
                        });
                    }
                    $scope.choiceStr=taskNos;
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.notice(data.msg);
            });
    };
    $scope.query();

    $scope.handleInfo=function (info) {
        if(info.minNum==info.maxNum){
            info.numStr=info.minNum;
        }else{
            info.numStr=info.minNum+" - "+info.maxNum;
        }
    };


    //-----------------活动选择---------------------------------
    //会员活动类型
    $scope.actTypeSelect = angular.copy($scope.actTypeList);
    $scope.actTypeStr=angular.toJson($scope.actTypeSelect);

    $scope.userGridActivity={                           //配置表格
        data: 'resultActivity',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'actCode',displayName:'活动编号',width:180},
            { field: 'actType',displayName:'活动类型',cellFilter:"formatDropping:" +  $scope.actTypeStr,width:180},
            { field: 'actName',displayName:'活动名称',width:180 },
            { field: 'actDesc',displayName:'活动说明',width:180 },
            {field: 'openStatusInt',displayName: '是否打开',width: 180,
                cellTemplate:
                    '<span class="lh30"> <span ng-show="row.entity.openStatusInt==1">开启</span><span ng-show="row.entity.openStatusInt==0">关闭</span></span>'
            },
            {field: 'showStatusInt',displayName: '是否首页显示',width: 180,
                cellTemplate:
                    '<span class="lh30"> <span ng-show="row.entity.showStatusInt==1">开启</span><span ng-show="row.entity.showStatusInt==0">关闭</span></span>'
            },
            { field: 'seqNo',displayName:'序列号',width:180 },
            { field: 'actRangeType',displayName:'适用范围',width:180,cellTemplate:
                    '<div class="lh30">'+
                    '<span>{{row.entity.actRangeType | actRangeTypeFilter}}</span> ' +
                    '</div>'
            },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',width:150,pinnedRight:true, cellTemplate:
                    '<div class="lh30">'+
                    '<div ng-show="row.entity.actType==1">'+
                    '<a target="_blank" ui-sref="management.busActivityConfigDetail({actCode:row.entity.actCode,businessNo:row.entity.businessNo,source:2})">详情</a> ' +
                    '<a target="_blank" ng-show="row.entity.actRangeType==1" ui-sref="management.rangeManagementDetail({actCode:row.entity.actCode,businessNo:row.entity.businessNo})"> | 范围管理 </a> ' +
                    '</div>'+
                    '<div ng-show="row.entity.actType==2">'+
                    '<a target="_blank" ui-sref="management.busLuckDrawpConfigDetail({actCode:row.entity.actCode,businessNo:row.entity.businessNo,source:2})">详情</a> ' +
                    '<a target="_blank" ng-show="row.entity.actRangeType==1" ui-sref="management.rangeManagementDetail({actCode:row.entity.actCode,businessNo:row.entity.businessNo})"> | 范围管理 </a> ' +
                    '</div>'+
                    '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApiActivity = gridApi;
        }
    };
    $scope.queryActivity=function(){
        $scope.infoActivity={businessNo:$scope.businessNo};
        $http.post("activityChoice/getActivityList",
            "info="+angular.toJson($scope.infoActivity)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.resultActivity=data.list;
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.notice(data.msg);
            });
    };
    $scope.queryActivity();


    //-----------------预览---------------------------------
    $scope.tableHeaderData = null;

    //创建表头
    $scope.createHeader = function(data){
        $scope.tableHeader = [];//表头
        $scope.tableHeader.push({field: 'rewardName',displayName:'权益名称',width:180,cellTemplate:'<input rewardCode="{{row.entity.rewardCode}}"  name="rewardName" disabled ng-model="row.entity.rewardName" style="line-height: 26px; border: 1px solid #eee; width: 90%; padding: 0 1%; margin-top: 4px;"  type="text"/>'});
        $.each(data,function(index,val){
            $scope.tableHeader.push({field:val.name,displayName:val.name,width:180,cellTemplate:'<input level="'+val.level+'" name="{{row.entity.rewardCode}}" ng-checked="row.entity.level'+index+'==1" disabled type="checkbox"/>' });
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


    $scope.userPreviewGrid={                           //配置表格
        data: 'resultPreview',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:$scope.tableHeader,
        onRegisterApi: function(gridApi) {
            $scope.gridPreviewApi = gridApi;
        }
    };

    $scope.queryPreview=function(){
        $http.post("businessInfoAction/businessTaskPreview","businessNo="+$scope.businessNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.resultPreview=data.list;
                    $scope.oldRewardNames = [];
                    $.each($scope.result,function(index,val){
                        $scope.oldRewardNames.push(val.rewardName);
                    });
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.notice(data.msg);
            });
    };

    $scope.queryPreview();


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
})
.filter('trustHtml', function ($sce) {
    return function (input) {
        return $sce.trustAsHtml(input);
    }
})
.filter('actRangeTypeFilter', function () {
    return function (value) {
        switch(value) {
            case "0" :
                return "全部";
                break;
            case "1" :
                return "部分";
                break;
        }
    }
});