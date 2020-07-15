/**
 * 活动内容修改
 */
angular.module('inspinia',['angularFileUpload']).controller('cashMeetActivityContentConfigCtrl',function($scope,$compile,$http,i18nService,$state,FileUploader,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.expriryDateTypeSelect = angular.copy($scope.expriryDateTypeList);
    $scope.expriryDateTypeStr=angular.toJson($scope.expriryDateTypeSelect);

    $scope.policyTypeSelect = [];
    $scope.policyTypeSelect.push({text:"概率随机",value:1});
    $scope.policyTypeSelect.push({text:"区间随机",value:2});
    $scope.policyTypeStr=angular.toJson($scope.policyTypeSelect);
    $scope.activeTypeSelect = [];
    $scope.activeTypeSelect.push({text:"登录APP",value:"5"});
    $scope.activeTypeSelect.push({text:"交易收款",value:"6"});
    $scope.activeTypeSelect.push({text:"会员积分",value:"7"});
    $scope.activeTypeSelect.push({text:"邀请好友",value:"8"});
    $scope.activeTypeSelect.push({text:"绑定公众号",value:"9"});
    $scope.activeTypeStr=angular.toJson($scope.activeTypeSelect);

    $scope.expriryDateTypeListSelect = angular.copy($scope.expriryDateTypeList);
    $scope.expriryDateTypeListSelect.forEach((item,index,array)=>{
        item.value = parseInt(item.value)
    })
    $scope.expriryDateTypeStr=angular.toJson($scope.expriryDateTypeListSelect);

    $scope.activeTypeAll = function(){
        $scope.loginApp = false;
        $scope.tradAmountUpdate = false;
        $scope.memberUpdate = false;
        $scope.inviteFriendUpdate = false;
        $scope.bindUpdateAccount = false;
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


    $scope.getCashMeetActivityContent=function(){
        $http.post("activityManagement/getActivityContentDetail",
            "actCode="+$stateParams.actCode+"&actDetCode="+$stateParams.actDetCode,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.addInfo=data.info;
                    $scope.policyTypeVisible1 = false;
                    $scope.policyTypeVisible2 = false;
                    $scope.addInfo.policyTypeList = [];
                    if(data.info.policyType == 1){
                        $scope.policyTypeVisible1 = true;
                        $scope.addInfo.policyList.forEach(function(item,index,arr){
                            $scope.addInfo.policyTypeList.push({awardNum:item.awardNum,dayNums:item.dayNums,awardOdds:item.awardOdds,awardCode:item.awardCode});
                        })
                    }else if(data.info.policyType == 2){
                        $scope.policyTypeVisible2 = true;
                        $scope.addInfo.policyTypeList.push({});
                    }
                    $scope.activeTypeChange();
                }else{
                    $scope.notice(data.msg);
                }
            })
    };
    $scope.getCashMeetActivityContent();

    $scope.policyTypeChange = function(event){
        if($scope.addInfo.policyType == 1){
            $scope.policyTypeVisible1 = true;
            $scope.policyTypeVisible2 = false;
        }else if($scope.addInfo.policyType == 2){
            $scope.policyTypeVisible1 = false;
            $scope.policyTypeVisible2 = true;
        }
    }

    $scope.addPolicy = function(event){
        $scope.addInfo.policyTypeList.push({});
    }

    $scope.deletePolicy = function(index){
        $scope.addInfo.policyTypeList.splice(index, 1);
    }

    //返回
    $scope.goBackFun=function(){
        window.open('welcome.do#/activities/cashMeetConfig/'+$stateParams.actCode+"/"+$stateParams.business+"/"+$stateParams.source, '_self');
    };

    $scope.submitting = false;
    $scope.updateActivityContent=function(){
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

        if($scope.addInfo.policyType == 1){
            for(var index in $scope.addInfo.policyTypeList) {
                var item = $scope.addInfo.policyTypeList[index];
                if(!item.awardNum || !item.dayNums || !item.awardOdds){
                    $scope.notice("请填写完整的奖励策略")
                    $scope.submitting = false;
                    return ;
                }
            }
        }

        if($scope.addInfo.activeType == 6){     //交易收款活动

        }else if($scope.addInfo.activeType == 7){       //会员积分活动

        }else if($scope.addInfo.activeType == 8){       //邀请好友活动

        }

        $scope.addInfoSub=angular.copy($scope.addInfo);
        var data={
            info:angular.toJson($scope.addInfoSub),
            policyList: angular.toJson($scope.addInfo.policyTypeList)
        };
        var postCfg = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };
        $http.post("activityManagement/updateActivityContent",data,postCfg)
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.goBackFun();
                    // $state.transitionTo('activities.activityManagement',null,{reload:true});
                }else{
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