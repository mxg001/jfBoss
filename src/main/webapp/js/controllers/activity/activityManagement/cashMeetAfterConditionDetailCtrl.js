/**
 * 后置条件修改
 */
angular.module('inspinia',['angularFileUpload']).controller('cashMeetAfterConditionDetailCtrl',function($scope,$compile,$http,i18nService,$state,FileUploader,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.source=$stateParams.source;

    $scope.expriryDateTypeSelect = angular.copy($scope.expriryDateTypeList);
    $scope.expriryDateTypeStr=angular.toJson($scope.expriryDateTypeSelect);

    $scope.policyTypeSelect = [];
    $scope.policyTypeSelect.unshift({text:"请选择",value:null});
    $scope.policyTypeSelect.push({text:"概率随机",value:1});
    $scope.policyTypeSelect.push({text:"区间随机",value:2});
    $scope.policyTypeStr=angular.toJson($scope.policyTypeSelect);

    $scope.expriryDateTypeListSelect = angular.copy($scope.expriryDateTypeList);
    $scope.expriryDateTypeListSelect.forEach((item,index,array)=>{
        item.value = parseInt(item.value)
    })
    $scope.expriryDateTypeStr=angular.toJson($scope.expriryDateTypeListSelect);

    $scope.addInfo={};
    $scope.getCashMeetAfterCondition=function(){
        $http.post("activityManagement/getAfterConditionDetail",
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
                        });
                        if($scope.addInfo.policyTypeList.length == 0){
                            $scope.addInfo.policyTypeList.push({awardNum:0,dayNums:0,awardOdds:0});
                        }
                    }else if(data.info.policyType == 2){
                        $scope.policyTypeVisible2 = true;
                    }
                }else{
                    $scope.notice(data.msg);
                }
            })
    };
    $scope.getCashMeetAfterCondition();


    //返回
    $scope.goBackFun=function(){
        if($stateParams.source == 1){
            window.open('welcome.do#/activities/cashMeetConfigDetail/'+$stateParams.actCode+"/"+$stateParams.business+"/"+$stateParams.source, '_self');
        }else{
            window.open('welcome.do#/activities/cashMeetConfig/'+$stateParams.actCode+"/"+$stateParams.business+"/"+$stateParams.source, '_self');
        }
    };

    $scope.addPolicy = function(event){
        console.log("新增策略");
        var str = '<div class="form-group">' +
            '  <label class="col-sm-2 control-label"></label>' +
            '    <div class="col-sm-3">' +
            '        奖励<input type="text" style="width: 50px;" name="awardDesc" disabled ng-model="addInfo.awardDesc">&nbsp;单日数量限制<input type="text" style="width: 50px;" name="awardDesc" disabled ng-model="addInfo.awardDesc">&nbsp;概率<input type="text" style="width: 50px;" name="awardDesc" disabled ng-model="addInfo.awardDesc">&nbsp;&nbsp;<a ng-click="deletePolicy($event)" style="color: red;">删除</a><br />' +
            '    </div>' +
            '</div>';
        var $html = $compile(str)($scope);
        angular.element('#awardPolicy').append($html);
    }

    $scope.deletePolicy = function(event){
        $(event.target).parent().parent().remove();
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

    $scope.saveCashMeetAfter=function(){
        $scope.addInfoSub=angular.copy($scope.addInfo);
        $scope.addInfoSub.actRule=null;
        var data={
            info:angular.toJson($scope.addInfoSub),
            actRule:$scope.addInfo.actRule
        };
        var postCfg = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };
        $http.post("activityManagement/saveLuckTemplate",data,postCfg)
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('activities.activityManagement',null,{reload:true});
                }else{
                    $scope.notice(data.msg);
                }
            });
    };

});

