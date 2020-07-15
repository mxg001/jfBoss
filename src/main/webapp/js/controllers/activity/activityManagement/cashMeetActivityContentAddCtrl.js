/**
 * 活动内容添加
 */
angular.module('inspinia').controller('cashMeetActivityContentAddCtrl',function($scope,$compile,$http,i18nService,$state,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.actCode=$stateParams.actCode;

    $scope.expriryDateTypeSelect = angular.copy($scope.expriryDateTypeList);
    $scope.expriryDateTypeStr=angular.toJson($scope.expriryDateTypeSelect);

    $scope.policyTypeSelect = [];
    $scope.policyTypeSelect.push({text:"概率随机",value:1});
    $scope.policyTypeSelect.push({text:"区间随机",value:2});
    $scope.policyTypeStr=angular.toJson($scope.policyTypeSelect);

    $scope.activeTypeSelect = [];
    $scope.activeTypeSelect.push({text:"登录APP",value:5});
    $scope.activeTypeSelect.push({text:"交易收款",value:6});
    $scope.activeTypeSelect.push({text:"会员积分",value:7});
    $scope.activeTypeSelect.push({text:"邀请好友",value:8});
    $scope.activeTypeSelect.push({text:"绑定公众号",value:9});
    $scope.activeTypeStr=angular.toJson($scope.activeTypeSelect);

    $scope.expriryDateTypeListSelect = angular.copy($scope.expriryDateTypeList);
    $scope.expriryDateTypeListSelect.forEach((item,index,array)=>{
        item.value = parseInt(item.value)
    })
    $scope.expriryDateTypeStr=angular.toJson($scope.expriryDateTypeListSelect);


    $scope.addInfo={
        policyType: 1,
        activeType: 5,
        expiryDateType: "activity",
        isLinkBehind: 1
    };

    $scope.activeTypeAll = function(){
        $scope.loginApp = false;
        $scope.tradAmountUpdate = false;
        $scope.memberUpdate = false;
        $scope.inviteFriendUpdate = false;
        $scope.bindUpdateAccount = false;
    }

    $scope.policyTypeVisible1 = true;

    $scope.activeTypeAll();
    $scope.loginApp = true;

    //返回
    $scope.goBackFun=function(){
        window.history.back(-1);
    };

    $scope.addInfo.policyTypeList = [];
    $scope.addInfo.policyTypeList.push({});
    $scope.addPolicy = function(event){
        $scope.addInfo.policyTypeList.push({});
    }

    $scope.deletePolicy = function(index){
        $scope.addInfo.policyTypeList.splice(index, 1);
    }

    $scope.policyTypeChange = function(event){
        if($scope.addInfo.policyType == 1){
            $scope.policyTypeVisible1 = true;
            $scope.policyTypeVisible2 = false;
        }else if($scope.addInfo.policyType == 2){
            $scope.policyTypeVisible1 = false;
            $scope.policyTypeVisible2 = true;
        }
    }

    $scope.activeTypeChange = function(event){
        $scope.activeTypeAll();
        if($scope.addInfo.activeType == 5){
            $scope.loginApp = true;
        }else if($scope.addInfo.activeType == 6){
            $scope.tradAmountUpdate = true;
        }else if($scope.addInfo.activeType == 7){
            $scope.memberUpdate = true;
        }else if($scope.addInfo.activeType == 8){
            $scope.inviteFriendUpdate = true;
        }else if($scope.addInfo.activeType == 9){
            $scope.bindUpdateAccount = true;
        }
    }

    $scope.getBusinessChildActivity=function(){
        $http.post("activityManagement/getBusinessChildActivity",
            "actCode="+$stateParams.actCode,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.existActivity = data.info;
                    // $scope.expendTypeChange(false);
                }else{
                    $scope.notice(data.msg);
                }
            });
    };
    $scope.getBusinessChildActivity();

    $scope.toLimit = function(addInfo,newValue,oldValue){
        if(/^\d*(\.\d{0,2})?$/.test(newValue)==false){
            addInfo.blackReward = oldValue;
        }
    };

    //新增提交
    $scope.submitting = false;
    $scope.saveCashMeetActivityContent=function(){
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;

        var flag = true;
        var reg = /^[1-9]+[0-9]*]*$/;
        if($scope.tradAmountUpdate){
            var value = $scope.addInfo.tradAmountUpdateValue;
            if(!reg.test(value)){
                $scope.notice("单笔成功交易金额格式错误!");
                flag = false;
            }
        }else if($scope.memberUpdate){
            var value = $scope.addInfo.memberUpdateValue;
            if(!reg.test(value)){
                $scope.notice("单次消耗会员积分格式错误!");
                flag = false;
            }
        }else if($scope.inviteFriendUpdate){
            var value = $scope.addInfo.inviteFriendUpdateValue;
            if(!reg.test(value)){
                $scope.notice("邀请好友注册金额格式错误!");
                flag = false;
            }
        }

        if(!flag){
            $scope.submitting = false;
            return ;
        }

        for(i=0; i<$scope.existActivity.length; i++){
            var existActivity = $scope.existActivity[i];
            if(existActivity.activeType == $scope.addInfo.activeType){
                $scope.notice("当前活动已存在该活动类型，无法重复添加！");
                $scope.submitting = false;
                return;
            }
        }
        if($scope.addInfo.policyType == 1){
            for(var index in $scope.addInfo.policyTypeList) {
                var item = $scope.addInfo.policyTypeList[index];
                if(!item.awardNum || !item.dayNums || !item.awardOdds){
                    $scope.notice("请填写完整的奖励策略");
                    $scope.submitting = false;
                    return ;
                }
            }
        }
        $scope.addInfoSub=angular.copy($scope.addInfo);
        var data={
            info:angular.toJson($scope.addInfoSub),
            actCode: $scope.actCode,
            policyList: angular.toJson($scope.addInfo.policyTypeList)
        };
        var postCfg = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };
        $http.post("activityManagement/saveCashMeetActivityContent",data,postCfg)
            .success(function(data){
                if(data.status){
                    $scope.submitting = false;
                    $scope.notice(data.msg);
                    $scope.goBackFun();
                    // $state.transitionTo('activities.activityManagement',null,{reload:true});
                }else{
                    $scope.submitting = false;
                    $scope.notice(data.msg);
                }
            });
    };

});

function checkFormat(obj){
    var reg = /^[0-9]+[0-9]*]*$/;
    if($(obj).val()!=""){
        if(!reg.test($(obj).val()) || $(obj).val() == 0){
            alert("输入格式错误！请输整数！",$(obj));
            $(obj).val("");
        }else{
            $(obj).val(parseInt($(obj).val()));
        }
    }
}

