/**
 * 奖项详情
 */
angular.module('inspinia',['angularFileUpload']).controller('prizeDetailCtrl',function($scope,$http,i18nService,$state,FileUploader,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.businessNo=$stateParams.businessNo;
    $scope.awardCode=$stateParams.awardCode;

    $scope.rewardTypeSelect = angular.copy($scope.rewardTypeList);
    $scope.rewardTypeSelect.unshift({text:"请选择",value:""});
    $scope.rewardTypeSelect.push({text:"未中奖",value:"99"});
    $scope.rewardTypeStr=angular.toJson($scope.rewardTypeSelect);

    $scope.addInfo={};
    $scope.getLuckPrize=function(){
        $http.post("activityChoice/getLuckPrize",
            "awardCode="+$scope.awardCode+"&businessNo="+$scope.businessNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.addInfo=data.info;
                    $scope.awardTypeChange(0);
                }else{
                    $scope.notice(data.msg);
                }
            })
    };
    $scope.getLuckPrize();

    $scope.conList=[];
    $scope.cleanSta=function(initSta){
        $scope.conSta=false;//卷
        $scope.moneySta=false;//金额
        $scope.numSta=false;//数值
        $scope.integralSta=false;//积分有效期
        if(initSta==1){
            $scope.addInfo.prize.awardNum="";
            $scope.addInfo.prize.expireDay="";
            $scope.addInfo.prize.calcType="";
        }
    };

    $scope.awardTypeChange=function(initSta){
        var sta=$scope.addInfo.prize.rewardType;
        $scope.cleanSta(initSta);
        if(sta==null||sta==""){
            return;
        }
        if(sta=="1"){//会员积分
            $scope.numSta=true;
            $scope.integralSta=true;
            $scope.addInfo.prize.calcType="1";
        }else if(sta=="2" ||sta=="3"){
            $scope.numSta=true;
            $scope.addInfo.prize.calcType="1";
        }else if(sta=="4" ||sta=="5" ||sta=="6"){//卷
            $scope.getCouponList(sta);
            $scope.conSta=true;
        }else if(sta=="7"){//现金
            $scope.moneySta=true;
            $scope.addInfo.prize.calcType="1";
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
                            $scope.conList.push({value:list[i].goodsCode,text:list[i].goodsName+"("+list[i].goodsCode+")"});
                        }
                    }
                }
            });
    };

    //上传图片,定义控制器路径
    var uploaderImg = $scope.uploaderImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            if(item.size>500*1024){
                return false;
            }
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });

    uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.saveInfo(response.url);
        }
    };

    //返回
    $scope.goBackFun=function(){
        if($stateParams.source==3){
            window.open('welcome.do#/management/busLuckDrawpConfig/'+$scope.addInfo.actCode+'/'+$scope.businessNo, '_self');
        }else{
            window.open('welcome.do#/management/busLuckDrawpConfigDetail/'+$scope.addInfo.actCode+'/'+$scope.businessNo+'/'+$stateParams.source, '_self');
        }
    };
});