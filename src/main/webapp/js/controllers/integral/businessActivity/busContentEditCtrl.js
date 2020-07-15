/**
 * 活动内容新修改
 */
angular.module('inspinia').controller('busContentEditCtrl',function($scope,$http,i18nService,$state,$stateParams,SweetAlert){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.actDetCode=$stateParams.actDetCode;
    $scope.businessNo=$stateParams.businessNo;

    //内容类型
    $scope.activeTypeSelect = [{text:"请选择",value:""},{text:"奖励赠送",value:"1"},{text:"达标奖励",value:"2"},{text:"交易返现",value:"3"}];
    $scope.activeTypeStr=angular.toJson($scope.activeTypeSelect);
    //奖励方式
    $scope.awardMethodSelect = [{text:"请选择",value:""},{text:"多选一",value:"1"},{text:"多选多",value:"2"}];
    $scope.awardMethodStr=angular.toJson($scope.awardMethodSelect);
    //值类型
    $scope.calcTypeSelect = [{text:"数值",value:"1"},{text:"比例",value:"2"}];
    $scope.calcTypeStr=angular.toJson($scope.calcTypeSelect);

    //类型
    $scope.tranTypeSelect = [{text:"按交易金额返现",value:"3"},{text:"按手续费返现",value:"4"}];
    $scope.tranTypeStr=angular.toJson($scope.tranTypeSelect);

    //奖品类型
    $scope.rewardTypeSelect = angular.copy($scope.rewardTypeList);
    $scope.rewardTypeSelect.unshift({text:"请选择",value:""});
    $scope.rewardTypeStr=angular.toJson($scope.rewardTypeSelect);


    $scope.addInfo={activeType:"",awardMethod:""};
    $scope.addInfoItemList=[];

    $scope.userGrid={                           //配置表格
        data: 'addInfoItemList',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };
    $scope.cleanStatus=function(){
        $scope.tableSta=false;
        $scope.methodSta=false;
    };

    $scope.activeTypeChange=function(){
        if($scope.addInfo.activeType==null||$scope.addInfo.activeType==""){
            $scope.cleanStatus();
            return;
        }
        $scope.cleanStatus();
        $scope.tableSta=true;
        if($scope.addInfo.activeType=="1"){//奖励赠送
            $scope.userGrid.columnDefs=[
                { field: 'rewardType',displayName:'奖励类型',cellFilter:"formatDropping:" +  $scope.rewardTypeStr,width:180},
                { field: 'calcType',displayName:'值类型',width:180,cellTemplate:
                        '<div class="lh30">' +
                        '<span ng-if="row.entity.rewardType==\'4\'||row.entity.rewardType==\'5\'||row.entity.rewardType==\'6\'">{{row.entity.goodName}}</span>' +
                        '<span ng-if="row.entity.rewardType!=\'4\'&&row.entity.rewardType!=\'5\'&&row.entity.rewardType!=\'6\'">{{row.entity.calcType | calcTypeFilter}}</span>'+
                        '</div>'
                },
                { field: 'awardNum',displayName:'数量',width:180},
                { field: 'expireDay',displayName:'有效期',width:180},
                { field: 'id',displayName:'操作',width:150,pinnedRight:true, cellTemplate:
                        '<div class="lh30">'+
                        '<a ng-click="grid.appScope.editDataModal(row,rowRenderIndex)">编辑</a>'+
                        '<a ng-click="grid.appScope.deleteAddInfoItem(row,rowRenderIndex)"> | 删除</a>'+
                        '</div>'
                }
            ];
            $scope.addTitle="奖励赠送";
        }else if($scope.addInfo.activeType=="2"){//达标奖励
            $scope.userGrid.columnDefs=[
                { field: 'standardAmount',displayName:'边界值',width:180},
                { field: 'rewardType',displayName:'奖励类型',cellFilter:"formatDropping:" +  $scope.rewardTypeStr,width:180},
                { field: 'calcType',displayName:'值类型',width:180,cellTemplate:
                        '<div class="lh30">' +
                        '<span ng-if="row.entity.rewardType==\'4\'||row.entity.rewardType==\'5\'||row.entity.rewardType==\'6\'">{{row.entity.goodName}}</span>' +
                        '<span ng-if="row.entity.rewardType!=\'4\'&&row.entity.rewardType!=\'5\'&&row.entity.rewardType!=\'6\'">{{row.entity.calcType | calcTypeFilter}}</span>'+
                        '</div>'
                },
                { field: 'awardNum',displayName:'数值',width:180},
                { field: 'expireDay',displayName:'有效期',width:180},
                { field: 'id',displayName:'操作',width:150,pinnedRight:true, cellTemplate:
                        '<div class="lh30">'+
                        '<a ng-click="grid.appScope.editDataModal(row,rowRenderIndex)">编辑</a>'+
                        '<a ng-click="grid.appScope.deleteAddInfoItem(row,rowRenderIndex)"> | 删除</a>'+
                        '</div>'
                }
            ];
            $scope.methodSta=true;
            $scope.addTitle="达标奖励";
        }else if($scope.addInfo.activeType=="3"){//交易返现
            $scope.userGrid.columnDefs=[
                { field: 'tranType',displayName:'类型',cellFilter:"formatDropping:" +  $scope.tranTypeStr,width:180},
                { field: 'calcType',displayName:'值类型',cellFilter:"formatDropping:" +  $scope.calcTypeStr,width:180},
                { field: 'awardNum',displayName:'奖励比例(%)',width:180},
                { field: 'awardTemplateFormula',displayName:'计算公式',width:180},
                { field: 'id',displayName:'操作',width:150,pinnedRight:true, cellTemplate:
                        '<div class="lh30">'+
                        '<a ng-click="grid.appScope.editDataModal(row,rowRenderIndex)">编辑</a>'+
                        '<a ng-click="grid.appScope.deleteAddInfoItem(row,rowRenderIndex)"> | 删除</a>'+
                        '</div>'
                }
            ];
            $scope.addTitle="交易返现";
        }
    };

    $scope.getActiDetail=function(){
        $http.post("activityChoice/getActiDetail",
            "actDetCode="+$scope.actDetCode+"&businessNo="+$scope.businessNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.addInfo=data.info;
                    $scope.addInfoItemList=data.info.actReward;
                    if($scope.addInfo!=null&&$scope.addInfo.activeType=="3"&&$scope.addInfoItemList!=null&&$scope.addInfoItemList.length>0){
                        for(var i=0;i<$scope.addInfoItemList.length;i++){
                            $scope.addInfoItemList[i].tranType="3";
                        }
                    }
                    $scope.activeTypeChange();
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.notice(data.msg);
            });
    };
    $scope.getActiDetail();

    $scope.editDataSta=false;
    $scope.addDataModal = function(){
        $scope.addInfoItem={rewardType:""};
        $scope.editDataSta=false;
        $scope.awardTypeChange(1);
        if($scope.addInfo.activeType=="1"){//奖励赠送

        }else if($scope.addInfo.activeType=="2"){

        }else if($scope.addInfo.activeType=="3"){
            $scope.addInfoItem.tranType="3";
            $scope.addInfoItem.calcType="2";
            $scope.addInfoItem.rewardType="7";
            $scope.addInfoItem.awardTemplateFormula="X*Y";
        }
        $('#addInfoModal').modal('show');
    };

    $scope.hideDataModal= function(){
        $('#addInfoModal').modal('hide');
    };
    $scope.conList=[];

    $scope.cleanSta=function(initSta){
        $scope.conSta=false;//卷
        $scope.moneySta=false;//金额
        $scope.numSta=false;//数值
        $scope.integralSta=false;//积分有效期
        if(initSta){
            $scope.addInfoItem.rewardNum="";
            $scope.addInfoItem.expireDay="";
            $scope.addInfoItem.calcType="";
            $scope.addInfoItem.awardNum="";
        }
    };

    $scope.awardTypeChange=function(entrance){
        var sta=$scope.addInfoItem.rewardType;
        if(entrance==1){//新增
            $scope.cleanSta(true);
        }else if(entrance==2){//编辑
            $scope.cleanSta(false);
        }else if(entrance==3){
            $scope.cleanSta(true);
        }
        if(sta==null||sta==""){
            return;
        }
        if($scope.addInfo.activeType=="3"){
            return;
        }
        if(sta=="1"){//会员积分
            $scope.numSta=true;
            $scope.integralSta=true;
            $scope.addInfoItem.calcType="1";
        }else if(sta=="2" ||sta=="3"){
            $scope.numSta=true;
            $scope.addInfoItem.calcType="1";
        }else if(sta=="4" ||sta=="5" ||sta=="6"){//卷
            $scope.getCouponList(sta);
            $scope.conSta=true;
        }else if(sta=="7"){//现金
            $scope.moneySta=true;
            $scope.addInfoItem.calcType="1";
        }
    };

    //券列表
    $scope.getCouponList=function(goodsType){
        $http.post("goodsManagement/getGoodsList",
            "goodsType="+goodsType+"&businessNo="+$scope.businessNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.conList=[{text:"请选择",value:""}];
                    var list=data.list;
                    if(list!=null&&list.length>0){
                        for(var i=0; i<list.length; i++){
                            $scope.conList.push({value:list[i].goodsCode+","+list[i].goodsName,text:list[i].goodsName+"("+list[i].goodsCode+")"});
                        }
                    }
                }
            });
    };
    //数据转换
    $scope.dataInfo=function(info){
        if(info.rewardType=="4"||info.rewardType=="5"||info.rewardType=="6"){
            var strs=info.calcType.split(",");
            info.calcType=strs[0];
            info.goodName=strs[1];
        }
    };
    //保存数据到表格内
    $scope.saveDataTable=function(){
        if(!$scope.saveCheck($scope.addInfoItem,$scope.editDataSta)){
            return ;
        }
        if($scope.editDataSta){//修改
            var index=$scope.editDataIndex;
            if($scope.addInfo.activeType=="1") {//奖励赠送
                $scope.dataInfo($scope.addInfoItem);
                $scope.addInfoItemList[index].rewardType=$scope.addInfoItem.rewardType;
                $scope.addInfoItemList[index].calcType=$scope.addInfoItem.calcType;
                $scope.addInfoItemList[index].id=$scope.addInfoItem.id;
                $scope.addInfoItemList[index].actRewardNo=$scope.addInfoItem.actRewardNo;
                $scope.addInfoItemList[index].goodName=$scope.addInfoItem.goodName;
                $scope.addInfoItemList[index].awardNum=$scope.addInfoItem.awardNum;
                $scope.addInfoItemList[index].expireDay=$scope.addInfoItem.expireDay;

            }else if($scope.addInfo.activeType=="2"){
                $scope.dataInfo($scope.addInfoItem);
                $scope.addInfoItemList[index].rewardType=$scope.addInfoItem.rewardType;
                $scope.addInfoItemList[index].calcType=$scope.addInfoItem.calcType;
                $scope.addInfoItemList[index].id=$scope.addInfoItem.id;
                $scope.addInfoItemList[index].actRewardNo=$scope.addInfoItem.actRewardNo;
                $scope.addInfoItemList[index].goodName=$scope.addInfoItem.goodName;
                $scope.addInfoItemList[index].awardNum=$scope.addInfoItem.awardNum;
                $scope.addInfoItemList[index].expireDay=$scope.addInfoItem.expireDay;
                $scope.addInfoItemList[index].standardAmount=$scope.addInfoItem.standardAmount;

            }else if($scope.addInfo.activeType=="3"){
                $scope.addInfoItemList[index].tranType=$scope.addInfoItem.tranType;
                $scope.addInfoItemList[index].rewardType=$scope.addInfoItem.rewardType;
                $scope.addInfoItemList[index].calcType=$scope.addInfoItem.calcType;
                $scope.addInfoItemList[index].id=$scope.addInfoItem.id;
                $scope.addInfoItemList[index].actRewardNo=$scope.addInfoItem.actRewardNo;
                $scope.addInfoItemList[index].awardNum=$scope.addInfoItem.awardNum;
                $scope.addInfoItemList[index].awardTemplateFormula=$scope.addInfoItem.awardTemplateFormula;

            }
        }else{
            if($scope.addInfo.activeType=="1") {//奖励赠送
                $scope.dataInfo($scope.addInfoItem);
                $scope.addInfoItemList.push({rewardType:$scope.addInfoItem.rewardType,calcType:$scope.addInfoItem.calcType,
                    id:$scope.addInfoItem.id,actRewardNo:$scope.addInfoItem.actRewardNo,
                    goodName:$scope.addInfoItem.goodName,
                    awardNum:$scope.addInfoItem.awardNum,expireDay:$scope.addInfoItem.expireDay});

            }else if($scope.addInfo.activeType=="2"){
                $scope.dataInfo($scope.addInfoItem);
                $scope.addInfoItemList.push({standardAmount:$scope.addInfoItem.standardAmount,
                    id:$scope.addInfoItem.id,actRewardNo:$scope.addInfoItem.actRewardNo,
                    goodName:$scope.addInfoItem.goodName,
                    rewardType:$scope.addInfoItem.rewardType,calcType:$scope.addInfoItem.calcType,
                    awardNum:$scope.addInfoItem.awardNum,expireDay:$scope.addInfoItem.expireDay});

            }else if($scope.addInfo.activeType=="3"){
                $scope.addInfoItemList.push({tranType:$scope.addInfoItem.tranType,calcType:$scope.addInfoItem.calcType,
                    id:$scope.addInfoItem.id,actRewardNo:$scope.addInfoItem.actRewardNo,
                    rewardType:$scope.addInfoItem.rewardType,awardNum:$scope.addInfoItem.awardNum,
                    awardTemplateFormula:$scope.addInfoItem.awardTemplateFormula});

            }
        }

        $scope.hideDataModal();
    };

    //保存校验
    $scope.saveCheck=function(info,editSta){
        if($scope.addInfo.activeType=="1") {//奖励赠送
            if(info.rewardType==null||info.rewardType==""){
                $scope.notice("奖励类型不能为空!");
                return false;
            }else{
                if(info.rewardType=="1"){
                    if(info.expireDay==null||info.expireDay==""){
                        $scope.notice("有效期不能为空!");
                        return false;
                    }else{
                        var reg=/^[1-9]\d*$/;
                        if(!reg.test(Number(info.expireDay))) {
                            $scope.notice("有效期格式不对!");
                            return false;
                        }
                    }
                }
            }
            if(info.calcType==null||info.calcType==""){
                $scope.notice("值类型不能为空!");
                return false;
            }
            if(info.awardNum==null|info.awardNum==""){
                $scope.notice("数值不能为空!");
                return false;
            }else{
                if(info.rewardType=="7"){
                    if(Number(info.awardNum)<=0){
                        $scope.notice("现金-数值不能小于等于0!");
                        return false;
                    }
                }
            }
        }else if($scope.addInfo.activeType=="2"){
            if(info.standardAmount==null||info.standardAmount==""){
                $scope.notice("边界值不能为空!");
                return false;
            }else{
                if($scope.addInfoItemList.length>0){
                    for(var i=0;i<$scope.addInfoItemList.length;i++){
                        if(Number(info.standardAmount)==Number($scope.addInfoItemList[i].standardAmount)){
                            if(editSta) {//修改
                                if($scope.editDataIndex==i){
                                    continue;
                                }else{
                                    $scope.notice("该边界值已存在!");
                                    return false;
                                }
                            }else{
                                $scope.notice("该边界值已存在!");
                                return false;
                            }

                        }
                    }
                }
            }
            if(info.rewardType==null||info.rewardType==""){
                $scope.notice("奖励类型不能为空!");
                return false;
            }else{
                if(info.rewardType=="1"){
                    if(info.expireDay==null||info.expireDay==""){
                        $scope.notice("有效期不能为空!");
                        return false;
                    }else{
                        var reg=/^[1-9]\d*$/;
                        if(!reg.test(Number(info.expireDay))) {
                            $scope.notice("有效期格式不对!");
                            return false;
                        }
                    }
                }
            }
            if(info.calcType==null||info.calcType==""){
                $scope.notice("值类型不能为空!");
                return false;
            }
            if(info.awardNum==null|info.awardNum==""){
                $scope.notice("数值不能为空!");
                return false;
            }else{
                if(info.rewardType=="7"){
                    if(Number(info.awardNum)<=0){
                        $scope.notice("现金-数值不能小于等于0!");
                        return false;
                    }
                }
            }
        }else if($scope.addInfo.activeType=="3"){
            if(info.awardNum==null|info.awardNum==""){
                $scope.notice("奖励比例不能为空!");
                return false;
            }
            if(!editSta){
                if($scope.addInfoItemList.length>0){
                    $scope.notice("交易奖励类型下,已存在记录了!");
                    return false;
                }
            }

        }
        return true;
    };
    //编辑
    $scope.editDataModal = function(row,rowRenderIndex){
        $scope.addInfoItem=angular.copy(row.entity);
        if($scope.addInfoItem.goodName){//反解析还原
            $scope.addInfoItem.calcType=$scope.addInfoItem.calcType+","+$scope.addInfoItem.goodName;
        }
        $scope.editDataSta=true;
        $scope.editDataIndex=rowRenderIndex;
        $scope.awardTypeChange(2);
        if($scope.addInfo.activeType=="1"){//奖励赠送

        }else if($scope.addInfo.activeType=="2"){

        }else if($scope.addInfo.activeType=="3"){

        }
        $('#addInfoModal').modal('show');
    };

    $scope.deleteAddInfoItem=function (row,index) {
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
                    $scope.addInfoItemList.splice(index, 1);
                }
            });
    };

    //提交内容数据
    $scope.submitting = false;
    $scope.commitInfo=function(){
        if($scope.addInfoItemList==null||$scope.addInfoItemList.length<1){
            $scope.notice("奖励配置不能为空!");
            return;
        }
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;
        $scope.infoSub = angular.copy($scope.addInfo);
        $scope.activeDescTmp = $scope.infoSub.activeDesc;
        $scope.infoSub.activeDesc = null;
        $scope.infoSub.actReward=$scope.addInfoItemList;
        $http.post("activityChoice/editActiInfo",
            "info="+angular.toJson($scope.infoSub)+"&businessNo="+$scope.businessNo+"&activeDesc="+encodeURI($scope.activeDescTmp),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.goBackFun();
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.submitting = false;
                $scope.notice(data.msg);
            });
    };

    //返回
    $scope.goBackFun=function(){
        window.open('welcome.do#/management/busActivityConfig/'+$scope.addInfo.actCode+'/'+$scope.businessNo, '_self');
    };
}).filter('calcTypeFilter', function () {
    return function (value) {
        switch(value) {
            case "1" :
                return "数值";
                break;
            case "2" :
                return "比例";
                break;
        }
    }
});