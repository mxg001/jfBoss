/**
 * 业务组-基本信息
 */
angular.module('inspinia').controller('businessBasicCtrl',function($scope,$http,i18nService,$state,$stateParams,SweetAlert){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);


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
            { field: 'level',displayName:'会员等级',cellFilter:"formatDropping:" +  $scope.levelStr},
            { field: 'levelName',displayName:'等级名称'},
            { field: 'sectionRemark',displayName:'成长值'},
            { field: 'id',displayName:'操作', cellTemplate:
            '<div class="lh30">'+
            '<a ng-click="grid.appScope.editModal(1,row.entity)">编辑</a> ' +
            '<a ng-click="grid.appScope.deleteAddInfo(1,row,rowRenderIndex)"> | 删除</a> ' +
            '</div>'
            }
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
            { field: 'level',displayName:'M等级',cellFilter:"formatDropping:" +  $scope.levelStr},
            { field: 'levelName',displayName:'M等级名称'},
            { field: 'sectionRemark',displayName:'M值'},
            { field: 'id',displayName:'操作',cellTemplate:
            '<div class="lh30">'+
            '<a ng-click="grid.appScope.editModal(2,row.entity)"">编辑</a> ' +
            '<a ng-click="grid.appScope.deleteAddInfo(2,row,rowRenderIndex)"> | 删除</a> ' +
            '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.mLevelGridApi = gridApi;
        }
    };


    $scope.addModal = function(sta){
        $scope.editSta=false;
        $scope.addInfoEntry={level:""};
        $scope.initModel(sta);
    };
    $scope.editModal = function(sta,entry){
        $scope.editSta=true;
        $scope.oldAddInfoEntry = angular.copy(entry);
        $scope.addInfoEntry=angular.copy(entry);
        $scope.addInfoEntry.maxNum=$scope.addInfoEntry.maxNum==-1?"n":$scope.addInfoEntry.maxNum;
        $scope.initModel(sta);
    };
    $scope.initModel=function (sta) {
        if(sta==1){//会员
            $scope.levelTitle="新增会员等级";
            $scope.levelNameStr="会员等级";
            $scope.numNameStr="成长值";
            $scope.type=1;
        }else{
            $scope.levelTitle="新增M等级";
            $scope.levelNameStr="M等级";
            $scope.numNameStr="M值";
            $scope.type=2;
        }
        $('#addInfoModal').modal('show');
    };



    $scope.hideModal= function(){
        $('#addInfoModal').modal('hide');
    };
    $scope.deleteAddInfo=function (sta,row,index) {
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
                    if(sta==1){
                        $scope.growLevelInfoList.splice(index, 1);
                    }else{
                        $scope.mLevelInfoList.splice(index, 1);
                    }

                }
            });
    };

    //保存弹窗
    $scope.saveModal=function (mode) {
        var list=null;
        if($scope.type==1){//会员
            list=$scope.growLevelInfoList;
        }else{
            list=$scope.mLevelInfoList;
        }
        if(mode=="add"){
            if($scope.checkDate(list,$scope.addInfoEntry,null)){
                $scope.handleInfo($scope.addInfoEntry);
                list.push({
                    level:$scope.addInfoEntry.level,
                    levelName:$scope.addInfoEntry.levelName,
                    sectionRemark:$scope.addInfoEntry.sectionRemark,
                    minNum:$scope.addInfoEntry.minNum,
                    maxNum:$scope.addInfoEntry.maxNum=="n"?-1:$scope.addInfoEntry.maxNum
                });
                $scope.hideModal();
            }
        }else if(mode=="edit"){
            var oldInfo=$scope.oldAddInfoEntry;
            if($scope.checkDate(list,$scope.addInfoEntry,oldInfo)){
                $scope.handleInfo($scope.addInfoEntry);
                for (var j = 0; j < list.length; j++) {
                    var item = list[j];
                    if (oldInfo.level == item.level
                        &&oldInfo.levelName == item.levelName
                        && oldInfo.minNum == item.minNum
                        && oldInfo.maxNum == item.maxNum){
                            item.level=$scope.addInfoEntry.level;
                            item.levelName=$scope.addInfoEntry.levelName;
                            item.sectionRemark=$scope.addInfoEntry.sectionRemark;
                            item.minNum=$scope.addInfoEntry.minNum;
                            item.maxNum=$scope.addInfoEntry.maxNum=="n"?-1:$scope.addInfoEntry.maxNum;
                    }
                }
                $scope.hideModal();
            }
        }

    };
    $scope.handleInfo=function (info) {
        if(info.minNum==info.maxNum){
            info.sectionRemark=info.minNum;
        }else{
            info.sectionRemark=info.minNum+" - "+info.maxNum;
        }
    };

    $scope.checkDate = function(dateList,info,oldInfo){
        if(info.minNum==null||info.minNum===""
            ||info.maxNum==null||info.maxNum===""){
            $scope.notice($scope.numNameStr+"区间不能为空!");
            return false;
        }else{
            var errorStr=$scope.numNameStr+"区间值格式不对!";
            if(Number(info.minNum)>=0){
                var reg=/^[0-9]\d*$/;
                if(!reg.test(Number(info.minNum))){
                    $scope.notice(errorStr);
                    return false;
                }
            }else{
                $scope.notice(errorStr);
                return false;
            }
            if(info.maxNum!="n"){
                if(Number(info.maxNum)>=0){
                    var reg=/^[0-9]\d*$/;
                    if(!reg.test(Number(info.maxNum))){
                        $scope.notice(errorStr);
                        return false;
                    }
                }else{
                    $scope.notice(errorStr);
                    return false;
                }
            }
        }
        if(info.maxNum!="n"&&Number(info.minNum)>Number(info.maxNum)){
            $scope.notice($scope.numNameStr+"区间最小值不能大于最大值!");
            return false;
        }

        if(dateList!=null&&dateList.length>0){
            for(var i=0;i<dateList.length;i++){
                var item=dateList[i];
                if(oldInfo!=null){
                    if(item.level==oldInfo.level
                        &&item.minNum==oldInfo.minNum
                        &&item.maxNum==oldInfo.maxNum){
                        continue;
                    }
                }
                if(item.level==info.level){
                    $scope.notice("该等级已存在,添加失败!");
                    return false;
                }

                if(item.maxNum!="n"&&item.maxNum!="-1"){
                    if((Number(item.minNum)<=Number(info.minNum) &&Number(info.minNum)<=Number(item.maxNum))
                        ||(Number(item.minNum)<=Number(info.maxNum) &&Number(info.maxNum)<=Number(item.maxNum))
                    ){
                        $scope.notice($scope.numNameStr+"区间已存在,添加失败!");
                        return false;
                    }
                }else{
                    if(Number(item.minNum)<=Number(info.minNum)){
                        $scope.notice($scope.numNameStr+"区间已存在,添加失败!");
                        return false;
                    }
                }
                //反包含校验
                if(info.maxNum!="n"&&info.maxNum!="-1"){
                    if((Number(info.minNum)<=Number(item.minNum) &&Number(item.minNum)<=Number(info.maxNum))
                        ||(Number(info.minNum)<=Number(item.maxNum) &&Number(item.maxNum)<=Number(info.maxNum))
                    ){
                        $scope.notice($scope.numNameStr+"区间已存在,添加失败!");
                        return false;
                    }
                }else{
                    if(Number(info.minNum)<=Number(item.minNum)){
                        $scope.notice($scope.numNameStr+"区间已存在,添加失败!");
                        return false;
                    }
                }
            }
        }
        return true;
    };

    $scope.submitting = false;
    //新增提交
    $scope.addInfoSubmit = function(){
        if($scope.submitting){
            return;
        }
        if($scope.growLevelInfoList==null||$scope.growLevelInfoList.length<=0){
            $scope.notice("会员等级阶梯配置不能为空!");
            return false;
        }
        if($scope.mLevelInfoList==null||$scope.mLevelInfoList.length<=0){
            $scope.notice("M等级阶梯配置不能为空!");
            return false;
        }
        $scope.submitting = true;

        $scope.addInfoSub=angular.copy($scope.addInfo);
        $scope.addInfoSub.scoreDesc=null;
        $scope.addInfoSub.levelDesc=null;
        $scope.addInfoSub.digestKey=null;

        $scope.addInfoSub.growLevelInfoList=$scope.growLevelInfoList;
        $scope.addInfoSub.mLevelInfoList=$scope.mLevelInfoList;

        var data={
            businessNo:$stateParams.businessNo,
            info:angular.toJson($scope.addInfoSub),
            scoreDesc:$scope.scoreDesc,
            levelDesc:$scope.levelDesc
        };
        var postCfg = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };
        $http.post("businessInfoAction/updateBusinessBasic",data,postCfg)
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('management.business',null,{reload:true});
                }else{
                    $scope.notice(data.msg);
                }
                $scope.submitting = false;
            })
            .error(function(data){
                $scope.submitting = false;
                $scope.notice(data.msg);
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