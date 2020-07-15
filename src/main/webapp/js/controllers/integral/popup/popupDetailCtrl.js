/**
 * 弹窗详情
 */
angular.module('inspinia',['angularFileUpload']).controller('popupDetailCtrl',function($scope,$http,i18nService,$state,$stateParams,SweetAlert,FileUploader){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.statusList = [{text:'否', value:0},{text:'是', value:1}];
    $scope.showSelects = [{name:'APP首页',id:1,checkStatus:0},{name:'会员首页',id:2,checkStatus:0}];

    //获取数据
    $http.post("pop/getPopupConfig","id="+$stateParams.id,
        {headers: {'Content-Type':'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.info=data.info;
                $scope.info.condition = "满足当前活动参与条件";

                var position = $scope.info.popPosition.split(",");
                if(position.length > 0){
                    angular.forEach($scope.showSelects, function (item) {
                        for (var i = 0; i < position.length; i++) {
                            if(position[i] == item.id){
                                item.checkStatus = 1;
                            }
                        }
                    });
                }
            }
        }).error(function(data){
            $scope.notice(data.msg);
        });



}).filter('trustHtml', function ($sce) {
    return function (input) {
        return $sce.trustAsHtml(input);
    }
});