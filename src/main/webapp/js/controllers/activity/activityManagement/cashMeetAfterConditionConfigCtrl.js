/**
 * 活动内容修改
 */
angular.module('inspinia',['angularFileUpload']).controller('cashMeetAfterConditionConfigCtrl',function($scope,$compile,$http,i18nService,$state,FileUploader,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.expriryDateTypeSelect = angular.copy($scope.expriryDateTypeList);
    $scope.expriryDateTypeStr=angular.toJson($scope.expriryDateTypeSelect);

    $scope.policyTypeSelect = [];
    $scope.policyTypeSelect.push({text:"概率随机",value:1});
    $scope.policyTypeSelect.push({text:"区间随机",value:2});
    $scope.policyTypeStr=angular.toJson($scope.policyTypeSelect);

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

    $scope.policyTypeChange = function(event){
        if($scope.addInfo.policyType == 1){
            $scope.policyTypeVisible1 = true;
            $scope.policyTypeVisible2 = false;
        }else if($scope.addInfo.policyType == 2){
            $scope.policyTypeVisible1 = false;
            $scope.policyTypeVisible2 = true;
        }
    }

    $scope.getCashMeetAfterCondition=function(){
        $http.post("activityManagement/getAfterConditionDetail",
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
                        });
                        if($scope.addInfo.policyTypeList.length == 0){
                            $scope.addInfo.policyTypeList.push({awardNum:0,dayNums:0,awardOdds:0});
                        }
                    }else if(data.info.policyType == 2){
                        $scope.policyTypeVisible2 = true;
                        $scope.addInfo.policyTypeList.push({});
                    }
                }else{
                    $scope.notice(data.msg);
                }
            })
    };
    $scope.getCashMeetAfterCondition();

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
    $scope.updateAfterCondition=function(){
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;

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
            policyList: angular.toJson($scope.addInfo.policyTypeList)
        };
        var postCfg = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };
        $http.post("activityManagement/updateAfterCondition",data,postCfg)
            .success(function(data){
                if(data.status){
                    $scope.submitting = false;
                    $scope.notice(data.msg);
                    // $state.transitionTo('activities.activityManagement',null,{reload:true});
                    $scope.goBackFun();
                }else{
                    $scope.notice(data.msg);
                    $scope.submitting = false;
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