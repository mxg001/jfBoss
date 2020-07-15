/**
 * 活动内容详情
 */
angular.module('inspinia',['angularFileUpload']).controller('cashMeetActivityContentDetailCtrl',function($scope,$compile,$http,i18nService,$state,FileUploader,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.expriryDateTypeSelect = angular.copy($scope.expriryDateTypeList);
    $scope.expriryDateTypeStr=angular.toJson($scope.expriryDateTypeSelect);

    $scope.policyTypeSelect = [];
    $scope.policyTypeSelect.unshift({text:"请选择",value:null});
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
                    if(data.info.policyType == 1){
                        $scope.addInfo.policyTypeList = [];
                        $scope.policyTypeVisible1 = true;
                        $scope.addInfo.policyList.forEach(function(item,index,arr){
                            $scope.addInfo.policyTypeList.push({awardNum:item.awardNum,dayNums:item.dayNums,awardOdds:item.awardOdds});
                        })
                    }else if(data.info.policyType == 2){
                        $scope.policyTypeVisible2 = true;
                    }
                    $scope.activeTypeChange();
                }else{
                    $scope.notice(data.msg);
                }
            })
    };
    $scope.getCashMeetActivityContent();

    //返回
    $scope.goBackFun=function(){
        if($stateParams.source == 3){
            window.open('welcome.do#/activities/cashMeetConfigDetail/'+$stateParams.actCode+"/"+$stateParams.business+"/1", '_self');
        }else{
            window.open('welcome.do#/activities/cashMeetConfig/'+$stateParams.actCode+"/"+$stateParams.business+"/"+$stateParams.source, '_self');
        }
    };


});

